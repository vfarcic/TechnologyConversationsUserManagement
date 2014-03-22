package com.technologyconversations.usermanagement;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.StaticHttpHandler;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.grizzly.http.server.HttpHandler;

import java.net.URI;

public class Server {

    public static final String BASE_API_URI = "http://localhost:8080/usermanagementapi/";

    public static void main(String[] args) throws Exception {
        Server server = new Server();
        final HttpServer httpServer = server.startServer();
        System.out.println("Press enter to stop the server...");
        System.in.read();
        httpServer.shutdown();
    }

    public HttpServer startServer() {
        final ResourceConfig rc = new ResourceConfig().packages("com.technologyconversations.usermanagement");
        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_API_URI), rc);
        server.getServerConfiguration().addHttpHandler(getHttpHandler(), "/usermanagement");
        return server;
    }

    public HttpHandler getHttpHandler() {
        return new StaticHttpHandler("src/main/resources/webapp/");
    }

}
