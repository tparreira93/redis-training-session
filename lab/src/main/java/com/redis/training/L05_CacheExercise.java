package com.redis.training;

import com.redis.base.FakeService;
import com.redis.base.LabBase;
import redis.clients.jedis.JedisPool;

/**
 * Exercise 5: Redis Cache Operations
 *
 * In this exercise, you'll implement common caching patterns used in real-world applications.
 * You'll learn read-through, write-through, and cache-aside patterns that are essential
 * for building scalable applications with Redis as a caching layer.
 *
 * Time: 20 minutes
 *
 * Learning Objectives:
 * - Implement read-through caching pattern
 * - Implement write-through caching pattern
 * - Handle cache misses and cache warming
 * - Use appropriate TTL strategies for different data types
 */
public class L05_CacheExercise extends LabBase {
    private final FakeService fakeService;

    public L05_CacheExercise(JedisPool jedisPool, FakeService fakeService) {
        super(jedisPool);
        this.fakeService = fakeService;
    }

    public L05_CacheExercise(FakeService fakeService) {
        super();
        this.fakeService = fakeService;
    }

    /**
     * Exercise 5.1: Read-Through Cache
     *
     * Implements a read-through cache pattern where data is fetched from the cache first,
     * and if not found (cache miss), fetched from the "database" and stored in cache.
     *
     * @param userId User ID to fetch data for
     * @return User data (simulated as "User:<userId>:Data")
     */
    public String getUserData(String userId) {
        // >>> START CODING CHALLENGE <<<
        // TODO: Implement read-through cache pattern
        // 1. Try to get user data from cache using key "user:<userId>"
        // 2. If cache hit, return the cached data
        // 3. If cache miss, simulate database fetch: "User:" + userId + ":Data"
        // 4. Store the fetched data in cache with 300 seconds TTL
        // 5. Return the data
        return null;
        // >>> END CODING CHALLENGE <<<
    }

    /**
     * Exercise 5.2: Write-Through Cache
     *
     * Implements a write-through cache pattern where data is written to both
     * cache and "database" simultaneously to maintain consistency.
     *
     * @param userId User ID
     * @param userData User data to store
     * @return true if both cache and database write succeeded
     */
    public boolean updateUserData(String userId, String userData) {
        // >>> START CODING CHALLENGE <<<
        // TODO: Implement write-through cache pattern
        // 1. Store data in cache with key "user:<userId>" and 300 seconds TTL
        // 2. Simulate database write by storing in key "db:user:<userId>" (no TTL)
        // 3. Return true if both operations succeeded
        return false;
        // >>> END CODING CHALLENGE <<<
    }

    /**
     * Exercise 5.3: Cache Warming
     *
     * Pre-loads frequently accessed data into the cache to improve performance.
     * This is typically done during application startup or scheduled maintenance.
     *
     * @param userIds Array of user IDs to warm up
     * @return Number of users successfully cached
     */
    public int warmUpUserCache(String[] userIds) {
        // >>> START CODING CHALLENGE <<<
        // TODO: Implement cache warming
        // 1. For each userId, simulate fetching from database: "User:" + userId + ":Data"
        // 2. Store each user's data in cache with key "user:<userId>" and 600 seconds TTL
        // 3. Count successful cache operations
        // 4. Return the count
        return 0;
        // >>> END CODING CHALLENGE <<<
    }
}