package org.exercise4.students.bogdan.CrudUsers.POJOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPOJO {
    private int id;
    private String email;
    private String first_name;
    private String last_name;
    private String avatar;

    public String toString() {
        return "\nName: " + first_name + " " + last_name + "\nemail: " + email + "\nid: " + id;
    }
}
