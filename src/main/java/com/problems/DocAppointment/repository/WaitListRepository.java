package com.problems.DocAppointment.repository;

import com.problems.DocAppointment.dto.TimeSlot;
import com.problems.DocAppointment.entity.WaitList;

import java.util.List;

public interface WaitListRepository {
    void saveWaitList(WaitList waitList);
    List<WaitList> findWaitListByPatient(String id);
    List<WaitList> findWaitListByDoctorAndSlot(String id, TimeSlot slot);
    List<WaitList> findWaitListByDoctorAndSlotOrderByCreatedAt(String id, TimeSlot slot);

}
