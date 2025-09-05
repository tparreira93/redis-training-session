package com.redis.training;

import org.junit.Test;
import org.junit.Before;
import redis.clients.jedis.Jedis;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Tests for ListExercise.
 * Run these tests to validate your Redis list implementations.
 */
public class L03_ListExerciseTest extends TestBase {
    private L03_ListExercise l03ListExercise;

    @Before
    @Override
    public void setUp() {
        super.setUp();
        l03ListExercise = new L03_ListExercise(jedisPool);
    }

    @Test
    public void testEnqueue() {
        String queueKey = getTestKey("queue");

        Long length1 = l03ListExercise.enqueue(queueKey, "item1");
        Long length2 = l03ListExercise.enqueue(queueKey, "item2");

        assertThat("First enqueue should return length 1", length1, is(1L));
        assertThat("Second enqueue should return length 2", length2, is(2L));

        // Verify items are in correct order (FIFO)
        try (Jedis jedis = jedisPool.getResource()) {
            List<String> items = jedis.lrange(queueKey, 0, -1);
            assertThat("Should have 2 items", items.size(), is(2));
            assertThat("First item should be at head", items.get(0), is("item1"));
            assertThat("Second item should be at tail", items.get(1), is("item2"));
        }
    }

    @Test
    public void testDequeue() {
        String queueKey = getTestKey("dequeue");

        // Set up test data
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.rpush(queueKey, "first", "second", "third");
        }

        String item1 = l03ListExercise.dequeue(queueKey);
        String item2 = l03ListExercise.dequeue(queueKey);

        assertThat("Should dequeue first item", item1, is("first"));
        assertThat("Should dequeue second item", item2, is("second"));

        // Verify remaining item
        try (Jedis jedis = jedisPool.getResource()) {
            Long length = jedis.llen(queueKey);
            assertThat("Should have 1 item remaining", length, is(1L));
        }
    }

    @Test
    public void testDequeueEmptyQueue() {
        String queueKey = getTestKey("emptyQueue");

        String result = l03ListExercise.dequeue(queueKey);
        assertThat("Dequeue from empty queue should return null", result, is(nullValue()));
    }

    @Test
    public void testPushToStack() {
        String stackKey = getTestKey("stack");

        Long length1 = l03ListExercise.pushToStack(stackKey, "bottom");
        Long length2 = l03ListExercise.pushToStack(stackKey, "top");

        assertThat("First push should return length 1", length1, is(1L));
        assertThat("Second push should return length 2", length2, is(2L));

        // Verify items are in correct order (LIFO)
        try (Jedis jedis = jedisPool.getResource()) {
            List<String> items = jedis.lrange(stackKey, 0, -1);
            assertThat("Should have 2 items", items.size(), is(2));
            assertThat("Last pushed item should be at head", items.get(0), is("top"));
            assertThat("First pushed item should be at tail", items.get(1), is("bottom"));
        }
    }

    @Test
    public void testPopFromStack() {
        String stackKey = getTestKey("popStack");

        // Set up test data (LIFO order)
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.lpush(stackKey, "first", "second", "third");
        }

        String item1 = l03ListExercise.popFromStack(stackKey);
        String item2 = l03ListExercise.popFromStack(stackKey);

        assertThat("Should pop most recent item", item1, is("third"));
        assertThat("Should pop second most recent item", item2, is("second"));

        // Verify remaining item
        try (Jedis jedis = jedisPool.getResource()) {
            Long length = jedis.llen(stackKey);
            assertThat("Should have 1 item remaining", length, is(1L));
        }
    }

    @Test
    public void testPopFromEmptyStack() {
        String stackKey = getTestKey("emptyStack");

        String result = l03ListExercise.popFromStack(stackKey);
        assertThat("Pop from empty stack should return null", result, is(nullValue()));
    }

    @Test
    public void testBlockingDequeue() {
        String queueKey = getTestKey("blockingQueue");

        // Set up test data
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.rpush(queueKey, "available");
        }

        List<String> result = l03ListExercise.blockingDequeue(queueKey, 1);

        assertThat("Should return list with key and value", result, is(notNullValue()));
        assertThat("Should have 2 elements", result.size(), is(2));
        assertThat("First element should be key", result.get(0), is(queueKey));
        assertThat("Second element should be value", result.get(1), is("available"));
    }

    @Test
    public void testBlockingDequeueTimeout() {
        String queueKey = getTestKey("emptyBlockingQueue");

        long startTime = System.currentTimeMillis();
        List<String> result = l03ListExercise.blockingDequeue(queueKey, 1);
        long endTime = System.currentTimeMillis();

        assertThat("Should return null on timeout", result, is(nullValue()));
        assertThat("Should wait approximately 1 second", endTime - startTime, greaterThan(900L));
    }

    @Test
    public void testGetListLength() {
        String listKey = getTestKey("lengthTest");

        // Test empty list
        Long emptyLength = l03ListExercise.getListLength(listKey);
        assertThat("Empty list should have length 0", emptyLength, is(0L));

        // Add items and test length
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.rpush(listKey, "item1", "item2", "item3");
        }

        Long length = l03ListExercise.getListLength(listKey);
        assertThat("List should have length 3", length, is(3L));
    }

    @Test
    public void testGetListRange() {
        String listKey = getTestKey("rangeTest");

        // Set up test data
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.rpush(listKey, "a", "b", "c", "d", "e");
        }

        List<String> range = l03ListExercise.getListRange(listKey, 1, 3);

        assertThat("Should return 3 items", range.size(), is(3));
        assertThat("Should return correct range", range, contains("b", "c", "d"));
    }

    @Test
    public void testGetListRangeNegativeIndices() {
        String listKey = getTestKey("negativeRange");

        // Set up test data
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.rpush(listKey, "first", "second", "third", "fourth", "last");
        }

        List<String> lastTwo = l03ListExercise.getListRange(listKey, -2, -1);

        assertThat("Should return last 2 items", lastTwo.size(), is(2));
        assertThat("Should return correct items", lastTwo, contains("fourth", "last"));
    }

    @Test
    public void testGetAllListItems() {
        String listKey = getTestKey("allItems");

        // Set up test data
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.rpush(listKey, "one", "two", "three");
        }

        List<String> allItems = l03ListExercise.getAllListItems(listKey);

        assertThat("Should return all items", allItems.size(), is(3));
        assertThat("Should return items in order", allItems, contains("one", "two", "three"));
    }

    @Test
    public void testTrimList() {
        String listKey = getTestKey("trimTest");

        // Set up test data
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.rpush(listKey, "a", "b", "c", "d", "e", "f");
        }

        l03ListExercise.trimList(listKey, 1, 4);

        // Verify list was trimmed correctly
        try (Jedis jedis = jedisPool.getResource()) {
            List<String> remaining = jedis.lrange(listKey, 0, -1);
            assertThat("Should have 4 items remaining", remaining.size(), is(4));
            assertThat("Should keep correct range", remaining, contains("b", "c", "d", "e"));
        }
    }
}
