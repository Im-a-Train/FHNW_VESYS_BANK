package bank.server;

import bank.commands.Command;
import bank.commands.CommandHandler;
import bank.local.LocalBank;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TCP_Server {

    public static void main(String[] args) throws IOException {
        LocalBank bank = new LocalBank();
        int port = 1234;
        try(ServerSocket serverSocket = new ServerSocket(port)){
            System.out.println("Server up an running on port:" + port);
            while (true){
                Socket client = serverSocket.accept();
                System.out.println("Client connected: "+ client.getInetAddress().toString());
                TCP_Handler tcp_handler = new TCP_Handler(client, bank);
                Thread t = new Thread(tcp_handler);
                System.out.println("Client Thread started");
                t.start();
            }
        }
    }
}
class TCP_Handler implements CommandHandler, Runnable{
    private final LocalBank bank;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;
    private final Socket client;

    public TCP_Handler(Socket client, LocalBank bank) throws IOException{
        this.bank = bank;
        out = new ObjectOutputStream(client.getOutputStream());
        in = new ObjectInputStream(client.getInputStream());
        this.client = client;
    }

    @Override
    public Command handle(Command command) {
        Command executedCommand = command.execute(bank);
        try{
            out.writeObject(executedCommand);
            out.flush();
        }catch (IOException e){
            executedCommand.setException(e);
        }
        return executedCommand;
    }

    @Override
    public void run() {
        System.out.println("Client TCP Handler started");
        while(client.isConnected()){
            try{
                Command req = (Command) in.readObject();
                handle(req);
            }catch (IOException e){
                System.out.println(e.toString());
                System.exit(0);
            }
            catch (ClassNotFoundException e){
                System.out.println("Command not found");
                System.out.println(e.toString());
            }
        }
    }
}