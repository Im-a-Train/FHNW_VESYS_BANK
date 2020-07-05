package bank.commands;

import bank.Account;
import bank.Bank;

import java.io.IOException;

public class GetAccountCommand extends Command {
    private  Account currentAccount;
    private final String accountNumber;

    public GetAccountCommand(String accountNumber){
        this.accountNumber = accountNumber;
    }

    @Override
    public Command execute(Bank b) {
        try{
            currentAccount = b.getAccount(accountNumber);
        }catch (IOException e){
            setException(e);
        }
        return this;
    }

    public Account getCurrentAccount(){
        return this.currentAccount;
    }
}
