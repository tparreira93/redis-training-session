package com.redis.training;

import org.junit.Test;
import org.junit.Before;
import redis.clients.jedis.Jedis;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Tests for TransactionExercise.
 * Run these tests to validate your Redis transaction implementations.
 * <p>
 */
public class L04_TransactionExerciseTest extends TestBase {
    private L04_TransactionExercise l04TransactionExercise;

    @Before
    @Override
    public void setUp() {
        super.setUp();
        l04TransactionExercise = new L04_TransactionExercise(jedisPool);
    }

    @Test
    public void testTransferFunds() {
        String fromAccount = getTestKey("account_from");
        String toAccount = getTestKey("account_to");

        // Setup initial balances
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set(fromAccount, "100");
            jedis.set(toAccount, "50");
        }

        List<Object> result = l04TransactionExercise.transferFunds(fromAccount, toAccount, 30);

        assertThat("Transaction should return results", result, is(not(nullValue())));
        assertThat("Transaction should have 2 results", result.size(), is(2));

        // Verify final balances
        try (Jedis jedis = jedisPool.getResource()) {
            String fromBalance = jedis.get(fromAccount);
            String toBalance = jedis.get(toAccount);

            assertThat("From account should be decremented", fromBalance, is("70"));
            assertThat("To account should be incremented", toBalance, is("80"));
        }
    }

    @Test
    public void testTransferFundsWithNonExistentAccounts() {
        String fromAccount = getTestKey("new_account_from");
        String toAccount = getTestKey("new_account_to");

        try (Jedis jedis = jedisPool.getResource()) {
            List<Object> result = l04TransactionExercise.transferFunds(fromAccount, toAccount, 25);

            assertThat("Transaction should return results", result, is(not(nullValue())));

        }

        // Verify balances (decrBy on non-existent key creates negative value)
        try (Jedis jedis = jedisPool.getResource()) {
            String fromBalance = jedis.get(fromAccount);
            String toBalance = jedis.get(toAccount);

            assertThat("From account should be -25", fromBalance, is("-25"));
            assertThat("To account should be 25", toBalance, is("25"));
        }
    }

    @Test
    public void testConditionalIncrementExecuted() {
        String key = getTestKey("conditional_incr");
        long threshold = 100;
        long increment = 10;

        // Setup initial value below threshold
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set(key, "50");
        }

        String result = l04TransactionExercise.conditionalIncrement(key, threshold, increment);

        assertThat("Transaction should be executed", result, is("EXECUTED"));

        // Verify value was incremented
        try (Jedis jedis = jedisPool.getResource()) {
            String newValue = jedis.get(key);
            assertThat("Value should be incremented", newValue, is("60"));
        }
    }

    @Test
    public void testConditionalIncrementDiscarded() {
        String key = getTestKey("conditional_incr_discard");
        long threshold = 100;
        long increment = 10;

        // Setup initial value that would exceed threshold when incremented
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set(key, "95");
        }

        String result = l04TransactionExercise.conditionalIncrement(key, threshold, increment);

        assertThat("Transaction should be discarded", result, is("DISCARDED"));

        // Verify value was not changed
        try (Jedis jedis = jedisPool.getResource()) {
            String currentValue = jedis.get(key);
            assertThat("Value should remain unchanged", currentValue, is("95"));
        }
    }

    @Test
    public void testPipelineVsTransaction() {
        String baseKey = getTestKey("pipeline_vs_tx");

        List<Object>[] results = l04TransactionExercise.pipelineVsTransaction(baseKey);

        assertThat("Should return array of 2 result lists", results, is(not(nullValue())));
        assertThat("Should return exactly 2 result lists", results.length, is(2));

        List<Object> pipelineResults = results[0];
        List<Object> transactionResults = results[1];

        assertThat("Pipeline results should not be null", pipelineResults, is(not(nullValue())));
        assertThat("Transaction results should not be null", transactionResults, is(not(nullValue())));

        assertThat("Pipeline should have 4 results", pipelineResults.size(), is(4));
        assertThat("Transaction should have 4 results", transactionResults.size(), is(4));

        // Both should produce similar results for these operations
        // Note: The actual values might differ slightly due to timing, but structure should be same
        assertThat("Both should set counter to 0", pipelineResults.get(0), is(transactionResults.get(0)));
    }

    @Test
    public void testTransactionAtomicity() {
        String key1 = getTestKey("atomic1");
        String key2 = getTestKey("atomic2");

        // Use transfer funds to test atomicity
        // Both operations should complete or both should fail
        List<Object> result = l04TransactionExercise.transferFunds(key1, key2, 50);

        assertThat("Transaction should complete", result, is(not(nullValue())));

        // Verify both operations completed
        try (Jedis jedis = jedisPool.getResource()) {
            assertThat("Both keys should exist after transaction",
                    keyExists(key1) && keyExists(key2), is(true));
        }
    }
}
