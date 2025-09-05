package com.redis.training;

import com.redis.base.RedisConnection;
import org.junit.After;
import org.junit.Before;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * Base class for all Redis training tests.
 * Provides common setup, teardown, and utility methods.
 */
public abstract class TestBase {
    protected JedisPool jedisPool;
    private String keyPrefix;

    @Before
    public void setUp() {
        jedisPool = RedisConnection.getPool();
        // Use a unique prefix for each test to avoid conflicts
        keyPrefix = "test:" + this.getClass().getSimpleName() + ":" + System.currentTimeMillis() + ":";
    }

    @After
    public void tearDown() {
        // Clean up all test keys after each test
        deleteAllTestKeys();
    }

    /**
     * Generates a unique key name for testing.
     * All test keys are prefixed to avoid conflicts and enable cleanup.
     *
     * @param suffix The suffix to append to the test key prefix
     * @return A unique key name for testing
     */
    protected String getTestKey(String suffix) {
        return keyPrefix + suffix;
    }

    /**
     * Generates a unique key name for testing.
     * All test keys are prefixed to avoid conflicts and enable cleanup.
     *
     * @return A unique key name for testing
     */
    protected String getTestKey() {
        return getTestKey("");
    }

    /**
     * Deletes all keys created during this test.
     * Uses the key prefix to identify and remove test keys.
     */
    private void deleteAllTestKeys() {
        try (Jedis jedis = jedisPool.getResource()) {
            Set<String> keys = jedis.keys(keyPrefix + "*");
            if (!keys.isEmpty()) {
                jedis.del(keys.toArray(new String[0]));
            }
        }
    }

    /**
     * Utility method to check if a key exists in Redis.
     *
     * @param key The key to check
     * @return true if key exists, false otherwise
     */
    protected boolean keyExists(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.exists(key);
        }
    }

    /**
     * Utility method to get the TTL of a key.
     *
     * @param key The key to check
     * @return TTL in seconds, -1 if no expiration, -2 if key doesn't exist
     */
    protected Long getKeyTTL(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.ttl(key);
        }
    }
}
