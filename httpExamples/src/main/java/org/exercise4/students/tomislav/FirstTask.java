package org.exercise4.students.tomislav;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class FirstTask {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final HttpClient client = HttpClient.newHttpClient();
    public static final String usersURL = "https://reqres.in/api/users/";

    public static void main(String[] args) throws IOException, InterruptedException {
        listUsers();
        getUser(2);
        getUser(23);
        createUser("Misho", "unlucky");
        updateUser(2, "Misho", "Senior Software Engineer");
        deleteUser(2);
    }

    public static void listUsers() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(usersURL))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        prettyPrintJson(response.body());
    }

    public static void getUser(int userId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(usersURL + userId))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            System.out.println("User found:");
            prettyPrintJson(response.body());
        }

        else {
            System.out.println("User with ID " + userId + " not found.");
        }
    }

    public static void createUser(String name, String job) throws IOException, InterruptedException {
        String json = String.format("""
                {
                    "name": "%s",
                    "job": "%s"
                }
                """, name, job);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(usersURL))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("New user created:");
        prettyPrintJson(response.body());
    }

    public static void updateUser(int userId, String name, String job) throws IOException, InterruptedException {
        String json = String.format("""
                {
                    "name": "%s",
                    "job": "%s"
                }
                """, name, job);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(usersURL + userId))
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("User updated:");
        prettyPrintJson(response.body());
    }

    public static void deleteUser(int userId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(usersURL + userId))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 204) {
            System.out.println("User with ID " + userId + " deleted successfully.");
        }

        else {
            System.out.println("User with ID " + userId + " not found.");
        }
    }

    private static void prettyPrintJson(String json) throws IOException {
        JsonNode jsonNode = objectMapper.readTree(json);
        String prettyJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
        System.out.println(prettyJson);
    }
}