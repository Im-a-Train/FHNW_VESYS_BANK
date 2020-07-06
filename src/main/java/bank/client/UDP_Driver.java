package bank.client;

import bank.Bank;
import bank.BankDriver;
import bank.commands.Command;
import bank.commands.CommandBank;
import bank.commands.CommandHandler;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;

public class UDP_Driver implements BankDriver {
  private DatagramSocket socket;
  private CommandBank bank;
  @Override
  public void connect(String[] args) throws IOException {
    String host = args[0];
    int port = Integer.parseInt(args[1]);
    socket = new DatagramSocket();
    socket.connect(new InetSocketAddress(host, port));
    socket.setSoTimeout(2000);
    bank = new CommandBank(new UDP_Handler(socket));
    System.out.println("Connection established");
  }
  @Override
  public void disconnect() throws IOException {
      socket.close();
      bank = null;
      System.out.println("Connection closed");
  }
  @Override
  public Bank getBank() {
    return bank;
  }
}
class UDP_Handler implements CommandHandler {
  private int transactionId;
  private final DatagramSocket clientSocket;
  public UDP_Handler (DatagramSocket clientSocket) throws IOException {
    this.clientSocket = clientSocket;
    transactionId = 0;
  }
  @Override
  public Command handle(Command command) throws IOException{
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutputStream out = new ObjectOutputStream(bos);
    ByteArrayInputStream in;
    transactionId++;
    command.setTransactionId(transactionId);
    out.writeObject(command);
    out.flush();
    out.close();
    byte[] buf = bos.toByteArray();
    DatagramPacket request = new DatagramPacket(buf, buf.length);
    buf = new byte[2048];
    DatagramPacket response = new DatagramPacket(buf, buf.length);
    while(true){
      clientSocket.send(request);
      try{
        while(true){
          clientSocket.receive(response);
          in = new ByteArrayInputStream(response.getData());
          try {
            Command answer = (Command) new ObjectInputStream(in).readObject();
            System.out.println("Received Answer for Transaction"+ transactionId+" : " + answer.getClass().getName());
            if (answer.getTransactionId() == transactionId){
              return answer;
            }
          }catch (ClassNotFoundException | StreamCorruptedException e){
            System.out.println("Received trash from Server");
          }
        }
      }catch (SocketTimeoutException e){
        System.out.println("No answer received");
      }
    }
  }
}
