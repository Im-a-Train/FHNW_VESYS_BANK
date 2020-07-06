package bank.local;

import bank.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class LocalBank implements bank.Bank {

    protected final Map<String, LocalAccount> accounts = new HashMap<>();

    @Override
    public Set<String> getAccountNumbers() {
        System.out.println("Return all: "+ accounts.size() + " account numbers.");

        return accounts.keySet().stream().filter(e -> this.getAccount(e).isActive()).collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public String createAccount(String owner) throws IOException {
        LocalAccount newLocalAccount = new LocalAccount(owner);
        accounts.put(newLocalAccount.getNumber(), newLocalAccount);
        return newLocalAccount.getNumber();
    }

    @Override
    public boolean closeAccount(String number) throws IOException{
        LocalAccount currentLocalAccount = accounts.get(number);
        try{
            return currentLocalAccount.close();
        }catch (InactiveException e){
            return false;
        }
    }

    @Override
    public LocalAccount getAccount(String number) {
        return accounts.get(number);
    }

    @Override
    public void transfer(bank.Account from, bank.Account to, double amount)
            throws IOException, InactiveException, OverdrawException {
        if(from.isActive() && to.isActive()){
            try{
                from.withdraw(amount);
                to.deposit(amount);
            }catch (OverdrawException e){
                throw e;
            }
        }else{
            throw new InactiveException("One of the accounts is inactive");
        }
    }



}
