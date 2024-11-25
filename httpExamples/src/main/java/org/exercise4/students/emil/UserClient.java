import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@Slf4j
public class UserClient {

    private static final String BASE_URL = "https://reqres.in/api/users";
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Method to List Users
    public void listUsers() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "?page=2"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            log.info("List of Users on Page 2: {}", response.body());
        } catch (Exception e) {
            log.error("Error occurred while listing users: {}", e.getMessage(), e);
        }
    }

    // Method to Get Single User by ID
    public void getUserById(int userId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "/" + userId))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                log.info("User found: {}", response.body());
            } else {
                log.warn("User not found with ID: {}", userId);
            }
        } catch (Exception e) {
            log.error("Error occurred while fetching user with ID {}: {}", userId, e.getMessage(), e);
        }
    }

    // Method to Create a New User
    public void createUser(String name, String job) {
        try {
            String newUser = objectMapper.writeValueAsString(new UserRequest(name, job));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(newUser))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            log.info("New User Created: {}", response.body());
        } catch (Exception e) {
            log.error("Error occurred while creating a new user: {}", e.getMessage(), e);
        }
    }

    // Method to Update a User by ID
    public void updateUser(int userId, String name, String job) {
        try {
            String updatedUser = objectMapper.writeValueAsString(new UserRequest(name, job));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "/" + userId))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(updatedUser))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            log.info("User Updated: {}", response.body());
        } catch (Exception e) {
            log.error("Error occurred while updating user with ID {}: {}", userId, e.getMessage(), e);
        }
    }

    // Method to Delete a User by ID
    public void deleteUser(int userId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "/" + userId))
                    .DELETE()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 204) {
                log.info("User deleted successfully with ID: {}", userId);
            } else {
                log.warn("Failed to delete user. User ID {} might not exist.", userId);
            }
        } catch (Exception e) {
            log.error("Error occurred while deleting user with ID {}: {}", userId, e.getMessage(), e);
        }
    }

    // Method for URL Validation and Encoding
    public void validateAndEncodeUrl(String url) {
        try {
            URI uri = new URI(url);
            if (uri.isAbsolute() && (uri.getScheme().equals("http") || uri.getScheme().equals("https"))) {
                String encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString());
                log.info("Valid URL. Encoded URL: {}", encodedUrl);

                // Decode the encoded URL
                String decodedUrl = java.net.URLDecoder.decode(encodedUrl, StandardCharsets.UTF_8.toString());
                log.info("Decoded URL: {}", decodedUrl);
            } else {
                log.warn("Invalid URL format: {}", url);
            }
        } catch (Exception e) {
            log.error("Error validating URL: {}", e.getMessage(), e);
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class UserRequest {
        private String name;
        private String job;
    }
}
