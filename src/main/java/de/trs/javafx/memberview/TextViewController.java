package de.trs.javafx.memberview;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import de.trs.javafx.dbcontroller.DbService;
import de.trs.javafx.model.Event;
import de.trs.javafx.model.Mitglied;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    private SplitPane splitPane;

    @FXML
    private TableView<Mitglied> memberTableView;

    @FXML
    private TableView<Event> eventTableView;

    @Autowired
    private DbService dbService;

    private void generateTableView() {
        // ArrayList<TableColumn<Mitglied, String>> tableColumn = new ArrayList<>();
        String[] titles = { "nName", "vName", "street", "zipCode", "town", "telephone", "seat" };
        for (String s : titles) {
            TableColumn<Mitglied, String> temp = new TableColumn<>(s);
            temp.setCellValueFactory(new PropertyValueFactory<Mitglied, String>(s));
            memberTableView.getColumns().add(temp);
        }
        memberTableView.getItems().addAll(dbService.getMembers());
        // ArrayList<TableColumn<Event, String>> tableColumn = new ArrayList<>();
        String[] titles2 = { "name", "performanceDate", "location" };
        for (String s : titles2) {
            TableColumn<Event, String> temp = new TableColumn<>(s);
            temp.setCellValueFactory(new PropertyValueFactory<Event, String>(s));
            eventTableView.getColumns().add(temp);
        }
        eventTableView.getItems().addAll(dbService.getEvents());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.generateTableView();
    }
}
