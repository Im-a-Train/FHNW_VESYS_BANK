package bank.server;

import bank.Bank;
import bank.commands.Command;
import bank.local.LocalBank;

import javax.xml.crypto.Data;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class UDP_Server {

  public static void main(String[] args) throws IOException {
    UDP_Server s = new UDP_Server(new bank.local.LocalBank());
    s.start();
  }
  private final Bank bank;
  public UDP_Server(Bank bank) throws IOException{
    this.bank = bank;
  }

  private Map<SocketAddress, Command> handledCommands = new HashMap<>();

  public void start() throws IOException{
    int port = 1234;
    DatagramSocket serverSocket = new DatagramSocket(port);
    byte[] buf = new byte[2048];
    DatagramPacket packet = new DatagramPacket(buf, buf.length);
    System.out.println("Server up an running on port:" + port);
    while (true){
        packet.setData(buf);
        serverSocket.receive(packet);
        serverSocket.send(handle(packet));
      }
    }

  private DatagramPacket handle(DatagramPacket packet) throws IOException{
    ByteArrayInputStream bis = new ByteArrayInputStream(packet.getData());
    ObjectInputStream in = new ObjectInputStream(bis);
    SocketAddress clientAdr = packet.getSocketAddress();
    try{
      Command command = (Command) in.readObject();
      System.out.println("Recived command: " + command.getClass().getName() + command.getTransactionId());
      if(handledCommands.get(clientAdr) == null){
        handledCommands.put(clientAdr, command.execute(bank));
      }else if(handledCommands.get(clientAdr).getTransactionId() != command.getTransactionId()){
        handledCommands.put(clientAdr, command.execute(bank));
      }
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      ObjectOutputStream out = new ObjectOutputStream(bos);
      out.writeObject(handledCommands.get(clientAdr));
      out.flush();
      byte[] answerData = bos.toByteArray();
      packet.setData(answerData);
    }catch (ClassNotFoundException | StreamCorruptedException e){
      System.out.println("Received Trash from client");
    }
    System.out.println("Sent Answer for Transaction "+handledCommands.get((clientAdr)).getTransactionId()+"");
    return packet;
}
}
