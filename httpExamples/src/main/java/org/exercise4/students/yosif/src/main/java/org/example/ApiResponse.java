package org.example;

import lombok.*;


import java.util.List;

@Getter(AccessLevel.PUBLIC)
@Setter
@ToString
@Data
public class ApiResponse {
    private int per_page;
    private int total;
    private int total_pages;
    private List<User> data;
    private int page;
}

