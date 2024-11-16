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
public class AsyncHttpRequestExample {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final HttpClient client = HttpClient.newHttpClient();
    public static final String URL = "https://jsonplaceholder.typicode.com/posts/1";

    public static void main(String[] args) {

        // Create HttpRequest for GET request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .GET()
                .build();

        // Send the request asynchronously
        CompletableFuture<HttpResponse<String>> responseFuture = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());

        // Process the response when it's received
        responseFuture
                .thenApply(HttpResponse::body)
                .thenApply(AsyncHttpRequestExample::parseJsonResponse)
                .thenAccept(post -> log.info("Deserialized Post: {}", post))
                .exceptionally(e -> {
                    log.error("Request failed: ", e);
                    return null;
                })
                .join();
    }

    private static Post parseJsonResponse(String responseBody) {
        try {
            return objectMapper.readValue(responseBody, Post.class);
        } catch (Exception e) {
            log.error("Failed to parse JSON response", e);
            return null;
        }
    }
}
