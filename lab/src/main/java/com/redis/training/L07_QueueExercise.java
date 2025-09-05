package com.redis.training;

import com.redis.base.LabBase;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;

/**
 * Exercise 7: Redis Queue Operations
 * <p>
 * In this exercise, you'll implement a notification service using Redis lists as queues.
 * You'll learn to build producer-consumer patterns for email and SMS notifications,
 * which are common in real-world applications for background job processing.
 * <p>
 * Time: 20 minutes
 * <p>
 * Learning Objectives:
 * - Implement producer-consumer pattern with Redis lists
 * - Use blocking operations for efficient queue processing
 * - Handle different priority queues
 * - Build reliable queue processing with error handling
 */
public class L07_QueueExercise extends LabBase {

    public L07_QueueExercise(JedisPool jedisPool) {
        super(jedisPool);
    }

    public L07_QueueExercise() {
        super();
    }

    /**
     * Exercise 7.1: Enqueue Notification
     * <p>
     * Adds a notification to the appropriate queue based on type.
     * This simulates a notification service that handles different types of messages.
     *
     * @param prefixKey Prefix for queue keys
     * @param type      Notification type ("email" or "sms")
     * @param recipient Recipient identifier
     * @param message   Message content
     * @return Queue length after adding the notification
     */
    public Long enqueueNotification(String prefixKey, String type, String recipient, String message) {
        // >>> START CODING CHALLENGE <<<
        // TODO: Implement notification enqueueing
        // 1. Create notification data: "<prefixKey><type>:<recipient>:<message>"
        // 2. Add to appropriate queue: "<prefixKey>queue:<type>" using LPUSH
        // 3. Return the queue length after adding
        return null;
        // >>> END CODING CHALLENGE <<<
    }

    /**
     * Exercise 7.2: Process Notification Queue
     * <p>
     * Processes notifications from a queue in FIFO order.
     * This simulates a worker that processes notifications from the queue.
     *
     * @param prefixKey   Prefix for queue keys
     * @param type        Queue type to process ("email" or "sms")
     * @param maxMessages Maximum number of messages to process
     * @return Array of processed notification messages
     */
    public String[] processNotificationQueue(String prefixKey, String type, int maxMessages) {
        // >>> START CODING CHALLENGE <<<
        // TODO: Implement queue processing
        // 1. Process up to maxMessages from "<prefixKey>queue:<type>" using RPOP
        // 2. Continue until queue is empty or maxMessages reached
        // 3. Return array of processed messages
        // 4. Return empty array if no messages
        return new String[] {};
        // >>> END CODING CHALLENGE <<<
    }

    /**
     * Exercise 7.3: Priority Queue Processing
     * <p>
     * Processes notifications with priority - high priority queue first, then normal.
     * This demonstrates how to implement priority queues using multiple Redis lists.
     *
     * @param prefixKey Prefix for queue keys
     * @return Next high-priority or normal notification, null if no notifications
     */
    public String processNextPriorityNotification(String prefixKey) {
        // >>> START CODING CHALLENGE <<<
        // TODO: Implement priority queue processing
        // 1. Try to get message from "<prefixKey>queue:high_priority" using RPOP
        // 2. If high priority queue is empty, try "<prefixKey>queue:normal_priority"
        // 3. Return the message or null if both queues are empty
        return null;
        // >>> END CODING CHALLENGE <<<
    }
}