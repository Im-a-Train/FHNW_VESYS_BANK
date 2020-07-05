package bank.data;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashSet;
import java.util.Set;

@XmlRootElement
public class BankData {
    private final Set<String> accounts;

    public BankData(){
        this.accounts = new HashSet<String>();
    }

    public BankData(Set<String> accounts){
        this.accounts = accounts;
    }

    public Set<String> getAccounts(){
        return accounts;
    }
}
