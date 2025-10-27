package com.problems.asyncLogger;

public class ConsoleAppender implements LogAppender {
    @Override
    public void append(LogMessage logMessage) {
        System.out.println(String.format("[%s][%s][%s]", logMessage.getTimestamp(), logMessage.getLevel(), logMessage.getMessage()));
    }
}
