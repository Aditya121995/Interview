package com.problems.splitwise;

import lombok.Getter;
import lombok.ToString;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BalanceSheet {
    Map<String, Map<String, Double>> globalBalance;
    Map<String, Map<String, Map<String, Double>>> groupBalance;

    public BalanceSheet() {
        globalBalance=new ConcurrentHashMap<>();
        groupBalance=new ConcurrentHashMap<>();
    }

    public synchronized void addBalance(String fromUser, String toUser, double amount, String groupId){
        updateBalanceMap(globalBalance, fromUser, toUser, amount);

        if (groupId!=null && !groupId.isEmpty()){
            groupBalance.putIfAbsent(groupId, new ConcurrentHashMap<>());
            updateBalanceMap(groupBalance.get(groupId), fromUser, toUser, amount);
            System.out.println(groupBalance.get(groupId).get(fromUser));
        }
    }

    private void updateBalanceMap(Map<String, Map<String, Double>> balanceMap,
                                  String fromUser, String toUser, double amount) {
        balanceMap.putIfAbsent(fromUser, new ConcurrentHashMap<>());
        balanceMap.putIfAbsent(toUser, new ConcurrentHashMap<>());

        balanceMap.get(fromUser).put(toUser,
                balanceMap.get(fromUser).getOrDefault(toUser, 0.0) + amount);
        balanceMap.get(toUser).put(fromUser,
                balanceMap.get(toUser).getOrDefault(fromUser, 0.0) - amount);
    }

    public Map<String, Double> getGlobalBalance(String userId) {
        return globalBalance.getOrDefault(userId, new HashMap<>());
    }

    public Map<String, Double> getGroupBalance(String userId, String groupId) {
        if (!groupBalance.containsKey(groupId)){
            return new HashMap<>();
        }

        return groupBalance.get(groupId).getOrDefault(userId, new HashMap<>());
    }

    public void showGlobalBalances(String userId) {
        System.out.println("========Global Balances for user: " + userId + "=========");
        showBalances(getGlobalBalance(userId));
    }

    public void showGroupBalances(String userId,  String groupId, String groupName) {
        System.out.println("========Group " + groupName + " Balances for user: " + userId + "=========");
        showBalances(getGroupBalance(userId, groupId));
    }

    public void showAllGroupBalances(String userId) {
        System.out.println("========All Groups Balances for user: " + userId + "=========");
        for (Map.Entry<String, Map<String, Map<String, Double>>> groupEntry : groupBalance.entrySet()) {
            String groupId = groupEntry.getKey();
            Map<String, Map<String, Double>> userBalances = groupEntry.getValue();
            Map<String, Double> userBalanceInGroup = userBalances.getOrDefault(userId,  new HashMap<>());
            if (!userBalanceInGroup.isEmpty()){
                System.out.println("...." + groupId + "....");
                showBalances(userBalanceInGroup);
            }
        }
    }

    private void showBalances(Map<String, Double> balances) {
        if (balances==null||balances.isEmpty()){
            System.out.println("No Balances");
            return;
        }

        for (Map.Entry<String, Double> entry : balances.entrySet()) {
            if (Math.abs(entry.getValue()) <= 0.01){
                System.out.println("You and " + entry.getKey() + "are settled");
                continue;
            }

            if (entry.getValue() > 0){
                System.out.println(entry.getKey() + " owes you: " + entry.getValue());
            } else {
                System.out.println("You owe " + entry.getKey() + ": " + entry.getValue());
            }
        }
    }

    public synchronized void settleGlobalBalance(String fromUser, String toUser) {
        double amountToSettle = globalBalance.get(fromUser).get(toUser);
        updateBalanceMap(globalBalance, fromUser, toUser, -amountToSettle);

        // clear all the group balances as well
        for (Map<String, Map<String, Double>> userBalance : groupBalance.values()) {
            if (userBalance.containsKey(fromUser) && userBalance.get(fromUser).containsKey(toUser)) {
                double groupAmount = userBalance.get(fromUser).get(toUser);
                updateBalanceMap(userBalance, fromUser, toUser, -groupAmount);
            }
        }
    }

    public synchronized void settleGroupBalance(String fromUser, String toUser, String groupId) {
        if (!groupBalance.containsKey(groupId)){
            return;
        }

        Map<String, Map<String, Double>> userBalance =  groupBalance.get(groupId);

        if (userBalance.containsKey(fromUser) && userBalance.get(fromUser).containsKey(toUser)) {
            double groupAmount = userBalance.get(fromUser).get(toUser);
            updateBalanceMap(userBalance, fromUser, toUser, -groupAmount);
            updateBalanceMap(globalBalance, fromUser, toUser, -groupAmount);
        }
    }

    public synchronized List<SimplifySettlement> simplifyDebts(String groupId) {
        if (!groupBalance.containsKey(groupId)){
            return new ArrayList<>();
        }

        Map<String, Map<String, Double>> groupBalances = groupBalance.get(groupId);
        System.out.println("groupBalances: " + groupBalances);
        Map<String, Double> netBalances = new HashMap<>();

        for (Map.Entry<String, Map<String, Double>> entry : groupBalances.entrySet()) {
            String user =  entry.getKey();
            double netAmount = 0.0;

            for (double amount : entry.getValue().values()){
                netAmount += amount;
            }

            if (Math.abs(netAmount) > 0.01) {
                netBalances.put(user, netAmount);
            }
        }

        List<DebtEntry> debts = new ArrayList<>();
        for (Map.Entry<String, Double> entry : netBalances.entrySet()) {
            debts.add(new DebtEntry(entry.getKey(), entry.getValue()));
        }

        System.out.println("Debts :: " + debts);

        if (debts.isEmpty()) return new ArrayList<>();

        return simplify(debts);

//        int minTransactions = dfsMinTransactions(debts, 0);
//        System.out.println("Debts simplified! Minimum transactions needed: "
//                + minTransactions);
    }

    private List<SimplifySettlement> simplify(List<DebtEntry> debts) {
        // PriorityQueue for creditors (largest positive first)
        PriorityQueue<DebtEntry> creditors = new PriorityQueue<>((a,b) -> {
            return Double.compare(b.amount, a.amount);
        });

        // PriorityQueue for debitors (most negative first by absolute value)
        PriorityQueue<DebtEntry> debitors = new PriorityQueue<>((a,b) -> {
            return Double.compare(Math.abs(b.amount), Math.abs(a.amount));
        });

        for (DebtEntry debt : debts) {
            if (debt.amount < 0) {
                debitors.add(debt);
            } else if (debt.amount > 0) {
                creditors.add(debt);
            }
        }

        List<SimplifySettlement> result = new ArrayList<>();
        while (!creditors.isEmpty() && !debitors.isEmpty()) {
            DebtEntry cred = creditors.poll();
            DebtEntry debt = debitors.poll();

            double settlementAmount = Math.min(-1*debt.amount, cred.amount);
            result.add(new SimplifySettlement(debt.getUserId(), cred.getUserId(), settlementAmount));

            double credLeft = cred.amount - settlementAmount;
            double debLeft = debt.amount + settlementAmount;

            if (credLeft > 0) creditors.add(new DebtEntry(cred.getUserId(), credLeft));
            if (debLeft < 0) debitors.add(new DebtEntry(debt.getUserId(), debLeft));
        }

        return result;

    }

//    private int dfsMinTransactions(List<DebtEntry> debts, int currentIndex) {
//        while (currentIndex < debts.size() &&
//                Math.abs(debts.size() - currentIndex) <= 0.01) {
//            currentIndex++;
//        }
//
//        if (currentIndex >= debts.size()-1) {
//            return 0;
//        }
//
//        int minTxns = Integer.MAX_VALUE;
//        DebtEntry currentDebt = debts.get(currentIndex);
//
//        System.out.println("currentIndex: " + currentIndex);
//
//        for (int i=currentIndex+1; i < debts.size(); i++) {
//            DebtEntry otherDebt = debts.get(i);
//
//            if (currentDebt.getAmount()*otherDebt.getAmount() < 0) {
//                otherDebt.amount += currentDebt.getAmount();
//
//                int txnNeeded = dfsMinTransactions(debts, currentIndex+1) + 1;
//                System.out.println("Transactions needed: " + txnNeeded);
//
//                minTxns = Math.min(minTxns, txnNeeded);
//                otherDebt.amount -= currentDebt.getAmount();
//            }
//        }
//
//        return minTxns;
//    }

    @Getter
    @ToString
    private static class DebtEntry {
        String userId;
        double amount;
        public DebtEntry(String userId, double amount) {
            this.userId = userId;
            this.amount = amount;
        }
    }
}


