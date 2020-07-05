package bank.commands;


import bank.Account;
import bank.InactiveException;
import bank.OverdrawException;

import java.io.IOException;

public class CommandAccount implements Account {

    private final CommandHandler handler;
    private final String number;
    private final String owner;


    public CommandAccount(CommandHandler handler, Account account) throws IOException {
        this.handler = handler;
        try{
            this.number = account.getNumber();
            this.owner = account.getOwner();
        }catch (IOException e){
            throw new IOException(e);
        }

    }

    <R extends Command> R writeRequest(R r) throws IOException, IllegalArgumentException{
        R resp = (R)handler.handle(r);
        if(resp.getException() != null ){
            Exception e = resp.getException();
            if(e instanceof IOException) throw (IOException) e;
        }
        return resp;
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
        return writeRequest(new IsAccountActiveCommand(number)).getIsActive();
    }

    @Override
    public void deposit(double amount) throws IOException, IllegalArgumentException, InactiveException {
        Command resp = writeRequest(new DepositCommand(number, amount));
        Exception e = resp.getException();
        if(e != null){
            if(e instanceof IllegalArgumentException) throw (IllegalArgumentException) e;
            if(e instanceof InactiveException) throw (InactiveException) e;
        }


    }

    @Override
    public void withdraw(double amount) throws IOException, IllegalArgumentException, OverdrawException, InactiveException {
        Command resp = writeRequest(new WithdrawCommand(number, amount));
        Exception e = resp.getException();
        if(e != null){
            if(e instanceof IllegalArgumentException) throw (IllegalArgumentException) e;
            if(e instanceof OverdrawException) throw (OverdrawException) e;
            if(e instanceof InactiveException) throw (InactiveException) e;
        }
    }

    @Override
    public double getBalance() throws IOException {
        return writeRequest(new GetBalanceCommand(number)).getBalance();
    }
}
