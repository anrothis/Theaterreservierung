package de.trs.javafx.searchpane;

import de.trs.javafx.model.Member;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * OverviewTableView
 */
public class OverviewTableView extends TableView {

    public OverviewTableView(ObservableList<Member> items) {
        super(items);
        initiate();

    }

    private void initiate() {
        TableColumn<String, Member> tableColumnVname = new TableColumn<>("Vorname");
        tableColumnVname.setCellValueFactory(new PropertyValueFactory<>("vorname"));

        TableColumn<String, Member> tableColumnNname = new TableColumn<>("Nachname");
        tableColumnNname.setCellValueFactory(new PropertyValueFactory<>("nachname"));

        TableColumn<String, Member> tableColumnSeat = new TableColumn<>("Sitzplatz");
        tableColumnSeat.setCellValueFactory(new PropertyValueFactory<>("sitzplatz"));

        getColumns().addAll(tableColumnVname, tableColumnNname, tableColumnSeat);
    }
}