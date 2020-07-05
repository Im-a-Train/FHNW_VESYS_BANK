package bank.local;

import bank.InactiveException;
import bank.OverdrawException;

import java.io.Serializable;
import java.util.UUID;

public class LocalAccount implements bank.Account, Serializable {
    private String number;
    private String owner;
    private double balance;
    private boolean active = true;

    LocalAccount(String owner) {
        this.owner = owner;
        this.number = UUID.randomUUID().toString();
    }

    @Override
    public double getBalance() {
        return balance;
    }

    @Override
    public String getOwner() {
        return owner;
    }

    @Override
    public String getNumber() {
        return number;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public boolean close() throws InactiveException {
        if(active){
            if(balance == 0.0){
                active = false;
                return true;
            }else{
                System.out.println("Account could not be closed, balance is not null");
                return false;
            }
        }else{
            throw new InactiveException("The account is already inactive");
        }
    }

    @Override
    public void deposit(double amount) throws InactiveException, IllegalArgumentException {
        if(amount < 0){
            throw new IllegalArgumentException();
        }
        if(active){
            balance += amount;
            System.out.println("Deposit: "+amount+" on Account: "+number+", new balance: "+balance);
        }else{
            throw new InactiveException("The account is inactive.");
        }
    }

    @Override
    public void withdraw(double amount) throws InactiveException, OverdrawException, IllegalArgumentException {
        if(amount < 0){
            throw new IllegalArgumentException();
        }
        if(active){
            if(balance-amount < 0){
                throw new OverdrawException("The expected withdraw is higher than the current account balance");
            }else{
                balance -= amount;
                System.out.println("Withdaw: "+amount+" on Account: "+number+", new balance: "+balance);
            }
        }else{
            throw new InactiveException("The account is inactive");
        }
    }

}
