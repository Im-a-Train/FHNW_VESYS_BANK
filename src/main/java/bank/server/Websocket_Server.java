package bank.server;

import bank.*;
import bank.commands.Command;
import bank.local.UpdatableBank;
import org.glassfish.tyrus.server.Server;


import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import java.io.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
@ServerEndpoint(value = "/ws",
  configurator = WebSocketConfig.class,
  decoders = RequestDecoder.class,
  encoders = RequestEncoder.class)
public class Websocket_Server {
  private final Bank bank = new UpdatableBank(id -> notifyListeners(id));
  private static final List<Session> sessions = new CopyOnWriteArrayList<>();

   @OnOpen
   public void onOpen(Session session){
    sessions.add(session);
   }
   @OnClose
   public void onClose(Session session){
     sessions.remove(session);
   }
  public static void main(String[] args) throws Exception {
    Server server = new Server("localhost", 2222, "/bank", null, Websocket_Server.class);
    server.start();
    System.out.println("Server started");
    System.in.read();
  }

  private void notifyListeners(String id){
     for(Session s: sessions){
       try {
         s.getBasicRemote().sendText(id);
       }catch (IOException e){
         sessions.remove(s);
       }
     }
  }

  @OnMessage
  public Command onMessage(Command cmd){
     return cmd.execute(bank);
  }

}
