package com.problems.Splitwise;

import java.util.List;

public interface ExpenseSplitStrategy {
    void split(Expense expense, List<Split> splits);
}
