# Redis Data Types (II)

## Data Structures

Redis supports following data structures:

- **List**: `key -> [element1, element2, element3, ...]`
- **Hash**: `key -> {field1: value1, field2: value2, ...}`
- **Set**: `key -> {member1, member2, member3, ...}`

**Maximum**: 2^32 - 1 elements (4 billion) per list, set or hash

Additional data structures also exist in Redis, but won't be covered in this training:

- **Sorted Set**: `key -> {member1: score1, member2: score2, ...}`
  - Sorted by score
- **Stream**: `key -> [entry1, entry2, ...]`
  - Append-only data structure
- **Bitmap**: `key -> [bit1, bit2, ...]`
  - Offset-based data structure
- **Probabilistic**:
  - **HyperLogLog**: `key -> {element1, element2, ...}`
    - Approximate cardinality estimation of unique elements
  - **Bloom Filter**: `key -> {element1, element2, ...}`
    - Probabilistic set membership
  - **t-Digest**: `key -> {element1, element2, ...}`
    - Probabilistic quantile estimation

Examples here are using the Redis CLI.
To test locally use [redis-cluster](../redis-cluster) by starting the cluster and connecting to the Redis CLI:

```bash
❯ ./manage-cluster.sh start
🚀 Starting Redis cluster...
...
⏳ Waiting for cluster initialization...
✅ Redis cluster started successfully!
...

❯ ./manage-cluster.sh cli
🔌 Connecting to Redis CLI on node 7001...
127.0.0.1:7001>
```

### Lists

Redis lists are **ordered collections** of strings, implemented as **doubly-linked lists**

```redis
# Left add to list: ["second message"]
LPUSH messages "first message"

# Left add to list: ["second message", "first message"]
LPUSH messages "second message"

# Right add to list: ["second message", "first message", "third message"]
RPUSH messagesf"third message"

# List length: 3
LLEN messages

# Get all elements: ["second message", "first message", "third message"]
LRANGE messages 0 -1
# Get first 2 elements: ["second message", "first message"]
LRANGE messages 0 1

# Get first element: "second message"
LINDEX messages 0
# Get last element: "third message" (last element)
LINDEX messages -1

# Remove and return elements: "second message", removes from left
LPOP messages
# Returns: "third message", removes from right
RPOP messages
```

### Sets

Redis sets are **unordered collections** of unique strings

```redis
# Add elements (duplicates are ignored)
SADD user:1000:tags "redis" "database" "caching"
SADD user:1000:tags "redis"  # Ignored - already exists

# Check if element exists
SISMEMBER user:1000:tags "redis"     # Returns: 1 (true)
SISMEMBER user:1000:tags "mysql"     # Returns: 0 (false)

# Get all members
SMEMBERS user:1000:tags  # Returns: ["redis", "database", "caching"] (order varies)

# Get count
SCARD user:1000:tags     # Returns: 3

# Remove elements
SREM user:1000:tags "caching"
SMEMBERS user:1000:tags  # Returns: ["redis", "database"]
```

### Hashes

Redis hashes are **collections of field-value pairs**.

```redis
# Set individual fields
HSET user:1000 name "John Doe"
HSET user:1000 email "john@example.com"
HSET user:1000 age 30

# Set multiple fields at once (preferred)
HMSET user:2000 name "Jane Smith" email "jane@example.com" age 25 city "New York"

# Get individual fields
HGET user:1000 name      # Returns: "John Doe"
HGET user:1000 email     # Returns: "john@example.com"

# Get multiple fields
HMGET user:1000 name email age  # Returns: ["John Doe", "john@example.com", "30"]

# Get all fields and values
HGETALL user:1000 # Returns: ["name", "John Doe", "email", "john@example.com", "age", "30"]

# Check if field exists
HEXISTS user:1000 name    # Returns: 1 (true)
HEXISTS user:1000 phone   # Returns: 0 (false)

# Get all field names or values
HKEYS user:1000           # Returns: ["name", "email", "age"]
HVALS user:1000           # Returns: ["John Doe", "john@example.com", "30"]

# Get number of fields
HLEN user:1000            # Returns: 3

# Delete fields
HDEL user:1000 age        # Returns: 1 (number of fields deleted)
HGETALL user:1000         # Returns: ["name", "John Doe", "email", "john@example.com"]
```
