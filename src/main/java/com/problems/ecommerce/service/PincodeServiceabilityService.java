package com.problems.ecommerce.service;

import com.problems.ecommerce.entity.PincodeServiceability;
import com.problems.ecommerce.enums.PaymentMode;
import com.problems.ecommerce.repository.PincodeServiceabilityRepository;
import com.problems.ecommerce.repository.PincodeServiceabilityRepositoryImpl;

import java.util.Optional;
import java.util.Set;

public class PincodeServiceabilityService {
    private final PincodeServiceabilityRepository pincodeServiceabilityRepository;

    public PincodeServiceabilityService() {
        this.pincodeServiceabilityRepository = new PincodeServiceabilityRepositoryImpl();
    }

    public void addServiceability(String pincode, Set<PaymentMode> paymentModes) {
        PincodeServiceability pincodeServiceability = new PincodeServiceability(pincode, paymentModes);
        pincodeServiceabilityRepository.savePincodeServiceability(pincodeServiceability);
        System.out.println("Service ability added successfully");
    }

    public boolean removeServiceability(String pincode) {
        Optional<PincodeServiceability> pincodeServiceabilityOpt = pincodeServiceabilityRepository
                .findPincodeServiceabilityByPincode(pincode);
        if (pincodeServiceabilityOpt.isEmpty()) {
            System.out.println("Service ability could not be found");
            return false;
        }
        PincodeServiceability pincodeServiceability = pincodeServiceabilityOpt.get();
        pincodeServiceability.setServiceable(false);
        return true;
    }

    public boolean isServiceable(String pincode) {
        Optional<PincodeServiceability> pincodeServiceabilityOpt = pincodeServiceabilityRepository
                .findPincodeServiceabilityByPincode(pincode);
        if (pincodeServiceabilityOpt.isEmpty()) {
            System.out.println("Service ability could not be found");
            return false;
        }

        PincodeServiceability pincodeServiceability = pincodeServiceabilityOpt.get();
        return pincodeServiceability.isServiceable();
    }

    public Set<PaymentMode> getSupportedPaymentMethods(String pincode) {
        Optional<PincodeServiceability> pincodeServiceabilityOpt = pincodeServiceabilityRepository
                .findPincodeServiceabilityByPincode(pincode);
        if (pincodeServiceabilityOpt.isEmpty()) {
            System.out.println("Service ability could not be found");
            return null;
        }

        PincodeServiceability pincodeServiceability = pincodeServiceabilityOpt.get();
        return pincodeServiceability.getSupportedPayModes();
    }
}
