package com.problems.DocAppointment.validator;

import java.time.LocalTime;

public class TimeSlotValidator {
    private final static LocalTime MIN_TIME_SLOT = LocalTime.of(9,0);
    private final static LocalTime MAX_TIME_SLOT = LocalTime.of(21,0);

    public static void validateTimeSlot(LocalTime startTime, LocalTime endTime) {
        if(!startTime.isBefore(endTime) || startTime.equals(endTime)){
            throw new IllegalArgumentException("Start time must be before End time");
        }

        if(!endTime.equals(startTime.plusMinutes(30))) {
            throw new IllegalArgumentException("Time slot must be of 30 minutes");
        }

        if(startTime.isBefore(MIN_TIME_SLOT) || endTime.isAfter(MAX_TIME_SLOT)) {
            throw new IllegalArgumentException("Time should be in range 9am to 9pm");
        }
    }
}
