package com.redis.training;

import com.redis.base.LabBase;
import redis.clients.jedis.JedisPool;

/**
 * Exercise 6: Redis Analytics Operations
 *
 * In this exercise, you'll implement real-time analytics tracking using Redis.
 * You'll track page views, clicks, and time spent using Redis numeric operations
 * and learn how to build analytics dashboards with Redis counters.
 *
 * Time: 20 minutes
 *
 * Learning Objectives:
 * - Track real-time metrics using Redis counters
 * - Implement time-based analytics with expiration
 * - Use atomic increment operations for concurrent analytics
 * - Build analytics aggregation patterns
 */
public class L06_AnalyticsExercise extends LabBase {

    public L06_AnalyticsExercise(JedisPool jedisPool) {
        super(jedisPool);
    }

    public L06_AnalyticsExercise() {
        super();
    }

    /**
     * Exercise 6.1: Track Page Views
     *
     * Tracks page views for different pages with daily and total counters.
     * This is essential for understanding user engagement and popular content.
     *
     * @param keyPrefix Key prefix
     * @param pageId Page identifier
     * @param date Date in YYYY-MM-DD format
     * @return Current total page views for this page
     */
    public Long trackPageView(String keyPrefix, String pageId, String date) {
        // >>> START CODING CHALLENGE <<<
        // TODO: Implement page view tracking
        // 1. Increment daily counter: "<keyPrefix>pageviews:<pageId>:<date>" with 24h TTL
        // 2. Increment total counter: "<keyPrefix>pageviews:<pageId>:total" (no TTL)
        // 3. Return the current total page views
        return null;
        // >>> END CODING CHALLENGE <<<
    }

    /**
     * Exercise 6.2: Track User Session Time
     *
     * Tracks time spent by users on pages using Redis numeric operations.
     * This helps understand user engagement and content effectiveness.
     *
     * @param keyPrefix Key prefix
     * @param userId User identifier
     * @param pageId Page identifier
     * @param timeSpentSeconds Time spent in seconds
     * @return Total time spent by this user on this page
     */
    public Long trackTimeSpent(String keyPrefix, String userId, String pageId, long timeSpentSeconds) {
        // >>> START CODING CHALLENGE <<<
        // TODO: Implement time tracking
        // 1. Add time to user's total time on page: "<keyPrefix>time:<userId>:<pageId>"
        // 2. Add time to page's total time: "<keyPrefix>time:page:<pageId>:total"
        // 3. Set 7-day TTL on user-specific key
        // 4. Return user's total time on this page
        return null;
        // >>> END CODING CHALLENGE <<<
    }

    /**
     * Exercise 6.3: Get Analytics Summary
     *
     * Retrieves analytics summary for a page including views and average time spent.
     * This demonstrates how to build analytics dashboards with Redis data.
     *
     * @param keyPrefix Key prefix
     * @param pageId Page identifier
     * @param date Date in YYYY-MM-DD format
     * @return Array containing [dailyViews, totalViews, totalTimeSpent]
     */
    public Long[] getPageAnalytics(String keyPrefix, String pageId, String date) {
        // >>> START CODING CHALLENGE <<<
        // TODO: Implement analytics summary
        // 1. Get daily page views from "<keyPrefix>pageviews:<pageId>:<date>"
        // 2. Get total page views from "<keyPrefix>pageviews:<pageId>:total"
        // 3. Get total time spent from "<keyPrefix>time:page:<pageId>:total"
        // 4. Return array with [dailyViews, totalViews, totalTimeSpent]
        // 5. Use 0 for missing values
        return new Long[]{0L, 0L, 0L};
        // >>> END CODING CHALLENGE <<<
    }
}