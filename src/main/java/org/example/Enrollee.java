package org.example;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Enrollee {

    @CsvBindByName(column = "id")
    private String userId;
    @CsvBindByName
    private String firstName;
    @CsvBindByName
    private String lastName;
    @CsvBindByName
    private int version;
    @CsvBindByName(column = "insurance")
    private String insuranceCompany;

    /**
     * Returns a list of Enrollee values. This is used as output a new CSV file.
     * @return
     */
    public List<String> getValues() {
        List<String> values = new ArrayList<>();
        values.add(userId);
        values.add(firstName);
        values.add(lastName);
        values.add(String.valueOf(version));
        values.add(insuranceCompany);
        return values;
    }
}
