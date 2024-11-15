package org.exercise4.students.bogdan.CrudUsers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.exercise4.students.bogdan.CrudUsers.POJOs.GetAllUsersPOJO;
import org.exercise4.students.bogdan.CrudUsers.POJOs.GetSingleUserPOJO;
import org.exercise4.students.bogdan.CrudUsers.POJOs.UserPOJO;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Slf4j
public class Main {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final HttpClient client = HttpClient.newHttpClient();
    public static final String GET_ALL_USERS_URL = "https://reqres.in/api/users?page=2";
    public static final String GET_SINGLE_USER_URL = "https://reqres.in/api/users/2";
    public static final String GET_NOT_FOUND_URL = "https://reqres.in/api/users/23";
    public static final String POST_USER_URL = "https://reqres.in/api/users";
    public static final String PUT_USER_URL = "https://reqres.in/api/users/2";
    public static final String DELETE_USER_URL = "https://reqres.in/api/users/2";

    public static void main(String[] args) throws IOException, InterruptedException {
        log.info("Get single user");
        getSingleUser();
        log.info("Get all users");
        getAllUsers();
        log.info("Get not found user");
        getNotFound();
        log.info("Post user");
        postUser();
        log.info("Put user");
        putUser();
        log.info("Delete user");
        deleteUser();
    }

    private static void getSingleUser() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(GET_SINGLE_USER_URL))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        log.info("Response status code: {}", response.statusCode());
        if(response.statusCode() == 200) {
            GetSingleUserPOJO user = objectMapper.readValue(response.body(), GetSingleUserPOJO.class);
            log.info(user.getData().toString());
        }
    }

    private static void getAllUsers() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(GET_ALL_USERS_URL))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        log.info("Response status code: {}", response.statusCode());
        if(response.statusCode() == 200) {
            GetAllUsersPOJO users = objectMapper.readValue(response.body(), GetAllUsersPOJO.class);
            for (UserPOJO user : users.getData()) {
                log.info(user.toString());
            }
        }
    }

    private static void getNotFound() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(GET_NOT_FOUND_URL))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if(response.statusCode() == 404) {
            log.info("User not found");
        }
        log.info("Response status code: {}", response.statusCode());
    }

    private static void postUser() throws IOException, InterruptedException {
        UserPOJO user = new UserPOJO();
        user.setAvatar("gfgfgf");
        user.setFirst_name("sggdfg");
        user.setLast_name("kfghkghkkfgh");
        user.setEmail("email@reqres.com");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(POST_USER_URL))
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(user)))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        log.info("Response status code: {}", response.statusCode());
        log.info("Response body: {}", response.body());
    }

    private static void putUser() throws IOException, InterruptedException {
        UserPOJO user = new UserPOJO();
        user.setEmail("email@reqres.com");
        user.setFirst_name("Kakakakaka");
        user.setLast_name("sdfsdfsdf");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(PUT_USER_URL))
                .PUT(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(user)))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        log.info("Response status code: {}", response.statusCode());
        log.info("Response body: {}", response.body());
    }

    private static void deleteUser() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(DELETE_USER_URL))
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        log.info("Response status code: {}", response.statusCode());
    }
}