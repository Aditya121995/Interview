package com.problems.loggingFrameWork;

public enum LoggerLevel {
    DEBUG(1), INFO(2), WARNING(3), ERROR(4), FATAL(5);

    private final int level;

    LoggerLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
