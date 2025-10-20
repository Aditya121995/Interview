package com.problems.atmMachine;

public class NoteDispenser implements DispenseChain {
    private DispenseChain nextChain;
    private final int noteValue;
    private int noteCount;

    public NoteDispenser(int noteValue, int noteCount) {
        this.noteValue = noteValue;
        this.noteCount = noteCount;
    }


    @Override
    public void setNextChain(DispenseChain dispenseChain) {
        this.nextChain = dispenseChain;
    }

    @Override
    public void dispense(int amount) {
        if (amount >= noteValue) {
            int dispenseCount = Math.min(amount/noteValue,  noteCount);
            int remainingAmount = amount - (dispenseCount*noteValue);

            if (dispenseCount > 0) {
                System.out.println("Dispensing " + dispenseCount + " of note :: " + noteValue);
                this.noteCount -= dispenseCount;
            }

            if (remainingAmount > 0 &&  nextChain != null) {
                nextChain.dispense(remainingAmount);
            }
        } else if (this.nextChain != null) {
            this.nextChain.dispense(amount);
        }
    }

    @Override
    public boolean canDispense(int amount) {
        if (amount < 0) {
            return false;
        }

        if (amount == 0) {
            return true;
        }

        int dispenseCount = Math.min(amount/noteValue,  noteCount);
        int remainingAmount = amount - (dispenseCount*noteValue);

        if (remainingAmount == 0) {
            return true;
        }

        if (this.nextChain != null) {
            return this.nextChain.canDispense(remainingAmount);
        }

        return false;
    }
}
