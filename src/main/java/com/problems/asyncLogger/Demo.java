package com.problems.asyncLogger;

public class Demo {
    public static void main(String[] args) throws InterruptedException {
        AsyncLogger asyncLogger = new AsyncLogger(LogLevel.DEBUG, "app");

        asyncLogger.addAppender(new ConsoleAppender());

        asyncLogger.info("Application started");
        asyncLogger.debug("Debug information");
        asyncLogger.warn("Warning message");
        asyncLogger.error("Error occurred");

        for (int i = 0; i < 10; i++) {
            final int threadId = i;
            new Thread(() -> {
                asyncLogger.info("Log from thread :: " + threadId);
            }).start();
        }

        Thread.sleep(2000);

        asyncLogger.shutdown();

    }
}
