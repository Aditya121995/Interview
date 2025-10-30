package com.problems.Splitwise;

import lombok.Getter;

@Getter
public class SimplifySettlement {
    private final String fromUser;
    private final String toUser;
    private final double amount;

    public SimplifySettlement(String fromUser, String toUser, double amount) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.amount = amount;
    }
}
