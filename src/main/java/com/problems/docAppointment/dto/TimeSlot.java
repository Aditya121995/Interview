package com.problems.docAppointment.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Data
@EqualsAndHashCode(of = {"startTime", "endTime"})
public class TimeSlot {
    private final LocalTime startTime;
    private final LocalTime endTime;

    public TimeSlot(LocalTime startTime, LocalTime endTime) {
        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException("Start time and End time cannot be null");
        }
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public boolean overlaps(TimeSlot ts) {
        return !(startTime.isAfter(ts.endTime) || endTime.isBefore(ts.startTime));
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return startTime.format(formatter) + "-" + endTime.format(formatter);
    }


}
