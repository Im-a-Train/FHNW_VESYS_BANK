package bank.server.ressources;

import bank.data.AccountData;
import bank.data.BankData;
import bank.local.LocalBank;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.inject.Singleton;
import javax.ws.rs.*;
import java.io.IOException;


@Singleton
@Path("/")
public class BankRes {
    LocalBank bank;
    Response.ResponseBuilder builder;

    public BankRes(){
        bank = new LocalBank();
    }
    @GET
    @Produces({"application/json", "application/xml"})
    public BankData getAccountNumbers() {
        return new BankData(bank.getAccountNumbers());
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response createAccount(String owner){
        try{
            String accountName = bank.createAccount(owner);
            builder = Response.ok()
                .entity(accountName);
            System.out.println("created Account: " + accountName);
        }catch (IOException e){
            builder = Response.serverError();
        }
        return builder.build();
    }

    @GET
    @Produces("application/json")
    @Path("/account/{id}")
    public Response getAccount(@PathParam("id") String id){
        try{
            AccountData account = new AccountData(bank.getAccount(id));
            builder = Response.ok()
                .entity(account);
        }catch (IOException e){
            builder = Response.noContent();
        }
        return builder.build();
    }
}
