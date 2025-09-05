package com.problems.handler;

import com.problems.dto.TimeSlot;
import com.problems.entity.Doctor;

import java.util.List;
import java.util.Map;

public interface SlotRankingStrategyHandler {
    List<Map.Entry<Doctor, TimeSlot>> rankSlots(List<Map.Entry<Doctor, TimeSlot>> slots);
}
