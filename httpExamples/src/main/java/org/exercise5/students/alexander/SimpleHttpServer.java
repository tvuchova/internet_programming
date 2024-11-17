package org.exercise5.students.alexander;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
public class SimpleHttpServer
{
    private static User userState = new User(null, null, 0, null, null);

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        server.createContext("/hello", new HelloHandler());
        server.createContext("/user", new UserHandler());

        server.setExecutor(null);
        server.start();
        System.out.println("Server started on port 8000");
    }

    static class HelloHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if("GET".equals(exchange.getRequestMethod())){
                String response = "Hello World!";
                exchange.sendResponseHeaders(200, response.length());
                exchange.getResponseBody().write(response.getBytes(StandardCharsets.UTF_8));
                exchange.getResponseBody().close();
            }
            else{
                exchange.sendResponseHeaders(405, 0);
                exchange.getResponseBody().close();
            }
        }
    }

    static class UserHandler implements HttpHandler{

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            ObjectMapper objectMapper = new ObjectMapper();
            String method = exchange.getRequestMethod();

            switch(method) {
                case "POST":
                    InputStream postData = exchange.getRequestBody();

                    User user = objectMapper.readValue(postData, User.class);
                    userState = user;
                    log.info("User created: {}", user.toString());

                    String postResponse = objectMapper.writeValueAsString(user);

                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(200, postResponse.length());

                    exchange.getResponseBody().write(postResponse.getBytes(StandardCharsets.UTF_8));
                    exchange.getResponseBody().close();
                    break;

                case "PUT":
                    InputStream putData = exchange.getRequestBody();
                    userState = objectMapper.readValue(putData, User.class);
                    log.info("User updated: {}", userState.toString());

                    String putResponse = objectMapper.writeValueAsString(userState);

                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(200, putResponse.length());
                    exchange.getResponseBody().write(putResponse.getBytes(StandardCharsets.UTF_8));
                    exchange.getResponseBody().close();
                    break;

                case "PATCH":
                    InputStream patchData = exchange.getRequestBody();
                    User patchUser = objectMapper.readValue(patchData, User.class);
                    if(patchUser.getId() != null){
                        userState.setId(patchUser.getId());
                    }
                    if(patchUser.getName() != null){
                        userState.setName(patchUser.getName());
                    }
                    if(patchUser.getAge() != 0){
                        userState.setAge(patchUser.getAge());
                    }
                    if(patchUser.getEmail() != null){
                        userState.setEmail(patchUser.getEmail());
                    }
                    if(patchUser.getStatus() != null){
                        userState.setStatus(patchUser.getStatus());
                    }
                    log.info("User patched: {}", userState.toString());

                    String patchResponse = objectMapper.writeValueAsString(userState);

                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(200, patchResponse.length());
                    exchange.getResponseBody().write(patchResponse.getBytes(StandardCharsets.UTF_8));
                    exchange.getResponseBody().close();
                    break;

                default:
                    exchange.sendResponseHeaders(405, 0);
                    exchange.getResponseBody().close();
                    break;
            }
        }
    }
}
