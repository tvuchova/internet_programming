package org.lecture.initial;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import java.io.IOException;


@Slf4j
//AbstractHandler in Jetty provides more flexibility t
public class SimpleJettyServer {

    public static void main(String[] args) throws Exception {
        // Step 1: Create a Jetty server instance on port 8080
        // Step 2: Set the handler to handle requests
        // Step 3: Start the server
        // Step 4: Keep the server running


    }

    // Define a custom handler by extending AbstractHandler
    public static class HelloHandler extends AbstractHandler {
        @Override
        public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
                throws IOException {
            // Set response type and status code
            // Write response content
            // Mark the request as handled


        }
    }
}
