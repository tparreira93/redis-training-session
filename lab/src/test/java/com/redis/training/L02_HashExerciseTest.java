package com.redis.training;

import org.junit.Test;
import org.junit.Before;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Tests for HashExercise.
 * Run these tests to validate your Redis hash implementations.
 */
public class L02_HashExerciseTest extends TestBase {
    private L02_HashExercise l02HashExercise;

    @Before
    @Override
    public void setUp() {
        super.setUp();
        l02HashExercise = new L02_HashExercise(jedisPool);
    }

    @Test
    public void testCreateUserProfile() {
        String key = getTestKey("userProfile");

        l02HashExercise.createUserProfile(key);

        // Verify all expected fields were set
        try (Jedis jedis = jedisPool.getResource()) {
            Map<String, String> profile = jedis.hgetAll(key);

            assertThat("Profile should contain firstname", profile.get("firstname"), is("Jane"));
            assertThat("Profile should contain lastname", profile.get("lastname"), is("Doe"));
            assertThat("Profile should contain email", profile.get("email"), is("jane.doe@example.com"));
            assertThat("Profile should contain department", profile.get("department"), is("Engineering"));
            assertThat("Profile should contain yearjoined", profile.get("yearjoined"), is("2020"));
        }
    }

    @Test
    public void testGetUserProfile() {
        String key = getTestKey("getUserProfile");

        // Set up test data
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.hset(key, "firstname", "John");
            jedis.hset(key, "lastname", "Smith");
            jedis.hset(key, "email", "john.smith@example.com");
        }

        Map<String, String> profile = l02HashExercise.getUserProfile(key);

        assertThat("Should return all hash fields", profile.size(), is(3));
        assertThat("Should contain firstname", profile.get("firstname"), is("John"));
        assertThat("Should contain lastname", profile.get("lastname"), is("Smith"));
        assertThat("Should contain email", profile.get("email"), is("john.smith@example.com"));
    }

    @Test
    public void testGetUserField() {
        String key = getTestKey("getUserField");

        // Set up test data
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.hset(key, "firstname", "Alice");
            jedis.hset(key, "department", "Marketing");
        }

        String firstname = l02HashExercise.getUserField(key, "firstname");
        String department = l02HashExercise.getUserField(key, "department");
        String nonExistent = l02HashExercise.getUserField(key, "nonexistent");

        assertThat("Should return correct firstname", firstname, is("Alice"));
        assertThat("Should return correct department", department, is("Marketing"));
        assertThat("Should return null for non-existent field", nonExistent, is(nullValue()));
    }

    @Test
    public void testUpdateUserField() {
        String key = getTestKey("updateUserField");

        // Set up initial data
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.hset(key, "department", "Engineering");
        }

        l02HashExercise.updateUserField(key, "department", "Sales");

        // Verify the field was updated
        try (Jedis jedis = jedisPool.getResource()) {
            String department = jedis.hget(key, "department");
            assertThat("Field should be updated", department, is("Sales"));
        }
    }

    @Test
    public void testHasUserField() {
        String key = getTestKey("hasUserField");

        // Set up test data
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.hset(key, "email", "test@example.com");
        }

        boolean hasEmail = l02HashExercise.hasUserField(key, "email");
        boolean hasPhone = l02HashExercise.hasUserField(key, "phone");

        assertThat("Should return true for existing field", hasEmail, is(true));
        assertThat("Should return false for non-existent field", hasPhone, is(false));
    }

    @Test
    public void testDeleteUserField() {
        String key = getTestKey("deleteUserField");

        // Set up test data
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.hset(key, "firstname", "Bob");
            jedis.hset(key, "lastname", "Johnson");
        }

        Long deleted = l02HashExercise.deleteUserField(key, "firstname");

        assertThat("Should return 1 for successful deletion", deleted, is(1L));

        // Verify field was deleted
        try (Jedis jedis = jedisPool.getResource()) {
            boolean exists = jedis.hexists(key, "firstname");
            assertThat("Field should no longer exist", exists, is(false));

            boolean lastnameExists = jedis.hexists(key, "lastname");
            assertThat("Other fields should still exist", lastnameExists, is(true));
        }
    }

    @Test
    public void testIncrementUserField() {
        String key = getTestKey("incrementUserField");

        // Test incrementing non-existent field
        Long result = l02HashExercise.incrementUserField(key, "loginCount", 1);
        assertThat("First increment should return 1", result, is(1L));

        // Test incrementing existing field
        result = l02HashExercise.incrementUserField(key, "loginCount", 5);
        assertThat("Second increment should return 6", result, is(6L));

        // Verify the value in Redis
        try (Jedis jedis = jedisPool.getResource()) {
            String value = jedis.hget(key, "loginCount");
            assertThat("Field value should be 6", value, is("6"));
        }
    }

    @Test
    public void testGetUserFields() {
        String key = getTestKey("getUserFields");

        // Set up test data
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.hset(key, "firstname", "Carol");
            jedis.hset(key, "lastname", "Williams");
            jedis.hset(key, "email", "carol@example.com");
            jedis.hset(key, "department", "HR");
        }

        List<String> values = l02HashExercise.getUserFields(key, "firstname", "email", "nonexistent");

        assertThat("Should return 3 values", values.size(), is(3));
        assertThat("First value should be firstname", values.get(0), is("Carol"));
        assertThat("Second value should be email", values.get(1), is("carol@example.com"));
        assertThat("Third value should be null for non-existent field", values.get(2), is(nullValue()));
    }
}
