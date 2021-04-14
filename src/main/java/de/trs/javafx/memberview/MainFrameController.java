package de.trs.javafx.memberview;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import de.trs.javafx.dbcontroller.DbService;
import de.trs.javafx.dbcontroller.MemberRepository;
import de.trs.javafx.model.Mitglied;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class MainFrameController implements Initializable {

    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField nnameTextfield, vnameTextfield, streetTextfield;
    @FXML
    private TableView<Mitglied> customerTableView, hallPlanTable;
    @FXML
    private Button startSearchButton, addButton, getItemButton;
    @FXML
    private TableColumn<Mitglied, String> seatTableColumn, nnameTableColumn, vnameTableColumn, streetTableColumn,
            zipTableColumn, townTableColumn, phoneTableColumn, seatHallColumn, nnameHallColumn, vnameHallColumn;

    private LocalDate eventDate;
    private ArrayList<Mitglied> listC;

    /** MemberRepository init */
    private MemberRepository memberRepository;
    @Autowired
    private DbService dbService;

    public MainFrameController() {

    }

    @Autowired
    public MainFrameController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void getDate() throws IOException {
        eventDate = datePicker.getValue();
        System.out.println(eventDate);
    }

    public void getSearchFields() {
        System.out
                .println(nnameTextfield.getText() + ", " + vnameTextfield.getText() + ", " + streetTextfield.getText());
    }

    public void listCustomers(ArrayList<Mitglied> list) {

        customerTableView.getItems().addAll(list);

    }

    public void addCustomerToHallPlan() {
        Mitglied temp = customerTableView.getSelectionModel().selectedItemProperty().get();
        hallPlanTable.getItems().add(temp);
    }

    public void getItem() throws IOException {
        System.out.println(customerTableView.getSelectionModel().selectedItemProperty().get());
        System.out.println(customerTableView.getSelectionModel().selectedItemProperty().getValue());

    }

    public void init() {
        log.info("INITIALIZING MainFrameController");
    }

    /*
     * Deaktivieren des getItemButton wenn kein Eintrag angewählt wurde.
     * Initialisieren der Table Columns
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        getItemButton.disableProperty().bind(customerTableView.getSelectionModel().selectedItemProperty().isNull());

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
         * Initialisierung der Sahlbelegungsplanliste
         */
        seatHallColumn.setCellValueFactory(new PropertyValueFactory<>("seat"));
        nnameHallColumn.setCellValueFactory(new PropertyValueFactory<>("nName"));
        vnameHallColumn.setCellValueFactory(new PropertyValueFactory<>("vName"));

        /**
         * test Initialisierung //TODO: entfernen der Testinitialisierung
         */

        listC = (ArrayList<Mitglied>) dbService.getMembers();

        listCustomers(listC);
    }

    public MemberRepository getMemberRepository() {
        return memberRepository;
    }

    public void setMemberRepository(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public DbService getDbService() {
        return dbService;
    }

    public void setDbService(DbService dbService) {
        this.dbService = dbService;
    }
}
