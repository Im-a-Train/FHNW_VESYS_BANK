package bank.commands;

import bank.Account;
import bank.Bank;
import bank.InactiveException;

import java.io.IOException;

public class DepositCommand extends Command {
    private final String accountNumber;
    private final double depositAmount;

    public DepositCommand(String accountNumber, double depositAmount){
        this.depositAmount = depositAmount;
        this.accountNumber = accountNumber;
    }

    @Override
    public Command execute(Bank b) {
        try{
            Account bankAccount = b.getAccount(accountNumber);
            bankAccount.deposit(depositAmount);
        }catch (InactiveException | IllegalArgumentException | IOException e) {
            setException(e);
        }
        return this;
    }
}
