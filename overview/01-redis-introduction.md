# Redis Introduction

- **Redis** = **RE**mote **DI**ctionary **S**erver
- Key value store
- Extremely fast due to being in-memory
- Open source, with enterprise version

## Architecture

- Each key is mapped to a slot using the following formula: `CRC16(key) % 16384`
- There are a total of 16384 slots (0-16383)
- Atomic operations possible (within a slot)
- Single thread event loop for command execution
- Optional persistence with Redis Database (RDB) snapshots or Append Only File (AOF), or combination of both
- Cluster mode for horizontal scaling

## Common use cases

### 1. Caching

```text
Application → Database
            |
            v
Application → Redis Cache
            OR
            → Database
```

### 2. Session store with auto expiry

```text
# Traditional Problem

User-1 logs in → Server 1 (stores session locally)
Next request → Server 2 (no session data) → User must login again
            |
            v
# Redis Solution
User-1 logs in → Server 1 → Redis (Session store)
Next request → Server 2 → Redis → Session data available
User-1 session expires automatically after X days
```

### 3. Analytics

Using atomic operations, sorted sets and other data structures

- Counters
- Unique tracking
- Leaderboards
- Rate limiting

### 4. Message queues

Task queue pattern using Redis lists

```text
Producer: Application 1 → Redis (queue)
Consumer: Redis (queue) → Application 2
```

Pub/sub pattern using Redis pub/sub

```text
Publisher: PUBLISH notifications "New order received"
Subscriber: SUBSCRIBE notifications
```

### 5. Time Series Data

Store and analyze time-based metrics and events like data from IoT devices.

```text
# Store metrics with timestamps
ZADD temperature 1642262400 20
ZADD temperature 1642262460 20.1
ZADD temperature 1642262520 22.2

# Get metrics for time range
ZRANGEBYSCORE temperature 1642262400 1642262520
```

**Use Cases:** IoT sensors, application monitoring, financial data, log analysis

### 6. Search and Indexing

Indexing and searching capabilities for fast lookups using sets.

**Examples:**

```text
# Add tags to posts
SADD tag:redis "post:1" "post:20" "post:30"
SADD tag:database "post:1" "post:2" "post:3"

# Find posts with both tags
SINTER tag:redis tag:database  # Returns: ["post:1"]
```

## When to Choose Redis

**Choose Redis when you need:**

- ✅ High performance (sub-millisecond latency)
- ✅ Simple data structures (strings, lists, sets, hashes)
- ✅ Atomic operations
- ✅ Real-time applications
- ✅ Horizontal scaling

**Consider alternatives when you need:**

- ❌ Complex queries (SQL)
- ❌ ACID transactions across multiple keys
- ❌ Large dataset that doesn't fit in memory
- ❌ Strong consistency guarantees
