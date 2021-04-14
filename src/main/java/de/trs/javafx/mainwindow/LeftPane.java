package de.trs.javafx.mainwindow;

import java.util.Optional;

import de.trs.javafx.SceneChangeEvent;
import de.trs.javafx.model.DataHandler;
import de.trs.javafx.model.Mitglied;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;

/**
 * LeftPane
 * 
 * 
 * @Slf4j - Anotierung für Logger
 */
@Slf4j
public class LeftPane extends VBox {

    private Button buttonCreateMember = null;
    private Button buttonDeleteMember = null;
    private OverviewTableView overviewTableView;

    // public LeftPane() {
    // initiate();
    // }

    public LeftPane(OverviewTableView overviewTableView) {
        this.overviewTableView = overviewTableView;
        initiate();
    }

    public void initiate() {
        this.setSpacing(10);
        this.setPadding(new Insets(5));
        this.getChildren().add(getButtenCreateMember());
        this.getChildren().add(getButtonDeleteMember());
    }

    public Button getButtenCreateMember() {
        if (buttonCreateMember == null) {
            buttonCreateMember = new Button("Mitglied anlegen");
            buttonCreateMember.setOnAction(e -> {
                log.info("Mitglied anlegen");
                fireEvent(new SceneChangeEvent(SceneChangeEvent.GO_TO_CREATE_SCENE));
            });
        }
        return buttonCreateMember;
    }

    public Button getButtonDeleteMember() {
        if (buttonDeleteMember == null) {
            buttonDeleteMember = new Button("Mitglied löschen");
            buttonDeleteMember.setOnAction(e -> {
                log.info("BEGIN Mitglied loeschen");
                Mitglied selectedMember = (Mitglied) overviewTableView.getSelectionModel().getSelectedItem();

                if (selectedMember == null) {
                    log.info("Kein Mitglied ausgewaehlt");
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Kein Mitglied ausgewählt!");
                    alert.showAndWait();
                    return;
                }

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Soll Mitglied wiklich gelöscht werden?");
                alert.setHeaderText(selectedMember.toString());
                alert.setTitle("Mitglied löschen?");
                Optional<ButtonType> buttonType = alert.showAndWait();
                log.info("ButtonType: " + buttonType.get().getText());
                if (buttonType.get() == ButtonType.OK) {
                    DataHandler.INSTANCE.deleteMitglied(selectedMember);
                    overviewTableView.setItems(DataHandler.INSTANCE.mitgliedAsObservableList());
                } else {
                    log.info("Loeschvorgang abgebrochen");
                }

                log.info("END Mitglied loeschen");
            });
        }
        return buttonDeleteMember;
    }
}