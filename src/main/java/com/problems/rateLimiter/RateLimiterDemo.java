package com.problems.rateLimiter;

public class RateLimiterDemo {

    public static void main(String[] args) throws InterruptedException {

        // Initialize service with global default
        RateLimitConfig globalDefault = RateLimitConfig.builder()
                .maxRequest(10)
                .timeWindowInSeconds(60)
                .rateLimitStrategy(RateLimitStrategy.TOKEN_BUCKET)
                .build();

        RateLimiterService service = new RateLimiterService(globalDefault);

        // ===== FLEXIBLE CLIENT CONFIGURATIONS =====

        // 1. User-based rate limiting
        service.configureRateLimiter("user:premium_123",
                RateLimitConfig.builder()
                        .maxRequest(100)
                        .timeWindowInSeconds(60)
                        .rateLimitStrategy(RateLimitStrategy.SLIDING_WINDOW)
                        .build()
        );

        service.configureRateLimiter("user:regular_456",
                RateLimitConfig.builder()
                        .maxRequest(20)
                        .timeWindowInSeconds(60)
                        .rateLimitStrategy(RateLimitStrategy.TOKEN_BUCKET)
                        .build()
        );

        // 2. API endpoint-based rate limiting
        service.configureRateLimiter("api:/users/create",
                RateLimitConfig.builder()
                        .maxRequest(5)
                        .timeWindowInSeconds(60)
                        .rateLimitStrategy(RateLimitStrategy.FIXED_WINDOW)
                        .build()
        );

        service.configureRateLimiter("api:/users/read",
                RateLimitConfig.builder()
                        .maxRequest(100)
                        .timeWindowInSeconds(60)
                        .rateLimitStrategy(RateLimitStrategy.TOKEN_BUCKET)
                        .build()
        );

        // 3. IP-based rate limiting
        service.configureRateLimiter("ip:192.168.1.100",
                RateLimitConfig.builder()
                        .maxRequest(50)
                        .timeWindowInSeconds(60)
                        .rateLimitStrategy(RateLimitStrategy.TOKEN_BUCKET)
                        .build()
        );

        // 4. API Key-based rate limiting
        service.configureRateLimiter("apikey:xyz789",
                RateLimitConfig.builder()
                        .maxRequest(1000)
                        .timeWindowInSeconds(3600) // 1 hour
                        .rateLimitStrategy(RateLimitStrategy.SLIDING_WINDOW)
                        .build()
        );

        // 5. Organization-based rate limiting (easy to add new types!)
        service.configureRateLimiter("org:acme_corp",
                RateLimitConfig.builder()
                        .maxRequest(10000)
                        .timeWindowInSeconds(3600)
                        .rateLimitStrategy(RateLimitStrategy.TOKEN_BUCKET)
                        .build()
        );

        // 6. Composite identifiers (user + endpoint combination)
        service.configureRateLimiter("user:premium_123:api:/sensitive",
                RateLimitConfig.builder()
                        .maxRequest(10)
                        .timeWindowInSeconds(60)
                        .rateLimitStrategy(RateLimitStrategy.FIXED_WINDOW)
                        .build()
        );

//         ===== TEST SCENARIOS =====

        System.out.println("===== Testing Premium User =====");
        testRateLimiter(service, "user:premium_123", 80);

        System.out.println("\n===== Testing Regular User =====");
        testRateLimiter(service, "user:regular_456", 25);

        System.out.println("\n===== Testing API Endpoint =====");
        testRateLimiter(service, "api:/users/create", 70);

        System.out.println("\n===== Testing IP Address =====");
        testRateLimiter(service, "ip:192.168.1.100", 5);

        System.out.println("\n===== Testing API Key =====");
        testRateLimiter(service, "apikey:xyz789", 5);

        System.out.println("\n===== Testing Organization =====");
        testRateLimiter(service, "org:acme_corp", 5);

        System.out.println("\n===== Testing Composite Identifier =====");
        testRateLimiter(service, "user:premium_123:api:/sensitive", 16);

        System.out.println("\n===== Testing Unconfigured Identifier (Uses Global Default) =====");
        testRateLimiter(service, "user:new_user_999", 6);
    }

    private static void testRateLimiter(RateLimiterService service,
                                        String rateLimiterName,
                                        int requests)
            throws InterruptedException {

        for (int i = 1; i <= requests; i++) {
            boolean response = service.allowRequest(rateLimiterName);

            String append = response ? "success" : "fail";

            System.out.println(String.format(
                    "Request %d is %s",
                    i,
                    append
            ));

            Thread.sleep(100);
        }
    }
}

