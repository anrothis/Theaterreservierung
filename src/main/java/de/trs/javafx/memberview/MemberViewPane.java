package de.trs.javafx.memberview;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import de.trs.javafx.model.Mitglied;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class MemberViewPane implements Initializable {

    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField nnameTextfield, vnameTextfield, streetTextfield;
    @FXML
    private TableView<Mitglied> memberTableView, hallPlanTable;
    @FXML
    private Button startSearchButton, addButton, getItemButton;
    @FXML
    private TableColumn<Mitglied, String> seatTableColumn, nnameTableColumn, vnameTableColumn, streetTableColumn,
            zipTableColumn, townTableColumn, phoneTableColumn, seatHallColumn, nnameHallColumn, vnameHallColumn;

    private LocalDate eventDate;
    private ArrayList<Mitglied> listC;

    public void getDate() throws IOException {
        eventDate = datePicker.getValue();
        System.out.println(eventDate);
    }

    public void getSearchFields() {
        System.out
                .println(nnameTextfield.getText() + ", " + vnameTextfield.getText() + ", " + streetTextfield.getText());
    }

    public void listCustomers(ArrayList<Mitglied> list) {

        memberTableView.getItems().addAll(list);

    }

    public void addCustomerToHallPlan() {
        Mitglied temp = memberTableView.getSelectionModel().selectedItemProperty().get();
        hallPlanTable.getItems().add(temp);
    }

    public void getItem() throws IOException {
        System.out.println(memberTableView.getSelectionModel().selectedItemProperty().get());
        System.out.println(memberTableView.getSelectionModel().selectedItemProperty().getValue());

    }

    /*
     * Initialisieren der Table Columns über initialize() Wird automatisch nach
     * laden der FXML ausgeführ damit sichergestellt ist, dass alle Bezüge auch
     * initialisiert wurden.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        /**
         * deaktivieren des Add-Buttons wennn kein Eintrag ausgewählt wurde
         */
        getItemButton.disableProperty().bind(memberTableView.getSelectionModel().selectedItemProperty().isNull());

        /**
         * Initalisierung der Customer Table Columns
         */
        nnameTableColumn.setCellValueFactory(new PropertyValueFactory<Mitglied, String>("nName"));
        vnameTableColumn.setCellValueFactory(new PropertyValueFactory<Mitglied, String>("vName"));
        streetTableColumn.setCellValueFactory(new PropertyValueFactory<Mitglied, String>("street"));
        zipTableColumn.setCellValueFactory(new PropertyValueFactory<Mitglied, String>("zipCode"));
        townTableColumn.setCellValueFactory(new PropertyValueFactory<Mitglied, String>("town"));
        phoneTableColumn.setCellValueFactory(new PropertyValueFactory<Mitglied, String>("telephone"));
        seatTableColumn.setCellValueFactory(new PropertyValueFactory<Mitglied, String>("seat"));

        /**
         * Initialisierung der Sahlbelegungsplansliste
         */
        seatHallColumn.setCellValueFactory(new PropertyValueFactory<>("seat"));
        nnameHallColumn.setCellValueFactory(new PropertyValueFactory<>("nName"));
        vnameHallColumn.setCellValueFactory(new PropertyValueFactory<>("vName"));

        /**
         * test Initialisierung
         */
        Mitglied a = new Mitglied(1L, "Rie", "Seb", "zepp", "123", "DD", "09744", "email", "9e");
        Mitglied b = new Mitglied(1L, "Zwei", "Jul", "zepp", "123", "DD", "0945764", "email@rtrt-de", "22f");
        Mitglied c = new Mitglied(1L, "Wi", "Do", "asdlfj", "45", "N", "159485", "dowi@hor.de", "19c");

        listC = new ArrayList<Mitglied>();
        listC.add(a);
        listC.add(b);
        listC.add(c);

        listCustomers(listC);
    }

    public void refreshDate() {
    }

}
