package de.trs.javafx.memberview;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import de.trs.javafx.dbcontroller.DbService;
import de.trs.javafx.dbcontroller.MemberRepository;
import de.trs.javafx.model.CsvHandler;
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
         * Initialisierung der Sahlbelegungsplansliste
         */
        seatHallColumn.setCellValueFactory(new PropertyValueFactory<>("seat"));
        nnameHallColumn.setCellValueFactory(new PropertyValueFactory<>("nName"));
        vnameHallColumn.setCellValueFactory(new PropertyValueFactory<>("vName"));

        /**
         * test Initialisierung //TODO: entfernen der Testinitialisierung
         */
        // Mitglied a = new Mitglied(1L, "Rie", "Seb", "zepp", "123", "DD", "09744",
        // "email", "9e");
        // Mitglied b = new Mitglied(1L, "Zwei", "Jul", "zepp", "123", "DD", "0945764",
        // "email@rtrt-de", "22f");
        // Mitglied c = new Mitglied(1L, "Wi", "Do", "asdlfj", "45", "N", "159485",
        // "dowi@hor.de", "19c");
        // listC = new ArrayList<Mitglied>();
        // listC.add(a);
        // listC.add(b);
        // listC.add(c);

        listC = (ArrayList<Mitglied>) dbService.getMembers();
        // listC = (ArrayList<Mitglied>) memberRepository.findAll();
        // listC = getMemberfromCSV();
        listCustomers(listC);
    }

    private ArrayList<Mitglied> getMemberfromCSV() {
        List<List<String>> memberListCSV;
        memberListCSV = CsvHandler.readCSV("members.csv", true);
        if (memberListCSV == null) {
            memberListCSV = new ArrayList<List<String>>();
            List<String> memberCSV = new ArrayList<String>();
            memberCSV.add("Seb");
            memberCSV.add("Ried");
            memberCSV.add("3d");
            memberCSV.add("");
            memberListCSV.add(memberCSV);
        }
        ArrayList<Mitglied> memberList = new ArrayList<Mitglied>();
        for (List<String> list : memberListCSV) {
            memberList.add(new Mitglied(list.get(0), list.get(1), list.get(2), list.get(3), list.get(4), list.get(5),
                    list.get(6), list.get(7)));
        }
        return memberList;
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
