package de.trs.javafx.searchpane;

import de.trs.javafx.model.DataHandler;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

public class SearchPane extends BorderPane {

    private LeftPane leftPane = null;
    private OverviewTableView overviewTableView = null;

    public SearchPane() {
        initiate();
    }

    private void initiate() {
        this.setTop(new Text("Suchleiste"));
        this.setLeft(getLeftPane(getOverviewTableView()));
        this.setCenter(getOverviewTableView());
        this.setPrefHeight(600);
        this.setPrefWidth(800);
    }

    private LeftPane getLeftPane(OverviewTableView overviewTableView) {
        if (leftPane == null) {
            leftPane = new LeftPane(overviewTableView);
        }
        return leftPane;
    }

    private void setLeftPane(LeftPane leftPane) {
        this.leftPane = leftPane;
    }

    public OverviewTableView getOverviewTableView() {
        if (overviewTableView == null) {
            overviewTableView = new OverviewTableView(DataHandler.INSTANCE.membersAsObservableList());
        }
        return overviewTableView;
    }

    public void setOverviewTableView(OverviewTableView overviewTableView) {
        this.overviewTableView = overviewTableView;
    }

    public void refreshDate() {
        getOverviewTableView().setItems(DataHandler.INSTANCE.membersAsObservableList());
    }

}