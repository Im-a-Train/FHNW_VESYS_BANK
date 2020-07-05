package bank.client;

import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.pattern.Patterns;
import akka.util.Timeout;
import bank.Bank;
import bank.BankDriver;
import bank.commands.Command;
import bank.commands.CommandBank;
import bank.commands.CommandHandler;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import scala.concurrent.Await;
import scala.concurrent.Future;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Akka_Driver implements BankDriver {
    private Bank bank;
    @Override
    public void connect(String[] args) throws IOException {
        Config config = ConfigFactory.load().getConfig("AkkaClient");
        System.out.println(config);
        ActorSystem system = ActorSystem.create("BankApplication", config);
        ActorSelection serverActor = system.actorSelection("akka://BankApplication@127.0.0.1:2553/user/BankServer");
        bank = new CommandBank(new AkkaHandler(serverActor));
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

class AkkaHandler implements CommandHandler {
    private final ActorSelection serverActor;
    private Timeout timeout = new Timeout(5, TimeUnit.SECONDS);

    public AkkaHandler(ActorSelection serverActor){
        this.serverActor = serverActor;
    }
    @Override
    public Command handle(Command command) throws IOException {
        Future<Object> question = Patterns.ask(serverActor, command, timeout);
        try {
            Command answer = (Command) Await.result(question, timeout.duration());
            return answer;
        } catch (TimeoutException | InterruptedException e) {
            throw new IOException(e);
        }
    }
}
