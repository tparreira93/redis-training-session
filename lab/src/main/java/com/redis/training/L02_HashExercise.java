package com.redis.training;


import com.redis.base.LabBase;
import redis.clients.jedis.JedisPool;
import java.util.Map;
import java.util.HashMap;

/**
 * Exercise 2: Redis Hash Operations
 *
 * Hashes are perfect for representing objects with multiple fields.
 * They're memory efficient and allow you to get/set individual fields
 * without retrieving the entire object.
 *
 * Time: 15 minutes
 *
 * Learning Objectives:
 * - Store and retrieve hash data structures
 * - Work with individual hash fields
 * - Understand when to use hashes vs strings
 * - Perform atomic operations on hash fields
 */
public class L02_HashExercise extends LabBase {

    public L02_HashExercise(JedisPool jedisPool) {
        super(jedisPool);
    }

    public L02_HashExercise() {
        super();
    }

    /**
     * Exercise 2.1: Creating a User Profile Hash
     *
     * Set the value stored at key to be a hash containing user profile data.
     * Create a hash with the following fields:
     * - firstname: "Jane"
     * - lastname: "Doe"
     * - email: "jane.doe@example.com"
     * - department: "Engineering"
     * - yearjoined: "2020"
     *
     * @param key Redis key name for the user profile
     */
    public void createUserProfile(String key) {
        // >>> START CODING CHALLENGE <<<
        // TODO: Implement using jedis.hset() with a Map
        // or multiple jedis.hset() calls for individual fields

        // >>> END CODING CHALLENGE <<<
    }

    /**
     * Exercise 2.2: Retrieving Complete Hash
     *
     * Gets the complete hash stored at key, returning it as a Map.
     *
     * @param key Redis key name
     * @return Map containing all hash field name/value pairs
     */
    public Map<String, String> getUserProfile(String key) {
        // >>> START CODING CHALLENGE <<<
        // TODO: Implement using jedis.hgetAll()
        return new HashMap<>();
        // >>> END CODING CHALLENGE <<<
    }

    /**
     * Exercise 2.3: Getting Individual Hash Field
     *
     * Gets the value of a specific field in the hash stored at key.
     *
     * @param key   Redis key name
     * @param field Hash field name
     * @return Value of the specified field, or null if field doesn't exist
     */
    public String getUserField(String key, String field) {
        // >>> START CODING CHALLENGE <<<
        // TODO: Implement using jedis.hget()
        return null;
        // >>> END CODING CHALLENGE <<<
    }

    /**
     * Exercise 2.4: Updating Hash Field
     *
     * Sets the value of a specific field in the hash stored at key.
     * If the field already exists, it will be overwritten.
     *
     * @param key   Redis key name
     * @param field Hash field name
     * @param value New value for the field
     */
    public void updateUserField(String key, String field, String value) {
        // >>> START CODING CHALLENGE <<<
        // TODO: Implement using jedis.hset()

        // >>> END CODING CHALLENGE <<<
    }

    /**
     * Exercise 2.5: Checking if Hash Field Exists
     *
     * Determines if a field exists in the hash stored at key.
     *
     * @param key   Redis key name
     * @param field Hash field name to check
     * @return true if field exists, false otherwise
     */
    public boolean hasUserField(String key, String field) {
        // >>> START CODING CHALLENGE <<<
        // TODO: Implement using jedis.hexists()
        return false;
        // >>> END CODING CHALLENGE <<<
    }

    /**
     * Exercise 2.6: Deleting Hash Field
     *
     * Removes the specified field from the hash stored at key.
     *
     * @param key   Redis key name
     * @param field Hash field name to delete
     * @return Number of fields that were removed (0 or 1)
     */
    public Long deleteUserField(String key, String field) {
        // >>> START CODING CHALLENGE <<<
        // TODO: Implement using jedis.hdel()
        return 0L;
        // >>> END CODING CHALLENGE <<<
    }

    /**
     * Exercise 2.7: Incrementing Numeric Hash Field
     *
     * Increments the number stored at field in the hash stored at key by increment.
     * If the field doesn't exist, it's set to 0 before performing the increment.
     * This is useful for counters within objects (e.g., user login count).
     *
     * @param key       Redis key name
     * @param field     Hash field name
     * @param increment Amount to increment by
     * @return The value of field after the increment
     */
    public Long incrementUserField(String key, String field, long increment) {
        // >>> START CODING CHALLENGE <<<
        // TODO: Implement using jedis.hincrBy()
        return 0L;
        // >>> END CODING CHALLENGE <<<
    }

    /**
     * Exercise 2.8: Getting Multiple Hash Fields
     *
     * Gets the values of multiple fields in the hash stored at key.
     * This is more efficient than multiple HGET calls.
     *
     * @param key    Redis key name
     * @param fields Array of field names to retrieve
     * @return List of values corresponding to the given fields
     */
    public java.util.List<String> getUserFields(String key, String... fields) {
        // >>> START CODING CHALLENGE <<<
        // TODO: Implement using jedis.hmget()
        return new java.util.ArrayList<>();
        // >>> END CODING CHALLENGE <<<
    }
}
