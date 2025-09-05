package com.problems.DocAppointment.handler;

import com.problems.DocAppointment.dto.TimeSlot;
import com.problems.DocAppointment.entity.Doctor;

import java.util.List;
import java.util.Map;

public interface SlotRankingStrategyHandler {
    List<Map.Entry<Doctor, TimeSlot>> rankSlots(List<Map.Entry<Doctor, TimeSlot>> slots);
}
