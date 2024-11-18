package org.exercise4.students.mihail;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class FirstTask {
    private static final Logger logger = LoggerFactory.getLogger(FirstTask.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final String USERS_URL = "https://reqres.in/api/users";

    public static void main(String[] args) throws IOException, InterruptedException {
        listUsers();
        getUser(2);
        getUser(23);
        createUser("Tomi", "codbex stazhant");
        updateUser(2, "Tomi", "codbex Senior Software Engineer");
        updateUserJob(2, "codbex Lead Engineer");
        deleteUser(2);
    }

    public static void listUsers() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(USERS_URL + "?page=2"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            logger.info("User list from page 2:");
            prettyPrintJson(response.body());
        } else {
            logger.error("Failed to retrieve user list.");
        }
    }

    public static void getUser(int userId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(USERS_URL + "/" + userId))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            logger.info("User found:");
            prettyPrintJson(response.body());
        } else {
            logger.error("User with ID " + userId + " not found.");
        }
    }

    public static void createUser(String name, String job) throws IOException, InterruptedException {
        UserUpdateAndCreateRequest userUpdateRequest = new UserUpdateAndCreateRequest(name, job);
        String json = objectMapper.writeValueAsString(userUpdateRequest);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(USERS_URL))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 201) { // 201 Created
            logger.info("New user created:");
            prettyPrintJson(response.body());
        } else {
            logger.error("Failed to create user.");
        }
    }

    public static void updateUser(int userId, String name, String job) throws IOException, InterruptedException {
        UserUpdateAndCreateRequest userUpdateRequest = new UserUpdateAndCreateRequest(name, job);
        String json = objectMapper.writeValueAsString(userUpdateRequest);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(USERS_URL + "/" + userId))
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            logger.info("User updated:");
            prettyPrintJson(response.body());
        } else {
            logger.error("Failed to update user with ID " + userId);
        }
    }

    public static void updateUserJob(int userId, String job) throws IOException, InterruptedException {
        UpdateJobRequest jobUpdateRequest = new UpdateJobRequest(job);
        String json = objectMapper.writeValueAsString(jobUpdateRequest);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(USERS_URL + "/" + userId))
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            logger.info("User job updated:");
            prettyPrintJson(response.body());
        } else {
            logger.error("Failed to update job for user with ID " + userId);
        }
    }

    public static void deleteUser(int userId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(USERS_URL + "/" + userId))
                .DELETE()
                .build();


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 204) {
            logger.info("User with ID " + userId + " deleted successfully.");
        } else {
            logger.error("Failed to delete user with ID " + userId);
        }
    }

    private static void prettyPrintJson(String json) throws IOException {
        JsonNode jsonNode = objectMapper.readTree(json);
        String prettyJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
        logger.info(prettyJson);
    }
}
