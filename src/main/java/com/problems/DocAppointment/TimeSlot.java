package com.problems.DocAppointment;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@EqualsAndHashCode(of = {"startTime", "endTime"})
public class TimeSlot {
    private final LocalTime startTime;
    private final LocalTime endTime;

    public TimeSlot(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public boolean overlaps(TimeSlot other) {
        return !startTime.isAfter(other.getEndTime()) && !other.getStartTime().isAfter(endTime);
    }
}
