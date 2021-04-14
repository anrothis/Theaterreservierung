package de.trs.javafx.model;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * DataHandler übernimmt das lesen und schrieben der Daten
 */
public enum DataHandler {
    INSTANCE;

    private List<Mitglied> mitglied = null;

    private List<Mitglied> getMitglied() {
        if (mitglied == null) {
            mitglied = new ArrayList<Mitglied>();
            mitglied.add(new Mitglied("Seb", "Ried", "3d"));
            mitglied.add(new Mitglied("Jul", "Zwei", "6g"));

        }
        return mitglied;
    }

    public void createMitglied(Mitglied mitglied) {
        getMitglied().add(mitglied);
    }

    public void deleteMitglied(Mitglied mitglied) {
        getMitglied().remove(mitglied);
    }

    /**
     * ObservableList wird von JavaFX TableView erwartet
     * 
     * TODO: migrate to MainFrameController
     */
    public ObservableList<Mitglied> mitgliedAsObservableList() {
        ObservableList<Mitglied> observableList = FXCollections.observableList(getMitglied());
        return observableList;

    }
}