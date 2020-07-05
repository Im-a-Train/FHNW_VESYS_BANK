package bank.commands;

import bank.Bank;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class GetAccountNumbersCommand extends Command {
    private Set<String> accountNumbers = new HashSet<>();


    @Override
    public Command execute(Bank b) {
        try{
            accountNumbers = b.getAccountNumbers();
        }catch (IOException e){
            setException(e);
        }
        return this;
    }

    public Set<String> getAccountNumbers(){
        return accountNumbers;
    }
}
