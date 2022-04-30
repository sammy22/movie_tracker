package com.movietracker;

import java.net.URL;
import java.util.logging.Level;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import com.movietracker.Observer.*;

/**
 * Starts up a server that serves static files from the top-level directory.
 * 
 * from: https://happycoding.io/tutorials/java-server/embedded-jetty
 */
public class App {

    public static void main(String[] args) throws Exception {
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);

        // Create a server that listens on port 8080.
        Server server = new Server(8080);
        WebAppContext webAppContext = new WebAppContext();
        server.setHandler(webAppContext);

        // Load static content from the resources directory.

        URL webAppDir = App.class.getClassLoader().getResource("META-INF/resources");
        webAppContext.setResourceBase(webAppDir.toURI().toString());

        // // Look for annotations in the classes directory (dev server) and in the
        // // jar file (live server).
        webAppContext.setAttribute(
                "org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern",
                ".*/target/classes/|.*\\.jar");

        Publisher p = new Publisher();

        Log logger = Log.getInstance();

        p.attach(logger);

        webAppContext.setAttribute(
                "publisher",
                p);

        // Start the server! 🚀
        server.start();
        System.out.println("Server started!");

        // Keep the main thread alive while the server is running.
        server.join();
    }
}