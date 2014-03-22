package com.technologyconversations.usermanagement;

import org.glassfish.grizzly.http.server.CLStaticHttpHandler;
import org.glassfish.grizzly.http.server.HttpHandler;

public class StandaloneServer extends Server {

    @Override
    public HttpHandler getHttpHandler() {
        return new CLStaticHttpHandler(StandaloneServer.class.getClassLoader(), "webapp/");
    }


}
