package de.trs.javafx.memberview;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.Printer.MarginType;
import javafx.print.PrinterJob;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.extern.slf4j.Slf4j;

/**
 * PrintSceneController
 */
@Slf4j
@Controller
public class PrintSceneController implements Initializable {

    @FXML
    private BorderPane borderPane;

    @FXML
    private DatePicker eventDatePicker;

    @FXML
    private ComboBox<Event> eventComboBox;

    @FXML
    private ComboBox<Printer> printerComboBox;

    @FXML
    private Button printButton;

    @FXML
    private Spinner<Integer> rowsPerPageSpinner;

    @FXML
    private TableView<Mitglied> reservationTableView;

    @Autowired
    private DbService dbService;

    /**
     * Druckfunktion die beim Bestätigen des Drucke Knopfes ausgeführt wird. Kümmert
     * sich um das aufteilen der Reservierungsliste auf mehrere Seiten und die
     * Unterteilung Mitglieder pro Seite.
     */
    @FXML
    void printReservation() {

        log.info("PRINT START");
        if (printerComboBox.getSelectionModel().getSelectedIndex() == -1) {
            Alert alert = new Alert(AlertType.ERROR, "Kein Printer ausgewählt", ButtonType.CLOSE);
            return;
        }
        Printer printer = printerComboBox.getValue();
        PrinterJob printerJob = PrinterJob.createPrinterJob(printer);
        PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, MarginType.DEFAULT);
        double scaleX = pageLayout.getPrintableWidth();
        double scaleY = pageLayout.getPrintableHeight();

        ObservableList<Mitglied> reservationList = reservationAsObservableList();
        int size = reservationList.size();

        GridPane printGrid = createGridPane(scaleX, scaleY);
        int rowCounter = 0;
        int colCounter = 0;
        int colCount = 2;
        int vRowsCount = (size % colCount != 0) ? size / colCount + 1 : size / colCount;
        int pages = 1;
        int rowsPerPage = rowsPerPageSpinner.getValue();
        if (vRowsCount > rowsPerPage) {
            pages = (vRowsCount % rowsPerPage != 0) ? vRowsCount / rowsPerPage + 1 : vRowsCount / rowsPerPage;
        }

        Event selectedEvent = eventComboBox.getSelectionModel().getSelectedItem();
        ArrayList<GridPane> pagesList = new ArrayList<>();
        pagesList.add(printGrid);
        for (int i = 0; i < size; i++) {
            if (rowCounter > rowsPerPage - 1) {
                printGrid = createGridPane(scaleX, scaleY);
                pagesList.add(printGrid);
                colCounter = 0;
                rowCounter = 0;
            }

            Mitglied mitglied = reservationList.get(i);
            TextFlow card = new TextFlow();
            card.setPadding(new Insets(5.));
            card.getChildren()
                    .add(new Label("Name: " + mitglied.getNName() + " " + mitglied.getVName() + "\nSitzplatz: "
                            + mitglied.getSeat() + "\n\nDatum: " + selectedEvent.getPerformanceDate()
                            + "\nVeranstaltung:\n" + selectedEvent.getName()));
            card.setPrefWidth(scaleX / colCount);
            card.setTextAlignment(TextAlignment.LEFT);

            printGrid.add(card, colCounter, rowCounter);
            colCounter = (i % colCount == 0) ? colCounter + 1 : 0;
            rowCounter = (colCounter % colCount == 0) ? rowCounter + 1 : rowCounter;
        }

        FlowPane pageing = new FlowPane();
        for (GridPane g : pagesList) {
            printerJob.printPage(pageLayout, g);
            pageing.getChildren().add(g);
        }
        printerJob.endJob();

        Group root = new Group();
        Scene printScene = new Scene(root);
        Stage printPage = new Stage(StageStyle.DECORATED);
        printPage.setTitle("Druckvorschau");
        ScrollPane scrollPane = new ScrollPane(pageing);
        scrollPane.setPrefSize(scaleX + 30, scaleY);
        printPage.setScene(printScene);
        root.getChildren().add(scrollPane);
        printPage.setMaxHeight(700);
        printPage.showAndWait();

    }

    /**
     * Erstellt ein neues GridPane für das Drucklayout
     * 
     * @param scaleX Bevorzugte Seitenbreite
     * @param scaleY Bevorzugte Seitenhöhe
     * @return ein neues leeres GridPane
     */
    private GridPane createGridPane(double scaleX, double scaleY) {

        GridPane printGridPane = new GridPane();
        printGridPane.setGridLinesVisible(true);

        printGridPane.setPrefHeight(scaleY);
        printGridPane.setPrefWidth(scaleX);

        return printGridPane;
    }

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

    /**
     * Löscht und befüllt die ReservationTableView
     */
    private void loadReservationList() {
        reservationTableView.getItems().clear();
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

    /**
     * Läd die Liste für die Event ComboBox und wählt den ersten Eintrag aus
     */
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

    /**
     * Ruft die Events für ein bestimmtes Datum ab und wandelt dies in ein Liste um
     * 
     * @return eine ObservableList vom Typ Event
     */
    private ObservableList<Event> getEventsAsObservable() {
        LocalDate eventLocaleDate = eventDatePicker.getValue();
        Date eventDate = Date.from(eventLocaleDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        ObservableList<Event> eventList = FXCollections.observableList(dbService.getEventsByDate(eventDate));
        return eventList;
    }

    /**
     * Initialisierungsmethode für die Klasse PrintSceneController
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rowsPerPageSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 5));

        /** Initiation DatePicker */
        eventDatePicker.setValue(LocalDate.now());
        eventDatePicker.setOnAction(e -> {
            this.loadEvents();
        });

        /** Initiation Event ComboBox */
        eventComboBox.setOnAction(e -> {
            int selectedIndex = eventComboBox.getSelectionModel().getSelectedIndex();
            log.info("SELECTION " + String.valueOf(selectedIndex));
            reservationTableView.getItems().clear();
            this.loadReservationList();
        });

        /** Initiation Printer ComboBox */
        for (Printer p : Printer.getAllPrinters()) {
            printerComboBox.getItems().add(p);
        }
        printerComboBox.setValue(Printer.getDefaultPrinter());
        printerComboBox.setOnAction(e -> {
            int selectedIndex = eventComboBox.getSelectionModel().getSelectedIndex();
            log.info("PRINTER SELECTION " + String.valueOf(selectedIndex));
        });

        this.loadEvents();
        this.generateTableView();
        // this.loadReservationList();
    }

}
