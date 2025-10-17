package com.problems.loggingFrameWork;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileAppender implements LogAppender {
    private LogFormatter formatter;
    private final String filePath;

    public FileAppender(String filePath) {
        this.filePath = filePath;
        this.formatter = new PatternFormatter();

        File file = new File(filePath);
        try {
            System.out.println("=== FILE APPENDER DEBUG ===");
            System.out.println("Relative path: " + filePath);
            System.out.println("Absolute path: " + file.getAbsolutePath());
            System.out.println("Canonical path: " + file.getCanonicalPath());
            System.out.println("Current working directory: " +
                    System.getProperty("user.dir"));
            System.out.println("File exists: " + file.exists());
            System.out.println("Can write: " + file.canWrite());
            System.out.println("===========================");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileAppender(LogFormatter formatter,  String filePath) {
        this.formatter = formatter;
        this.filePath = filePath;
    }

    @Override
    public void append(LogMessage logMessage) {
        System.out.println("=== LOG APPENDER DEBUG ===");
        try (PrintWriter writer = new PrintWriter(
                new FileWriter(filePath, true))) {
            writer.println(formatter.format(logMessage));
        } catch (IOException e) {
            System.out.println("Error writing to log file: " + e.getMessage());
        }
    }

    @Override
    public void setLogFormatter(LogFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public LogFormatter getLogFormatter() {
        return formatter;
    }
}
