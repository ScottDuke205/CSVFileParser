package org.example.service;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.example.Enrollee;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnrolleeService {

    // The HashMap is to separate enrollee by insurance company.
    private Map<String, List<Enrollee>> insuranceHashMap = new HashMap();

    public void clearList() {
        insuranceHashMap.clear();
    }

    public void addUpdateEnrolleeList(Enrollee enrollee) {
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

    public void sortAndWriteNewCSVFile() {
        insuranceHashMap.forEach((k, v) -> {
            // Get the enrollee list from insurance company hashmap for the insurance company.
            List<Enrollee> insEnrolleeList = insuranceHashMap.get(k);

            // Sort enrollee list by last name, first name ascending order before writing to file.
            Comparator<Enrollee> compareByName = Comparator
                    .comparing(Enrollee::getLastName)
                    .thenComparing(Enrollee::getFirstName);

            // Sorts original list into a new list.
//            List<Enrollee> sortedInsEnrolleeList = insEnrolleeList.stream()
//                    .sorted(compareByName)
//                    .collect(Collectors.toList());

            // Sorts existing list.
            insEnrolleeList.sort(compareByName);

//            writeNewCSVFileForInsuranceCompany(k, sortedInsEnrolleeList);
            writeNewCSVFileForInsuranceCompany(k, insEnrolleeList);
        });
    }

    private void writeNewCSVFileForInsuranceCompany(String insuranceCompanyKey, List<Enrollee> insEnrolleeList) {
        String fileName = "./" + insuranceCompanyKey.replaceAll(" ", "") + ".csv";
        try (FileWriter fw = new FileWriter(fileName);
             BufferedWriter writer = new BufferedWriter(fw);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                     .withHeader("userId", "fistName", "lastName", "version", "insuranceCompany"));
        ) {

            // Changing this to a lambda will result in a need for an additional try-catch.
            for (Enrollee enrollee : insEnrolleeList) {
                csvPrinter.printRecord(enrollee.getValues());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
