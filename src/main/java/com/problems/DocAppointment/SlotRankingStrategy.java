package com.problems.DocAppointment;

import java.util.List;

public interface SlotRankingStrategy {
    List<DoctorSlot> rank(List<DoctorSlot> slots);
}
