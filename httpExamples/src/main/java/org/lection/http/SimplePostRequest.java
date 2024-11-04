package org.lection.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Slf4j
public class SimplePostRequest {
    public static final String URL = "https://jsonplaceholder.typicode.com/posts";

    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) {
        try {
            // Define the target URI and create a new Post object
            URI uri = URI.create(URL);
            Post post = new Post(1, 123, "Post title", "This is a sample POST request.");
            String jsonPayload = objectMapper.writeValueAsString(post);
            log.info("JSON payload: {}", jsonPayload);

            // Build HTTP POST request with JSON payload
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            log.info("Response status code: " + response.statusCode());
            log.info("Response body: " + response.body());

            // Convert JSON response body back into a Post object
            Post responsePost = objectMapper.readValue(response.body(), Post.class);

            // Log the converted Post object
            log.info("Response Post object: " + responsePost);
        } catch (Exception e) {
            log.error("Error creating or sending POST request", e);
        }
    }
}
