package de.trs.javafx.memberview;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class AddSceneController implements Initializable {

    @FXML
    private TextField firstnameTextField;

    @FXML
    private TextField lastnameTextField;

    @FXML
    private TextField seatTextField;

    @FXML
    private TextField seatAltTextField;

    @FXML
    private TextField streetTextField;

    @FXML
    private TextField postcodeTextField;

    @FXML
    private TextField townTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField telephoneTextField;

    @FXML
    private Button resetMemberButton;

    @FXML
    private Button addMemberButton;

    @FXML
    private TextField eventnameTextField;

    @FXML
    private DatePicker eventDatePicker;

    @FXML
    private TextField locationTextField;

    @FXML
    private DatePicker copyDatePicker;

    @FXML
    private ComboBox<String> eventComboBox;

    @FXML
    private CheckBox copyMembersCheckBox;

    @FXML
    private Button resetEventButton;

    @FXML
    private Button addEventButton;

    @FXML
    private Label memberCountLabel;

    @Autowired
    private DbService dbService;
    private ObservableList<Event> eventsAsObservable;

    @FXML
    private void addMember() {
        Mitglied mitglied = new Mitglied(lastnameTextField.getText(), firstnameTextField.getText(),
                streetTextField.getText(), postcodeTextField.getText(), townTextField.getText(),
                telephoneTextField.getText(), emailTextField.getText(), seatTextField.getText(),
                seatAltTextField.getText());

        if ((lastnameTextField.getText().equals("") && firstnameTextField.getText().equals("")
                && streetTextField.getText().equals("") && postcodeTextField.getText().equals("")
                && townTextField.getText().equals("") && telephoneTextField.getText().equals("")
                && emailTextField.getText().equals("") && seatTextField.getText().equals("")
                && seatAltTextField.getText().equals(""))) {

            Alert alert = new Alert(AlertType.INFORMATION, "Bitte mindestens ein Feld ausfüllen.", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        Alert alert = new Alert(AlertType.CONFIRMATION, "Soll die Person angelegt werden?", ButtonType.APPLY,
                ButtonType.CANCEL);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.APPLY) {
            dbService.addMember(mitglied);
        }
    }

    @FXML
    private void addEvent() {

        if (eventnameTextField.getText().equals("") || locationTextField.getText().equals("")) {

            Alert alert = new Alert(AlertType.INFORMATION, "Bitte Veranstaltungsname und Ort ausfüllen.",
                    ButtonType.OK);
            alert.showAndWait();
            return;
        }

        Event event = new Event(eventnameTextField.getText(), Date.valueOf(eventDatePicker.getValue()),
                locationTextField.getText());

        if (copyMembersCheckBox.isSelected()) {
            if (eventComboBox.getSelectionModel().getSelectedIndex() == -1) {
                Alert alert = new Alert(AlertType.ERROR, "Keine Veranstaltung zum kopieren ausgewählt.", ButtonType.OK);
                alert.showAndWait();
                return;
            }
            List<Mitglied> reservationList = eventsAsObservable
                    .get(eventComboBox.getSelectionModel().getSelectedIndex()).getReservationsList();
            ObservableList<Mitglied> copyList = FXCollections.observableArrayList();
            for (Mitglied mitglied : reservationList) {
                copyList.add(mitglied);
            }
            event.setReservationsList(List.copyOf(copyList));
        }
        Alert alert = new Alert(AlertType.CONFIRMATION, "Soll die Aufführung angelegt werden?", ButtonType.APPLY,
                ButtonType.CANCEL);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.APPLY) {
            dbService.addEvent(event);
        }
        this.refreshList();
    }

    @FXML
    private void clearMemberTextFields() {
        lastnameTextField.setText("");
        firstnameTextField.setText("");
        streetTextField.setText("");
        postcodeTextField.setText("");
        townTextField.setText("");
        telephoneTextField.setText("");
        emailTextField.setText("");
        seatTextField.setText("");
        seatAltTextField.setText("");
    }

    @FXML
    private void clearEventTextFields() {
        eventnameTextField.setText("");
        eventDatePicker.setValue(LocalDate.now());
        locationTextField.setText("");

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        copyMembersCheckBox.setSelected(true);
        eventDatePicker.setValue(LocalDate.now());

        /** Initiation DatePicker */
        copyDatePicker.setOnAction(e -> {
            refreshList();
        });
        copyDatePicker.setValue(LocalDate.now());
        this.refreshList();
    }

    /**
     * Aktuallisiert die EventComboBox bei Seitenaufruf oder Datumswahl
     */
    private void refreshList() {
        eventComboBox.getItems().clear();
        LocalDate eventLocaleDate = copyDatePicker.getValue();
        java.util.Date eventDate = java.util.Date
                .from(eventLocaleDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        this.eventsAsObservable = FXCollections.observableList(dbService.getEventsByDate(eventDate));

        if (this.eventsAsObservable.isEmpty()) {
            log.info("COPYEVENT LIST IS EMPTY");
            eventComboBox.setValue("-- Keine Events --");
            memberCountLabel.setText("-leer-");
            return;
        }
        for (Event event : this.eventsAsObservable) {

            eventComboBox.getItems().add(event.getName());
        }
        eventComboBox.getSelectionModel().selectFirst();
        int memberCount = eventsAsObservable.get(eventComboBox.getSelectionModel().getSelectedIndex())
                .getReservationsList().size();
        memberCountLabel.setText(String.valueOf(memberCount));
    }

}
