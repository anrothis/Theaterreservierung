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
import de.trs.javafx.model.Event;
import de.trs.javafx.model.Mitglied;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class TextViewController extends AnchorPane implements Initializable {

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

    @Autowired
    private DbService dbService;

    @FXML
    private void deleteMember() {
        Mitglied selctedMitglied = (Mitglied) memberTableView.getSelectionModel().getSelectedItem();
        log.info("DELETING " + selctedMitglied);

        if (selctedMitglied == null) {
            log.info("Kein Mitglied ausgewaehlt");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Kein Mitglied ausgewählt!");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Soll Mitglied wiklich gelöscht werden?");
        alert.setHeaderText(selctedMitglied.toString());
        alert.setTitle("Mitglied löschen?");
        Optional<ButtonType> buttonType = alert.showAndWait();

        log.info("ButtonType: " + buttonType.get().getText());

        if (buttonType.get() == ButtonType.OK) {
            dbService.deleteMember(selctedMitglied);
            memberTableView.setItems(mitgliedAsObservableList());
        } else {
            log.info("Loeschvorgang abgebrochen");
        }

        log.info("END Mitglied loeschen");

    }

    private ObservableList<Mitglied> mitgliedAsObservableList() {
        ObservableList<Mitglied> observableList = FXCollections.observableList(dbService.getMembers());
        return observableList;

    }

    private ObservableList<Event> eventAsObservableList() {
        Date eventDate = Date.valueOf(eventDatePicker.getValue());
        ObservableList<Event> observableEventList = FXCollections.observableList(dbService.getEventsByDate(eventDate));
        return observableEventList;
    }

    @FXML
    private void reloadMemberView() {
        memberTableView.setItems(mitgliedAsObservableList());
    }

    @FXML
    private void reloadEventView() {
        ObservableList<Event> events = eventAsObservableList();
        ObservableList<String> eventNameObservableList = FXCollections.observableArrayList();
        ObservableList<Mitglied> mitglieder = FXCollections.observableArrayList();

        eventChoiceBox.getItems().clear();

        if (events.size() != 0) {
            for (int i = 0; i < events.size(); i++) {
                eventNameObservableList.add(events.get(i).getName());
            }
            eventChoiceBox.setItems(eventNameObservableList);
            eventChoiceBox.getSelectionModel().clearAndSelect(0);

            mitglieder.add(events.get(0).getReservationsList());

            // eventTableView.setItems();

        } else {
            eventChoiceBox.setValue("-- Keine Events --");
        }
    }

    private void generateTableView() {
        /**
         * initialisieren der Mitglieder TabelView
         */
        List<String> columnTitles = List.of("Sitzplatz", "Nachname", "Vorname", "Strasse", "PLZ", "Stadt", "Telefon",
                "Email");

        Iterator<String> titleIterator = columnTitles.iterator();

        String[] attributeTitles = { "seat", "nName", "vName", "street", "zipCode", "town", "telephone", "email" };
        for (String s : attributeTitles) {

            String title = titleIterator.next();

            TableColumn<Mitglied, String> memberTabel = new TableColumn<>(title);
            memberTabel.setCellValueFactory(new PropertyValueFactory<Mitglied, String>(s));
            memberTableView.getColumns().add(memberTabel);

            TableColumn<Mitglied, String> eventTabel = new TableColumn<>(title);
            eventTabel.setCellValueFactory(new PropertyValueFactory<Mitglied, String>(s));
            eventTableView.getColumns().add(eventTabel);
        }

        this.reloadMemberView();
        this.reloadEventView();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        eventDatePicker.setValue(LocalDate.now());

        eventChoiceBox.setOnAction(e -> {
            log.info(String.valueOf(eventChoiceBox.getSelectionModel().getSelectedIndex()));
        });
        /** propreäter */
        // eventChoiceBox.getSelectionModel().selectedIndexProperty().addListener((e, a,
        // b) -> {
        // log.info("CHANGELISTENER " + b.intValue());
        // });

        this.generateTableView();

    }
}
