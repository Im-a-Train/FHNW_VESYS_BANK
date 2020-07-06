package bank.client;

import bank.Bank;
import bank.BankDriver2;
import bank.commands.Command;
import bank.commands.CommandBank;
import bank.server.RequestDecoder;
import bank.server.RequestEncoder;
import org.glassfish.tyrus.client.ClientManager;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.SynchronousQueue;

@ClientEndpoint(
    decoders = RequestDecoder.class,
    encoders = RequestEncoder.class)
public class WebSocket_Driver implements BankDriver2 {
  private CommandBank bank;
  private final SynchronousQueue<Command> cmdQueue = new SynchronousQueue<>();
  private Session session;
  private final List<UpdateHandler> listeners = new CopyOnWriteArrayList<>();

  @OnMessage
  public void onMessage(Command cmd) throws InterruptedException{
    cmdQueue.put(cmd);
  }

  @OnMessage
  public void onMessage(String accountId){
    for(UpdateHandler h: listeners){
      try {
        h.accountChanged(accountId);
      }catch (IOException e){
        e.printStackTrace();
      }
    }
  }
  @Override
  public void connect(String[] args) throws IOException {
    try {
      URI uri = new URI("ws://"+ args[0] +":" + args[1] +"/bank/ws");
      System.out.println("Connecting to "+ uri);
      ClientManager client = ClientManager.createClient();
      session = client.connectToServer(this, uri);
    } catch (URISyntaxException | DeploymentException e){
      throw new IOException(e);
    }
    bank = new CommandBank(cmd ->{
      try {
        session.getBasicRemote().sendObject(cmd);
        return cmdQueue.take();
      }catch (EncodeException | InterruptedException e){
        throw new RuntimeException(e);
      }
    });
  }

  @Override
  public void disconnect() throws IOException {
    bank = null;
    session.close();
    System.out.println("Disconnected");
  }

  @Override
  public Bank getBank() {
    return bank;
  }

  @Override
  public void registerUpdateHandler(UpdateHandler handler) throws IOException {
    listeners.add(handler);
  }
}
