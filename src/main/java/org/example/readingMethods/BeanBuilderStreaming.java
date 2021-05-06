package org.example.readingMethods;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.example.Enrollee;
import org.example.service.EnrolleeService;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

public class BeanBuilderStreaming {

    public void processFile(String file) {
        EnrolleeService enrolleeService = new EnrolleeService();

        Instant start = Instant.now();

        List<Enrollee> enrollees = convertCsvFileToTargetObject(file, Enrollee.class);
        enrollees.forEach((x -> enrolleeService.addUpdateEnrolleeList(x)));

        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        System.out.format("Elapsed time: %d milliseconds%n", timeElapsed);

        // This will sort and write new output files on any of the CSV parsing methods above.
        enrolleeService.sortAndWriteNewCSVFile();
    }

    // This is to load all of the records at once

    /**
     * Generic way to convert a CSV file to a target object.
     * @param file
     * @param target
     * @param <T>
     * @return a list of the target object
     */
    private <T> List<T> convertCsvFileToTargetObject(String file, Class<T> target) {
        List<T> listOfObjects = Collections.emptyList();
//        List<T> listOfObjects = new ArrayList<T>();
        if (file == null) {
            //throw new Exception("No file uploaded!");
            return listOfObjects;
        }
        // Resources need to be opened and closed properly
        try (FileInputStream in = new FileInputStream(file);
             InputStreamReader streamReader = new InputStreamReader(in, StandardCharsets.UTF_8);
        )
        {
            CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(streamReader)
                    .withType(target)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            listOfObjects = csvToBean.parse();
        } catch (IOException ex) {
            ex.printStackTrace();
            //throw new Exception("No file uploaded!");
        }
        return listOfObjects;
    }
}
