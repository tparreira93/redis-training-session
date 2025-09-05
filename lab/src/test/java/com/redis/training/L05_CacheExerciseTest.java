package com.redis.training;

import com.redis.base.FakeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import redis.clients.jedis.Jedis;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Tests for CacheExercise.
 * Run these tests to validate your Redis cache implementations.
 */
public class L05_CacheExerciseTest extends TestBase {
    private L05_CacheExercise l05CacheExercise;
    private FakeService fakeService;

    @Before
    @Override
    public void setUp() {
        super.setUp();
        fakeService = Mockito.mock(FakeService.class);
        when(fakeService.doSomething(anyString()))
                .thenAnswer(invocation -> "something was done");
        l05CacheExercise = new L05_CacheExercise(jedisPool, fakeService);
    }

    @Test
    public void testGetUserDataCacheHit() {
        String userId = "123";
        String expectedData = "Cached User Data";

        String key = getTestKey("user:" + userId + ":profile");

        try (Jedis jedis = jedisPool.getResource()) {
            jedis.setex(key, 300, expectedData);
        }

        l05CacheExercise.getUserData(userId);

        verify(fakeService, never()).doSomething(userId);
    }

    @Test
    public void testGetUserDataCacheMiss() {
        String userId = "123";
        String expectedData = "something was done";

        String key = getTestKey("user:" + userId + ":profile");

        String result = l05CacheExercise.getUserData(key);

        verify(fakeService, Mockito.times(1)).doSomething(userId);

        assertThat("Should return database data", result, is(expectedData));

        // Verify data was cached
        try (Jedis jedis = jedisPool.getResource()) {
            String cachedData = jedis.get(key);
            assertThat("Data should be cached", cachedData, is(expectedData));

            Long ttl = jedis.ttl(key);
            assertThat("Cache should have TTL", ttl, greaterThan(0L));
        }
    }

    @Test
    public void testWarmUpUserCache() {
        String[] userIds = {"user1", "user2", "user3"};

        int result = l05CacheExercise.warmUpUserCache(userIds);
        
        assertThat("Should warm up all users", result, is(3));
        
        // Verify all users are cached
        try (Jedis jedis = jedisPool.getResource()) {
            for (String userId : userIds) {
                String cachedData = jedis.get("user:" + userId);
                assertThat("User should be cached", cachedData, is(not(nullValue())));
                assertThat("Cached data should match expected format", 
                          cachedData, is("User:" + userId + ":Data"));
            }
        }
    }
}