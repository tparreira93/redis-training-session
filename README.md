# Redis Training Session - 1h30m

## Overview
This is a 1h30 Redis training session designed for Java developers.
The session is composed of an overview part of at least 30 minutes and a hands-on lab of 1h.

The overview session covers the following topics:

- [Redis introduction and overview](overview/01-redis-introduction.md)
- [Redis cluster](overview/02-redis-cluster.md)
- [Redis data types I](overview/03-redis-data-types-i.md)
- [Redis data types II](overview/04-redis-data-types-ii.md)
- [Redis transactions](overview/05-redis-transactions.md)
- [Other features](overview/06-redis-other-features.md)

Lab folder contains the following:

- [Lab exercises](lab/src/main/java/com/redis/training) - 8 exercises

Additionally, there is a [Redis cluster](redis-cluster/README.md) that is used to showcase examples of Redis cluster operations and supports the lab exercises.

## Prerequisites

- Java 8 or higher
- Docker + Compose (for Redis)
- Basic Java programming knowledge

## Redis Lab

The lab consists of 8 organized exercises that use Redis as a data store. 
Each exercise has its own test. The exercises are designed to build upon each other, progressing from basic Redis operations to advanced (still simple) real-world scenarios.

### Labs—Quick Start

Navigate to [lab](lab) folder and execute the following command to start the Redis instance:

```bash
❯ docker compose up -d
```

### Exercises

The lab includes 8 hands-on exercises organized as follows:

1. **[String Operations](lab/src/main/java/com/redis/training/L01_StringExercise.java)** - Basic Redis strings and operations
   - The Respective test is [L01_StringExerciseTest.java](lab/src/test/java/com/redis/training/L01_StringExerciseTest.java)
2. **[Hash Operations](lab/src/main/java/com/redis/training/L02_HashExercise.java)** - Working with Redis hashes
   - The Respective test is [L02_HashExerciseTest.java](lab/src/test/java/com/redis/training/L02_HashExerciseTest.java)
3. **[List Operations](lab/src/main/java/com/redis/training/L03_ListExercise.java)** - Lists, queues, and stacks
   - The Respective test is [L03_ListExerciseTest.java](lab/src/test/java/com/redis/training/L03_ListExerciseTest.java)
4. **[Transaction Operations](lab/src/main/java/com/redis/training/L04_TransactionExercise.java)** - Atomic operations and transactions
   - The Respective test is [L04_TransactionExerciseTest.java](lab/src/test/java/com/redis/training/L04_TransactionExerciseTest.java)
5. **[Cache Operations](lab/src/main/java/com/redis/training/L05_CacheExercise.java)** - Caching patterns and strategies
   - The Respective test is [L05_CacheExerciseTest.java](lab/src/test/java/com/redis/training/L05_CacheExerciseTest.java)
6. **[Analytics Operations](lab/src/main/java/com/redis/training/L06_AnalyticsExercise.java)** - Real-time analytics and counters
   - The Respective test is [L06_AnalyticsExerciseTest.java](lab/src/test/java/com/redis/training/L06_AnalyticsExerciseTest.java)
7. **[Queue Operations](lab/src/main/java/com/redis/training/L07_QueueExercise.java)** - Producer-consumer patterns
   - The Respective test is [L07_QueueExerciseTest.java](lab/src/test/java/com/redis/training/L07_QueueExerciseTest.java)
8. **[Flash Sale Operations](lab/src/main/java/com/redis/training/L08_FlashSaleExercise.java)** - High-concurrency scenarios
   - The Respective test is [L08_FlashSaleExerciseTest.java](lab/src/test/java/com/redis/training/L08_FlashSaleExerciseTest.java)

## Redis Cluster

Docker compose file that starts a Redis cluster with 3 masters and 3 replicas.
The cluster is configured to use hostnames instead of IP addresses to make it easier to connect to the cluster.
The cluster is also configured to use a custom port range to avoid conflicts with other Redis instances running on the host machine.

To start the cluster, run the following command from the `redis-cluster` folder:

```bash
❯ ./manage-cluster.sh start
```
