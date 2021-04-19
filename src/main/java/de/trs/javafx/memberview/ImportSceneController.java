package de.trs.javafx.memberview;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

public class ImportSceneController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField filePathTextField;

    @FXML
    private Button lookupButton;

    @FXML
    private Button loadButton;

    @FXML
    private ListView<String> previewListView;

    @FXML
    private Button importButton;

    @FXML
    void initialize() {
        // assert filePathTextField != null
        // : "fx:id=\"filePathTextField\" was not injected: check your FXML file
        // 'ImportScene.fxml'.";
        // assert lookupButton != null
        // : "fx:id=\"lookupButton\" was not injected: check your FXML file
        // 'ImportScene.fxml'.";
        // assert loadButton != null : "fx:id=\"loadButton\" was not injected: check
        // your FXML file 'ImportScene.fxml'.";
        // assert previewListView != null
        // : "fx:id=\"previewListView\" was not injected: check your FXML file
        // 'ImportScene.fxml'.";
        // assert importButton != null
        // : "fx:id=\"importButton\" was not injected: check your FXML file
        // 'ImportScene.fxml'.";
        loadButton.setOnAction(event -> {
            new FileChooser();

        });
    }
}
