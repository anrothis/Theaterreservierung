package de.trs.javafx.memberview;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class AddSceneController implements Initializable {

    @FXML
    private TextField firstnameTextField;

    @FXML
    private TextField lastnameTextField;

    @FXML
    private TextField seatTextField;

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
    private Button copyMembersButton;

    @FXML
    private CheckBox copyMembersCheckBox;

    @FXML
    private Button resetEventButton;

    @FXML
    private Button addEventButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub

    }

}
