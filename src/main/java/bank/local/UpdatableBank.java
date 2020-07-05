package bank.local;

import bank.Account;
import bank.BankDriver2;
import bank.InactiveException;
import bank.OverdrawException;

import java.io.IOException;

public class UpdatableBank extends LocalBank{
    private final BankDriver2.UpdateHandler handler;
    public UpdatableBank(BankDriver2.UpdateHandler handler){
        this.handler = handler;
    }

    @Override
    public String createAccount(String owner) throws IOException {
        String accountId = super.createAccount(owner);
        if(accountId != null) handler.accountChanged(accountId);
        return accountId;
    }

    @Override
    public LocalAccount getAccount(String number){
        return new UpdatableAccount(number, handler);
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
