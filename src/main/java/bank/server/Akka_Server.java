package bank.server;

import akka.actor.AbstractActor;
import akka.actor.ActorSystem;
import akka.actor.Props;
import bank.commands.Command;
import bank.local.LocalBank;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class Akka_Server {
  public static void main(String[] args) {
    Config config = ConfigFactory.load().getConfig("BankServer");
    ActorSystem system = ActorSystem.create("BankApplication", config);
    system.actorOf(Props.create(BankActor.class), "BankServer");
    System.out.println("Started Bank Server");
  }

  static class BankActor extends AbstractActor{
    LocalBank bank = new LocalBank();

    @Override
    public Receive createReceive() {
      return receiveBuilder()
          .match(Command.class, cmd ->{
            getSender().tell(cmd.execute(bank), getSelf());
          }).build();
    }
  }
}
