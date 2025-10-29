package com.problems.docAppointment;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TimeBasedRankingStrategy implements SlotRankingStrategy {
    @Override
    public List<DoctorSlot> rank(List<DoctorSlot> slots) {
        return slots.stream()
                .sorted(Comparator.comparing(slot -> slot.getTimeSlot().getStartTime()))
                .collect(Collectors.toList());
    }
}
