package com.problems.atmMachine;

import lombok.Getter;
import lombok.Setter;

@Getter
public class BankAccount {
    private final String accountNumber;
    private double balance;
    @Setter
    private Card card;

    public BankAccount(String accountNumber){
        this.accountNumber=accountNumber;
        this.balance=0;
        this.card=null;
    }

    public void addBalance(double amount){
        this.balance+=amount;
    }

    public void deductBalance(double amount){
        this.balance-=amount;
    }
}
