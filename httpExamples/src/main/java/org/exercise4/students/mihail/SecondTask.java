package org.exercise4.students.mihail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class SecondTask {
    private static final Logger logger = LoggerFactory.getLogger(FirstTask.class);
    public static boolean isValidURL(String urlString) {
        try {
            new URL(urlString).toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public static String encodeURL(String urlString) {
        try {
            URI uri = new URI(urlString);
            String query = uri.getQuery();
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
            return uri.getScheme() + "://" + uri.getHost() + uri.getPath() + "?" + encodedQuery;
        } catch (Exception e) {
            logger.error("Error encoding query parameters", e);
            return urlString;
        }
    }
    public static String decodeURL(String encodedUrl) {
        return URLDecoder.decode(encodedUrl, StandardCharsets.UTF_8);
    }
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        logger.info("Enter a URL: ");
        String inputUrl = scanner.nextLine();

        if (isValidURL(inputUrl)) {
            logger.info("The URL is valid.");

            String encodedUrl = encodeURL(inputUrl);
            logger.info("Encoded URL: " + encodedUrl);

            String decodedUrl = decodeURL(encodedUrl);
            logger.info("Decoded URL: " + decodedUrl);
        } else {
            logger.error("Invalid URL format.");
        }

        scanner.close();
    }
}
