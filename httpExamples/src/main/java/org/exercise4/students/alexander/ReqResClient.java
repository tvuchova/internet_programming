package org.exercise4.students.alexander;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.util.Scanner;

@Slf4j
public class ReqResClient {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final HttpClient client = HttpClient.newHttpClient();

    public static void main(String[] args) throws IOException, InterruptedException {
        while(true) {
            System.out.println("What would you like to do?");
            System.out.println("1. List users");
            System.out.println("2. Get User");
            System.out.println("3. Get nonexistent user");
            System.out.println("4. Create user");
            System.out.println("5. Update user");
            System.out.println("6. Update user job");
            System.out.println("7. Delete user");
            System.out.println("8. Exit");
            System.out.println("Enter your choice: ");
            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            if (choice == 1) {
                ListUsers("https://reqres.in/api/users");
            } else if (choice == 2) {
                System.out.println("Enter user ID: ");
                int id = scanner.nextInt();
                GetUser("https://reqres.in/api/users/" + id);
            } else if (choice == 3) {
                System.out.println("Enter nonexistent user ID: ");
                int id = scanner.nextInt();
                GetUser("https://reqres.in/api/users/" + id);
            } else if (choice == 4) {
                System.out.println("Enter user name: ");
                String name = scanner.next();
                System.out.println("Enter user job: ");
                String job = scanner.next();
                CreateUser("https://reqres.in/api/users", name, job);
            } else if (choice == 5) {
                System.out.println("Enter user ID: ");
                int id = scanner.nextInt();
                System.out.println("Enter user name: ");
                String name = scanner.next();
                System.out.println("Enter user job: ");
                String job = scanner.next();
                UpdateUser("https://reqres.in/api/users/" + id, name, job);
            } else if (choice == 6) {
                System.out.println("Enter user ID: ");
                int id = scanner.nextInt();
                System.out.println("Enter user job: ");
                String job = scanner.next();
                UpdateUserJob("https://reqres.in/api/users/" + id,  job);
            } else if (choice == 7) {
                System.out.println("Enter user ID: ");
                int id = scanner.nextInt();
                DeleteUser("https://reqres.in/api/users/" + id);
            } else if (choice == 8) {
                break;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void DeleteUser(String URL) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 204) {
            log.info("User with deleted successfully.");
        } else if (response.statusCode() == 404) {
            log.info("User does not exist.");
        } else {
            log.error("Failed to delete user. Status code: {}", response.statusCode());
        }
    }

    private static void UpdateUserJob(String URL, String job) throws IOException, InterruptedException {
        UserUpdateJobRequest userUpdateJobRequest = new UserUpdateJobRequest(job);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .header("Content-Type", "application/json")
                .method("PATCH", HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(userUpdateJobRequest)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if(response.statusCode() != 200) {
            log.error("Failed to update user job. Status code: {}", response.statusCode());
            return;
        }

        JsonNode jsonNode = objectMapper.readTree(response.body());
        String updatedJob = jsonNode.get("job").asText();
        String updatedAt = jsonNode.get("updatedAt").asText();

        log.info("Updated User Job Details:");
        log.info("Job: {}", updatedJob);
        log.info("Updated At: {}", updatedAt);
    }

    private static void UpdateUser(String URL, String name, String job) throws IOException, InterruptedException {
        UserRequest userRequest = new UserRequest(name, job);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(userRequest)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if(response.statusCode() != 200) {
            log.error("Failed to update user. Status code: {}", response.statusCode());
            return;
        }

        log.info("User updated successfully with status code: {}", response.statusCode());


        JsonNode jsonNode = objectMapper.readTree(response.body());
        String updatedName = jsonNode.get("name").asText();
        String updatedJob = jsonNode.get("job").asText();
        String updatedAt = jsonNode.get("updatedAt").asText();

        log.info("Updated User Details:");
        log.info("Name: {}", updatedName);
        log.info("Job: {}", updatedJob);
        log.info("Updated At: {}", updatedAt);
    }

    private static void CreateUser(String URL, String name, String job) throws IOException, InterruptedException {
        UserRequest userRequest = new UserRequest(name, job);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(userRequest)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        log.info("Response status code: {}", response.statusCode());

        if(response.statusCode() != 201) {
            log.error("Failed to create user. Status code: {}", response.statusCode());
            return;
        }

        log.info("User created successfully.");

        JsonNode jsonNode = objectMapper.readTree(response.body());
        String createdName = jsonNode.get("name").asText();
        String createdJob = jsonNode.get("job").asText();
        String createdAt = jsonNode.get("createdAt").asText();
        String id = jsonNode.get("id").asText();

        log.info("Created User Details:");
        log.info("ID: {}", id);
        log.info("Name: {}", createdName);
        log.info("Job: {}", createdJob);
        log.info("Created At: {}", createdAt);
    }

    private static void GetUser(String URL) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        log.info("Response status code: {}", response.statusCode());

        if(response.statusCode() != 200) {
            log.error("Failed to get user. Status code: {}", response.statusCode());
            return;
        }

        UserResponse userResponse = objectMapper.readValue(response.body(), UserResponse.class);

        UserData user = userResponse.getData();
        if (user != null) {
            log.info(user.toString());
        } else {
            log.warn("No user data found in the response.");
        }
    }

    public static void ListUsers(String URL) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        log.info("Response status code: {}", response.statusCode());

        if(response.statusCode() != 200) {
            log.error("Failed to get users list. Status code: {}", response.statusCode());
            return;
        }
        Post post = objectMapper.readValue(response.body(), Post.class);
        log.info(post.toString());


        if (post.getData() != null) {
            log.info("Users list:");
            for (UserData user : post.getData()) {
                log.info(user.toString());
            }
        } else {
            log.warn("No user data found in the response.");
        }
    }

}
