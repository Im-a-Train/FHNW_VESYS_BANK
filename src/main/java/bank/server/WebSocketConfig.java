package bank.server;

import javax.websocket.server.ServerEndpointConfig;

public class WebSocketConfig extends ServerEndpointConfig.Configurator{
    private final Websocket_Server server = new Websocket_Server();

    @Override
    public <T> T getEndpointInstance(Class<T> endpointClass){
        return endpointClass == Websocket_Server.class ? (T)server : null;
    }
}
