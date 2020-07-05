package bank.commands;

import bank.Bank;

public class ConnectCommand extends Command {
    @Override
    public Command execute(Bank b) {
        return this;
    }
}
