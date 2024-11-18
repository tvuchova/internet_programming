package org.lecture.http.clients.async;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.lecture.http.clients.Post;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class AsyncPostExample {
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static final String URL = "https://jsonplaceholder.typicode.com/posts";

    public static void main(String[] args) {
        // Define the target URI and create a new Post object
        URI uri = URI.create(URL);
        Post post = new Post(1, 123, "Post title", "This is a sample POST request.");

        try {
            // Serialize the Post object to JSON
            String jsonPayload = objectMapper.writeValueAsString(post);
            log.info("JSON payload: {}", jsonPayload);

            // Build HTTP POST request with JSON payload
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            // Send request asynchronously and process response
            CompletableFuture<HttpResponse<String>> responseFuture = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());

            responseFuture
                    .thenApply(HttpResponse::body)
                    .thenAccept(responseBody -> log.info("Response: {}", responseBody))
                    .exceptionally(e -> {
                        log.error("POST request failed", e);
                        return null;
                    });

            // Wait for the asynchronous call to complete
            responseFuture.join();

        } catch (Exception e) {
            log.error("Error creating or sending POST request", e);
        }

    }

}
