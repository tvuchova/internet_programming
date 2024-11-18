package org.lecture.initial;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import java.io.IOException;

public class HttpServerTask {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    //private static User userState = new User();

    public static void main(String[] args) throws Exception {

    }

    static class RequestHandler extends AbstractHandler {
        @Override
        public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {

            switch (target) {
                case "/hello":

                    break;

                case "/user":

                    break;

                default:
                    handleNotFound(response);
                    break;
            }
            baseRequest.setHandled(true);

        }

        private boolean isMethod(HttpServletRequest request, String method) {
            return method.equalsIgnoreCase(request.getMethod());
        }

        private void handleHello(HttpServletResponse response) throws IOException {

        }

        private void handleCreateUser(HttpServletRequest request, HttpServletResponse response) throws IOException {

        }

        private void handleReplaceUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        }

        private void handlePatchUser(HttpServletRequest request, HttpServletResponse response) throws IOException {

        }

        private void handleMethodNotAllowed(HttpServletResponse response) throws IOException {
            response.setContentType("application/json;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            response.getWriter().println("{\"error\":\"405 Method Not Allowed\"}");
        }

        private void handleBadRequest(HttpServletResponse response) throws IOException {
            response.setContentType("application/json;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("{\"error\":\"Invalid JSON data\"}");
        }

        private void handleNotFound(HttpServletResponse response) throws IOException {
            response.setContentType("application/json;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().println("{\"error\":\"404 Not Found\"}");
        }
    }
}
