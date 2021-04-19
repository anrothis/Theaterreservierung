package de.trs.javafx.memberview;

public enum SwitchScene {
    TABLEVIEW("/fxml/StageScene.fxml"), SEARCHVIEW("/fxml/SearchScene.fxml"), ADDVIEW("/fxml/AddScene.fxml"),
    IMPORTVIEW("/fxml/ImportScene.fxml");

    private final String fxmlPath;

    private SwitchScene(String fxmlPath) {
        this.fxmlPath = fxmlPath;
    }

    public String getFxml() {
        return fxmlPath;
    }

}
