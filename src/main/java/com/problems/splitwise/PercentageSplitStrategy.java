package com.problems.splitwise;

import java.util.List;

public class PercentageSplitStrategy implements ExpenseSplitStrategy {

    @Override
    public void split(Expense expense, List<Split> splits) {
        double totalAmount = expense.getAmount();

        for (Split split : splits) {
            PercentageSplit percentageSplit = (PercentageSplit) split;
            double amount = (totalAmount*percentageSplit.getPercentage()) / 100.0;
            double roundedAmount = Math.round(amount * 100.0) / 100.0;
            split.setAmount(roundedAmount);
        }
    }
}
