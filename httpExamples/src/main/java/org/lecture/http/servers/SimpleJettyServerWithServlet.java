package org.lecture.http.servers;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.io.IOException;

@Slf4j
public class SimpleJettyServerWithServlet {

    public static void main(String[] args) throws Exception {
        // Step 1: Create a Jetty server instance on port 8080
        Server server = new Server(8000);

        // Step 2: Set up a servlet context handler
        ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        handler.setContextPath("/");

        // Step 3: Add a servlet to handle requests at "/hello"
        handler.addServlet(new ServletHolder(new HelloServlet()), "/hello");

        // Step 4: Attach the handler to the server
        server.setHandler(handler);

        // Step 5: Start the server
        server.start();
        log.info("Jetty server started on http://localhost:8080/hello");

        // Keep the server running
        server.join();
    }

    // Define a simple servlet that responds with "Hello, Jetty!"
    public static class HelloServlet extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            resp.setContentType("text/plain");
            resp.getWriter().write("Hello, Jetty!");
        }
    }
}
