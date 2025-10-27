package com.problems.asyncLogger;

import com.problems.loggingFrameWork.LogFormatter;

public interface LogAppender {
    void append(LogMessage logMessage);
}
