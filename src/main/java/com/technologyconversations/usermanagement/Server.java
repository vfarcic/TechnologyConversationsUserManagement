package com.technologyconversations.usermanagement;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.StaticHttpHandler;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

public class Server {

    public static final String BASE_API_URI = "http://localhost:8080/usermanagementapi/";

    public static HttpServer startServer() {
        final ResourceConfig rc = new ResourceConfig().packages("com.technologyconversations.usermanagement");
        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_API_URI), rc);
        server.getServerConfiguration().addHttpHandler(new StaticHttpHandler("src/main/resources/webapp/"), "/usermanagement");
        return server;
    }

    public static void main(String[] args) throws IOException {
        UserDao userDao = UserDaoImpl.getInstance();
        for (int index = 1; index <= 5; index++) {
            User user = new User("username" + index);
            user.setPassword("secret" + index);
            user.setFullName("My Name " + index);
            userDao.putUser(user);
        }
        final HttpServer server = startServer();
        System.out.println("Press enter to stop the server...");
        System.in.read();
        server.shutdown();
    }

}
