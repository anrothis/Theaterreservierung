package de.trs.javafx.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import de.trs.javafx.JavafxApplication;
import javafx.collections.ObservableList;
import lombok.extern.slf4j.Slf4j;

@Slf4j
/**
 * CSV File Reader Klasse zum einlesen einer Mitglieder- und Eventliste
 * 
 */
public class CsvHandler {

    private static List<List<String>> csvList;
    public static final String MEMBER_CSV_NAME = "MitgliederListe.csv";
    public static final String EVENT_CSV_NAME = "EventListe.csv";
    public static final char DEFAULT_CSV_SEPARATOR = ';';
    public static final char DEFAULT_OBJECT_SEPARATOR = ',';

    /**
     * liest die übergebene CSV Datei und geibt eine ArrayList mit einer String
     * ArrayListe zurück. Die separation der CSV Elemente erfolgt durch einen Regex
     * String "(?!.*\".)," um auszuschließen, dass Kommata im Namen zu einem Split
     * führen.
     * 
     * @param csvName   Name der CSV Datei die sich im /csv/ Ordner befinden muss
     * @param hasTitel  Boolsche Abfrage, ob die erste Spalte die
     *                  Spaltenüberschriften enthält und diese automatisch aus dem
     *                  Datensatz entfernt
     * @param csvType   Variable zur Festlegung, ob eine Mitglieder- oder Eventliste
     *                  eingelesen werden soll
     * @param seperator Der Charakter der von der .csv als Trennzeichen verwendet
     *                  wird
     * @return Als Rückgabewert wird eine ArrayList<List<String>> mit den Daten der
     *         Mitglieder zurückgegeben. Im Fehlerfall wird null zurückgegeben
     */
    public static List<List<String>> readCSV(String csvName, boolean hasTitel, char seperator) {
        csvList = new ArrayList<List<String>>();
        log.info("START READING CSV");
        log.info("READING CSV PATH " + JavafxApplication.class.getResource("/csv/" + csvName).getFile());
        try (BufferedReader csvReader = new BufferedReader(
                new FileReader(JavafxApplication.class.getResource("/csv/" + csvName).getFile()))) {
            log.info("LOADING CSV FILEREADER");
            String line;
            log.info("READING CSV FILE");
            while ((line = csvReader.readLine()) != null) {
                String[] values = line.split("(?!.*\".)" + seperator);
                csvList.add(Arrays.asList(values));
            }
            log.info("END READING CSV FILE");

        } catch (Exception e) {
            log.error("ERROR READING CSV", e);
            return csvList;
        }
        // TODO: automaitsche args Felderkennung
        if (hasTitel) {
            csvList.remove(0);
        }
        return csvList;
    }

    /**
     * Erstellt ein CSV Datei aus den übergebenen Daten
     * 
     * @param csvName   Name den die gewünschte Datei haben soll
     * @param arrayList Liste of StringLists welche die gewünschten Daten enthält
     */
    public static void saveCSV(String csvName, ArrayList<List<String>> arrayList) {

        String file = JavafxApplication.class.getResource("/csv/").getPath() + csvName;
        log.info("CSV FILE WRITE TO " + file);
        try (BufferedWriter csvWriter = new BufferedWriter(new FileWriter(file))) {
            log.info("LOADING CSV FILEWRITER");

            log.info("WRITING Member Array");
            for (List<String> list : arrayList) {
                String line = "";
                for (String element : list) {
                    line += element + CsvHandler.DEFAULT_CSV_SEPARATOR;
                }
                log.info("WRITING Member Array Line " + line);
                csvWriter.write(line);
                csvWriter.newLine();
                csvWriter.flush();
            }
            log.info("END WRITING CSV FILE");
        } catch (Exception e) {
            log.error("ERROR WRITING CSV FILE", e);
        }
    }

    /**
     * Subklasse zur Konvertierung der Mitgliederdaten
     */
    public static class ParseMemberList {

        public static ArrayList<Mitglied> getMemberfromCSV(String csvName, boolean hasTitel, char seperator) {
            List<List<String>> memberListCSV;
            memberListCSV = CsvHandler.readCSV(csvName, hasTitel, seperator);

            if (memberListCSV == null) {
                memberListCSV = new ArrayList<List<String>>();
                List<String> memberCSV = new ArrayList<String>();
                memberCSV.add("Seb(Placeholder)");
                memberCSV.add("Ried");
                memberCSV.add("3d");
                memberListCSV.add(memberCSV);
            }

            for (int i = 0; i < memberListCSV.size(); i++) {
                ArrayList<String> l = new ArrayList<String>(memberListCSV.get(i));
                while (l.size() < 9) {
                    l.add("n.a.");
                }
                memberListCSV.set(i, l);
            }

            ArrayList<Mitglied> memberList = new ArrayList<Mitglied>();
            for (List<String> list : memberListCSV) {
                memberList.add(new Mitglied(list.get(0), list.get(1), list.get(2), list.get(3), list.get(4),
                        list.get(5), list.get(6), list.get(7), list.get(8)));
            }
            return memberList;
        }

