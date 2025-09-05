package com.redis.training;

import com.redis.base.LabBase;
import redis.clients.jedis.JedisPool;
import java.util.List;

/**
 * Exercise 3: Redis List Operations
 *
 * Lists are ordered collections of strings, sorted by insertion order.
 * They're perfect for implementing queues, stacks, and activity feeds.
 * Lists support blocking operations, making them ideal for producer-consumer patterns.
 *
 * Time: 15 minutes
 *
 * Learning Objectives:
 * - Implement queue (FIFO) and stack (LIFO) patterns
 * - Use blocking operations for producer-consumer scenarios
 * - Understand list indexing and range operations
 * - Work with list trimming and length operations
 */
public class L03_ListExercise extends LabBase {

    public L03_ListExercise(JedisPool jedisPool) {
        super(jedisPool);
    }

    public L03_ListExercise() {
        super();
    }

    /**
     * Exercise 3.1: Adding Items to Queue (FIFO)
     *
     * Adds an item to the tail of the list (right side).
     * This implements a FIFO (First In, First Out) queue pattern.
     *
     * @param queueKey Redis key for the queue
     * @param item     Item to add to the queue
     * @return Length of the list after the push operation
     */
    public Long enqueue(String queueKey, String item) {
        // >>> START CODING CHALLENGE <<<
        // TODO: Implement using jedis.rpush() (right push)

        return 0L;
        // >>> END CODING CHALLENGE <<<
    }

    /**
     * Exercise 3.2: Removing Items from Queue (FIFO)
     *
     * Removes and returns an item from the head of the list (left side).
     * Combined with enqueue(), this implements a complete FIFO queue.
     *
     * @param queueKey Redis key for the queue
     * @return The item that was removed, or null if list is empty
     */
    public String dequeue(String queueKey) {
        // >>> START CODING CHALLENGE <<<
        // TODO: Implement using jedis.lpop() (left pop)
        return null;
        // >>> END CODING CHALLENGE <<<
    }

    /**
     * Exercise 3.3: Adding Items to Stack (LIFO)
     *
     * Adds an item to the head of the list (left side).
     * This implements a LIFO (Last In, First Out) stack pattern.
     *
     * @param stackKey Redis key for the stack
     * @param item     Item to push onto the stack
     * @return Length of the list after the push operation
     */
    public Long pushToStack(String stackKey, String item) {
        // >>> START CODING CHALLENGE <<<
        // TODO: Implement using jedis.lpush() (left push)
        return 0L;
        // >>> END CODING CHALLENGE <<<
    }

    /**
     * Exercise 3.4: Removing Items from Stack (LIFO)
     *
     * Removes and returns an item from the head of the list (left side).
     * Combined with pushToStack(), this implements a complete LIFO stack.
     *
     * @param stackKey Redis key for the stack
     * @return The item that was removed, or null if list is empty
     */
    public String popFromStack(String stackKey) {
        // >>> START CODING CHALLENGE <<<
        // TODO: Implement using jedis.lpop() (left pop)
        return null;
        // >>> END CODING CHALLENGE <<<
    }

    /**
     * Exercise 3.5: Blocking Queue Operation
     *
     * Removes and returns an item from the head of the list.
     * If the list is empty, blocks for up to timeout seconds waiting for an item.
     * This is perfect for producer-consumer patterns.
     *
     * @param queueKey Redis key for the queue
     * @param timeout  Maximum time to wait in seconds (0 = wait forever)
     * @return List containing the key and the item, or null if timeout
     */
    public List<String> blockingDequeue(String queueKey, int timeout) {
        // >>> START CODING CHALLENGE <<<
        // TODO: Implement using jedis.blpop()
        // Note: blpop returns a List with [key, value] or null if timeout
        return null;
        // >>> END CODING CHALLENGE <<<
    }

    /**
     * Exercise 3.6: Getting List Length
     *
     * Returns the length of the list stored at key.
     * If key doesn't exist, returns 0.
     *
     * @param listKey Redis key for the list
     * @return Length of the list
     */
    public Long getListLength(String listKey) {
        // >>> START CODING CHALLENGE <<<
        // TODO: Implement using jedis.llen()
        return 0L;
        // >>> END CODING CHALLENGE <<<
    }

    /**
     * Exercise 3.7: Getting List Range
     *
     * Returns the specified elements of the list stored at key.
     * Start and stop are zero-based indexes.
     * Use negative indexes to count from the end (-1 is the last element).
     *
     * @param listKey Redis key for the list
     * @param start   Start index (inclusive)
     * @param stop    Stop index (inclusive)
     * @return List of elements in the specified range
     */
    public List<String> getListRange(String listKey, long start, long stop) {
        // >>> START CODING CHALLENGE <<<
        // TODO: Implement using jedis.lrange()
        return null;
        // >>> END CODING CHALLENGE <<<
    }

    /**
     * Exercise 3.8: Getting All List Items
     *
     * Returns all elements in the list stored at key.
     * This is equivalent to LRANGE key 0 -1.
     *
     * @param listKey Redis key for the list
     * @return List containing all elements
     */
    public List<String> getAllListItems(String listKey) {
        // >>> START CODING CHALLENGE <<<
        // TODO: Implement using jedis.lrange() with start=0, stop=-1
        return null;
        // >>> END CODING CHALLENGE <<<
    }

    /**
     * Exercise 3.9: Trimming List
     *
     * Trims the list to contain only the specified range of elements.
     * Elements outside the range are removed.
     * This is useful for maintaining a fixed-size list (e.g., recent activity feed).
     *
     * @param listKey Redis key for the list
     * @param start   Start index to keep (inclusive)
     * @param stop    Stop index to keep (inclusive)
     */
    public void trimList(String listKey, long start, long stop) {
        // >>> START CODING CHALLENGE <<<
        // TODO: Implement using jedis.ltrim()

        // >>> END CODING CHALLENGE <<<
    }
}
