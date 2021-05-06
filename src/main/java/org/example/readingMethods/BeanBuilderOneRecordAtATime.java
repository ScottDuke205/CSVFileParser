package org.example.readingMethods;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.example.Enrollee;
import org.example.service.EnrolleeService;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;

public class BeanBuilderOneRecordAtATime {

    /**
     * Converts the CSV records to an object using CsvToBeanBuilder.
     *
     * NOTE: While this is nice, it is 9 times slower than the previous 2 examples.
     * @return
     */
    public void convertFileToTargetObjectOneRecordAtATime(String file) {
        EnrolleeService enrolleeService = new EnrolleeService();

        Instant start = Instant.now();

        try (
                Reader reader = Files.newBufferedReader(Paths.get(file));
        ) {
            CsvToBean<Enrollee> csvToBean = new CsvToBeanBuilder<Enrollee>(reader)
                    .withType(Enrollee.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withIgnoreQuotations(true)
                    .build();

            csvToBean.forEach(x -> enrolleeService.addUpdateEnrolleeList(x));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        System.out.format("Elapsed time: %d milliseconds%n", timeElapsed);

        // This will sort and write new output files on any of the CSV parsing methods above.
        enrolleeService.sortAndWriteNewCSVFile();
    }
}
