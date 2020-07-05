package bank.client;

import bank.Account;
import bank.Bank;
import bank.InactiveException;
import bank.OverdrawException;
import bank.data.AccountData;
import bank.data.BankData;
import com.sun.javafx.collections.UnmodifiableListSet;
import scala.util.parsing.json.JSON;
import scala.util.parsing.json.JSONFormat;

import javax.json.Json;
import javax.print.attribute.standard.Media;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;

public class REST_BankProxy implements Bank {
    Client c;
    private String url;
    public REST_BankProxy(String host, String port){
        this.c = ClientBuilder.newClient();
        url = "http://"+host+":"+port+"/bank";

    }

    @Override
    public String createAccount(String s) throws IOException {
        WebTarget r = c.target(url);
        Response response = r
            .request()
            .post(Entity
                .entity(
                    s,
                    MediaType.TEXT_PLAIN));
        if(response.getStatus() == 200){
            String account = response.readEntity(String.class);
            System.out.println(account);
            return account;
        }else {
            System.out.println(response.getStatus());
            throw new IOException();
        }
    }

    @Override
    public boolean closeAccount(String s) throws IOException {
        return false;
    }

    @Override
    public Set<String> getAccountNumbers() throws IOException {
        WebTarget r = c.target(url);
        Response response = r.request().get();
        if(response.getStatus() == 200 | response.getStatus() == 404){
            BankData bankData = response.readEntity(BankData.class);
            if(bankData == null){
                return new HashSet<>();
            }else{
                return bankData.getAccounts();
            }
        }else{
            System.out.println(response.getStatus());
            throw new IOException();
        }
    }

    @Override
    public Account getAccount(String s) throws IOException {
        return null;
    }

    @Override
    public void transfer(Account account, Account account1, double v) throws IOException, IllegalArgumentException, OverdrawException, InactiveException {

    }
}
