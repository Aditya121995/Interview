package com.problems.ecommerce.entity;

import com.problems.ecommerce.enums.PaymentMode;
import lombok.Data;

import java.util.Set;

@Data
public class PincodeServiceability {
    private final String pincode;
    private boolean isServiceable;
    private Set<PaymentMode> supportedPayModes;

    public PincodeServiceability(String pincode,  Set<PaymentMode> supportedPayModes) {
        this.pincode = pincode;
        this.supportedPayModes = supportedPayModes;
        this.isServiceable = true;
    }
}
