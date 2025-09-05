package com.problems.repository;

import com.problems.dto.TimeSlot;
import com.problems.entity.WaitList;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class WaitListRepositoryImpl implements WaitListRepository {
    Map<String, WaitList> waitLists = new ConcurrentHashMap<>();

    @Override
    public void saveWaitList(WaitList waitList) {
        waitLists.put(waitList.getWaitListId(), waitList);
    }

    @Override
    public List<WaitList> findWaitListByPatient(String id) {
        return waitLists.values().stream()
                .filter(waitList -> waitList.getPatient().getPatientId().equals(id))
                .filter(waitList -> !waitList.isWaitListConfirmed())
                .collect(Collectors.toList());
    }

    @Override
    public List<WaitList> findWaitListByDoctorAndSlot(String id, TimeSlot slot) {
        return waitLists.values().stream()
                .filter(waitList -> waitList.getDoctor().getDoctorId().equals(id))
                .filter(waitList -> waitList.getTimeSlot().equals(slot))
                .filter(waitList -> !waitList.isWaitListConfirmed())
                .collect(Collectors.toList());
    }

    @Override
    public List<WaitList> findWaitListByDoctorAndSlotOrderByCreatedAt(String id, TimeSlot slot) {
        return waitLists.values().stream()
                .filter(waitList -> waitList.getDoctor().getDoctorId().equals(id))
                .filter(waitList -> waitList.getTimeSlot().equals(slot))
                .filter(waitList -> !waitList.isWaitListConfirmed())
                .sorted(Comparator.comparing(WaitList::getCreatedAt))
                .collect(Collectors.toList());
    }
}
