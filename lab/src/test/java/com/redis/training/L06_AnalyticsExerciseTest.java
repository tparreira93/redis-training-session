package com.redis.training;

import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

/**
 * Tests for AnalyticsExercise.
 * Run these tests to validate your Redis analytics implementations.
 */
public class L06_AnalyticsExerciseTest extends TestBase {
    private L06_AnalyticsExercise l06AnalyticsExercise;

    @Before
    @Override
    public void setUp() {
        super.setUp();
        l06AnalyticsExercise = new L06_AnalyticsExercise(jedisPool);
    }

    @Test
    public void testTrackPageView() {
        String pageId = "home";
        String date = "2024-01-15";
        String keyPrefix = getTestKey();

        // Track multiple page views
        Long firstView = l06AnalyticsExercise.trackPageView(keyPrefix, pageId, date);
        Long secondView = l06AnalyticsExercise.trackPageView(keyPrefix, pageId, date);
        Long thirdView = l06AnalyticsExercise.trackPageView(keyPrefix, pageId, date);

        assertThat("First view should return 1", firstView, is(1L));
        assertThat("Second view should return 2", secondView, is(2L));
        assertThat("Third view should return 3", thirdView, is(3L));

        // Verify daily counter has TTL
        try (Jedis jedis = jedisPool.getResource()) {
            Long ttl = jedis.ttl(keyPrefix + "pageviews:" + pageId + ":" + date);
            assertThat("Daily counter should have TTL", ttl, greaterThan(0L));
        }
    }

    @Test
    public void testTrackTimeSpent() {
        String userId = "user123";
        String pageId = "article1";
        String keyPrefix = getTestKey();

        Long firstSession = l06AnalyticsExercise.trackTimeSpent(keyPrefix, userId, pageId, 120);
        Long secondSession = l06AnalyticsExercise.trackTimeSpent(keyPrefix, userId, pageId, 180);

        assertThat("First session should return 120", firstSession, is(120L));
        assertThat("Second session should return 300", secondSession, is(300L));

        // Verify page total was also updated
        try (Jedis jedis = jedisPool.getResource()) {
            String pageTotal = jedis.get(keyPrefix + "time:page:" + pageId + ":total");
            assertThat("Page total should be updated", Long.parseLong(pageTotal), is(300L));
        }
    }

    @Test
    public void testGetPageAnalytics() {
        String pageId = "product1";
        String date = "2024-01-15";
        String keyPrefix = getTestKey();

        // Set up test data
        l06AnalyticsExercise.trackPageView(keyPrefix, pageId, date);
        l06AnalyticsExercise.trackPageView(keyPrefix, pageId, date);
        l06AnalyticsExercise.trackTimeSpent(keyPrefix, "user1", pageId, 150);
        l06AnalyticsExercise.trackTimeSpent(keyPrefix, "user2", pageId, 250);

        Long[] analytics = l06AnalyticsExercise.getPageAnalytics(keyPrefix, pageId, date);

        assertThat("Daily views should be 2", analytics[0], is(2L));
        assertThat("Total views should be 2", analytics[1], is(2L));
        assertThat("Total time should be 400", analytics[2], is(400L));
    }

    @Test
    public void testGetPageAnalyticsNoData() {
        String pageId = "nonexistent";
        String date = "2024-01-15";
        String keyPrefix = getTestKey();

        Long[] analytics = l06AnalyticsExercise.getPageAnalytics(keyPrefix, pageId, date);

        assertThat("Daily views should be 0", analytics[0], is(0L));
        assertThat("Total views should be 0", analytics[1], is(0L));
        assertThat("Total time should be 0", analytics[2], is(0L));
    }
}