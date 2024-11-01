package org.lection.initial;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.net.http.HttpClient;


@Slf4j
public class AsyncHttpRequestExample {
    private static HttpClient client = HttpClient.newHttpClient();
    private static ObjectMapper objectMapper = new ObjectMapper();

    // Create HttpRequest for request
    //"https://jsonplaceholder.typicode.com/posts/1"
    // Send the request asynchronously
    // Process the response when it's received
    public static void main(String[] args) {


    }


}
