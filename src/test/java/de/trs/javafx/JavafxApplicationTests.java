package de.trs.javafx;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import de.trs.javafx.model.CsvHandler;
import de.trs.javafx.model.Mitglied;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
class JavafxApplicationTests {
    static Mitglied mitglied;
    static HashMap<String, String> hashMap;

    @BeforeAll
    static void init() {
        mitglied = new Mitglied(1L, "von Ried", "Seb", "Zepp 5", "73777", "DC", "0132145688", "askldjf@dkfjg.de", "321",
                "012");
        hashMap = new HashMap<String, String>();
        hashMap.put("email", "askldjf@dkfjg.de");
        hashMap.put("id", "1");
        hashMap.put("nName", "von Ried");
        hashMap.put("seat", "321");
        hashMap.put("seatAlt", "012");
        hashMap.put("street", "Zepp 5");
        hashMap.put("telephone", "0132145688");
        hashMap.put("town", "DC");
        hashMap.put("vName", "Seb");
        hashMap.put("zipCode", "73777");
    }

    @Test
    void MitgliedToStringTest() {

        HashMap<String, String> memberMap = new HashMap<>();
        memberMap = CsvHandler.ParseMemberList.memberToHashMap(mitglied, ',');

        assertTrue(memberMap.keySet().size() == hashMap.keySet().size(), "Keys Anzahl stimmen nicht überein");
        for (String arg : hashMap.keySet()) {
            // log.info("REGEX " + arg);
            assertTrue(memberMap.get(arg).equals(hashMap.get(arg)), "K,V Paar Stimmen nicht überein");
        }
    }

    @Test
    void MitgliederToCSVTest() {

        ObservableList<Mitglied> mitgliederList = FXCollections.observableArrayList(mitglied);
        ArrayList<List<String>> memberArrayList = new ArrayList<>();

        for (Mitglied mitglied : mitgliederList) {
            HashMap<String, String> memberHashMap = CsvHandler.ParseMemberList.memberToHashMap(mitglied,
                    CsvHandler.DEFAULT_OBJECT_SEPARATOR);
            SortedSet<String> keysSorted = new TreeSet<>(memberHashMap.keySet());
            ArrayList<String> memberString = new ArrayList<>();
            for (String keyString : keysSorted) {
                memberString.add(memberHashMap.get(keyString));
            }
            memberArrayList.add(memberString);
            memberArrayList.add(memberString);
        }
        log.info("CSV TEST " + memberArrayList);

        String csvName = "TestCSVSave1" + ".csv";
        CsvHandler.saveCSV(csvName, memberArrayList);
        assertTrue(JavafxApplication.class.getResource("/csv/" + csvName).getFile() != null,
                "MEMBER CSV FILE " + csvName + " nicht angelegt");

        csvName = "TestCSVSave2" + ".csv";
        CsvHandler.ParseMemberList.saveCSVfromMember(mitgliederList, csvName);
        assertTrue(JavafxApplication.class.getResource("/csv/" + csvName).getFile() != null,
                "MEMBER CSV FILE " + csvName + " nicht angelegt");
    }
}
