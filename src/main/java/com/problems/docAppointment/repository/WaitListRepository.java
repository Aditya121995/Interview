package com.problems.docAppointment.repository;

import com.problems.docAppointment.dto.TimeSlot;
import com.problems.docAppointment.entity.WaitList;

import java.util.List;

public interface WaitListRepository {
    void saveWaitList(WaitList waitList);
    List<WaitList> findWaitListByPatient(String id);
    List<WaitList> findWaitListByDoctorAndSlot(String id, TimeSlot slot);
    List<WaitList> findWaitListByDoctorAndSlotOrderByCreatedAt(String id, TimeSlot slot);

}
