package org.exercise4.students.mihail;

import java.net.URL;
import java.net.URLEncoder;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class SecondTask {


    public static boolean isValidURL(String urlString) {
        try {
            new URL(urlString).toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String encodeURL(String urlString) {
        return URLEncoder.encode(urlString, StandardCharsets.UTF_8);
    }

    public static String decodeURL(String encodedUrl) {
        return URLDecoder.decode(encodedUrl, StandardCharsets.UTF_8);
    }

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter a URL: ");
        String inputUrl = scanner.nextLine();

        if (isValidURL(inputUrl)) {
            System.out.println("The URL is valid.");

            String encodedUrl = encodeURL(inputUrl);
            System.out.println("Encoded URL: " + encodedUrl);

            String decodedUrl = decodeURL(encodedUrl);
            System.out.println("Decoded URL: " + decodedUrl);
        } else {
            System.out.println("Invalid URL format.");
        }

        scanner.close();
    }
}
