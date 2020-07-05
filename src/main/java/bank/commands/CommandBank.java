package bank.commands;

import bank.Account;
import bank.Bank;
import bank.InactiveException;
import bank.OverdrawException;

import java.io.IOException;
import java.util.Set;

public class CommandBank implements Bank{

    public final CommandHandler handler;

    public CommandBank(CommandHandler h) {handler = h;}

    <R extends Command> R writeRequest(R r) throws IOException{
        R resp = (R)handler.handle(r);
        if(resp.getException() != null ){
            Exception e = resp.getException();
            if(e instanceof IOException) throw (IOException) e;
        }
        return resp;
    }

    @Override
    public String createAccount(String owner) throws IOException {
        return writeRequest(new CreateAccountCommand(owner)).getAccountNumber();
    }

    @Override
    public boolean closeAccount(String number) throws IOException {
        return writeRequest(new CloseAccountCommand(number)).getStatus();
    }

    @Override
    public Set<String> getAccountNumbers() throws IOException {
        return writeRequest(new GetAccountNumbersCommand()).getAccountNumbers();
    }

    @Override
    public CommandAccount getAccount(String number) throws IOException {
        return new CommandAccount(handler,writeRequest(new GetAccountCommand(number)).getCurrentAccount());
    }

    @Override
    public void transfer(Account a, Account b, double amount) throws IOException, IllegalArgumentException, OverdrawException, InactiveException {
        TransferCommand resp = writeRequest(new TransferCommand(a.getNumber(), b.getNumber(), amount));
        Exception e = resp.getException();
        if(e != null){
            if(e instanceof OverdrawException) throw (OverdrawException) e;
            if(e instanceof InactiveException) throw (InactiveException) e;
        }
    }

    public void connect() throws IOException {
        writeRequest(new ConnectCommand());
        System.out.println("Connected");
    }
}
