package com.problems.handler;

import com.problems.dto.TimeSlot;
import com.problems.entity.Doctor;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RatingBasedRankingHandler implements SlotRankingStrategyHandler {

    @Override
    public List<Map.Entry<Doctor, TimeSlot>> rankSlots(List<Map.Entry<Doctor, TimeSlot>> slots) {
        return slots.stream()
                .sorted(Comparator.<Map.Entry<Doctor, TimeSlot>>comparingDouble(entry ->
                        entry.getKey().getRating()).reversed()
                .thenComparing(entry -> entry.getValue().getStartTime()))
                .collect(Collectors.toList());
    }
}
