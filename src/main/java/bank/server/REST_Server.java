package bank.server;


import bank.server.ressources.BankRes;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;

public class REST_Server {
    public static void main(String[] args) throws Exception {
        URI baseUri = new URI("http://localhost:9998/bank");

        // @Singleton annotations will be respected
        ResourceConfig rc = new ResourceConfig(BankRes.class);

        // Create and start the JDK HttpServer with the Jersey application
        JdkHttpServerFactory.createHttpServer(baseUri, rc);
    }
}
