package org.exercise4.students.tomislav;

import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class SecondTask {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter a URL:");

        String inputUrl = scanner.nextLine();
        scanner.close();

        try {
            URI uri = new URI(inputUrl);
            URL url = uri.toURL();
            System.out.println("URL is valid: " + url);

            String encodedUrl = encodeUrl(inputUrl);
            System.out.println("Encoded URL: " + encodedUrl);

            String decodedUrl = decodeUrl(encodedUrl);
            System.out.println("Decoded URL: " + decodedUrl);

        }

        catch (Exception e) {
            System.out.println("Invalid URL format: " + e.getMessage());
        }
    }

    private static String encodeUrl(String url) {
        try {
            return URLEncoder.encode(url, StandardCharsets.UTF_8);
        }

        catch (Exception e) {
            System.out.println("Error encoding the URL: " + e.getMessage());
            return null;
        }
    }

    private static String decodeUrl(String url) {
        try {
            return URLDecoder.decode(url, StandardCharsets.UTF_8);
        }

        catch (Exception e) {
            System.out.println("Error decoding the URL: " + e.getMessage());
            return null;
        }
    }
}
