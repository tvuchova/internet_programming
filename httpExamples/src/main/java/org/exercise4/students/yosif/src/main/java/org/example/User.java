package org.example;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class User {
    private int id;
    private String email;
    private String first_name;
    private String last_name;
    private String avatar;
}
