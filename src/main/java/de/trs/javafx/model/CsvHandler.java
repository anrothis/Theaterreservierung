package de.trs.javafx.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.trs.javafx.JavafxApplication;
import lombok.extern.slf4j.Slf4j;

@Slf4j
/**
 * CSV File Reader Klasse zum einlesen einer Mitglieder- und Eventliste
 * 
 */
public class CsvHandler {

    private static List<List<String>> csvList;

    /**
     * liest die übergebene CSV Datei und geibt eine ArrayList mit einer String
     * ArrayListe zurück. Die separation der CSV Elemente erfolgt durch einen Regex
     * String "(?!.*\".)," um auszuschließen, dass Kommata im Namen zu einem Split
     * führen.
     * 
     * @param csvName  Name der CSV Datei die sich im /csv/ Ordner befinden muss
     * @param hasTitel Boolsche Abfrage, ob die erste Spalte die
     *                 Spaltenüberschriften enthält und diese automatisch aus dem
     *                 Datensatz entfernt
     * @param csvType  Variable zur Festlegung, ob eine Mitglieder- oder Eventliste
     *                 eingelesen werden soll
     * 
     * @return Als Rückgabewert wird eine ArrayList<List<String>> mit den Daten der
     *         Mitglieder zurückgegeben. Im Fehlerfall wird null zurückgegeben
     */
    public static List<List<String>> readCSV(String csvName, boolean hasTitel) {
        csvList = new ArrayList<List<String>>();
        log.info("START READING CSV");
        log.info("READING CSV PATH " + JavafxApplication.class.getResource("/csv/" + csvName).getFile());
        try (BufferedReader csvReader = new BufferedReader(
                new FileReader(JavafxApplication.class.getResource("/csv/" + csvName).getFile()))) {
            log.info("LOADING CSV FILEREADER");
            String line;
            log.info("READING CSV FILE");
            while ((line = csvReader.readLine()) != null) {
                String[] values = line.split("(?!.*\".),");
                csvList.add(Arrays.asList(values));
            }
            log.info("END READING CSV FILE");
            if (hasTitel) {
                csvList.remove(0);
            }
            return csvList;
        } catch (Exception e) {
            log.error("ERROR READING CSV", e);
            return csvList = null;
        }
    }

    public static class ParseMemberList {

        public static ArrayList<Mitglied> getMemberfromCSV(String csvName, boolean hasTitel) {
            List<List<String>> memberListCSV;
            memberListCSV = CsvHandler.readCSV(csvName, hasTitel);
            if (memberListCSV == null) {
                memberListCSV = new ArrayList<List<String>>();
                List<String> memberCSV = new ArrayList<String>();
                memberCSV.add("Seb(Placeholder)");
                memberCSV.add("Ried");
                memberCSV.add("3d");
                memberListCSV.add(memberCSV);
            }
            ArrayList<Mitglied> memberList = new ArrayList<Mitglied>();
            for (List<String> list : memberListCSV) {
                memberList.add(new Mitglied(list.get(0), list.get(1), list.get(2), list.get(3), list.get(4),
                        list.get(5), list.get(6), list.get(7)));
            }
            return memberList;
        }
    }

    public static class ParseEventList {

        public static ArrayList<Event> getEventfromCSV(String csvName, boolean hasTitel) {
            List<List<String>> eventListCSV;
            eventListCSV = CsvHandler.readCSV(csvName, hasTitel);
            if (eventListCSV == null) {
                eventListCSV = new ArrayList<List<String>>();
                List<String> eventCSV = new ArrayList<String>();
                eventCSV.add("Shakespear(Placeholder)");
                eventCSV.add("2021-08-30");
                eventCSV.add("Immenstadt");
                eventListCSV.add(eventCSV);
            }
            ArrayList<Event> eventList = new ArrayList<Event>();
            for (List<String> list : eventListCSV) {
                eventList.add(new Event(list.get(0), new Date(Long.parseLong(list.get(1))), list.get(2)));
            }
            return eventList;
        }
    }

    public static class ParseSeating {

        public static ArrayList<Seating> getSeatingfromCSV(String csvName, boolean hasTitel) {
            List<List<String>> seatingListCSV;
            seatingListCSV = CsvHandler.readCSV(csvName, hasTitel);
            if (seatingListCSV == null) {
                seatingListCSV = new ArrayList<List<String>>();
                List<String> seatingCSV = new ArrayList<String>();
                seatingCSV.add("01-001(Placeholder)");
                seatingCSV.add("01-002");
                seatingCSV.add("02-001");
                seatingListCSV.add(seatingCSV);
            }
            ArrayList<Seating> seatingList = new ArrayList<Seating>();
            for (List<String> list : seatingListCSV) {
                seatingList.add(new Seating(list.get(0)));
            }
            return seatingList;
        }
    }
}
