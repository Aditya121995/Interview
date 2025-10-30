package com.problems.Splitwise;

import lombok.Getter;
import lombok.Setter;

@Getter
public abstract class Split {
    private final User user;
    private final SplitType type;
    @Setter
    protected double amount;

    public Split(User user, SplitType type) {
        this.user = user;
        this.type = type;
    }
}
