package bank.commands;

import bank.Account;
import bank.Bank;

import java.io.IOException;

import static bank.commands.Request.BANK_CLOSE_ACCOUNT;

public class CloseAccountCommand extends Command{
    public final String accountNumber;
    public final int requestCode;
    private boolean successfull;

    public CloseAccountCommand(String accountNumber){
        this.accountNumber = accountNumber;
        requestCode = BANK_CLOSE_ACCOUNT;
        successfull = false;
    }

    @Override
    public Command execute(Bank b) {
        try {
            Account account = b.getAccount(accountNumber);
            if(account != null){
                successfull = b.closeAccount(accountNumber);
            }else{
                setException(new IllegalArgumentException());
            }
        }catch (IOException e){
            setException(e);
        }
        return this;
    }

    public boolean getStatus(){
        return successfull;
    }
}
