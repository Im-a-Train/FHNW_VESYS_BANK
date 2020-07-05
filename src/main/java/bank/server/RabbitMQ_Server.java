package bank.server;

import bank.Bank;
import bank.local.LocalBank;
import com.rabbitmq.client.*;
import bank.commands.Command;

import java.io.*;
import java.util.concurrent.TimeoutException;

public class RabbitMQ_Server  {
    public static final String QUEUE_NAME = "jku.bank.command.queue";
    public static final String EXCHANGE_NAME = "jku.bank.command.exchange";

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();

        factory.setUsername("vesys-dev");
        factory.setPassword("vesys123");
        factory.setVirtualHost("vesys-dev-vhost");

        String host = "86.119.38.130";
        int port = 5672;

        factory.setHost(host);
        factory.setPort(port);

        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");


        Bank bank = new LocalBank();
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            AMQP.BasicProperties replyProps = new AMQP.BasicProperties.Builder()
                    .correlationId(delivery
                            .getProperties()
                            .getCorrelationId())
                    .build();
            try {
                Command cmd = (Command) deserialize(delivery.getBody());
                Command response = cmd.execute(bank);
                channel.basicPublish("", delivery.getProperties().getReplyTo(), replyProps, serialize(response));

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        };

        channel.basicConsume(QUEUE_NAME, true, deliverCallback, (consumerTag -> {
        }));
    }

    public static byte[] serialize(Command obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }

    public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }
}
