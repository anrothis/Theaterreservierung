package de.trs.javafx.memberview;

/**
 * Enum Klasse zum zentralen aufrufen der Fxml Pfade
 */
public enum SwitchScene {
    TABLEVIEW("/fxml/StageScene.fxml"), SEARCHVIEW("/fxml/SearchScene.fxml"), ADDVIEW("/fxml/AddScene.fxml"),
    IMPORTVIEW("/fxml/ImportScene.fxml"), PRINTVIEW("/fxml/PrintScene.fxml");

    private final String fxmlPath;

    private SwitchScene(String fxmlPath) {
        this.fxmlPath = fxmlPath;
    }

    public String getFxml() {
        return fxmlPath;
    }

}
