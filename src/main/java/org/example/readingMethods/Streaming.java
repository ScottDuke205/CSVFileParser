package org.example.readingMethods;

import org.example.Enrollee;
import org.example.service.EnrolleeService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Streaming
 *
 * Read all the lines and map to object
 */
public class Streaming {
    private static final String DELIMITER = ",";
    EnrolleeService enrolleeService = new EnrolleeService();

    public void readCSVFileJava8(String file) {
        Instant start = Instant.now();
        try {
            Files.readAllLines(Paths.get(file))
                    .stream()
                    .skip(1)
                    .map(mapToEnrollee)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        System.out.format("Elapsed time: %d milliseconds%n", timeElapsed);

        // This will sort and write new output files on any of the CSV parsing methods above.
        enrolleeService.sortAndWriteNewCSVFile();
    }

    private Function<String, String> mapToEnrollee = (line) -> {

        String[] field = line.replaceAll("\"", "")
                .replaceAll(" ", "")
                .split(DELIMITER);

        Enrollee enrollee = new Enrollee();
        enrollee.setUserId(field[0]);
        enrollee.setFirstName(field[1]);
        enrollee.setLastName(field[2]);
        enrollee.setVersion(Integer.parseInt(field[3]));
        enrollee.setInsuranceCompany(field[4]);

        enrolleeService.addUpdateEnrolleeList(enrollee);

        return line;
    };
}
