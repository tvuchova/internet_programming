package org.lecture.initial;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

import java.io.IOException;
import java.util.UUID;

public class HttpServerTask {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static User userState = new User();

    public static void main(String[] args) throws Exception {
        Server server = new Server(8000);
        server.setHandler(new RequestHandler());

        server.start();
        server.join();
    }

    static class RequestHandler extends AbstractHandler {
        @Override
        public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {

            switch (target) {
                case "/hello":
                    if("GET".equalsIgnoreCase(request.getMethod())) {
                        handleHello(response);
                    } else {
                        handleMethodNotAllowed(response);
                    }
                    break;

                case "/user":
                    if("POST".equalsIgnoreCase(request.getMethod())) {
                        handleCreateUser(request, response);
                    } else if("PUT".equalsIgnoreCase(request.getMethod())) {
                        handleReplaceUser(request, response);
                    } else if("PATCH".equalsIgnoreCase(request.getMethod())) {
                        handlePatchUser(request, response);
                    } else {
                        handleMethodNotAllowed(response);
                    }

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
            String message = "Hello World, geicho";
            response.setContentType("text/plain");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(message);
        }

        private void handleCreateUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
            try {
                User user = objectMapper.readValue(request.getInputStream(), User.class);
                synchronized (HttpServerTask.class) {
                    UUID uuid = UUID.randomUUID();
                    user.setId(uuid);
                    userState = user;
                }
                response.setContentType("application/json;charset=utf-8");
                response.setStatus(HttpServletResponse.SC_CREATED);
                response.getWriter().println(objectMapper.writeValueAsString(userState));
            }catch(Exception e){
                    handleBadRequest(response);
            }

        }

        private void handleReplaceUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
            try {
                synchronized (HttpServerTask.class) {
                    userState.setStatus("true");
                    userState.setName("");
                    userState.setAge(0);
                    userState.setEmail("");
                    userState.setId(null);
                }
                response.setContentType("application/json;charset=utf-8");
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println(objectMapper.writeValueAsString(userState));
            }catch(Exception e){
                handleBadRequest(response);
            }
        }

        private void handlePatchUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
            try {
                User user = objectMapper.readValue(request.getInputStream(), User.class);
                synchronized (HttpServerTask.class) {
                    if(user.getName() != null) userState.setName(user.getName());
                    if(user.getAge() != 0) userState.setAge(user.getAge());
                    if(user.getEmail() != null) userState.setEmail(user.getEmail());
                    if(user.getStatus() != null) userState.setStatus(user.getStatus());
                    if(user.getId() != null) userState.setId(user.getId());
                }
                response.setContentType("application/json;charset=utf-8");
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println(objectMapper.writeValueAsString(userState));
            }catch(Exception e){
                handleBadRequest(response);
            }
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
