package bank.client;

import bank.Bank;
import bank.BankDriver;
import bank.commands.Command;
import bank.commands.CommandBank;
import bank.commands.CommandHandler;


import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class Servlet_Driver implements BankDriver {
    private CommandBank bank;
    @Override
    public void connect(String[] args) throws IOException {
        String host = args[0];
        int port = Integer.parseInt(args[1]);

        URI serverURI = null;
        try {
            serverURI = new URI("http://"+host+":"+port+"/bank/bank");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        bank = new CommandBank(new Servlet_Handler(serverURI));
        bank.connect();
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

class Servlet_Handler implements CommandHandler {
    private final URI servletURI;
    private final HttpClient client;


    public Servlet_Handler(URI servletURI){
        this.servletURI = servletURI;
        client = HttpClient.newHttpClient();
    }

    @Override
    public Command handle(Command command) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(command);

        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofByteArray(baos.toByteArray());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(servletURI)
                .POST(body)
                .build();
        try{
            HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
            ObjectInputStream ois = new ObjectInputStream(response.body());
            return (Command) ois.readObject();
        }catch( InterruptedException | ClassNotFoundException e){
            throw new IOException(e);
        }
    }
}
