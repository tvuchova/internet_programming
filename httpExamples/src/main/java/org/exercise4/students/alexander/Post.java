package main.java.org.exercise4.students.alexander;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Post {
    private int page;
    private int per_page;
    private int total;
    private int total_pages;
    private List<UserData> data;
}

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
class UserData {
    private int id;
    private String email;
    private String first_name;
    private String last_name;
    private String avatar;
}

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
class UserResponse {
    private UserData data;
}

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
class UserRequest {
    private String name;
    private String job;
}

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
class UserUpdateJobRequest {
    private String job;
}