package com.problems.ecommerce.repository;

import com.problems.ecommerce.entity.PincodeServiceability;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class PincodeServiceabilityRepositoryImpl implements PincodeServiceabilityRepository {
    private final Map<String, PincodeServiceability> pincodeServiceabilityMap;

    public PincodeServiceabilityRepositoryImpl() {
        this.pincodeServiceabilityMap = new ConcurrentHashMap<>();
    }

    @Override
    public void savePincodeServiceability(PincodeServiceability pincodeServiceability) {
        pincodeServiceabilityMap.put(pincodeServiceability.getPincode(), pincodeServiceability);
    }

    @Override
    public Optional<PincodeServiceability> findPincodeServiceabilityByPincode(String pincode) {
        return Optional.ofNullable(pincodeServiceabilityMap.get(pincode));
    }
}
