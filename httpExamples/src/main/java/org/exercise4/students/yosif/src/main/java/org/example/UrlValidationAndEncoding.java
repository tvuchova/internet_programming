package org.example;

import java.io.UnsupportedEncodingException;
import java.net.*;

public class UrlValidationAndEncoding {

    // Method to validate URL
    public static boolean isValidUrl(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Method to encode URL
    public static String encodeUrl(String url) throws UnsupportedEncodingException {
        return URLEncoder.encode(url, "UTF-8");
    }

    // Method to decode URL
    public static String decodeUrl(String encodedUrl) throws UnsupportedEncodingException {
        return URLDecoder.decode(encodedUrl, "UTF-8");
    }

    // Main method
    public static void main(String[] args) throws UnsupportedEncodingException {
        // Prompt user for input
        System.out.println("Enter a URL:");
        String inputUrl = "https://www.example.com/search?query=hello world & java encoding";

        // Validate URL
        if (isValidUrl(inputUrl)) {
            System.out.println("Valid URL.");

            // Encode URL
            String encodedUrl = encodeUrl(inputUrl);
            System.out.println("Encoded URL: " + encodedUrl);

            // Decode URL
            String decodedUrl = decodeUrl(encodedUrl);
            System.out.println("Decoded URL: " + decodedUrl);
        } else {
            System.out.println("Invalid URL.");
        }
    }
}
