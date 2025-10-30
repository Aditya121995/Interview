package com.problems.Splitwise;

import java.util.List;

public class EqualSplitStrategy implements ExpenseSplitStrategy {
    @Override
    public void split(Expense expense, List<Split> splits) {
        double totalAmount = expense.getAmount();
        double numSplits = splits.size();

        double splitAmount = Math.round((totalAmount / numSplits) * 100.0) / 100.0;
        double remainder = totalAmount - splitAmount*numSplits;

        for (int i = 0; i < numSplits; i++) {
            splits.get(i).setAmount(splitAmount);
            if (i == 0) {
                splits.get(i).setAmount(splitAmount + remainder);
            }
        }
    }
}
