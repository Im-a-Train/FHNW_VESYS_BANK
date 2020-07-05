package bank.commands;
import bank.Bank;

import java.io.IOException;

import static bank.commands.Request.*;
public class CreateAccountCommand extends Command {
    public final String owner;
    public final int requestCode;
    private String accountNumber;


    public CreateAccountCommand(String owner) {
        this.owner = owner;
        requestCode = BANK_CREATE_ACCOUNT;
        accountNumber = null;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    @Override
    public Command execute(Bank b) {
        try{
            accountNumber = b.createAccount(owner);
            System.out.println("Account Created: "+ accountNumber);
        }catch (IOException e){
            setException(e);
        }
        return this;
    }

}
