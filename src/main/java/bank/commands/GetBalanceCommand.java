package bank.commands;

import bank.Account;
import bank.Bank;

import java.io.IOException;

public class GetBalanceCommand extends Command {
    private final String accountNumber;
    private double balance;
    public GetBalanceCommand(String accountNumber){
        this.accountNumber = accountNumber;
    }
    @Override
    public Command execute(Bank b) {
        try{
            Account bankAccount = b.getAccount(accountNumber);
            balance = bankAccount.getBalance();
        }catch (IOException e){
            setException(e);
        }
        return this;
    }

    public double getBalance(){
        return balance;
    }
}
