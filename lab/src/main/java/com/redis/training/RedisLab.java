package com.redis.training;

/**
 * Simple main class for the Redis Training Lab application.
 * This serves as the entry point when running the application with `./gradlew run`.
 */
public class RedisLab {
    public static void main(String[] args) {
        System.out.println("🚀 Redis Training Lab - Welcome!");
        System.out.println("📚 This project contains 8 Redis exercises (L01-L08)");
        System.out.println("🔧 To get started:");
        System.out.println("   1. Start Redis: docker compose up -d");
        System.out.println("   2. Run tests: ./gradlew test");
        System.out.println("   3. Run specific exercise tests: ./gradlew stringTests");
        System.out.println("✨ Happy learning with Redis!");
    }
}
