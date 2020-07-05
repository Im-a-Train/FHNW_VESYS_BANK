package bank.data;

import bank.Account;
import com.fasterxml.jackson.annotation.JsonRootName;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.IOException;

@XmlRootElement
public class AccountData {
    private String number;
    private String owner;
    private double balance;
    private boolean active = true;

    public AccountData(Account account) throws IOException {
        this(account.getNumber(), account.getOwner(), account.getBalance(), account.isActive());
    }
    public AccountData(String owner){
        this("",owner, 0);
    }
    public AccountData(String number, String owner){
        this(number, owner, 0);
    }

    public AccountData(String number, String owner, double balance){
        this(number, owner, balance, true);
    }
    public AccountData(String number, String owner, double balance, boolean isActive){
        this.balance = balance;
        this.owner = owner;
        this.number = number;
        this.active = isActive;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }


}
