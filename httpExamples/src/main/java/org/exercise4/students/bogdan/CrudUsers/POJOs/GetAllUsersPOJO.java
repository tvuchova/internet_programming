package org.exercise4.students.bogdan.CrudUsers.POJOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllUsersPOJO {
    private int page;
    private int per_page;
    private int total;
    private int total_pages;
    private List<UserPOJO> data;
    private SupportPOJO support;
}
