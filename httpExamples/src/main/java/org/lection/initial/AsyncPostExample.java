package org.lection.initial;

import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.http.HttpClient;

@Slf4j
public class AsyncPostExample {
    private static final HttpClient client = HttpClient.newHttpClient();

    public static void main(String[] args) {
        // Define the target URI and create a new Post object
        try {
            URI uri = URI.create("https://jsonplaceholder.typicode.com/posts");
            

        } catch (Exception e) {
            log.error("Error creating or sending POST request", e);
        }
    }

}
