package com.problems.atmMachine;

import com.problems.carRentalSystem.Car;
import lombok.Getter;
import lombok.Setter;

@Getter
public class AtmSystem {
    private final BankService bankService;
    @Setter
    private AtmState currentState;
    @Setter
    private Card currentCard;
    private final CashDispenser cashDispenser;

    public AtmSystem(BankService bankService) {
        this.bankService = bankService;
        this.currentState = new IdleState();
        this.currentCard = null;
        DispenseChain c1 = new NoteDispenser100(10);
        DispenseChain c2 = new NoteDispenser50(15);
        DispenseChain c3 = new NoteDispenser20(20);
        DispenseChain c4 = new NoteDispenser10(10);
        c1.setNextChain(c2);
        c2.setNextChain(c3);
        c3.setNextChain(c4);
        this.cashDispenser = new CashDispenser(c1);
    }

    public synchronized void insertCard(String cardNumber) {
        currentState.insertCard(this, cardNumber);
    }

    public synchronized void enterPin(String pin) {
        currentState.enterPin(this, pin);
    }

    public synchronized void selectOperation(OperationType operationType, int amount) {
        currentState.selectOperation(this, operationType, amount);
    }

    // private callers


    public Card getCard(String cardNumber) {
        return bankService.getCard(cardNumber);
    }

    public boolean authenticateCard(String pin) {
        return bankService.authenticateCard(currentCard, pin);
    }

    public void checkBalance() {
        double balance = bankService.getBalance(currentCard);
        System.out.println("Balance is " + balance);
    }

    public void withdrawCash(int amount) {
        double balance = bankService.getBalance(currentCard);
        if (balance < amount) {
            System.out.println("Insufficient funds. Cannot withdraw");
            return;
        }
        if (!cashDispenser.canDispenseCash(amount)) {
            System.out.println("Not enough cash in atm system");
            return;
        }

        bankService.deductMoney(currentCard, amount);
        cashDispenser.dispenseCash(amount);
    }

    public void depositCash(int amount) {
        bankService.addMoney(currentCard, amount);
    }
}
