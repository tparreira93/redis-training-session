package com.redis.training;

import com.redis.base.LabBase;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

/**
 * Exercise 9: Redis Flash Sale Operations
 *
 * In this exercise, you'll implement an e-commerce flash sale system using Redis transactions
 * and WATCH commands. You'll learn to handle atomic stock deduction and order creation,
 * which is critical for preventing overselling in high-concurrency scenarios.
 *
 * Time: 25 minutes
 *
 * Learning Objectives:
 * - Implement atomic stock deduction using WATCH and transactions
 * - Handle race conditions in high-concurrency scenarios
 * - Build order creation with inventory validation
 * - Use optimistic locking for flash sale scenarios
 */
public class L08_FlashSaleExercise extends LabBase {

    public L08_FlashSaleExercise(JedisPool jedisPool) {
        super(jedisPool);
    }

    public L08_FlashSaleExercise() {
        super();
    }

    /**
     * Exercise 8.1: Initialize Product Stock
     *
     * Sets up initial stock for flash sale products.
     * This simulates product inventory setup before a flash sale begins.
     *
     * @param prefixKey Key prefix
     * @param productId Product identifier
     * @param initialStock Initial stock quantity
     * @param price Product price
     * @return true if initialization succeeded
     */
    public boolean initializeProduct(String prefixKey, String productId, int initialStock, double price) {
        // >>> START CODING CHALLENGE <<<
        // TODO: Implement product initialization
        // 1. Set stock: "<prefixKey>stock:<productId>" to initialStock
        // 2. Set price: "<prefixKey>price:<productId>" to price
        // 3. Initialize sold counter: "<prefixKey>sold:<productId>" to 0
        // 4. Return true if all operations succeeded
        return false;
        // >>> END CODING CHALLENGE <<<
    }

    public String purchaseProduct(String prefixKey, String productId, String userId, int quantity) {
        try (Jedis jedis = jedisPool.getResource()) {
            return purchaseProduct(jedis, prefixKey, productId, userId, quantity);
        }
    }

    /**
     * Exercise 8.2: Atomic Purchase Operation
     *
     * Performs atomic stock deduction and order creation using WATCH and transactions.
     * This prevents overselling by ensuring stock checks and deductions are atomic.
     *
     * @param prefixKey Key prefix
     * @param productId Product identifier
     * @param userId User making the purchase
     * @param quantity Quantity to purchase
     * @return "SUCCESS" if purchase succeeded, "OUT_OF_STOCK" if insufficient stock, "FAILED" if transaction failed
     */
    public String purchaseProduct(Jedis jedis, String prefixKey, String productId, String userId, int quantity) {
        // >>> START CODING CHALLENGE <<<
        // TODO: Implement atomic purchase with optimistic locking
        // 1. Use Jedis connection
        // 2. WATCH the stock key: "<prefixKey>stock:<productId>"
        // 3. GET current stock
        // 4. Check if sufficient stock available
        // 5. If sufficient, start MULTI transaction:
        //    - DECRBY stock by quantity
        //    - INCRBY sold counter by quantity
        //    - SET order: "<prefixKey>order:<userId>:<productId>:<timestamp>" with quantity
        // 6. EXEC transaction
        // 7. Return appropriate status based on result
        return "OUT_OF_STOCK";
        // >>> END CODING CHALLENGE <<<
    }

    /**
     * Exercise 8.3: Get Product Status
     *
     * Retrieves current product status including stock, sold count, and price.
     * This is used for displaying real-time flash sale information.
     *
     * @param prefixKey Key prefix
     * @param productId Product identifier
     * @return Array containing [currentStock, soldCount, price] or null if product doesn't exist
     */
    public Object[] getProductStatus(String prefixKey, String productId) {
        // >>> START CODING CHALLENGE <<<
        // TODO: Implement product status retrieval
        // 1. Get current stock from "<prefixKey>stock:<productId>"
        // 2. Get sold count from "<prefixKey>sold:<productId>"
        // 3. Get price from "<prefixKey>price:<productId>"
        // 4. Return array [stock, sold, price] as [Integer, Integer, Double]
        // 5. Return null if product doesn't exist
        return null;
        // >>> END CODING CHALLENGE <<<
    }
}
