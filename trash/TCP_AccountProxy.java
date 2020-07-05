package bank.client;

import bank.Account;
import bank.InactiveException;
import bank.OverdrawException;
import bank.commands.DepositCommand;
import bank.commands.GetBalanceCommand;
import bank.commands.IsAccountActiveCommand;
import bank.commands.WithdrawCommand;

import java.io.*;

public class TCP_AccountProxy implements bank.Account{
    private String number;
    private String owner;
    private double balance;
    private boolean active = true;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public TCP_AccountProxy(String number, String owner, double balance, boolean isActive, ObjectInputStream in, ObjectOutputStream out){
        this.number = number;
        this.owner = owner;
        this.balance = balance;
        this.active = isActive;
        this.in = in;
        this.out = out;
    }

    public TCP_AccountProxy(Account a, ObjectInputStream in, ObjectOutputStream out){
        try{
            this.number = a.getNumber();
            this.owner = a.getOwner();
            this.balance = a.getBalance();
            this.active = a.isActive();
        }catch (IOException e){
            System.out.println("TCP Account Proixy could not be created");
            System.out.println(e.toString());
        }
        this.in = in;
        this.out = out;
    }

    @Override
    public String getNumber() throws IOException {
        return number;
    }

    @Override
    public String getOwner() throws IOException {
        return owner;
    }

    @Override
    public boolean isActive() throws IOException {
        IsAccountActiveCommand isActive = new IsAccountActiveCommand(this.number);
        out.writeObject(isActive);
        out.flush();
        try{
            isActive = (IsAccountActiveCommand) in.readObject();
            if(isActive.getException() == null){
                return isActive.getIsActive();
            }else{
                System.out.println("Is Active check failed");
                System.out.println(isActive.getException().toString());
            }
        }catch (ClassNotFoundException e){
            System.out.println("Command not found");
            System.out.println(e.toString());
        }
        return false;
    }

    @Override
    public void deposit(double amount) throws IOException, IllegalArgumentException, InactiveException {
        System.out.println("Deposit: "+amount+" to account: "+ number);
        DepositCommand depositCommand = new DepositCommand(this.number, amount);
        out.writeObject(depositCommand);
        out.flush();
        try{
            depositCommand = (DepositCommand) in.readObject();
            if(depositCommand.getException()==null){
                System.out.println("Deposit was successfull");
            }else{
                System.out.println("Deposit failed");
                System.out.println(depositCommand.getException().toString());
            }
        }catch (ClassNotFoundException e){
            System.out.println("Command not found");
            System.out.println(e.toString());
        }
    }

    @Override
    public void withdraw(double amount) throws IOException, IllegalArgumentException, OverdrawException, InactiveException {
        System.out.println("Withdraw: "+amount+" to account: "+ number);
        WithdrawCommand withdrawCommand = new WithdrawCommand(this.number, amount);
        out.writeObject(withdrawCommand);
        out.flush();

        try{
            withdrawCommand = (WithdrawCommand) in.readObject();
            if(withdrawCommand.getException() == null){
                System.out.println("Withdraw of "+ amount +  " was successfull");
            }else {
                System.out.println("Withdraw failed");
                System.out.println(withdrawCommand.getException().toString());
            }

        }catch (ClassNotFoundException e){
            System.out.println("Command not found");
            System.out.println(e.toString());
        }
    }

    @Override
    public double getBalance() throws IOException {
        GetBalanceCommand getBalance = new GetBalanceCommand(this.number);
        out.writeObject(getBalance);
        out.flush();
        try{
            getBalance = (GetBalanceCommand) in.readObject();
            if(getBalance.getException() == null){
                return getBalance.getBalance();
            }else{
                System.out.println("Get Balance failed");
                System.out.println(getBalance.getException().toString());
            }
        }catch (ClassNotFoundException e){
            System.out.println("Command not found");
            System.out.println(e.toString());
        }
        return 0.0;
    }
}
