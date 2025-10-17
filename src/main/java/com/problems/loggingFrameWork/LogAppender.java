package com.problems.loggingFrameWork;

public interface LogAppender {
    void append(LogMessage logMessage);
    void setLogFormatter(LogFormatter formatter);
    LogFormatter getLogFormatter();
}
