package org.example;

import com.google.gson.Gson;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;
import org.apache.http.util.EntityUtils;
import org.apache.http.entity.StringEntity;
import java.io.IOException;
import java.util.List;
import org.example.ApiResponse.*;
import org.example.User.*;

public class ReqResClient {

    private static final String BASE_URL = "https://reqres.in/api/users";
    private static final CloseableHttpClient httpClient = HttpClients.createDefault();
    private static final Gson gson = new Gson();

    // Task 1: List Users (Page 2)
    public static void listUsers() throws IOException {
        String url = BASE_URL + "?page=2";
        HttpGet request = new HttpGet(url);
        CloseableHttpResponse response = httpClient.execute(request);
        String jsonResponse = EntityUtils.toString(response.getEntity());
        ApiResponse apiResponse = gson.fromJson(jsonResponse, ApiResponse.class);

        System.out.println("Page: " + apiResponse.getPage());
        for (User user : apiResponse.getData()) {
            System.out.println(user.getFirst_name() + " " + user.getLast_name());
        }
    }

    // Task 2: Get Single User
    public static void getSingleUser(int userId) throws IOException {
        String url = BASE_URL + "/" + userId;
        HttpGet request = new HttpGet(url);
        CloseableHttpResponse response = httpClient.execute(request);
        String jsonResponse = EntityUtils.toString(response.getEntity());

        if (response.getStatusLine().getStatusCode() == 200) {
            User user = gson.fromJson(jsonResponse, User.class);
            System.out.println("User: " + user.getFirst_name() + " " + user.getLast_name());
        } else {
            System.out.println("User not found!");
        }
    }

    // Task 3: Handle "User Not Found"
    public static void handleUserNotFound(int userId) throws IOException {
        String url = BASE_URL + "/" + userId;
        HttpGet request = new HttpGet(url);
        CloseableHttpResponse response = httpClient.execute(request);
        if (response.getStatusLine().getStatusCode() == 404) {
            System.out.println("User not found (404 error).");
        }
    }

    // Task 4: Create a New User
    public static void createUser(String name, String job) throws IOException {
        String url = BASE_URL;
        HttpPost request = new HttpPost(url);
        String json = "{ \"name\": \"" + name + "\", \"job\": \"" + job + "\" }";
        StringEntity entity = new StringEntity(json);
        request.setEntity(entity);
        request.setHeader("Content-Type", "application/json");

        CloseableHttpResponse response = httpClient.execute(request);
        String jsonResponse = EntityUtils.toString(response.getEntity());
        User createdUser = gson.fromJson(jsonResponse, User.class);

        System.out.println("Created User: " + createdUser.getFirst_name() + " " + createdUser.getLast_name());
    }

    // Task 5: Update User
    public static void updateUser(int userId, String name, String job) throws IOException {
        String url = BASE_URL + "/" + userId;
        HttpPut request = new HttpPut(url);
        String json = "{ \"name\": \"" + name + "\", \"job\": \"" + job + "\" }";
        StringEntity entity = new StringEntity(json);
        request.setEntity(entity);
        request.setHeader("Content-Type", "application/json");

        CloseableHttpResponse response = httpClient.execute(request);
        String jsonResponse = EntityUtils.toString(response.getEntity());
        User updatedUser = gson.fromJson(jsonResponse, User.class);

        System.out.println("Updated User: " + updatedUser.getFirst_name() + " " + updatedUser.getLast_name());
    }

    // Task 6: Delete User
    public static void deleteUser(int userId) throws IOException {
        String url = BASE_URL + "/" + userId;
        HttpDelete request = new HttpDelete(url);
        CloseableHttpResponse response = httpClient.execute(request);
        if (response.getStatusLine().getStatusCode() == 204) {
            System.out.println("User " + userId + " has been deleted.");
        } else {
            System.out.println("Failed to delete user.");
        }
    }
}


