package com.redis.training;

import com.redis.base.LabBase;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Transaction;

import java.util.List;

/**
 * Exercise 4: Redis Transaction Operations
 *
 * In this exercise, you'll learn to work with Redis transactions using MULTI, EXEC, WATCH, and DISCARD.
 * Redis transactions allow you to execute multiple commands atomically, ensuring all commands
 * either succeed together or fail together. You'll also learn optimistic locking with WATCH.
 *
 * Time: 20 minutes
 *
 * Learning Objectives:
 * - Execute multiple commands atomically using MULTI/EXEC
 * - Handle transaction failures gracefully
 * - Use WATCH for optimistic locking
 * - Cancel transactions with DISCARD
 * - Understand when transactions are useful vs single commands
 */
public class L04_TransactionExercise extends LabBase {

    public L04_TransactionExercise(JedisPool jedisPool) {
        super(jedisPool);
    }

    public L04_TransactionExercise() {
        super();
    }

    /**
     * Exercise 4.1: Basic Transaction - Transfer Between Accounts
     *
     * Performs an atomic transfer between two account balances.
     * This simulates transferring money from one account to another atomically.
     * Both operations (debit and credit) must succeed or both must fail.
     *
     * @param fromAccount Key name of the account to transfer from
     * @param toAccount   Key name of the account to transfer to
     * @param amount      Amount to transfer
     * @return List of results from the transaction, or null if transaction failed
     */
    public List<Object> transferFunds(String fromAccount, String toAccount, long amount) {
        // >>> START CODING CHALLENGE <<<
        // TODO: Implement using jedis.multi() to start transaction
        // 1. Get a Jedis connection
        // 2. Start transaction with multi()
        // 3. Decrement fromAccount by amount using decrBy()
        // 4. Increment toAccount by amount using incrBy()
        // 5. Execute transaction with exec()
        // Hint: Use try-with-resources for connection management
        return null;
        // >>> END CODING CHALLENGE <<<
    }

    /**
     * Exercise 4.2: Transaction Rollback with DISCARD
     *
     * Starts a transaction but discards it based on a condition.
     * This demonstrates how to cancel a transaction before execution.
     *
     * @param key       Key to check and potentially modify
     * @param threshold Only proceed if current value is below this threshold
     * @param increment Amount to increment by
     * @return "DISCARDED" if transaction was cancelled, "EXECUTED" if it completed
     */
    public String conditionalIncrement(String key, long threshold, long increment) {
        // >>> START CODING CHALLENGE <<<
        // TODO: Implement transaction with conditional discard
        // 1. Get a Jedis connection
        // 2. Get current value of key (treat non-existent as 0)
        // 3. Start transaction with multi()
        // 4. Queue increment operation with incrBy()
        // 5. Check if current value + increment would exceed threshold
        // 6. If it would exceed, use discard() and return "DISCARDED"
        // 7. Otherwise, exec() and return "EXECUTED"
        return "OK";
        // >>> END CODING CHALLENGE <<<
    }

    /**
     * Exercise 4.4: Pipeline vs Transaction
     *
     * Demonstrates the difference between pipelining and transactions.
     * Executes the same operations both ways for comparison.
     *
     * @param baseKey Base key name for operations
     * @return Array containing [pipelineResults, transactionResults] where each is a List
     */
    public List<Object>[] pipelineVsTransaction(String baseKey) {
        // >>> START CODING CHALLENGE <<<
        // TODO: Compare pipeline vs transaction execution
        // Operations to perform:
        // 1. SET baseKey + ":counter" to "0"
        // 2. INCR baseKey + ":counter"
        // 3. GET baseKey + ":counter"
        // 4. EXPIRE baseKey + ":counter" 300 (5 minutes)
        //
        // First execute using pipeline (jedis.pipelined()), then using transaction
        // Return both result lists in an array [pipelineResults, transactionResults]

        return new List[]{null, null};
        // >>> END CODING CHALLENGE <<<
    }
}
