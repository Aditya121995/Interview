package com.problems.loggingFrameWork;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class LogMessage {
    private final String message;
    private final LocalDateTime timestamp;
    private final LoggerLevel level;
    private final String logName;

    public LogMessage(String message, LoggerLevel level, String logName) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.level = level;
        this.logName = logName;
    }
}
