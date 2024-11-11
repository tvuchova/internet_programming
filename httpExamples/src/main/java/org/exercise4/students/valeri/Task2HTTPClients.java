package org.exercise4.students.valeri;


import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Task2HTTPClients {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Please enter a URL: ");
        String inputUrl = scanner.nextLine();

        if (isValidURL(inputUrl)) {
            log.info("The URL is valid.");

            try {
                String encodedUrl = encodeUrl(inputUrl);
                System.out.println("Encoded URL: " + encodedUrl);

                String decodedUrl = URLDecoder.decode(encodedUrl, StandardCharsets.UTF_8);
                System.out.println("Decoded URL: " + decodedUrl);
            } catch (Exception e) {
                log.error("Error encoding/decoding the URL: " + e.getMessage());
            }
        } else {
            log.info("The URL is invalid.");
        }

        scanner.close();
    }

    private static boolean isValidURL(String url) {
        try {
            return url.startsWith("http://") || url.startsWith("https://");
        } catch (IllegalArgumentException  e) {
            return false;
        }
    }

    private static String encodeUrl(String url) throws Exception {
        int queryIndex = url.indexOf('?');
        if (queryIndex == -1) {
            return URLEncoder.encode(url, StandardCharsets.UTF_8);
        }

        String baseUrl = url.substring(0, queryIndex);
        String query = url.substring(queryIndex + 1);

        String encodedBaseUrl = URLEncoder.encode(baseUrl, StandardCharsets.UTF_8);
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);

        return encodedBaseUrl + "?" + encodedQuery;
    }
}
