package com.problems.asyncLogger;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class AsyncLogger {
    private volatile LogLevel minLogLevel;
    private final String name;
    private final List<LogAppender> appenders;
    private volatile boolean running;
    private final BlockingQueue<LogMessage> messageQueue;
    private final Thread workerThread;


    public AsyncLogger(LogLevel minLogLevel, String name) {
        this.minLogLevel = minLogLevel;
        this.name = name;
        this.appenders = new CopyOnWriteArrayList<>();
        this.messageQueue = new LinkedBlockingQueue<>();
        this.running = true;
        this.workerThread = new Thread(this::processMessages);
        workerThread.start();
    }

    public void addAppender(LogAppender appender) {
        this.appenders.add(appender);
    }

    public void removeAppender(LogAppender appender) {
        this.appenders.remove(appender);
    }

    public void changeLevel(LogLevel level) {
        this.minLogLevel = level;
    }

    public void debug(String logMessage) {
        log(logMessage, LogLevel.DEBUG);
    }

    public void info(String logMessage) {
        log(logMessage, LogLevel.INFO);
    }

    public void warn(String logMessage) {
        log(logMessage, LogLevel.WARNING);
    }

    public void error(String logMessage) {
        log(logMessage, LogLevel.ERROR);
    }

    public void fatal(String logMessage) {
        log(logMessage, LogLevel.FATAL);
    }

    private void log(String logMessage, LogLevel level) {
        if (level.getPriority() < minLogLevel.getPriority()) {
            return;
        }

        LogMessage message = new LogMessage(logMessage, level);
        try {
            messageQueue.put(message);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void processMessages() {
        while (running || !messageQueue.isEmpty()) {
            LogMessage message = null;
            try {
                message = messageQueue.poll(100, TimeUnit.MILLISECONDS);
                if (message != null) {
                    for (LogAppender appender : appenders) {
                        appender.append(message);
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public void shutdown() {
        running = false;

        try {
            workerThread.join(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
