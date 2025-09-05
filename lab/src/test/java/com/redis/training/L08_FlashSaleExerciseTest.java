package com.redis.training;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import redis.clients.jedis.Jedis;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;

/**
 * Tests for FlashSaleExercise.
 * Run these tests to validate your Redis flash sale implementations.
 */
public class L08_FlashSaleExerciseTest extends TestBase {
    private L08_FlashSaleExercise l09FlashSaleExercise;

    @Before
    @Override
    public void setUp() {
        super.setUp();
        l09FlashSaleExercise = new L08_FlashSaleExercise(jedisPool);
    }

    @Test
    public void testInitializeProduct() {
        String productId = "flash_product_1";
        int initialStock = 100;
        double price = 99.99;
        String prefixKey = getTestKey();

        boolean result = l09FlashSaleExercise.initializeProduct(prefixKey, productId, initialStock, price);

        assertThat("Product initialization should succeed", result, is(true));

        // Verify initialization
        try (Jedis jedis = jedisPool.getResource()) {
            String stock = jedis.get(prefixKey +"stock:" + productId);
            String priceStr = jedis.get(prefixKey +"price:" + productId);
            String sold = jedis.get(prefixKey +"sold:" + productId);

            assertThat("Stock should be set", stock, is(String.valueOf(initialStock)));
            assertThat("Price should be set", priceStr, is(String.valueOf(price)));
            assertThat("Sold counter should be initialized", sold, is("0"));
        }
    }

    @Test
    public void testSuccessfulPurchase() {
        String productId = "flash_product_2";
        String userId = "user123";
        String prefixKey = getTestKey();
        
        // Initialize product
        l09FlashSaleExercise.initializeProduct(prefixKey, productId, 50, 29.99);

        String result = l09FlashSaleExercise.purchaseProduct(prefixKey, productId, userId, 2);

        assertThat("Purchase should succeed", result, is("SUCCESS"));

        // Verify stock was decremented and sold was incremented
        try (Jedis jedis = jedisPool.getResource()) {
            String stock = jedis.get(prefixKey +"stock:" + productId);
            String sold = jedis.get(prefixKey +"sold:" + productId);

            assertThat("Stock should be decremented", stock, is("48"));
            assertThat("Sold should be incremented", sold, is("2"));
        }
    }

    @Test
    public void testOutOfStockPurchase() {
        String productId = "flash_product_3";
        String userId = "user456";
        String prefixKey = getTestKey();
        
        // Initialize product with low stock
        l09FlashSaleExercise.initializeProduct(prefixKey, productId, 5, 19.99);

        String result = l09FlashSaleExercise.purchaseProduct(prefixKey, productId, userId, 10);

        assertThat("Purchase should fail due to insufficient stock", result, is("OUT_OF_STOCK"));

        // Verify stock unchanged
        try (Jedis jedis = jedisPool.getResource()) {
            String stock = jedis.get(prefixKey + "stock:" + productId);
            String sold = jedis.get(prefixKey +"sold:" + productId);

            assertThat("Stock should remain unchanged", stock, is("5"));
            assertThat("Sold should remain unchanged", sold, is("0"));
        }
    }

    @Test
    public void testGetProductStatus() {
        String productId = "flash_product_4";
        String prefixKey = getTestKey();
        
        // Initialize and make some purchases
        l09FlashSaleExercise.initializeProduct(prefixKey, productId, 100, 49.99);
        l09FlashSaleExercise.purchaseProduct(prefixKey, productId, "user1", 3);
        l09FlashSaleExercise.purchaseProduct(prefixKey, productId, "user2", 7);

        Object[] status = l09FlashSaleExercise.getProductStatus(prefixKey, productId);

        assertThat("Status should not be null", status != null, is(true));
        assertThat("Current stock should be 90", status[0], is(90));
        assertThat("Sold count should be 10", status[1], is(10));
        assertThat("Price should be 49.99", status[2], is(49.99));
    }

    @Test
    public void testGetNonExistentProductStatus() {
        String prefixKey = getTestKey();

        Object[] status = l09FlashSaleExercise.getProductStatus(prefixKey, "nonexistent");
        
        assertThat("Non-existent product should return null", status, is(nullValue()));
    }

    @Test
    public void testTransferFundsFailure() {
        String productId = "flash_product_5";
        String prefixKey = getTestKey();

        // Initialize product with limited stock
        l09FlashSaleExercise.initializeProduct(prefixKey, productId, 10, 9.99);

        try (Jedis jedis = jedisPool.getResource()) {
            Jedis jedisSpy = Mockito.spy(jedis);

            Mockito.doAnswer(invocation -> {
                Object res = invocation.callRealMethod();

                try (Jedis tmpJedis = jedisPool.getResource()) {
                    l09FlashSaleExercise.purchaseProduct(tmpJedis, prefixKey, productId, "user1", 3);
                }
                return res;
            }).when(jedisSpy).watch(anyString());

            String result = l09FlashSaleExercise.purchaseProduct(jedisSpy, prefixKey, productId, "user2", 10);

            assertThat("Purchase should fail", result, is("OUT_OF_STOCK"));

            Object[] status = l09FlashSaleExercise.getProductStatus(prefixKey, productId);
            assertThat("Final stock should be 7", status[0], is(7));
            assertThat("Total sold should be 3", status[1], is(3));
        }
    }
}