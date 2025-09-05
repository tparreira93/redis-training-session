# Redis Transactions

Redis transactions provide a way to execute multiple commands as a single atomic operation, but do not behave in the same way as traditional RDBMS, namely it behaves as follows:

- All commands in a transaction are executed sequentially and atomically
- Transactions are only allowed within a single slot
- When a transaction is started, all commands are queued and not executed until the transaction is marked to execute
- Commands in a transaction are executed in the order they are received, even if a command fails
- Since Redis is single-threaded, there is no need for locking

Examples here are using the Redis CLI.
To test locally use [redis-cluster](../redis-cluster) by starting the cluster and connecting to the Redis CLI:

```bash
❯ ./manage-cluster.sh start
🚀 Starting Redis cluster...
...
⏳ Waiting for cluster initialization...
✅ Redis cluster started successfully!

❯ ./manage-cluster.sh cli
🔌 Connecting to Redis CLI on node 7001...
127.0.0.1:7001>
```

## Hash Tag

Before looking it at transactions, it is important to understand the concept of hash tag.
Transactions can only be executed within a single slot, so it is important to desgin keys in a way that they are all in the same slot.
For this, we have the concept of hash tag, which is a way indicating how to calculate the hash slot for a key.
This is done by using a `{}` around the part of the key that should be used to calculate the hash slot.
Example:

```redis
127.0.0.1:7001> CLUSTER KEYSLOT user:{42}:name
(integer) 8000
127.0.0.1:7001> CLUSTER KEYSLOT user:{42}:age
(integer) 8000
127.0.0.1:7001> CLUSTER KEYSLOT user:{42}:email
(integer) 8000
```

## Transaction Properties

- **No rollback**: Failed commands don't rollback successful ones
- **All commands are executed**: Even if a command fails, all commands are executed
- **Blocking**: Long transactions can block other clients

## Transaction Commands

Redis transactions use four main commands to control transaction behavior:

- **MULTI** - Start a transaction
- **EXEC** - Execute all commands in the transaction
- **DISCARD** - Cancel the transaction
- **WATCH** - Monitor keys for changes

## Basic Transactions

### MULTI and EXEC

```redis
MULTI

# Queue Command
SET account:{1000}:balance 100

# Queue Command
INCR account:{1000}:transactions # Result: QUEUED

# Queue Command
SET account:{1000}:last_update "2024-09-05" # Result: QUEUED

# Execute all queued commands atomically
EXEC
1) OK
2) (integer) 1
3) OK
```

### DISCARD

```redis
# Start a transaction
MULTI
OK

SET user:1000:name "John"
QUEUED

SET user:1000:email "john@example.com"
QUEUED

# Cancel the transaction - no commands are executed
DISCARD
OK

# Check - no changes were made
GET user:1000:name
(nil)
```

### WATCH Command

WATCH provides optimistic locking by monitoring keys for changes. If any watched key is modified before EXEC, the entire transaction is cancelled.

```redis
# Watch a key for changes
WATCH account:1000:balance

# Get current balance
GET account:1000:balance
"100"

# Start transaction
MULTI
OK

# Attempt to debit account
DECRBY account:1000:balance 50
QUEUED

SET account:1000:last_debit "2024-01-15"
QUEUED

# Execute transaction
EXEC
1) (integer) 50
2) OK
```

**Explanation**: If account:1000:balance was modified by another client between WATCH and EXEC, the transaction would return `(nil)` instead of executing.

### Failed WATCH Example

```redis
# Client 1: Watch and start transaction
WATCH account:1000:balance
MULTI
DECRBY account:1000:balance 30
QUEUED

# Meanwhile, Client 2 modifies the watched key:
# SET account:1000:balance 200

# Client 1: Try to execute - will fail
EXEC
(nil)  # Transaction was cancelled due to key modification
```

**Explanation**: DISCARD cancels the transaction and clears the command queue. No commands are executed.
