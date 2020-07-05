package bank.client;

import bank.Bank;
import bank.BankDriver;
import bank.commands.Command;
import bank.commands.CommandBank;
import bank.commands.CommandHandler;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeoutException;

import static bank.server.RabbitMQ_Server.*;

public class RabbitMQ_Driver implements BankDriver {
    Bank bank;
    SynchronousQueue<Command> queue = new SynchronousQueue<>();
    Channel channel;
    Connection connection;

    ConnectionFactory factory;
    String replyQueueName;
    @Override
    public void connect(String[] args) throws IOException {
        String host = args[0];
        int port = Integer.parseInt(args[1]);

        factory = new ConnectionFactory();
        factory.setUsername("vesys-dev");
        factory.setPassword("vesys123");
        factory.setVirtualHost("vesys-dev-vhost");
        factory.setHost(host);
        factory.setPort(port);

        System.out.println("Initialized Params");
        try {

            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            replyQueueName = channel.queueDeclare().getQueue();

            channel.basicConsume(replyQueueName, true,
                    (consumerTag, message) -> {
                        try {
                            queue.put((Command) deserialize(message.getBody()));
                        } catch (InterruptedException | ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    },
                    tag -> {
                    }
            );
        } catch (TimeoutException e) {
            throw new IOException(e);
        }
        bank = new CommandBank(new RabbitMQ_Handler(channel, replyQueueName, queue));
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
class RabbitMQ_Handler implements CommandHandler {
    private final Channel channel;
    private final String replyQueueName;
    private final SynchronousQueue<Command> queue;
    public RabbitMQ_Handler(Channel channel, String replyQueueName, SynchronousQueue<Command> queue){
        this.channel = channel;
        this.replyQueueName = replyQueueName;
        this.queue = queue;
    }
    @Override
    public Command handle(Command command) throws IOException {
        try {

            AMQP.BasicProperties props = new AMQP.BasicProperties
                    .Builder()
                    .replyTo(replyQueueName)
                    .build();

            channel.basicPublish("", QUEUE_NAME, props, serialize(command));

        } catch (IOException e) {
            e.printStackTrace();
        }
        try{
            return queue.take();
        }catch (InterruptedException e){
            throw new IOException(e);
        }
    }

}