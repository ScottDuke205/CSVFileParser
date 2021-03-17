package org.example;

import lombok.Getter;
import lombok.Setter;

public class Enrollee {

    @Getter
    @Setter
    private String userId;

    @Getter
    @Setter
    private String firstName;

    @Getter
    @Setter
    private String lastName;

    @Getter
    @Setter
    private int version;

    @Getter
    @Setter
    private String insuranceCompany;
}
