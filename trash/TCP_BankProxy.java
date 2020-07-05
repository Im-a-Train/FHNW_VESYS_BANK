package bank.client;

import bank.InactiveException;
import bank.OverdrawException;
import bank.commands.*;

import java.io.*;
import java.util.HashSet;
import java.util.Set;


public class TCP_BankProxy implements bank.Bank {
    ObjectInputStream in;
    ObjectOutputStream out;

    public TCP_BankProxy(ObjectInputStream in, ObjectOutputStream out){
        this.in = in;
        this.out = out;
    }


    @Override
    public String createAccount(String owner) throws IOException {
        CreateAccountCommand createAccount = new CreateAccountCommand(owner);
        out.writeObject(createAccount);
        out.flush();
        try{
            createAccount = (CreateAccountCommand) in.readObject();
            if(createAccount.getException() == null){
                return createAccount.getAccountNumber();
            }else{
                System.out.println("Create Account failed");
                System.out.println(createAccount.getException().toString());
            }
        }catch (ClassNotFoundException e){
            System.out.println("Command not found");
            System.out.println(e.toString());
        }
        return null;
    }

    @Override
    public boolean closeAccount(String number) throws IOException {
        CloseAccountCommand closeAccount = new CloseAccountCommand(number);
        out.writeObject(closeAccount);
        out.flush();
        try{
            closeAccount = (CloseAccountCommand) in.readObject();
            if(closeAccount.getException() == null){
                return closeAccount.getStatus();
            }else{
                System.out.println("Close Account failed");
                System.out.println(closeAccount.getException().toString());
            }
        }catch (ClassNotFoundException e){
            System.out.println("Command not found");
            System.out.println(e.toString());
        }
        return false;
    }

    @Override
    public Set<String> getAccountNumbers() throws IOException {
        GetAccountNumbersCommand getNumbers = new GetAccountNumbersCommand();
        out.writeObject(getNumbers);
        out.flush();
        try{
            getNumbers = (GetAccountNumbersCommand) in.readObject();
            if(getNumbers.getException() == null){
                return getNumbers.getAccountNumbers();
            }else{
                System.out.println("Get Account numbers failed");
                System.out.println(getNumbers.getException().toString());
            }
        }catch (ClassNotFoundException e){
            System.out.println("Command not found");
            System.out.println(e.toString());
        }
        return new HashSet<>();
    }

    @Override
    public TCP_AccountProxy getAccount(String number) throws IOException {
        GetAccountCommand getAccount = new GetAccountCommand(number);
        out.writeObject(getAccount);
        out.flush();
        try {
            getAccount = (GetAccountCommand) in.readObject();
            if (getAccount.getException() == null) {
                if (getAccount.getCurrentAccount() != null) {
                    return new TCP_AccountProxy(getAccount.getCurrentAccount(), in, out);
                } else {
                    System.out.println("Account: " + number + " not found");

                }
            }
        }catch (ClassNotFoundException e){
            System.out.println("Command not found");
            System.out.println(e.toString());
        }
        return null;
    }

    @Override
    public void transfer(bank.Account a, bank.Account b, double amount) throws IOException, IllegalArgumentException, OverdrawException, InactiveException {
        TransferCommand transferCommand = new TransferCommand(a.getNumber(), b.getNumber(), amount);
        out.writeObject(transferCommand);
        try{
            transferCommand = (TransferCommand) in.readObject();
            if (transferCommand.getException() == null){
                System.out.println("Transfer successfull");
            }else {
                System.out.println("Transfer failes");
                System.out.println(transferCommand.getException().toString());
            }
        }catch (Exception e){
            System.out.println("Command not found");
            System.out.println(e.toString());

        }

    }

}