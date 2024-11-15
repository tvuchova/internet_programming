package org.lecture.initial;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class HttpServerTask {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ConcurrentHashMap<UUID, User> userState = new ConcurrentHashMap<>();

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
                    if (isMethod(request, "GET")) {
                        handleHello(response);
                    } else {
                        handleMethodNotAllowed(response);
                    }
                    break;

                case "/user":
                    if (isMethod(request, "POST")) {
                        handleCreateUser(request, response);
                    } else if (isMethod(request, "PUT")) {
                        handleReplaceUser(request, response);
                    } else if (isMethod(request, "PATCH")) {
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
            String message = "Hello World!";
            response.setContentType("text/plain");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(message);
        }

        private void handleCreateUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
            try {
                User user = objectMapper.readValue(request.getReader(), User.class);
                UUID uuid = UUID.randomUUID();
                user.setId(uuid);
                userState.put(uuid, user);

                response.setContentType("application/json;charset=utf-8");
                response.setStatus(HttpServletResponse.SC_CREATED);
                response.getWriter().println(objectMapper.writeValueAsString(user));
            } catch (Exception e) {
                response.setContentType("application/json;charset=utf-8");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("{\"error\":\"Invalid JSON data\"}");
            }
        }

        private void handleReplaceUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
            try {
                User user = objectMapper.readValue(request.getReader(), User.class);
                 if (user.getId() == null || !userState.containsKey(user.getId())) {
                     response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                     response.getWriter().println("{\"error\":\"User not found\"}");
                     return;
                 }

                userState.put(user.getId(), user);

                response.setContentType("application/json;charset=utf-8");
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println(objectMapper.writeValueAsString(user));
            } catch (Exception e) {
                response.setContentType("application/json;charset=utf-8");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("{\"error\":\"Invalid JSON data\"}");
            }
        }

        private void handlePatchUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
            try {
                User user = objectMapper.readValue(request.getReader(), User.class);
                if (user.getId() == null || !userState.containsKey(user.getId())) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().println("{\"error\":\"User not found\"}");
                    return;
                }

                User existingUser = userState.get(user.getId());
                if (user.getName() != null) {
                    existingUser.setName(user.getName());
                }
                if (user.getEmail() != null) {
                    existingUser.setEmail(user.getEmail());
                }

                response.setContentType("application/json;charset=utf-8");
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println(objectMapper.writeValueAsString(existingUser));
            } catch (Exception e) {
                response.setContentType("application/json;charset=utf-8");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("{\"error\":\"Invalid JSON data\"}");
            }
        }

        private void handleMethodNotAllowed(HttpServletResponse response) throws IOException {
            response.setContentType("application/json;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            response.getWriter().println("{\"error\":\"405 Method Not Allowed\"}");
        }

        private void handleNotFound(HttpServletResponse response) throws IOException {
            response.setContentType("application/json;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().println("{\"error\":\"404 Not Found\"}");
        }
    }
}