        /** Aufruf mit default CSV Name */
        public static ArrayList<Mitglied> getMemberfromCSV(boolean hasTitel, char seperator) {
            return getMemberfromCSV(CsvHandler.MEMBER_CSV_NAME, hasTitel, seperator);
        }

        /**
         * wandelt ein übergebenes {@Mitglied} Object Liste in eine HashMap<Long,
         * HashMap<String, String>> und übergibt diese an den CSVHandler.csvSave().
         * 
         * @param mitglieder Optional List von Mitgliedern
         * @param string     Name der CSV Datei
         */
        public static void saveCSVfromMember(ObservableList<Mitglied> mitglieder, String csvName) {

            ArrayList<List<String>> listOfStringLists = new ArrayList<>();

            if (mitglieder != null && !mitglieder.isEmpty()) {

                HashMap<Long, HashMap<String, String>> mitgliederHashMap = new HashMap<>();
                HashMap<String, String> memberHash = new HashMap<>();
                for (Mitglied mitglied : mitglieder) {
                    /** Hash approach */
                    memberHash = memberToHashMap(mitglied, CsvHandler.DEFAULT_OBJECT_SEPARATOR);
                    mitgliederHashMap.put(mitglied.getId(), memberHash);
                }

                SortedSet<String> keysSorted = new TreeSet<>(memberHash.keySet());
                ArrayList<String> stringListOfMitglieder = new ArrayList<>();

                listOfStringLists.add(keysSorted.stream().collect(Collectors.toList()));

                for (Long mitgliedId : mitgliederHashMap.keySet()) {
                    for (String key : keysSorted) {

                        stringListOfMitglieder.add(mitgliederHashMap.get(mitgliedId).get(key));
                    }
                    log.info("CSV TO MEMBER : " + stringListOfMitglieder.toString());
                    listOfStringLists.add(stringListOfMitglieder);
                }
                CsvHandler.saveCSV(csvName, listOfStringLists);
            } else {
                // TODO: CSV Name frei wählbar
                CsvHandler.saveCSV(csvName, new ArrayList<List<String>>());
            }
        }

        /**
         * Wandelt ein Mitglied Objekt in eine HashMap<String, String> um. Achtung,
         * reihenfolge wird nicht gespeichert.
         * 
         * @param mitglied  Objekt der Klasse {@Mitglied}.
         * @param seperator Trennzeichen der {@Mitglied.toString} Methode
         * @return gibt eine HashMap mit den K,V Paare der Parameter von
         *         {@Mitglied} zurück.
         */
        public static HashMap<String, String> memberToHashMap(Mitglied mitglied, char seperator) {
            String mitgliedString = mitglied.toString();
            log.info("REGEX TEST");
            String[] argsAsArray = mitgliedString.split("^Mitglied \\[|=|" + seperator + "|\\]");
            HashMap<String, String> map = new HashMap<>();
            for (int i = 1; i < argsAsArray.length; i = i + 2) {
                log.info("REGEX " + argsAsArray[i].trim() + " : " + argsAsArray[i + 1]);
                map.put(argsAsArray[i].trim(), argsAsArray[i + 1].trim());
            }
            log.info("REGEX " + map.toString());
            return map;
        }
    }

    /**
     * Subklasse zur Konvertierung der Eventdaten
     */
    public static class ParseEventList {

        public static ArrayList<Event> getEventfromCSV(String csvName, boolean hasTitel, char seperator) {
            List<List<String>> eventListCSV;
            eventListCSV = CsvHandler.readCSV(csvName, hasTitel, seperator);
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

        public static ArrayList<Event> getEventfromCSV(boolean hasTitel, char seperator) {
            return getEventfromCSV(CsvHandler.EVENT_CSV_NAME, hasTitel, seperator);
        }
    }

    /**
     * Subklasse zur Konverierung der Sitzplatzdaten
     */
    public static class ParseSeating {

        public static ArrayList<Seating> getSeatingfromCSV(String csvName, boolean hasTitel, char seperator) {
            List<List<String>> seatingListCSV;
            seatingListCSV = CsvHandler.readCSV(csvName, hasTitel, seperator);
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
