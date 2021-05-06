package org.example.readingMethods;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.example.Enrollee;
import org.example.service.EnrolleeService;

import java.io.FileReader;
import java.io.Reader;
import java.time.Duration;
import java.time.Instant;

/**
 * ForLoop
 *
 * Uses the CSVParser() to read each record and a for-loop to parse each record. When a record
 * is parsed, it is added to the EnrolleeList for further processing.
 */
public class ForLoop {

    public void readCSVFile(String file) {
        EnrolleeService enrolleeService = new EnrolleeService();

        Instant start = Instant.now();

        try (Reader reader = new FileReader(file);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                     .withHeader("userId", "firstName", "lastName", "version", "insuranceCompany")
                     .withIgnoreHeaderCase()
                     .withIgnoreSurroundingSpaces(true)
                     .withSkipHeaderRecord(true)
                     .withTrim());
        ) {
            for (final CSVRecord record : csvParser) {
                Enrollee enrollee = new Enrollee();
                enrollee.setUserId(replaceAndTrim(record.get("userId")));
                enrollee.setFirstName(replaceAndTrim(record.get("firstName")));
                enrollee.setLastName(replaceAndTrim(record.get("lastName")));
                enrollee.setVersion(Integer.parseInt(record.get("version")));
                enrollee.setInsuranceCompany(replaceAndTrim(record.get("insuranceCompany")));

                enrolleeService.addUpdateEnrolleeList(enrollee);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        System.out.format("Elapsed time: %d milliseconds%n", timeElapsed);

        // This will sort and write new output files on any of the CSV parsing methods above.
        enrolleeService.sortAndWriteNewCSVFile();
    }

    private static String replaceAndTrim(String field) {
        return field.replaceAll("\"", "").trim();
    }

}
