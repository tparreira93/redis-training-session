package com.redis.training;

import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Tests for QueueExercise.
 * Run these tests to validate your Redis queue implementations.
 */
public class L07_QueueExerciseTest extends TestBase {
    private L07_QueueExercise l07QueueExercise;

    @Before
    @Override
    public void setUp() {
        super.setUp();
        l07QueueExercise = new L07_QueueExercise(jedisPool);
    }

    @Test
    public void testEnqueueNotification() {
        String type = "email";
        String recipient = "user@example.com";
        String message = "Welcome to our service!";
        String prefixKey = getTestKey();

        Long queueLength = l07QueueExercise.enqueueNotification(prefixKey, type, recipient, message);

        assertThat("Queue length should be 1", queueLength, is(1L));

        // Verify message was added to correct queue
        try (Jedis jedis = jedisPool.getResource()) {
            String queuedMessage = jedis.rpop(prefixKey + "queue:" + type);
            String expectedMessage = type + ":" + recipient + ":" + message;
            assertThat("Message should match expected format", queuedMessage, is(expectedMessage));
        }
    }

    @Test
    public void testProcessNotificationQueue() {
        String type = "sms";
        String prefixKey = getTestKey();
        
        // Add test messages
        l07QueueExercise.enqueueNotification(prefixKey, type, "1234567890", "Message 1");
        l07QueueExercise.enqueueNotification(prefixKey, type, "0987654321", "Message 2");
        l07QueueExercise.enqueueNotification(prefixKey, type, "1111111111", "Message 3");

        String[] processed = l07QueueExercise.processNotificationQueue(prefixKey, type, 2);

        assertThat("Should process 2 messages", processed.length, is(2));
        assertThat("Should process in FIFO order", processed[0], is(prefixKey + "sms:1234567890:Message 1"));
        assertThat("Should process in FIFO order", processed[1], is(prefixKey + "sms:0987654321:Message 2"));

        // Verify one message remains
        try (Jedis jedis = jedisPool.getResource()) {
            Long remainingCount = jedis.llen(prefixKey + "queue:" + type);
            assertThat("One message should remain", remainingCount, is(1L));
        }
    }

    @Test
    public void testProcessEmptyQueue() {
        String prefixKey = getTestKey();
        String[] processed = l07QueueExercise.processNotificationQueue(prefixKey, "empty", 5);
        
        assertThat("Should return empty array for empty queue", processed.length, is(0));
    }

    @Test
    public void testProcessNextPriorityNotification() {
        String prefixKey = getTestKey();
        // Add normal priority message
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.lpush(prefixKey + "queue:normal_priority", "normal:user1:Normal message");
        }

        // Add high priority message
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.lpush(prefixKey + "queue:high_priority", "high:user2:Urgent message");
        }

        // Should get high priority first
        String firstMessage = l07QueueExercise.processNextPriorityNotification(prefixKey);
        assertThat("Should get high priority message first", 
                  firstMessage, is("high:user2:Urgent message"));

        // Should get normal priority next
        String secondMessage = l07QueueExercise.processNextPriorityNotification(prefixKey);
        assertThat("Should get normal priority message next", 
                  secondMessage, is("normal:user1:Normal message"));

        // Should return null when empty
        String thirdMessage = l07QueueExercise.processNextPriorityNotification(prefixKey);
        assertThat("Should return null when queues are empty", thirdMessage, is(nullValue()));
    }
}