package com.problems.asyncLogger;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class LogMessage {
    private final String message;
    private final LogLevel level;
    private final LocalDateTime timestamp;


    public LogMessage(String message, LogLevel level) {
        this.message = message;
        this.level = level;
        this.timestamp = LocalDateTime.now();
    }
}
