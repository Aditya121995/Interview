package com.problems.splitwise;

import lombok.Getter;

public class PercentageSplit extends Split {
    @Getter
    private final double percentage;

    public PercentageSplit(User user,  double percentage) {
        super(user, SplitType.PERCENT);
        this.percentage = percentage;
    }
}
