package com.redis.training;

import org.junit.Test;
import org.junit.Before;
import redis.clients.jedis.Jedis;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Tests for StringExercise.
 * Run these tests to validate your Redis string implementations.
 */
public class L01_StringExerciseTest extends TestBase {
    private L01_StringExercise l01StringExercise;

    @Before
    @Override
    public void setUp() {
        super.setUp();
        l01StringExercise = new L01_StringExercise(jedisPool);
    }

    @Test
    public void testSetString() {
        String key = getTestKey("setString");
        String value = "Hello Redis!";

        l01StringExercise.setString(key, value);

        // Verify the value was set correctly
        try (Jedis jedis = jedisPool.getResource()) {
            String storedValue = jedis.get(key);
            assertThat("String should be stored correctly", storedValue, is(value));
        }
    }

    @Test
    public void testGetString() {
        String key = getTestKey("getString");
        String value = "Test Value";

        // Set up test data
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set(key, value);
        }

        String result = l01StringExercise.getString(key);
        assertThat("Should retrieve the correct string value", result, is(value));
    }

    @Test
    public void testGetStringNonExistent() {
        String key = getTestKey("nonExistent");

        String result = l01StringExercise.getString(key);
        assertThat("Should return null for non-existent key", result, is(nullValue()));
    }

    @Test
    public void testSetExpiry() {
        String key = getTestKey("setExpiry");
        String value = "Expiring Value";

        // Set up test data
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set(key, value);
        }

        l01StringExercise.setExpiry(key, 60);

        Long ttl = getKeyTTL(key);
        assertThat("Key should have expiration set", ttl, greaterThan(0L));
        assertThat("TTL should be around 60 seconds", ttl, lessThanOrEqualTo(60L));
    }

    @Test
    public void testGetTimeToLive() {
        String key = getTestKey("getTimeToLive");

        // Test key with no expiration
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set(key, "value");
        }

        Long ttl = l01StringExercise.getTimeToLive(key);
        assertThat("Key without expiration should return -1", ttl, is(-1L));

        // Test key with expiration
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.expire(key, 30);
        }

        ttl = l01StringExercise.getTimeToLive(key);
        assertThat("Key with expiration should return positive value", ttl, greaterThan(0L));
    }

    @Test
    public void testGetTimeToLiveNonExistent() {
        String key = getTestKey("nonExistent");

        Long ttl = l01StringExercise.getTimeToLive(key);
        assertThat("Non-existent key should return -2", ttl, is(-2L));
    }

    @Test
    public void testGetLastFiveCharacters() {
        String key = getTestKey("getLastFive");
        String value = "Hello Redis";

        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set(key, value);
        }

        String result = l01StringExercise.getLastFiveCharacters(key);
        assertThat("Should return last 5 characters", result, is("Redis"));
    }

    @Test
    public void testGetLastFiveCharactersShortString() {
        String key = getTestKey("shortString");
        String value = "Hi";

        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set(key, value);
        }

        String result = l01StringExercise.getLastFiveCharacters(key);
        assertThat("Should return entire string if shorter than 5 chars", result, is("Hi"));
    }

    @Test
    public void testIncrementCounter() {
        String key = getTestKey("counter");

        // Test incrementing non-existent key
        Long result = l01StringExercise.incrementCounter(key);
        assertThat("First increment should return 1", result, is(1L));

        // Test incrementing existing key
        result = l01StringExercise.incrementCounter(key);
        assertThat("Second increment should return 2", result, is(2L));

        // Verify the value in Redis
        try (Jedis jedis = jedisPool.getResource()) {
            String value = jedis.get(key);
            assertThat("Counter value should be 2", value, is("2"));
        }
    }

    @Test
    public void testSetWithExpiration() {
        String key = getTestKey("setWithExpiration");
        String value = "Expiring Value";
        int seconds = 30;

        l01StringExercise.setWithExpiration(key, value, seconds);

        // Verify value was set
        try (Jedis jedis = jedisPool.getResource()) {
            String storedValue = jedis.get(key);
            assertThat("Value should be set correctly", storedValue, is(value));
        }

        // Verify expiration was set
        Long ttl = getKeyTTL(key);
        assertThat("Key should have expiration", ttl, greaterThan(0L));
        assertThat("TTL should be around 30 seconds", ttl, lessThanOrEqualTo(30L));
    }
}
