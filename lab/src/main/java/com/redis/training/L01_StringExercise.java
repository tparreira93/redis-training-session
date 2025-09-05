package com.redis.training;

import com.redis.base.LabBase;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Exercise 1: Redis String Operations
 *
 * In this exercise, you'll learn to work with Redis strings, which are the most
 * basic Redis data type. Strings can store text, numbers, or binary data up to 512MB.
 *
 * Time: 15 minutes
 *
 * Learning Objectives:
 * - Perform basic string operations (SET, GET)
 * - Work with expiration and TTL
 * - Use string manipulation commands
 * - Handle numeric operations on strings
 */
public class L01_StringExercise extends LabBase {

    public L01_StringExercise(JedisPool jedisPool) {
        super(jedisPool);
    }

    public L01_StringExercise() {
        super();
    }

    /**
     * Exercise 1.1: Basic String Operations
     *
     * Sets the value stored at key to be value.
     * Use the Redis SET command.
     *
     * @param key   Redis key name
     * @param value Value to be stored
     */
    public void setString(String key, String value) {
        // >>> START CODING CHALLENGE <<<
        // TODO: Implement using jedis.set()
        // Hint: Use try-with-resources to get a Jedis connection from jedisPool
        // >>> END CODING CHALLENGE <<<
    }

    /**
     * Exercise 1.2: Retrieving String Values
     *
     * Gets the value stored at key.
     * Use the Redis GET command.
     *
     * @param key Redis key name
     * @return The value stored at key, or null if key doesn't exist
     */
    public String getString(String key) {
        // >>> START CODING CHALLENGE <<<
        // TODO: Implement using jedis.get()
        // Return the actual value, not an empty string
        return null;
        // >>> END CODING CHALLENGE <<<
    }

    /**
     * Exercise 1.3: Setting Expiration
     *
     * Sets an expiry time for a key using the EXPIRE command.
     * This is useful for caching scenarios where data should automatically
     * be removed after a certain time.
     *
     * @param key    Redis key name
     * @param expiry Time in seconds before the key expires
     */
    public void setExpiry(String key, int expiry) {
        // >>> START CODING CHALLENGE <<<
        // TODO: Implement using jedis.expire()
        // >>> END CODING CHALLENGE <<<
    }

    /**
     * Exercise 1.4: Checking Time To Live
     *
     * Gets the remaining time to live for a key using the TTL command.
     * Returns:
     * - Positive number: seconds until expiration
     * - -1: key exists but has no expiration
     * - -2: key does not exist
     *
     * @param key Redis key name
     * @return Long representing time to live of the key
     */
    public Long getTimeToLive(String key) {
        // >>> START CODING CHALLENGE <<<
        // TODO: Implement using jedis.ttl()
        return -1L;
        // >>> END CODING CHALLENGE <<<
    }

    /**
     * Exercise 1.5: String Manipulation
     *
     * Returns the last 5 characters from the string value stored at key.
     * Use the GETRANGE command to extract a substring.
     *
     * Example: If key contains "Hello Redis", this should return "Redis"
     *
     * @param key Redis key name
     * @return String containing last 5 characters from the value at key
     */
    public String getLastFiveCharacters(String key) {
        // >>> START CODING CHALLENGE <<<
        // TODO: Implement using jedis.getrange()
        // Hint: Use negative indices to count from the end
        // getrange(key, -5, -1) gets the last 5 characters
        return "";
        // >>> END CODING CHALLENGE <<<
    }

    /**
     * Exercise 1.6: Atomic Increment
     *
     * Increments the number stored at key by 1.
     * If the key doesn't exist, it's set to 0 before performing the increment.
     * This is useful for counters (page views, user sessions, etc.).
     *
     * @param key Redis key name
     * @return The value of key after the increment
     */
    public Long incrementCounter(String key) {
        // >>> START CODING CHALLENGE <<<
        // TODO: Implement using jedis.incr()
        return 0L;
        // >>> END CODING CHALLENGE <<<
    }

    /**
     * Exercise 1.7: Set with Expiration (Atomic)
     *
     * Sets a key to hold a string value and sets the key to timeout after
     * a given number of seconds. This is equivalent to executing SET and EXPIRE
     * as a single atomic operation.
     *
     * @param key     Redis key name
     * @param value   Value to store
     * @param seconds Expiration time in seconds
     */
    public void setWithExpiration(String key, String value, int seconds) {
        // >>> START CODING CHALLENGE <<<
        // TODO: Implement using jedis.setex()
        // >>> END CODING CHALLENGE <<<
    }
}
