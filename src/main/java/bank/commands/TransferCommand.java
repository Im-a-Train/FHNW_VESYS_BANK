package bank.commands;

import bank.Account;
import bank.Bank;

import java.io.IOException;

public class TransferCommand extends Command {
    private final String from;
    private final String to;
    private final double amount;

    public TransferCommand(String from, String to, double amount){
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    public TransferCommand(Account a, Account b, double amount) throws IOException {
        this(a.getNumber(), b.getNumber(), amount);
    }

    @Override
    public Command execute(Bank b) {
        Account bankFrom;
        Account bankTo;
        try{
            bankFrom = b.getAccount(from);
            bankTo = b.getAccount(to);

            if(!bankFrom.isActive() && !bankTo.isActive()){
                setException(new bank.InactiveException());
            }else {
                bankFrom.withdraw(amount);
                bankTo.deposit(amount);
            }
        }catch (IOException | bank.OverdrawException | bank.InactiveException | IllegalArgumentException e){
            setException(e);
        }
        return this;
    }
}
