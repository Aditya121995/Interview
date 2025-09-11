package com.problems.ecommerce.repository;

import com.problems.ecommerce.entity.PincodeServiceability;

import java.util.Optional;

public interface PincodeServiceabilityRepository {
    void savePincodeServiceability(PincodeServiceability pincodeServiceability);
    Optional<PincodeServiceability> findPincodeServiceabilityByPincode(String pincode);
}
