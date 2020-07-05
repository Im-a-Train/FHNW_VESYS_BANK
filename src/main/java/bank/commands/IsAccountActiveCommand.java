package bank.commands;

import bank.Account;
import bank.Bank;

import java.io.IOException;

public class IsAccountActiveCommand extends Command {
    private final String accountNumber;
    private boolean isActive;

    public IsAccountActiveCommand(String accountNumber){
        this.accountNumber = accountNumber;
        isActive = false;
    }

    public boolean getIsActive(){
        return isActive;
    }

    @Override
    public Command execute(Bank b) {
        Account bankAccount;
        try {
            bankAccount = b.getAccount(accountNumber);
            isActive = bankAccount.isActive();
        }catch (IOException e){
            setException(e);
        }
        return this;
    }
}
