package com.problems.Splitwise;

import java.util.Arrays;
import java.util.List;

public class SplitWiseDemo {
    public static void main(String[] args) {
        ExpenseService service =  new ExpenseService();

        // Add users
        User u1 = service.addUser("u1", "Alice", "alice@example.com");
        User u2 =service.addUser("u2", "Bob", "bob@example.com");
        User u3 =service.addUser("u3", "Charlie", "charlie@example.com");

        // create groups
        service.createGroup("g1", "Trip to Goa", "", Arrays.asList("u1", "u2", "u3"));
        service.createGroup("g2", "Apartment", "", Arrays.asList("u1", "u2"));

        // Add expense in Trip group
        List<Split> splits1 = Arrays.asList(
                new EqualSplit(u1),
                new EqualSplit(u2),
                new EqualSplit(u3)
        );
        service.addExpense("e1", "g1", 900, "u1", splits1,
                "lunch");

        // Add expense in Apartment group
        List<Split> splits2 = Arrays.asList(
                new EqualSplit(u1),
                new EqualSplit(u2)
        );
        service.addExpense("e2", "g2", 2000, "u2", splits2,
                 "box8");

        // Add non-group expense
        List<Split> splits3 = Arrays.asList(
                new EqualSplit(u1),
                new EqualSplit(u2)
        );
        service.addExpense("e3", null, 500, "u1", splits3,
                 "gym class");

        // Show different levels of balances
        service.showGlobalBalance("u1");           // Global view
        service.showGroupBalances("u1", "g1");      // Trip group only
        service.showGroupBalances("u1", "g2");      // Apartment group only
        service.showAllGroupBalances("u1");        // All groups breakdown

        service.simplifyGroup("g1");
        // Show different levels of balances
        service.showGlobalBalance("u1");           // Global view
        service.showGroupBalances("u1", "g1");      // Trip group only
        service.showGroupBalances("u1", "g2");      // Apartment group only
        service.showAllGroupBalances("u1");        // All groups breakdown

        // Settle only in Trip group
        service.settleInGroup("u1", "u2", "g1");
        // Show different levels of balances
        service.showGlobalBalance("u1");           // Global view
        service.showGroupBalances("u1", "g1");      // Trip group only
        service.showGroupBalances("u1", "g2");      // Apartment group only
        service.showAllGroupBalances("u1");        // All groups breakdown

        // Settle globally (all groups + non-group expenses)
        service.settleGlobally("u1", "u2");

        // Show different levels of balances
        service.showGlobalBalance("u1");           // Global view
        service.showGroupBalances("u1", "g1");      // Trip group only
        service.showGroupBalances("u1", "g2");      // Apartment group only
        service.showAllGroupBalances("u1");        // All groups breakdown
    }
}
