package de.trs.javafx.mainwindow;

import de.trs.javafx.model.Mitglied;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * OverviewTableView
 */
public class OverviewTableView extends TableView {

    public OverviewTableView(ObservableList<Mitglied> items) {
        super(items);
        initiate();

    }

    private void initiate() {
        TableColumn<String, Mitglied> tableColumnVname = new TableColumn<>("Vorname");
        tableColumnVname.setCellValueFactory(new PropertyValueFactory<>("vorname"));

        TableColumn<String, Mitglied> tableColumnNname = new TableColumn<>("Nachname");
        tableColumnNname.setCellValueFactory(new PropertyValueFactory<>("nachname"));

        TableColumn<String, Mitglied> tableColumnSeat = new TableColumn<>("Sitzplatz");
        tableColumnSeat.setCellValueFactory(new PropertyValueFactory<>("sitzplatz"));

        getColumns().addAll(tableColumnVname, tableColumnNname, tableColumnSeat);
    }
}