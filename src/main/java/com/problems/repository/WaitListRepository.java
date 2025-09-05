package com.problems.repository;

import com.problems.dto.TimeSlot;
import com.problems.entity.Appointment;
import com.problems.entity.WaitList;

import java.util.List;
import java.util.Optional;

public interface WaitListRepository {
    void saveWaitList(WaitList waitList);
    List<WaitList> findWaitListByPatient(String id);
    List<WaitList> findWaitListByDoctorAndSlot(String id, TimeSlot slot);
    List<WaitList> findWaitListByDoctorAndSlotOrderByCreatedAt(String id, TimeSlot slot);

}
