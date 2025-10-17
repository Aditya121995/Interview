package com.problems.loggingFrameWork;

public class PatternFormatter implements LogFormatter {
    private final String pattern;

    public PatternFormatter() {
        this("[%name] [%timestamp] [%logLevel] [%message]");
    }

    public PatternFormatter(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public String format(LogMessage logMessage) {
        return pattern.replace("name", logMessage.getLogName())
                .replace("timestamp", logMessage.getTimestamp().toString())
                .replace("logLevel", logMessage.getLevel().toString())
                .replace("message", logMessage.getMessage());
    }
}
