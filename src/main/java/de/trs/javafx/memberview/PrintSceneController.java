package de.trs.javafx.memberview;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import de.trs.javafx.dbcontroller.DbService;
import de.trs.javafx.model.Event;
import de.trs.javafx.model.Mitglied;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * PrintSceneController
 */
@Slf4j
@Controller
public class PrintSceneController implements Initializable {

    @FXML
    private DatePicker eventDatePicker;

    @FXML
    private ComboBox<Event> eventComboBox;

    @FXML
    private Button printButton;

    @FXML
    private Spinner<Integer> memberPerPageSpinner;

    @FXML
    private TableView<Mitglied> reservationTableView;

    @FXML
    void printReservation() {
        log.info("PRINT START");
        PrinterJob printerJob = PrinterJob.createPrinterJob();
        printerJob.showPageSetupDialog(null);
        printerJob.showPrintDialog(null);

    }

    @Autowired
    private DbService dbService;

    /**
     * Initiales erzeugen der TableView Elemente
     * 
     * Modifizieren der RowFacktory, damit Mitglieder, welche bereits auf der Event
     * TableView stehen, grün hinterlegt werden.
     */
    private void generateTableView() {
        /**
         * initialisieren der Mitglieder TabelView
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
            reservationTableView.getColumns().add(memberTabel);

        }
        this.loadReservationList();

    }

    private void loadReservationList() {
        reservationTableView.getItems().addAll(reservationAsObservableList());
    }

    /**
     * Ruft die Mitgliederliste zu einem bestimmten Event von der Datenbank ab und
     * konvertiert diese in eine ObservableList.
     * 
     * @param index benötigt den Index des Events in der EventChoiceBox um das
     *              entsprechende Element aus der currentEvents Liste zu laden.
     * @return ObservableList vom Typ Mitglied
     */
    private ObservableList<Mitglied> reservationAsObservableList() {
        int selectedIndex = eventComboBox.getSelectionModel().getSelectedIndex();
        log.info("LOADING reservationlist from index " + selectedIndex);
        List<Mitglied> reservationList;
        if (selectedIndex != -1) {
            reservationList = dbService.getReservationList(eventComboBox.getSelectionModel().getSelectedItem());
        } else {
            return FXCollections.observableArrayList();
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

    private void loadEvents() {
        eventComboBox.getItems().clear();

        ObservableList<Event> eventsAsObservable = getEventsAsObservable();
        if (eventsAsObservable.isEmpty()) {
            log.info("PRINTVIEW LIST IS EMPTY");
            return;
        }
        eventComboBox.getItems().addAll(eventsAsObservable);
        eventComboBox.getSelectionModel().selectFirst();
    }

    private ObservableList<Event> getEventsAsObservable() {
        LocalDate eventLocaleDate = eventDatePicker.getValue();
        Date eventDate = Date.from(eventLocaleDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        ObservableList<Event> eventList = FXCollections.observableList(dbService.getEventsByDate(eventDate));
        return eventList;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        memberPerPageSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 8));
        eventDatePicker.setValue(LocalDate.now());
        eventDatePicker.setOnAction(e -> {
            this.loadEvents();
        });

        eventComboBox.setOnAction(e -> {
            int selectedIndex = eventComboBox.getSelectionModel().getSelectedIndex();
            log.info("SELECTION " + String.valueOf(selectedIndex));
            reservationTableView.getItems().clear();
            this.loadReservationList();
        });

        // eventComboBox.setCellFactory(new Callback<ListView<Event>, ListCell<Event>>()
        // {
        // @Override
        // public ListCell<Event> call(ListView<Event> listView) {
        // return new ListCell<Event>() {
        // @Override
        // protected void updateItem(Event event, boolean empty) {
        // super.updateItem(event, empty);

        // if (empty || event == null) {
        // this.setText(null);
        // this.setGraphic(null);
        // // } else if (isMemberOnReservationlist(event)) {
        // // this.setStyle("-fx-background-color:lightgreen");
        // } else {
        // ;
        // }
        // }
        // };
        // }
        // });

        this.loadEvents();
        this.generateTableView();
        this.loadReservationList();
    }

}
