package com.problems.docAppointment.handler;

import com.problems.docAppointment.dto.TimeSlot;
import com.problems.docAppointment.entity.Doctor;

import java.util.List;
import java.util.Map;

public interface SlotRankingStrategyHandler {
    List<Map.Entry<Doctor, TimeSlot>> rankSlots(List<Map.Entry<Doctor, TimeSlot>> slots);
}
