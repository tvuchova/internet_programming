package org.exercise4.students.valeri;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Slf4j
public class Task1HTTPClients {
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void printSingleUser(User user) {
        System.out.println("ID: " + user.getId());
        if(user.getName() != null) {
            System.out.println("Name: " + user.getName());
        } else {
            System.out.println("Name: " + user.getFirstName() + " " + user.getLastName());
        }
        if(user.getEmail() != null) {
            System.out.println("Email: " + user.getEmail());
        }
        if(user.getJob() != null) {
            System.out.println("Job: " + user.getJob());
        }
        if(user.getAvatar() != null) {
            System.out.println("Avatar: " + user.getAvatar());
        }
        System.out.println("----------------------");
    }

    public static void handleUserNotFound() {
        log.warn("User not found");
        System.out.println("User not found");
    }

    public static void ListUsers(String URL) {
        try {
            URI uri = URI.create(URL);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            log.info("Response status code: " + response.statusCode());

            if (response.statusCode() == 404) {
                handleUserNotFound();
            } else {
                UsersResponse usersResponse = objectMapper.readValue(response.body(), UsersResponse.class);

                for (User user : usersResponse.getData()) {
                    printSingleUser(user);
                }
            }

        } catch (Exception e) {
            log.error("Error fetching or processing the users", e);
        }
    }

    public static void GetSingleUser(String URL) {
        try {
            URI uri = URI.create(URL);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            log.info("Response status code: " + response.statusCode());

            if (response.statusCode() == 404) {
                handleUserNotFound();
            } else {
                SingleUserResponse singleUserResponse = objectMapper.readValue(response.body(), SingleUserResponse.class);
                User user = singleUserResponse.getData();
                printSingleUser(user);
            }

        } catch (Exception e) {
            log.error("Error fetching or processing the user", e);
        }
    }

    public static void CreateUser(String URL, String name, String job) {
        try {
            URI uri = URI.create(URL);
            String jsonPayload = String.format("{\"name\": \"%s\", \"job\": \"%s\"}", name, job);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Content-Type", "application/json")
                    .method("PATCH", HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            log.info("Response status code: " + response.statusCode());

            if (response.statusCode() == 201) {
                User createdUser = objectMapper.readValue(response.body(), User.class);
                printSingleUser(createdUser);
            } else {
                log.error("Failed to create user. Status code: " + response.statusCode());
            }

        } catch (Exception e) {
            log.error("Error creating the user", e);
        }
    }

    public static void UpdateUser(String URL, int userId, String name, String job) {
        try {
            URI uri = URI.create(URL + "/" + userId);
            String jsonPayload = String.format("{\"name\": \"%s\", \"job\": \"%s\"}", name, job);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            log.info("Response status code: " + response.statusCode());

            if (response.statusCode() == 404) {
                handleUserNotFound();
            } else {System.out.println(response.body());
                if (response.statusCode() == 200) {
                    User updatedUser = objectMapper.readValue(response.body(), User.class);
                    printSingleUser(updatedUser);
                } else {
                    log.error("Failed to update user. Status code: " + response.statusCode());
                }
            }

        } catch (Exception e) {
            log.error("Error updating the user", e);
        }
    }

    public static void UpdateUserJob(String URL, int userId, String job) {
        try {
            URI uri = URI.create(URL + "/" + userId);
            String jsonPayload = String.format("{\"job\": \"%s\"}", job);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Content-Type", "application/json")
                    .method("PATCH", HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            log.info("Response status code: " + response.statusCode());

            if (response.statusCode() == 404) {
                handleUserNotFound();
            } else {System.out.println(response.body());
                if (response.statusCode() == 200) {
                    User updatedUser = objectMapper.readValue(response.body(), User.class);
                    printSingleUser(updatedUser);
                } else {
                    log.error("Failed to update user job. Status code: " + response.statusCode());
                }
            }

        } catch (Exception e) {
            log.error("Error updating the user job", e);
        }
    }

    public static void DeleteUser(String URL, int userId) {
        try {
            URI uri = URI.create(URL + "/" + userId);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .DELETE()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            log.info("Response status code: " + response.statusCode());

            if (response.statusCode() == 204) {
                System.out.println("User successfully deleted.");
            } else if (response.statusCode() == 404) {
                handleUserNotFound();
            } else {
                log.error("Failed to delete user. Status code: " + response.statusCode());
            }

        } catch (Exception e) {
            log.error("Error deleting the user", e);
        }
    }

    public static void main(String[] args) {
//        ListUsers("https://reqres.in/api/users?page=2");
//        GetSingleUser("https://reqres.in/api/users/70");
//        GetSingleUser("https://reqres.in/api/users/7");
//        CreateUser("https://reqres.in/api/users", "Mario Kovachev", "Software Developer");
//        UpdateUser("https://reqres.in/api/users", 2, "Mario Kovachev", "Software Developer");
        UpdateUserJob("https://reqres.in/api/users", 2, "Software Engineer");
//        DeleteUser("https://reqres.in/api/users", 2);
    }
}


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
class UsersResponse {
    private int page;
    private int perPage;
    private int total;
    private int totalPages;
    private List<User> data;
}

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
class SingleUserResponse {
    private User data;
}

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
class User {
    @JsonProperty("id")
    private int id;
    @JsonProperty("email")
    private String email;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("avatar")
    private String avatar;
    @JsonProperty("name")
    private String name;
    @JsonProperty("job")
    private String job;
    @JsonProperty("createdAt")
    private String createdAt;
}