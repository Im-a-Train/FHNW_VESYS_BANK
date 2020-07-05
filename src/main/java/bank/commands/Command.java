package bank.commands;

import bank.Bank;

import java.io.*;

public abstract class Command implements Serializable {
    private Exception e;
    private int transactionId;
    public void setException(Exception e){
        this.e = e;
    }
    public Exception getException(){
        return e;
    }

    public abstract Command execute(Bank b);

    public int getTransactionId() {
        return transactionId;
    }
    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

}
