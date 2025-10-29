package com.problems.docAppointment;

import javax.print.Doc;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RatingBasedRankingStrategy implements SlotRankingStrategy {
    @Override
    public List<DoctorSlot> rank(List<DoctorSlot> slots) {
        return slots.stream()
                .sorted(Comparator.comparingDouble((DoctorSlot slot) -> slot.getDoctor().getRating()).reversed()
                        .thenComparing(slot -> slot.getTimeSlot().getStartTime()))
                .collect(Collectors.toList());
    }
}
