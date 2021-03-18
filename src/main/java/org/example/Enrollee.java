package org.example;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Enrollee {

    private String userId;
    private String firstName;
    private String lastName;
    private int version;
    private String insuranceCompany;
}
