package com.problems.docAppointment;

import java.util.List;

public interface SlotRankingStrategy {
    List<DoctorSlot> rank(List<DoctorSlot> slots);
}
