package com.problems.atmMachine;

import com.problems.carRentalSystem.Car;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BankService {
    private final Map<String, BankAccount> accounts;
    private final Map<String, Card> cards;
    private final Map<Card, BankAccount> cardBankAccountMap;

    public BankService() {
        this.accounts=new ConcurrentHashMap<>();
        this.cards=new ConcurrentHashMap<>();
        this.cardBankAccountMap=new ConcurrentHashMap<>();
    }

    public void createAccount(String accountNumber){
        BankAccount account = new BankAccount(accountNumber);
        this.accounts.put(accountNumber,account);
    }

    public synchronized void createCard(String cardNumber, String pin, String accountNumber){
        if (!accounts.containsKey(accountNumber)){
            System.out.println("Account number not found");
            return;
        }

        if (cards.containsKey(cardNumber)){
            System.out.println("Card number already exists");
            return;
        }

        BankAccount account = accounts.get(accountNumber);
        Card card = new Card(cardNumber, pin);
        this.cards.put(cardNumber,card);
        this.cardBankAccountMap.put(card, account);
    }

    public synchronized double getBalance(Card card){
        BankAccount account = this.cardBankAccountMap.get(card);
        if (account == null){
            System.out.println("Account not found");
            return 0;
        }

        return account.getBalance();
    }

    public synchronized boolean authenticateCard(Card card, String pin){
        return card.getPin().equals(pin);
    }

    public synchronized void addMoney(Card card, double amount){
        if (!cardBankAccountMap.containsKey(card)){
            System.out.println("Card number not mapped to any account");
            return;
        }
        BankAccount bankAccount = cardBankAccountMap.get(card);
        bankAccount.addBalance(amount);
    }

    public synchronized void deductMoney(Card card, double amount){
        if (!cardBankAccountMap.containsKey(card)){
            System.out.println("Card number not mapped to any account");
            return;
        }
        BankAccount bankAccount = cardBankAccountMap.get(card);
        bankAccount.deductBalance(amount);
    }

    public synchronized Card getCard(String cardNumber){
        if (!cards.containsKey(cardNumber)){
            System.out.println("Card number not found");
            return null;
        }
        return cards.get(cardNumber);
    }
}
