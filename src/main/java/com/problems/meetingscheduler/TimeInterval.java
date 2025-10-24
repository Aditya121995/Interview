package com.problems.meetingscheduler;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TimeInterval {
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;

    public TimeInterval(LocalDateTime startTime, LocalDateTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public boolean isOverlap(TimeInterval other) {
        return startTime.isBefore(other.endTime) && endTime.isAfter(other.startTime);
    }
}
