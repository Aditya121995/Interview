package com.problems.Splitwise;

public class ExactSplit extends Split {
    public ExactSplit(User user, double amount) {
        super(user,  SplitType.EXACT);
        this.amount = amount;
    }
}
