package com.redis.base;

import redis.clients.jedis.*;

import java.time.Duration;

/**
 * Redis connection management utility.
 * Provides connection options for both standalone and cluster configurations.
 */
public class RedisConnection {
    private static JedisPool jedisPool;
    private static JedisCluster jedisCluster;

    // Default Redis connection settings
    private static final String REDIS_HOST = "localhost";
    private static final int REDIS_PORT = 6379;
    private static final int REDIS_TIMEOUT = 2000;

    /**
     * Gets the singleton JedisPool instance for standalone Redis.
     * Creates the pool if it doesn't exist.
     *
     * @return JedisPool instance for Redis connections
     */
    public static synchronized JedisPool getPool() {
        if (jedisPool == null) {
            JedisPoolConfig poolConfig = buildPoolConfig();
            jedisPool = new JedisPool(poolConfig, REDIS_HOST, REDIS_PORT, REDIS_TIMEOUT);
        }
        return jedisPool;
    }

    /**
     * Builds optimized pool configuration for standalone Redis connections.
     *
     * @return JedisPoolConfig with performance-tuned settings for standalone
     */
    private static JedisPoolConfig buildPoolConfig() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();

        // Connection pool sizing for standalone
        poolConfig.setMaxTotal(10);
        poolConfig.setMaxIdle(5);
        poolConfig.setMinIdle(1);

        // Connection validation
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);

        return poolConfig;
    }

    /**
     * Builds optimized pool configuration for Redis cluster connections.
     *
     * @return JedisPoolConfig with cluster-specific settings
     */
    private static ConnectionPoolConfig buildClusterPoolConfig() {
        ConnectionPoolConfig poolConfig = new ConnectionPoolConfig();

        // Connection pool sizing for cluster (higher for multiple nodes)
        poolConfig.setMaxTotal(30);  // Higher for cluster load across nodes
        poolConfig.setMaxIdle(15);
        poolConfig.setMinIdle(3);

        // Connection validation
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);

        // Eviction policy for idle connections
        poolConfig.setMinEvictableIdleTimeMillis(Duration.ofSeconds(60).toMillis());
        poolConfig.setTimeBetweenEvictionRunsMillis(Duration.ofSeconds(30).toMillis());
        poolConfig.setNumTestsPerEvictionRun(3);

        // Block when pool is exhausted
        poolConfig.setBlockWhenExhausted(true);
        poolConfig.setMaxWaitMillis(Duration.ofSeconds(5).toMillis());

        return poolConfig;
    }

    /**
     * Closes the JedisPool and releases resources.
     * Call this when shutting down the application.
     */
    public static synchronized void closePool() {
        if (jedisPool != null && !jedisPool.isClosed()) {
            jedisPool.close();
            jedisPool = null;
        }
    }

    /**
     * Closes the JedisCluster and releases resources.
     * Call this when shutting down the application.
     */
    public static synchronized void closeCluster() {
        if (jedisCluster != null) {
            try {
                jedisCluster.close();
            } catch (Exception e) {
                // Log error if needed
            }
            jedisCluster = null;
        }
    }

    /**
     * Closes all connections (pool and cluster).
     * Call this when shutting down the application.
     */
    public static synchronized void closeAll() {
        closePool();
        closeCluster();
    }
}
