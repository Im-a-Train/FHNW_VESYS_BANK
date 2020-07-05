package bank.commands;

import bank.Account;
import bank.Bank;

import java.io.IOException;

public class WithdrawCommand extends Command {
    private final String accountNumber;
    private final double withdrawAmount;

    public WithdrawCommand(String accountNumber, double withdrawAmount){
        this.accountNumber = accountNumber;
        this.withdrawAmount = withdrawAmount;
    }


    @Override
    public Command execute(Bank b) {
        Account bankAccount;
        try{
            bankAccount = b.getAccount(accountNumber);
            bankAccount.withdraw(withdrawAmount);
        }catch (IOException | bank.OverdrawException | bank.InactiveException e){
            setException(e);
        }
        return this;
    }
}
