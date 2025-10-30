package com.problems.Splitwise;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ExpenseService {
    private final Map<String, User>  users;
    private final Map<String, Group> groups;
    private final Map<String, Expense> expenses;
    private final BalanceSheet balanceSheet;

    public ExpenseService() {
        users=new ConcurrentHashMap<>();
        groups=new ConcurrentHashMap<>();
        expenses=new ConcurrentHashMap<>();
        balanceSheet=new BalanceSheet();
    }

    public synchronized User addUser(String userId, String name, String email) {
        User user=new User(userId, name, email);
        users.put(userId, user);
        return user;
    }

    public synchronized Group createGroup(String groupId, String name, String description, List<String> userIds) {
        Group group=new Group(groupId, name, description);
        for(String userId : userIds){
            User user=users.get(userId);
            if(user!=null){
                group.addMember(user);
            }
        }
        groups.put(groupId, group);
        return group;
    }

    public synchronized void addGroupMember(String groupId, String userId) {
        Group group=groups.get(groupId);
        if(group==null){
            System.out.println("group not found");
            return;
        }
        User user=users.get(userId);
        if(user==null){
            System.out.println("user not found");
            return;
        }

        group.addMember(user);
    }

    public synchronized void addExpense(String expenseId, String groupId, double amount,
                                        String paidByUserId, List<Split> splits, String description ) {
        User user=users.get(paidByUserId);
        if(user==null){
            System.out.println("user not found");
            return;
        }

        Expense expenseExist=expenses.get(expenseId);
        if(expenseExist!=null){
            System.out.println("expense already exists with id: "+expenseId);
            return;
        }

        if(splits.isEmpty()){
            System.out.println("No splits found");
            return;
        }

        Expense expense = new Expense(expenseId, description, amount, user, splits, groupId);

        ExpenseSplitStrategy splitStrategy = getSplitStrategy(splits.get(0));
        if(splitStrategy==null){
            return;
        }

        splitStrategy.split(expense, splits);
        if(!expense.validate()) {
            System.out.println("expense validation failed");
            return;
        }

        // update balances
        for (Split split : splits) {
            if (!split.getUser().getUserId().equals(user.getUserId())) {
                System.out.println("split of user :: " + split.getUser().getUserId());
                balanceSheet.addBalance(user.getUserId(), split.getUser().getUserId(), split.getAmount(), groupId);
            }
        }

        expenses.put(expenseId, expense);
    }

    public synchronized void showGlobalBalance(String userId) {
        balanceSheet.showGlobalBalances(userId);
    }

    public synchronized void showGroupBalances(String userId, String groupId) {
        balanceSheet.showGroupBalances(userId, groupId, groups.get(groupId).getName());
    }

    public synchronized void showAllGroupBalances(String userId) {
        balanceSheet.showAllGroupBalances(userId);
    }

    public synchronized void settleGlobally(String fromUser, String toUser) {
        balanceSheet.settleGlobalBalance(fromUser, toUser);
    }

    public synchronized void settleInGroup(String fromUser, String toUser, String groupId) {
        balanceSheet.settleGroupBalance(fromUser, toUser, groupId);
    }

    public synchronized void simplifyGroup(String groupId) {
        Group group=groups.get(groupId);
        if(group==null){
            System.out.println("group not found");
            return;
        }

        List<SimplifySettlement> settlements = balanceSheet.simplifyDebts(groupId);
        for(SimplifySettlement simplifySettlement : settlements){
            balanceSheet.addBalance(simplifySettlement.getFromUser(), simplifySettlement.getToUser(),
                    simplifySettlement.getAmount(), groupId);
        }
    }

    private ExpenseSplitStrategy getSplitStrategy(Split split) {
        switch (split.getType()) {
            case PERCENT:
                return new PercentageSplitStrategy();
            case EQUAL:
                return new EqualSplitStrategy();
            case EXACT:
                return new ExactSplitStrategy();
            default:
                System.out.println("invalid type");
                return null;
        }
    }

}
