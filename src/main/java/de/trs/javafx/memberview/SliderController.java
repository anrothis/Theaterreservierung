package de.trs.javafx.memberview;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;

/**
 * Slider Controller zum Steuern der UI Frame Funktionen
 */
@Slf4j
@Controller
public class SliderController implements Initializable {

    @FXML
    private BorderPane borderPane;
    @FXML
    private Label Menu;
    @FXML
    private Label MenuBack;
    @FXML
    private Label titleLabel;
    @FXML
    private AnchorPane slider;

    @Autowired
    private ApplicationContext context;
    private double x, y;

    /**
     * getting window position
     * 
     * @param event
     */
    @FXML
    void setXY(MouseEvent event) {
        x = event.getSceneX();
        y = event.getSceneY();
    }

    /**
     * moving window by mousclick
     * 
     * @param event
     */
    @FXML
    void moveFrame(MouseEvent event) {
        double xs = event.getScreenX() - x;
        double ys = event.getScreenY() - y;
        Stage.getWindows().get(0).setX(xs);
        Stage.getWindows().get(0).setY(ys);
    }

    /**
     * Funktion zum Laden der Terminverwaltungsansicht
     */
    @FXML
    void mainView() {
        try {
            Parent root = reloadFxml(SwitchScene.TABLEVIEW.getFxml());
            titleLabel.setText("Terminverwaltung");
            borderPane.setCenter(root);
        } catch (Exception e) {
            log.error("ERROR Loading FXML TableView", e);
        }
    }

    /**
     * Funktion zum Laden der Importansicht
     */
    @FXML
    void importView() {
        try {
            Parent root = reloadFxml(SwitchScene.IMPORTVIEW.getFxml());
            titleLabel.setText("Importieren...");
            borderPane.setCenter(root);
        } catch (Exception e) {
            log.error("ERROR Loading FXML TableView", e);
        }
    }

    /**
     * Funktion zum Laden der Hizuf??genansicht
     */
    @FXML
    void addView() {
        try {
            Parent root = reloadFxml(SwitchScene.ADDVIEW.getFxml());
            titleLabel.setText("Hinzuf??gen...");
            borderPane.setCenter(root);
        } catch (Exception e) {
            log.error("ERROR Loading FXML TableView", e);
        }
    }

    /**
     * Funktion zum Laden der Druckansicht
     */
    @FXML
    void printerView() {
        try {
            Parent root = reloadFxml(SwitchScene.PRINTVIEW.getFxml());
            titleLabel.setText("Drucken...");
            borderPane.setCenter(root);
        } catch (Exception e) {
            log.error("ERROR Loading FXML TableView", e);
        }
    }

    /**
     * FXML Loader zum laden der Menueunterfenster
     * 
     * @param fxml Pfad zur FXML Datei. SwitchScene.xxx zum schnellladen
     * @return gibt die Parent root zur??ck
     * @throws IOException
     */
    private Parent reloadFxml(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));

        loader.setControllerFactory(new Callback<Class<?>, Object>() {
            @Override
            public Object call(Class<?> param) {
                return context.getBean(param);
            }
        });
        return loader.load();
    }

    /**
     * Initialisierung der Standardparameter und ActionListenern Laden des
     * Terminverwaltungsframes
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        Menu.setVisible(false);
        Menu.setOnMouseClicked(event -> {
            borderPane.setLeft(slider);
            TranslateTransition sliderTranslation = new TranslateTransition();
            sliderTranslation.setDuration(Duration.millis(400));
            sliderTranslation.setNode(slider);
            sliderTranslation.setToX(0);
            sliderTranslation.play();

            sliderTranslation.setOnFinished(e -> {
                MenuBack.setVisible(true);
                Menu.setVisible(false);
            });
        });

        MenuBack.setOnMouseClicked(event -> {
            TranslateTransition sliderTranslation = new TranslateTransition();
            sliderTranslation.setDuration(Duration.millis(400));
            sliderTranslation.setNode(slider);
            sliderTranslation.setToX(-100);
            sliderTranslation.play();

            sliderTranslation.setOnFinished(e -> {
                borderPane.setLeft(null);
                Menu.setVisible(true);
                MenuBack.setVisible(false);
            });
        });
        // TODO: Column initialisation
        this.mainView();

    }

}
