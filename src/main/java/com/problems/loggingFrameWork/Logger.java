package com.problems.loggingFrameWork;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Logger {
    private volatile LoggerLevel loggerLevel;
    private final List<LogAppender> appenders;
    private final ReentrantReadWriteLock lock;
    private final String name;

    public Logger(String name) {
        this.loggerLevel = LoggerLevel.INFO;
        this.appenders = new CopyOnWriteArrayList<>();
        appenders.add(new ConsoleAppender());
        this.lock = new ReentrantReadWriteLock();
        this.name = name;
    }

    public void addAppender(LogAppender appender) {
        lock.writeLock().lock();
        try {
            this.appenders.add(appender);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void removeAppender(LogAppender appender) {
        lock.writeLock().lock();
        try {
            this.appenders.remove(appender);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void  clearAppenders() {
        lock.writeLock().lock();
        try {
            this.appenders.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void changeLogLevel(LoggerLevel loggerLevel) {
        lock.writeLock().lock();
        try {
            this.loggerLevel = loggerLevel;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void debug(String logMessage) {
        log(logMessage, LoggerLevel.DEBUG);
    }

    public void info(String logMessage) {
        log(logMessage, LoggerLevel.INFO);
    }

    public void warn(String logMessage) {
        log(logMessage, LoggerLevel.WARNING);
    }

    public void error(String logMessage) {
        log(logMessage, LoggerLevel.ERROR);
    }

    public void fatal(String logMessage) {
        log(logMessage, LoggerLevel.FATAL);
    }

    private void log(String message, LoggerLevel level) {
        lock.writeLock().lock();
        try {
            for (LogAppender appender : appenders) {
                if (level.getLevel() >= loggerLevel.getLevel()) {
                    LogMessage logMessage = new LogMessage(message, level, this.name);
                    appender.append(logMessage);
                }
            }
        } finally {
            lock.writeLock().unlock();
        }
    }
}
