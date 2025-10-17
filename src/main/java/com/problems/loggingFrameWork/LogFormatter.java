package com.problems.loggingFrameWork;

import java.util.logging.LogRecord;

public interface LogFormatter {
    String format(LogMessage logMessage);
}
