package de.trs.javafx.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.trs.javafx.JavafxApplication;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CsvHandler {

    private static List<List<String>> memberList;

    public static List<List<String>> readCSV(String csvName) {
        log.info("START READING CSV");
        try (BufferedReader csvReader = new BufferedReader(
                new FileReader(JavafxApplication.class.getResource("/csv/" + csvName).toString()))) {
            String line;
            while ((line = csvReader.readLine()) != null) {
                String[] values = line.split(",");
                memberList.add(Arrays.asList(values));
            }
            return memberList;
        } catch (Exception e) {
            log.error("ERROR READING CSV", e);
            return memberList = null;
        }
    }
}
