package org.example;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class App {
    private static final String SAMPLE_CSV_FILE_PATH = "./sample.csv";

    // The HashMap is to separate enrollee by insurance company.
    private static Map<String, List<Enrollee>> insuranceHashMap = new HashMap();

    public static void main(String[] args) {
        readCSVFile();
        sortAndWriteNewCSVFile();
    }

    private static void readCSVFile() {
        try (Reader reader = new FileReader(SAMPLE_CSV_FILE_PATH);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                     .withHeader("userId", "firstName", "lastName", "version", "insuranceCompany")
                     .withIgnoreHeaderCase()
                     .withTrim());
        ) {
            for (final CSVRecord record : csvParser) {
                Enrollee enrollee = new Enrollee();
                enrollee.setUserId(record.get("userId"));
                enrollee.setFirstName(record.get("firstName"));
                enrollee.setLastName(record.get("lastName"));
                enrollee.setVersion(Integer.parseInt(record.get("version")));
                enrollee.setInsuranceCompany(record.get("insuranceCompany"));

                addUpdateEnrolleeList(enrollee);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void addUpdateEnrolleeList(Enrollee enrollee) {
        boolean isNew = true;

        // If insurance does not exist in hashMap, return to add insurance company/enrollee.
        if (!insuranceHashMap.containsKey(enrollee.getInsuranceCompany())) {
            List<Enrollee> enrolleeList = new ArrayList<>();
            enrolleeList.add(enrollee);
            insuranceHashMap.put(enrollee.getInsuranceCompany(), enrolleeList);
            return;
        }

        // Get the enrollee list from insurance company hashmap for the insurance company.
        List<Enrollee> insEnrolleeList = insuranceHashMap.get(enrollee.getInsuranceCompany());

        for (int i = 0; i < insEnrolleeList.size(); i++) {
            Enrollee existEnrollee = insEnrolleeList.get(i);
            if (existEnrollee.getUserId().equals(enrollee.getUserId())) {
                if (existEnrollee.getVersion() < enrollee.getVersion()) {
                    insEnrolleeList.set(i, enrollee);
                }
                isNew = false;
                break;
            }
        }

        // insurance company exists, but userId not in list.
        if (isNew) {
            insEnrolleeList.add(enrollee);
        }
    }

    private static void sortAndWriteNewCSVFile() {
        insuranceHashMap.forEach((k, v) -> {
            // Get the enrollee list from insurance company hashmap for the insurance company.
            List<Enrollee> insEnrolleeList = insuranceHashMap.get(k);

            // Sort enrollee list by last name, first name ascending order before writing to file.
            Comparator<Enrollee> compareByName = Comparator
                    .comparing(Enrollee::getLastName)
                    .thenComparing(Enrollee::getFirstName);

            List<Enrollee> sortedInsEnrolleeList = insEnrolleeList.stream()
                    .sorted(compareByName)
                    .collect(Collectors.toList());

            writeNewCSVFileForInsuranceCompany(k, sortedInsEnrolleeList);
        });
    }

    private static void writeNewCSVFileForInsuranceCompany(String insuranceCompanyKey, List<Enrollee> insEnrolleeList) {
        String fileName = "./" + insuranceCompanyKey.replaceAll(" ", "") + ".csv";
        try (FileWriter fw = new FileWriter(fileName);
             BufferedWriter writer = new BufferedWriter(fw);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                     .withHeader("userId", "fistName", "lastName", "version", "insuranceCompany"));
        ) {
            for (Enrollee enrollee : insEnrolleeList) {
                csvPrinter.printRecord(enrollee.getUserId(), enrollee.getFirstName(),
                        enrollee.getLastName(), enrollee.getVersion(), enrollee.getInsuranceCompany());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
