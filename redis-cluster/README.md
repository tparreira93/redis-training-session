# Redis Cluster with Docker Compose

## Master-Slave Configuration

- **Master 7001** ↔ **Slave 7005**
- **Master 7002** ↔ **Slave 7006**
- **Master 7003** ↔ **Slave 7004**

*IP addresses are dynamically assigned but hostnames remain consistent*

## Quick Start

### Using the Management Script (Recommended)

```bash
# Start the cluster
❯ ./manage-cluster.sh start

# Check cluster status
❯ ./manage-cluster.sh status

# View cluster nodes
❯ ./manage-cluster.sh nodes

# Connect to Redis CLI
❯ ./manage-cluster.sh cli

# Restart the cluster
❯ ./manage-cluster.sh restart

# Stop the cluster
❯ ./manage-cluster.sh stop

# Reset cluster (removes all data)
❯ ./manage-cluster.sh reset

# View logs
❯ ./manage-cluster.sh logs
```

## Testing the Cluster

### Basic Functionality Test

```bash
❯ ./manage-cluster.sh cli

# In the Redis CLI:
127.0.0.1:7001> SET user:1000 "John Doe"
-> Redirected to slot [1894] located at 172.21.0.3:7001
OK

127.0.0.1:7001> SET user:2000 "Jane Smith"
-> Redirected to slot [1893] located at 172.21.0.3:7001
OK

127.0.0.1:7001> GET user:1000
"John Doe"

127.0.0.1:7001> GET user:2000
"Jane Smith"
```

### Failover Test

```bash
# Stop a master node
❯ ./manage-cluster.sh stop -n redis-7003

# Check cluster status - slave should be promoted
❯ ./manage-cluster.sh status

# Restart the node - it should rejoin as slave
❯ ./manage-cluster.sh start -n redis-7003
```

## Files

- `docker-compose.yml` - Main cluster configuration with static IPs
- `manage-cluster.sh` - Management script for easy cluster operations
- `README.md` - This documentation

## Port Mapping

| Container | Container Port | Host Port | Cluster Bus Port | Hostname |
|-----------|---------------|-----------|-----------------|----------|
| redis-7001 | 7001 | 7001 | 17001 | redis-7001 |
| redis-7002 | 7002 | 7002 | 17002 | redis-7002 |
| redis-7003 | 7003 | 7003 | 17003 | redis-7003 |
| redis-7004 | 7004 | 7004 | 17004 | redis-7004 |
| redis-7005 | 7005 | 7005 | 17005 | redis-7005 |
| redis-7006 | 7006 | 7006 | 17006 | redis-7006 |

*IPs are dynamically assigned by Docker but hostnames are consistent*
