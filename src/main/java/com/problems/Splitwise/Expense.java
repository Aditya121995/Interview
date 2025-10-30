package com.problems.Splitwise;

import lombok.Getter;

import java.util.List;

@Getter
public class Expense {
    private final String expenseId;
    private final String description;
    private final double amount;
    private final User paidBy;
    private final List<Split> splits;
    private final String groupId;

    public Expense(String expenseId, String description,
                   double amount, User paidBy, List<Split> splits,  String groupId) {
        this.expenseId = expenseId;
        this.description = description;
        this.amount = amount;
        this.paidBy = paidBy;
        this.splits = splits;
        this.groupId = groupId;
    }

    public void removeSplit(Split split) {
        this.splits.remove(split);
    }

    public void addSplit(Split split) {
        this.splits.add(split);
    }

    public boolean validate() {
        double totalAmountInSplit = 0;
        for (Split split : splits) {
            totalAmountInSplit += split.getAmount();
        }

        return Math.abs(amount-totalAmountInSplit) <= 0.01;
    }
}
