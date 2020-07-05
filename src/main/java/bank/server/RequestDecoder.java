package bank.server;

import bank.commands.Command;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class RequestDecoder implements Decoder.BinaryStream<Command> {
    @Override
    public void init(EndpointConfig config) {

    }

    @Override
    public void destroy() {

    }
    @Override
    public Command decode(InputStream is) throws DecodeException{
        try {
            return (Command) new ObjectInputStream(is).readObject();
        }catch (IOException | ClassNotFoundException e){
            throw new RuntimeException(e);
        }
    }
}
