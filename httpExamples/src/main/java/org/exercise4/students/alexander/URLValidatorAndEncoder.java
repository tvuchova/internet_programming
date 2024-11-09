package main.java.org.exercise4.students.alexander;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class URLValidatorAndEncoder {
    public static void main(String[] args) throws MalformedURLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter URL: ");
        String url = scanner.nextLine();
        if(URLIsValid(url)) {
            System.out.println("URL is valid");
            String encodedURL = URLEncode(url);
            System.out.println("Encoded URL: " + encodedURL);

            String decodedURL = URLDecoder.decode(encodedURL, StandardCharsets.UTF_8);
            System.out.println("Decoded URL: " + decodedURL);
        } else {
            System.out.println("URL is invalid");
        }
    }

    public static boolean URLIsValid(String URLString) throws MalformedURLException {
        URL url = new URL(URLString);
        return url.getProtocol().equals("http") || url.getProtocol().equals("https");
    }

    public static String URLEncode(String URLString) {
        String[] parts = URLString.split("\\?");
        StringBuilder newURL = new StringBuilder(parts[0]);

        if(parts.length == 1) {
            return newURL.toString();
        }

        newURL.append("?");
        String query = parts[1];
        String[] queryParts = query.split("&");
        for(String queryPart: queryParts){
            String[] param = queryPart.split("=", 2);
            String encodedKey = URLEncoder.encode(param[0], StandardCharsets.UTF_8);
            if(param.length == 1) {
                newURL.append(encodedKey);
            } else {
                String encodedValue = URLEncoder.encode(param[1], StandardCharsets.UTF_8);
                newURL.append(encodedKey).append("=").append(encodedValue);
            }
            if(!queryPart.equals(queryParts[queryParts.length - 1])) {
                newURL.append("&");
            }
        }

        return newURL.toString();
    }
}
