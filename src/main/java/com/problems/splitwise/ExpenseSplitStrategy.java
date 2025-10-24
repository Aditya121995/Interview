package com.problems.splitwise;

import java.util.List;

public interface ExpenseSplitStrategy {
    void split(Expense expense, List<Split> splits);
}
