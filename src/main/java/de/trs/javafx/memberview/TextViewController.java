package de.trs.javafx.memberview;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import de.trs.javafx.dbcontroller.DbService;
import de.trs.javafx.model.CsvHandler;
import de.trs.javafx.model.Event;
import de.trs.javafx.model.Mitglied;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class TextViewController extends AnchorPane implements Initializable {

    // TODO: Bild Sitzplätze
    @FXML
    private AnchorPane textViewPane;

    @FXML
    private DatePicker eventDatePicker;

    @FXML
    private SplitPane splitPane;

    @FXML
    private TableView<Mitglied> memberTableView;

    @FXML
    private TableView<Mitglied> eventTableView;

    @FXML
    private ChoiceBox<String> eventChoiceBox;

    @FXML
    private TextField filterTextField;

    @FXML
    private CheckBox filterCheck;

    @Autowired
    private DbService dbService;

    private ObservableList<Event> currentEvents;

    /**
     * Filtert die Mitgliederliste, anhand der Textfeldeingabe. Dabei wird der
     * Filterstring an den Leerstellen gesplittet und über ein RegEx Muster mit den
     * Mitglied.toString() Daten abgeglichen.
     */
    // TODO: BUG: Filter + "nur neu" Checkbox!
    @FXML
    private void filterMemberTableView() {
        memberTableView.getItems().clear();
        String filterString = filterTextField.getText().toLowerCase().trim();
        ObservableList<Mitglied> members = mitgliedAsObservableList();
        ObservableList<Mitglied> membersFiltered = FXCollections.observableArrayList();

        for (Mitglied mitglied : members) {

            String trim = filterString.trim();
            String[] args = trim.split(" ");
            String regex = ""; // (?=.*Ried.*)(?=.*012.*)(?=.*askld.*).*
            for (String arg : args) {
                regex += "(?=.*" + arg + ".*).*";
            }
            // log.info("DEBUG FILTER REGEX STRING " + regex);
            if (mitglied.toString().toLowerCase().matches(regex)) {
                membersFiltered.add(mitglied);
            }
        }
        memberTableView.getItems().setAll(membersFiltered);
    }

    /**
     * Exportiert die aktuelle Mitglieder- und Reservierungsliste in ein CSV Datei
     */
    // TODO: Exportfunktion weiter ausbauen
    @FXML
    private void exportMembersToCSV() {
        log.info("EXPORTING CSV MEMBERLIST");

        String fileName = LocalDate.now() + "_" + CsvHandler.MEMBER_CSV_NAME;
        log.info("EXPORTING Mitglieder " + fileName);
        List<Mitglied> members = dbService.getMembers();
        CsvHandler.ParseMemberList.saveCSVfromMember(members, fileName);

        if (eventChoiceBox.getSelectionModel().getSelectedIndex() != -1) {

            fileName = eventDatePicker.getValue() + "_" + CsvHandler.RESERVATION_CSV_NAME;
            log.info("EXPORTING Reservation " + fileName);
            ObservableList<Mitglied> reservationAsObservableList = reservationAsObservableList(
                    eventChoiceBox.getSelectionModel().getSelectedIndex());
            CsvHandler.ParseMemberList.saveCSVfromMember(reservationAsObservableList, fileName);
        }
    }

    /**
     * Die Reservierugsliste eines Events wird auf den aktuellen Stand überschrieben
     * Sollte kein Event in der EventChoiceBox ausgewählt worden sein, wird eine
     * Meldung ausgegeben.
     */
    @FXML
    private void updateReservationList() {
        ObservableList<Mitglied> mitglieder = eventTableView.getItems();
        log.info("SELECTED INDEX " + eventChoiceBox.getSelectionModel().getSelectedIndex());
        log.info("SELECTED Memberlist " + mitglieder.size());
        int index = eventChoiceBox.getSelectionModel().getSelectedIndex();
        if (index == -1) {
            log.info("SELECTION no event selected");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Keine Veranstaltung ausgewählt!");
            alert.showAndWait();
            return;
        }
        Event currentEvent = currentEvents.get(index);
        dbService.updateReservationList(currentEvent, mitglieder);
    }

    /**
     * Fügt ein selektiertes Mitglied aus der Mitglieder TableView der Event
     * TableView hinzu. Es wird eine Meldung ausgegeben, wenn:
     * 
     * - Kein Event ausgewählt wurde - Kein Mitglied ausgewählt wurde - Das gewählte
     * Mitglied bereits hinzugefügt wurde
     */
    @FXML
    private void addMemberToResrevationList() {

        Mitglied selctedMitglied = (Mitglied) memberTableView.getSelectionModel().getSelectedItem();
        log.info("ADDING " + selctedMitglied);

        if (eventChoiceBox.getSelectionModel().getSelectedIndex() == -1) {
            log.info("EMPTY Keine Veranstalung ausgewaehlt");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Keine Veranstalung ausgewaehlt!");
            alert.showAndWait();
            return;
        }

        if (selctedMitglied == null) {
            log.info("Kein Mitglied ausgewaehlt");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Kein Mitglied ausgewählt!");
            alert.showAndWait();
            return;
        }

        if (isMemberOnReservationlist(selctedMitglied)) {
            log.info("COLLISION Member already in Reservation List");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Mitglied wurde bereits hinzugefügt!");
            alert.showAndWait();
            return;
        }

        log.info("ADDED Member to Reservation List");
        eventTableView.getItems().add(selctedMitglied);

        if (filterCheck.isSelected()) {
            reloadMemberView();
            memberTableView.getSelectionModel().selectFirst();
        } else {
            memberTableView.getSelectionModel().selectNext();
        }
    }

    /**
     * Entfernt das ausgewählte Mitglied von der Event TableView Gibt eine Meldung
     * aus, sollte kein Mitglied ausgewählt worden sein. Anschließend wird die
     * reservationLise aktuallisiert.
     */
    @FXML
    private void removeMemberFromReservation() {
        Mitglied selctedMitglied = eventTableView.getSelectionModel().getSelectedItem();
        log.info("REMOVING " + selctedMitglied);

        if (selctedMitglied == null) {
            log.info("Kein Mitglied ausgewaehlt");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Kein Mitglied ausgewählt!");
            alert.showAndWait();
            return;
        }
        eventTableView.getItems().remove(selctedMitglied);
        updateReservationList();
        reloadMemberView();
    }

    /**
     * Endgültiges Löschen eines Mitgliedes von der List und aus Datenbank Gibt eine
     * Meldung aus wenn kein Mitglied ausgewählt wurde. Gibt eine Warnmeldung aus
     * und bittet um Bestätigung.
     */
    @FXML
    private void deleteMember() {
        Mitglied selctedMitglied = memberTableView.getSelectionModel().getSelectedItem();
        log.info("DELETING " + selctedMitglied);

        if (selctedMitglied == null) {
            log.info("Kein Mitglied ausgewaehlt");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Kein Mitglied ausgewählt!");
            alert.showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.WARNING,
                "Soll Mitglied " + selctedMitglied.toString() + " wiklich gelöscht werden?", ButtonType.APPLY,
                ButtonType.CANCEL);
        // TODO: Mitglieder Daten schön darstellen
        alert.setHeaderText("Fortfahren?");
        alert.setTitle("Mitglied löschen?");
        Optional<ButtonType> buttonType = alert.showAndWait();

        log.info("DELETE ButtonType: " + buttonType.get().getText());

        if (buttonType.get() == ButtonType.APPLY) {
            dbService.deleteMember(selctedMitglied);
            reloadMemberView();
            reloadEventView();
        } else {
            log.info("DELETE Loeschvorgang abgebrochen");
        }
        log.info("END Mitglied loeschen");
    }

    /**
     * Löscht ein ausgewähltes Event aus der eventTableView
     */
    @FXML
    private void deleteEvent() {

        int selectedIndex = eventChoiceBox.getSelectionModel().getSelectedIndex();

        if (selectedIndex == -1) {
            log.info("EMPTY Keine Veranstalung ausgewaehlt");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Keine Veranstalung ausgewaehlt!");
            alert.showAndWait();
            return;
        }

        Event selectedEvent = currentEvents.get(selectedIndex);
        log.info("DELETING " + selectedEvent);

        Alert alert = new Alert(Alert.AlertType.WARNING,
                "Soll " + selectedEvent.getName() + " wiklich gelöscht werden?", ButtonType.APPLY, ButtonType.CANCEL);
        alert.setHeaderText("Fortfahren?");
        alert.setTitle("Event löschen?");
        Optional<ButtonType> buttonType = alert.showAndWait();

        log.info("DELETE ButtonType: " + buttonType.get().getText());

        if (buttonType.get() == ButtonType.APPLY) {
            dbService.deleteEvent(selectedEvent);
            reloadMemberView();
            reloadEventView();
        } else {
            log.info("DELETE Loeschvorgang abgebrochen");
        }
        log.info("END Event loeschen");

    }

    /**
     * Ruft die Mitgliederliste von der Datenbank ab und konvertiert diese in eine
     * ObservableList.
     * 
     * @return ObservableList vom Typ Mitglied
     */
    private ObservableList<Mitglied> mitgliedAsObservableList() {
        ObservableList<Mitglied> observableList = FXCollections.observableList(dbService.getMembers());
        return observableList;

    }

    /**
     * Ruft die Events für ein bestimmtes Datum von der Datenbank ab und konvertiert
     * diese in eine ObservableList.
     * 
     * @return ObservableList vom Typ Event
     */
    private ObservableList<Event> eventAsObservableList() {
        Date eventDate = Date.valueOf(eventDatePicker.getValue());
        ObservableList<Event> observableEventList = FXCollections.observableList(dbService.getEventsByDate(eventDate));
        return observableEventList;
    }

    /**
     * Ruft die Mitgliederliste zu einem bestimmten Event von der Datenbank ab und
     * konvertiert diese in eine ObservableList.
     * 
     * @param index benötigt den Index des Events in der EventChoiceBox um das
     *              entsprechende Element aus der currentEvents Liste zu laden.
     * @return ObservableList vom Typ Mitglied
     */
    private ObservableList<Mitglied> reservationAsObservableList(int index) {
        log.info("LOADING reservationlist from index " + index);
        List<Mitglied> reservationList;
        if (index != -1) {
            reservationList = dbService.getReservationList(currentEvents.get(index));

        } else {
            reservationList = FXCollections.observableArrayList();
        }
        ObservableList<Mitglied> reservationObservableList = FXCollections.observableArrayList();

        if (reservationList.size() != 0) {
            try {
                log.info("LOADING reservationlist");
                reservationObservableList = FXCollections.observableArrayList(reservationList);
            } catch (Exception e) {
                log.error("ERROR loading reservationList", e);
            }
        }
        return reservationObservableList;
    }

    /**
     * Erneutes Laden der Mitglieder TextView.
     * 
     */
    @FXML
    private void reloadMemberView() {
        ObservableList<Mitglied> mitgliedAsObservableList = mitgliedAsObservableList();
        ObservableList<Mitglied> filteredList = FXCollections.observableArrayList();

        if (filterCheck.isSelected() && !mitgliedAsObservableList.isEmpty()) {
            log.info("DEBUG Filter Check" + filterCheck.isArmed());
            for (Mitglied mitglied : mitgliedAsObservableList) {
                if (!isMemberOnReservationlist(mitglied)) {
                    filteredList.add(mitglied);
                }
            }
            memberTableView.setItems(filteredList);
        } else {
            memberTableView.setItems(mitgliedAsObservableList);
        }

    }

    /**
     * Hilfsfunktion zur Kontrolle anhand der ID, ob sich ein Mitglied bereits auf
     * der Event TextView befindet.
     * 
     * @param mitglied das zu überprüfende Mitglied
     * @return gibt true oder false zurück
     */
    private boolean isMemberOnReservationlist(Mitglied mitglied) {
        for (Mitglied mitglied2 : eventTableView.getItems()) {
            if (mitglied.getId() == mitglied2.getId()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Erneutes Laden der Event TableView und der Event ChoiceBox.
     * 
     * Sollte am gewählten Tag keine Veranstaltung stattfinden wird die ChoiceBox
     * auf -- Keine Events -- gesetzt.
     */
    @FXML
    private void reloadEventView() {
        currentEvents = eventAsObservableList();
        ObservableList<String> eventNameObservableList = FXCollections.observableArrayList();
        eventTableView.getItems().clear();
        eventChoiceBox.getItems().clear();

        if (currentEvents.size() != 0) {
            for (int i = 0; i < currentEvents.size(); i++) {
                eventNameObservableList.add(currentEvents.get(i).getName());
            }
            eventChoiceBox.setItems(eventNameObservableList);
            eventChoiceBox.getSelectionModel().clearAndSelect(0);
        } else {
            eventChoiceBox.setValue("-- Keine Events --");
        }
    }

    /**
     * Initiales erzeugen der TableView Elemente
     * 
     * Modifizieren der RowFacktory, damit Mitglieder, welche bereits auf der Event
     * TableView stehen, grün hinterlegt werden.
     */
    private void generateTableView() {
        /**
         * initialisieren der Mitglieder und Event TabelView
         */
        List<String> columnTitles = List.of("Sitzplatz", "Alt. Sitzplatz", "Nachname", "Vorname", "Strasse", "PLZ",
                "Stadt", "Telefon", "Email");
        Iterator<String> titleIterator = columnTitles.iterator();

        String[] attributeTitles = { "seat", "seatAlt", "nName", "vName", "street", "zipCode", "town", "email",
                "telephone" };
        for (String s : attributeTitles) {

            String title = titleIterator.next();

            TableColumn<Mitglied, String> memberTabel = new TableColumn<>(title);
            memberTabel.setCellValueFactory(new PropertyValueFactory<Mitglied, String>(s));
            memberTableView.getColumns().add(memberTabel);

            TableColumn<Mitglied, String> eventTabel = new TableColumn<>(title);
            eventTabel.setCellValueFactory(new PropertyValueFactory<Mitglied, String>(s));
            eventTableView.getColumns().add(eventTabel);
        }
        memberTableView.setRowFactory(new Callback<TableView<Mitglied>, TableRow<Mitglied>>() {
            @Override
            public TableRow<Mitglied> call(TableView<Mitglied> tableView) {
                return new TableRow<Mitglied>() {
                    @Override
                    protected void updateItem(Mitglied mitglied, boolean empty) {
                        super.updateItem(mitglied, empty);

                        if (empty || mitglied == null) {
                            this.setText(null);
                            this.setGraphic(null);
                        } else if (isMemberOnReservationlist(mitglied)) {
                            this.setStyle("-fx-background-color:lightgreen");
                        } else {
                            this.setStyle("");
                        }
                    }
                };
            }
        });
        this.reloadMemberView();
        this.reloadEventView();
    }

    /**
     * Initialisierung der Startvariablen und ActionListener
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        eventDatePicker.setValue(LocalDate.now());

        eventChoiceBox.setOnAction(e -> {
            int selectedIndex = eventChoiceBox.getSelectionModel().getSelectedIndex();
            log.info("SELECTION " + String.valueOf(selectedIndex));
            eventTableView.getItems().clear();
            eventTableView.getItems().addAll(reservationAsObservableList(selectedIndex));
            reloadMemberView();
        });

        /** propreäter */
        // eventChoiceBox.getSelectionModel().selectedIndexProperty().addListener((e, a,
        // b) -> {
        // log.info("CHANGELISTENER " + b.intValue());
        // });

        this.generateTableView();

    }
}
