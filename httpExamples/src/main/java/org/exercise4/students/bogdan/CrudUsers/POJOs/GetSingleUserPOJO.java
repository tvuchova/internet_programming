package org.exercise4.students.bogdan.CrudUsers.POJOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetSingleUserPOJO {
    private UserPOJO data;
    private SupportPOJO support;
}
