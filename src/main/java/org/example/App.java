package org.example;

import org.example.readingMethods.BeanBuilderOneRecordAtATime;
import org.example.readingMethods.BeanBuilderStreaming;
import org.example.readingMethods.ForLoop;
import org.example.readingMethods.Streaming;

public class App {
    private static final String SAMPLE_CSV_FILE_PATH = "./sample.csv";

    public static void main(String[] args) {
        System.out.println("----- Times shown are only on reading the CSV file and add/update to insuranceHashMap -----");
        System.out.println("----- Read file using for-loop -----");
        ForLoop forLoop = new ForLoop();
        forLoop.readCSVFile(SAMPLE_CSV_FILE_PATH);

        System.out.println("----- Read file using Java 8 Streams -----");
        Streaming streaming = new Streaming();
        streaming.readCSVFileJava8(SAMPLE_CSV_FILE_PATH);

        System.out.println("----- Read file using CsvToBeanBuilder one record at a time -----");
        BeanBuilderOneRecordAtATime beanBuilderOneRecordAtATime = new BeanBuilderOneRecordAtATime();
        beanBuilderOneRecordAtATime.convertFileToTargetObjectOneRecordAtATime(SAMPLE_CSV_FILE_PATH);

        System.out.println("----- Read file using CsvToBeanBuilder -----");
        BeanBuilderStreaming beanBuilder = new BeanBuilderStreaming();
        beanBuilder.processFile(SAMPLE_CSV_FILE_PATH);
    }

}
