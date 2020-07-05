package bank.client;

import bank.Bank;
import bank.BankDriver;

import java.io.IOException;

public class REST_Driver implements BankDriver {
    private Bank bank;
    @Override
    public void connect(String[] args) throws IOException {
        bank = new REST_BankProxy(args[0], args[1]);
    }

    @Override
    public void disconnect() throws IOException {
        bank = null;
    }

    @Override
    public Bank getBank() {
        return bank;
    }
}
