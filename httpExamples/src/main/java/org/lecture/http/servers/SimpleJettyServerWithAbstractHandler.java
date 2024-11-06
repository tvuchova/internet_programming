package org.lecture.http.servers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

import java.io.IOException;


@Slf4j
//AbstractHandler in Jetty provides more flexibility t
public class SimpleJettyServerWithAbstractHandler {

    public static void main(String[] args) throws Exception {
        // Step 1: Create a Jetty server instance on port 8080
        Server server = new Server(8080);

        // Step 2: Set the handler to handle requests
        server.setHandler(new HelloHandler());

        // Step 3: Start the server
        server.start();
        log.info("Jetty server started on http://localhost:8080");

        // Step 4: Keep the server running
        server.join();
    }

    // Define a custom handler by extending AbstractHandler
    public static class HelloHandler extends AbstractHandler {
        @Override
        public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
                throws IOException {
            // Set response type and status code
            response.setContentType("text/plain; charset=utf-8");
            response.setStatus(HttpServletResponse.SC_OK);

            // Write response content
            response.getWriter().println("Hello, I am jetty server!");

            // Mark the request as handled
            baseRequest.setHandled(true);
        }
    }
}
