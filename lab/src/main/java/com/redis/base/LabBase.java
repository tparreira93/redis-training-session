package com.redis.base;


import redis.clients.jedis.JedisPool;

/**
 * Base class for all Redis training lab exercises.
 * Provides common functionality and connection management.
 */
public abstract class LabBase {
    protected final JedisPool jedisPool;

    /**
     * Constructor that accepts a JedisPool for Redis connections.
     * 
     * @param jedisPool The JedisPool to use for Redis operations
     */
    public LabBase(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }
    
    /**
     * Default constructor that uses the default Redis connection.
     */
    public LabBase() {
        this.jedisPool = RedisConnection.getPool();
    }
}
