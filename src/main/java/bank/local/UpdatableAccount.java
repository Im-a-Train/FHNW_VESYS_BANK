package bank.local;

import bank.BankDriver2;
import bank.InactiveException;
import bank.OverdrawException;

import java.io.IOException;

class UpdatableAccount extends LocalAccount{
    private final transient BankDriver2.UpdateHandler handler;

    public UpdatableAccount(String number, BankDriver2.UpdateHandler handler){
        super(number);
        this.handler = handler;
    }

    @Override
    public void deposit(double amount) throws InactiveException {
        super.deposit(amount);
        try {
            handler.accountChanged(getNumber());
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void withdraw(double amount) throws InactiveException, OverdrawException{
        super.withdraw(amount);
        try {
            handler.accountChanged(getNumber());
        }catch (IOException e){
            e.printStackTrace();
        }

    }


}
