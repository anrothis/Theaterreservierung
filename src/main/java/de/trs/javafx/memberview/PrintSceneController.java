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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.Printer.MarginType;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
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
    private AnchorPane textPreview;

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
    private TextFlow textFlowPane;

    @FXML
    void printReservation(ActionEvent event) {

        log.info("PRINT START");
        Printer printer = Printer.getDefaultPrinter();

        for (Printer p : Printer.getAllPrinters()) {
            log.info("PRINT PRINTER available " + p.getName());
            if (p.getName().contains("Microsoft Print to PDF")) {
                printer = p;
            }
        }
        PrinterJob printerJob = PrinterJob.createPrinterJob(printer);

        PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, MarginType.DEFAULT);

        double scaleX = pageLayout.getPrintableWidth();
        double scaleY = pageLayout.getPrintableHeight();

        ObservableList<Mitglied> reservationList = reservationAsObservableList();
        int size = reservationList.size();

        // textFlowPane.getChildren().clear();
        // textFlowPane.getChildren().add(new Label(mitglied.getNName() + "\n" +
        // mitglied.getSeat()));

        VBox rows = new VBox();
        rows.setAlignment(Pos.CENTER);
        // rows.setPadding(new Insets(20));

        rows.setSpacing(10);
        rows.setMinWidth(scaleX);
        rows.setPrefWidth(scaleX);
        rows.setFillWidth(true);

        HBox col = createHBox(scaleY, rows);
        GridPane printGrid = createGridPane(scaleX, scaleY);

        int rowCounter = 0;
        int colCounter = 0;
        int colCount = 2;
        int vRowsCount = (size % colCount != 0) ? size / colCount + 1 : size / colCount;
        for (int i = 0; i < size; i++) {

            Mitglied mitglied = reservationList.get(i);
            TextFlow card = new TextFlow();
            card.setPadding(new Insets(5.));
            card.getChildren().add(new Label("Name: " + mitglied.getNName() + " " + mitglied.getVName()
                    + "\n\nSitzplatz: " + mitglied.getSeat()));
            card.setPrefWidth(scaleX / colCount);
            card.setTextAlignment(TextAlignment.LEFT);

            col.getChildren().add(card);
            if (i % colCount != 0) {
                col = createHBox(scaleY, rows);
            }

            printGrid.add(card, colCounter, rowCounter);
            colCounter = (i % colCount == 0) ? colCounter + 1 : 0;
            rowCounter = (colCounter % colCount == 0) ? rowCounter + 1 : rowCounter;
        }

        AnchorPane printPane = new AnchorPane();

        printPane.getChildren().add(printGrid);
        // printPane.getChildren().add(rows);
        AnchorPane.setTopAnchor(rows, 0.);
        AnchorPane.setBottomAnchor(rows, 0.);
        AnchorPane.setLeftAnchor(rows, 0.);
        AnchorPane.setRightAnchor(rows, 0.);
        // textPreview.setTranslateY(-500);
        // borderPane.setBottom(textPreview);

        printerJob.printPage(pageLayout, printPane);
        printerJob.endJob();

        // Stage printPage = new Stage(StageStyle.DECORATED);
        // boolean success = printerJob.showPageSetupDialog(printPage);
        // boolean success = printerJob.showPrintDialog(printPage.getOwner());
        // if (success) {
        // }

    }

    private HBox createHBox(double scaleY, VBox rows) {
        HBox col = new HBox();
        col.setSpacing(20);
        col.setAlignment(Pos.CENTER_LEFT);
        col.setPadding(new Insets(20));
        col.setPrefHeight(scaleY / memberPerPageSpinner.getValue());
        rows.getChildren().add(col);
        return col;
    }

    private GridPane createGridPane(double scaleX, double scaleY) {

        GridPane printGridPane = new GridPane();
        printGridPane.setGridLinesVisible(true);

        printGridPane.setPrefHeight(scaleY);
        printGridPane.setPrefWidth(scaleX);

        return printGridPane;
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
        borderPane.setBottom(null);
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

        this.loadEvents();
        this.generateTableView();
        this.loadReservationList();
    }

}
