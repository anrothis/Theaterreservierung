package de.trs.javafx.memberview;

import java.io.File;
import java.util.ArrayList;

import org.springframework.stereotype.Controller;

import de.trs.javafx.model.CsvHandler;
import de.trs.javafx.model.Mitglied;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class ImportSceneController {

    @FXML
    private TextField filePathTextField;

    @FXML
    private Button lookupButton;

    @FXML
    private Button loadFileButton;

    @FXML
    private CheckBox hasTitle;

    @FXML
    private ListView<String> previewMemberListView;

    @FXML
    private ListView<String> addMemberListView;

    @FXML
    private Button importMemberButton;

    @FXML
    private ListView<String> previewEventListView;

    @FXML
    private ListView<String> addEventListView;

    @FXML
    private Button importEventButton;

    private FileChooser fileChooser;
    private File selectedFile;

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
            selectedFile = fileChooser.showOpenDialog(mainStage);
            if (selectedFile != null) {
                // mainStage.display(selectedFile);
                filePathTextField.setText(selectedFile.getAbsolutePath());
            }

        });

        loadFileButton.setOnAction(evet -> {
            if (!filePathTextField.getText().equals("")) {
                try {
                    log.info("LOADING FILE");
                    ArrayList<Mitglied> tempList = CsvHandler.PareseMemberList
                            .getMemberfromCSV(filePathTextField.getText(), hasTitle.isArmed());
                    // TODO: load list to ListView

                } catch (Exception e) {
                    // TODO: handle exception
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Fehler beim laden der ausgewählten Datei.");
                    alert.showAndWait();
                }
            }
        });
    }
}
