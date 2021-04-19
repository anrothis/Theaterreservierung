package de.trs.javafx.memberview;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.stereotype.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

@Controller
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

    private FileChooser fileChooser;

    @FXML
    void initialize() {

        lookupButton.setOnAction(event -> {
            Stage mainStage = (Stage) javafx.stage.Window.getWindows().get(0);
            fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            fileChooser.getExtensionFilters().addAll(new ExtensionFilter("CSV Files", "*.csv"),
                    new ExtensionFilter("Text Files", "*.txt"),
                    new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
                    new ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"),
                    new ExtensionFilter("All Files", "*.*"));
            File selectedFile = fileChooser.showOpenDialog(mainStage);
            if (selectedFile != null) {
                // mainStage.display(selectedFile);
                filePathTextField.setText(selectedFile.getAbsolutePath());
            }

        });
    }
}
