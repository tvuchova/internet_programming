package org.exercise5.teacher;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class HttpServerTask {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final List<User> users = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        Server server = new Server(8000);
        server.setHandler(new RequestHandler());

        server.start();
        server.join();
    }

    static class RequestHandler extends AbstractHandler {

        public static final String TEXT_PLAIN = "text/plain";
        public static final String APPLICATION_JSON_CHARSET_UTF_8 = "application/json;charset=utf-8";

        @Override
        public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
            baseRequest.setHandled(true);

            if (target.equals("/hello") && isMethod(request, "GET")) {
                handleHello(response);
                return;
            }

            if (target.startsWith("/user")) {
                String[] parts = target.split("/");
                if (parts.length == 2) {
                    handleUserOperations(request, response);
                } else if (parts.length == 3) {
                    String userId = parts[2];
                    handleUserById(userId, request, response);
                } else {
                    handleNotFound(response);
                }
            } else {
                handleNotFound(response);
            }
        }

        private void handleUserOperations(HttpServletRequest request, HttpServletResponse response) throws IOException {
            if (isMethod(request, "POST")) {
                handleCreateUser(request, response);
            } else {
                handleMethodNotAllowed(response);
            }
        }

        private void handleUserById(String userId, HttpServletRequest request, HttpServletResponse response) throws IOException {
            if (isMethod(request, "GET")) {
                handleGetUserById(userId, response);
            } else if (isMethod(request, "PUT")) {
                handleReplaceUser(userId, request, response);
            } else if (isMethod(request, "PATCH")) {
                handlePatchUser(userId, request, response);
            } else {
                handleMethodNotAllowed(response);
            }
        }

        private void handleHello(HttpServletResponse response) throws IOException {
            response.setContentType(TEXT_PLAIN);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("Hello, World!");
        }

        private void handleCreateUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
            try {
                response.setContentType(APPLICATION_JSON_CHARSET_UTF_8);

                User receivedUser = objectMapper.readValue(request.getReader(), User.class);
                final UUID newId = generateUniqueUserId();
                receivedUser.setId(newId);

                synchronized (HttpServerTask.class) {
                    users.add(receivedUser);
                }

                response.setStatus(HttpServletResponse.SC_CREATED);
                response.getWriter().println(objectMapper.writeValueAsString(receivedUser));
            } catch (Exception e) {
                handleBadRequest(response);
            }
        }


        private void handleReplaceUser(String userId, HttpServletRequest request, HttpServletResponse response) throws IOException {
            try {
                response.setContentType(APPLICATION_JSON_CHARSET_UTF_8);

                User updatedUser = objectMapper.readValue(request.getReader(), User.class);
                UUID id = UUID.fromString(userId);
                updatedUser.setId(id);

                synchronized (HttpServerTask.class) {
                    Optional<User> foundUser = users.stream()
                            .filter(user -> user.getId().equals(id))
                            .findFirst();
                    if (foundUser.isPresent()) {
                        users.remove(foundUser.get());
                        users.add(updatedUser);
                    } else {
                        handleNotFound(response);
                        return;
                    }
                }

                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println(objectMapper.writeValueAsString(updatedUser));
            } catch (Exception e) {
                handleBadRequest(response);
            }
        }

        private void handlePatchUser(String userId, HttpServletRequest request, HttpServletResponse response) throws IOException {
            try {
                response.setContentType(APPLICATION_JSON_CHARSET_UTF_8);

                User partialUser = objectMapper.readValue(request.getReader(), User.class);
                UUID id = UUID.fromString(userId);

                synchronized (HttpServerTask.class) {
                    Optional<User> foundUser = users.stream()
                            .filter(user -> user.getId().equals(id))
                            .findFirst();

                    if (foundUser.isPresent()) {
                        User user = foundUser.get();
                        if (partialUser.getName() != null) {
                            user.setName(partialUser.getName());
                        }
                        if (partialUser.getAge() != 0) {
                            user.setAge(partialUser.getAge());
                        }
                        if (partialUser.getEmail() != null) {
                            user.setEmail(partialUser.getEmail());
                        }
                        if (partialUser.getStatus() != null) {
                            user.setStatus(partialUser.getStatus());
                        }
                        response.setStatus(HttpServletResponse.SC_OK);
                        response.getWriter().println(objectMapper.writeValueAsString(user));
                    } else {
                        handleNotFound(response);
                    }
                }
            } catch (Exception e) {
                handleBadRequest(response);
            }
        }

        private void handleGetUserById(String userId, HttpServletResponse response) throws IOException {
            try {
                UUID id = UUID.fromString(userId);
                Optional<User> foundUser = users.stream()
                        .filter(user -> user.getId().equals(id))
                        .findFirst();

                if (foundUser.isPresent()) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().println(objectMapper.writeValueAsString(foundUser.get()));
                } else {
                    handleNotFound(response);
                }
            } catch (Exception e) {
                handleBadRequest(response);
            }
        }

        private UUID generateUniqueUserId() {
            Set<UUID> existingIds = users.stream()
                    .map(User::getId)
                    .collect(Collectors.toSet());

            UUID uniqueId = UUID.randomUUID();
            while (existingIds.contains(uniqueId)) {
                uniqueId = UUID.randomUUID();
            }

            return uniqueId;
        }

        private boolean isMethod(HttpServletRequest request, String method) {
            return method.equalsIgnoreCase(request.getMethod());
        }

        private void handleMethodNotAllowed(HttpServletResponse response) throws IOException {
            response.setContentType(APPLICATION_JSON_CHARSET_UTF_8);
            response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            response.getWriter().println("{\"error\":\"405 Method Not Allowed\"}");
        }

        private void handleBadRequest(HttpServletResponse response) throws IOException {
            response.setContentType(APPLICATION_JSON_CHARSET_UTF_8);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("{\"error\":\"Invalid JSON data\"}");
        }

        private void handleNotFound(HttpServletResponse response) throws IOException {
            response.setContentType(APPLICATION_JSON_CHARSET_UTF_8);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().println("{\"error\":\"404 Not Found\"}");
        }
    }
}
