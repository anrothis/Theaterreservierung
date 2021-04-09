package de.trs.javafx.searchpane;

import javafx.scene.Scene;

public class SearchPaneScene extends Scene {
    private SearchPane searchPane;

    public SearchPaneScene() {
        super(new SearchPane());
        this.searchPane = (SearchPane) getRoot();
    }

    public void refreshDate() {
        searchPane.refreshDate();
    }
}