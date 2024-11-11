import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class UserClient {

    private static final String BASE_URL = "https://reqres.in/api/users";
    private final HttpClient client = HttpClient.newHttpClient();

    // Method to List Users
    public void listUsers() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "?page=2"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Print the raw JSON response directly
            System.out.println("List of Users on Page 2:");
            System.out.println(response.body());
        } catch (Exception e) {
            e.printStackTrace();
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
                System.out.println("User found:");
                System.out.println(response.body());
            } else {
                System.out.println("User not found with ID: " + userId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to Create a New User
    public void createUser(String name, String job) {
        try {
            // Creating the JSON string manually
            String newUser = "{\"name\":\"" + name + "\",\"job\":\"" + job + "\"}";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(newUser))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("New User Created:");
            System.out.println(response.body());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to Update a User by ID
    public void updateUser(int userId, String name, String job) {
        try {
            String updatedUser = "{\"name\":\"" + name + "\",\"job\":\"" + job + "\"}";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "/" + userId))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(updatedUser))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("User Updated:");
            System.out.println(response.body());
        } catch (Exception e) {
            e.printStackTrace();
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
                System.out.println("User deleted successfully with ID: " + userId);
            } else {
                System.out.println("Failed to delete user. User ID " + userId + " might not exist.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method for URL Validation and Encoding
    public void validateAndEncodeUrl(String url) {
        try {
            URI uri = new URI(url);
            if (uri.isAbsolute() && (uri.getScheme().equals("http") || uri.getScheme().equals("https"))) {
                String encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString());
                System.out.println("Valid URL. Encoded URL: " + encodedUrl);

                // Decode the encoded URL
                String decodedUrl = java.net.URLDecoder.decode(encodedUrl, StandardCharsets.UTF_8.toString());
                System.out.println("Decoded URL: " + decodedUrl);
            } else {
                System.out.println("Invalid URL format.");
            }
        } catch (Exception e) {
            System.out.println("Error validating URL: " + e.getMessage());
        }
    }
}
