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
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;

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
    private AnchorPane slider;

    @Autowired
    private ApplicationContext context;
    private double x, y;

    @FXML
    void setXY(MouseEvent event) {
        x = event.getSceneX();
        y = event.getSceneY();
    }

    @FXML
    void moveFrame(MouseEvent event) {
        double xs = event.getScreenX() - x;
        double ys = event.getScreenY() - y;

        Stage.getWindows().get(0).setX(xs);
        Stage.getWindows().get(0).setY(ys);
    }

    @FXML
    void mainView(ActionEvent event) {
        try {
            Parent root = reloadFxml(SwitchScene.TABLEVIEW.getFxml());
            borderPane.setCenter(root);
        } catch (Exception e) {
            log.error("ERROR Loading FXML TableView", e);
        }
    }

    @FXML
    void importView(ActionEvent event) {
        try {
            Parent root = reloadFxml(SwitchScene.IMPORTVIEW.getFxml());
            borderPane.setCenter(root);
        } catch (Exception e) {
            log.error("ERROR Loading FXML TableView", e);
        }
    }

    @FXML
    void addView(ActionEvent event) {
        try {
            Parent root = reloadFxml(SwitchScene.ADDVIEW.getFxml());
            borderPane.setCenter(root);
        } catch (Exception e) {
            log.error("ERROR Loading FXML TableView", e);
        }
    }

    /**
     * FXML Loader zum laden der Menueunterfenster
     * 
     * @param fxml Pfad zur FXML Datei. SwitchScene.xxx zum schnellladen
     * @return gibt die Parent root zurück
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

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // center = (AnchorPane) borderPane.getCenter();
        // slider.setTranslateX(0);
        // center.setTranslateX(0);
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
        this.mainView(null);

    }

}
