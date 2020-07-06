package bank.local;

import bank.Account;
import bank.BankDriver2;
import bank.InactiveException;
import bank.OverdrawException;

import java.io.IOException;

public class UpdatableBank extends LocalBank{
    private final BankDriver2.UpdateHandler handler;
    public UpdatableBank(BankDriver2.UpdateHandler handler){
        super();
        this.handler = handler;
    }


    @Override
    public String createAccount(String owner) throws IOException {
        UpdatableAccount newAccount = new UpdatableAccount(owner, handler);
        accounts.put(newAccount.getNumber(), newAccount);
        if(newAccount.getNumber() != null) handler.accountChanged(newAccount.getNumber());
        return newAccount.getNumber();
    }

    @Override
    public LocalAccount getAccount(String number){
        return accounts.get(number);
    }

    @Override
    public boolean closeAccount(String number) throws IOException{
        boolean res = super.closeAccount(number);
        if(res) handler.accountChanged(number);
        return res;
    }

    @Override
    public void transfer(Account a, Account b, double amount) throws IOException, InactiveException, OverdrawException {
        super.transfer(a,b,amount);
        handler.accountChanged(a.getNumber());
        handler.accountChanged(b.getNumber());
    }
}
