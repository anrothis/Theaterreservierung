package de.trs.javafx.memberview;

import java.io.IOException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import de.trs.javafx.JavafxApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.util.Callback;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MemberViewScene {

    /**
     * Autowired test
     * 
     * sollte automatisch initialisiert und gemanaged werden
     */
    @Autowired
    private MemberViewPane memberViewPane;
    @Autowired
    private ConfigurableApplicationContext context;

    /**
     * Initialisiert im Konstruktor die entsprechende FXML Datei
     * 
     * @throws IOException
     */
    public MemberViewScene() {

        // this.memberViewPane = (MemberViewPane) loadFXML("MainFrame");
        // this.memberViewPane = (MemberViewPane) getRoot();

    }

    public void refreshDate() {
        memberViewPane.refreshDate();
    }

    /*
     * FXML loader Funktion zum laden einer Layout FXML Datei
     */
    private Parent fxmlLoader(String fxml) {
        try {
            Parent root;
            log.info("START LOADING FXML: ");
            URL fxml_path = JavafxApplication.class.getResource(fxml);
            log.info("LOADING FXML: " + fxml_path);
            FXMLLoader fxmlLoader = new FXMLLoader(fxml_path);
            log.info("LOADING ApplicationContext");
            fxmlLoader.setControllerFactory(new Callback<Class<?>, Object>() {
                @Override
                public Object call(Class<?> param) {
                    return context.getBean(param);
                }
            }); // vorzeitiges setzen der Controller
            log.info("LOADING FXML PARENT ROOT");
            root = fxmlLoader.load();
            log.info("DONE LOADING FXML PARENT ROOT");
            return root;
        } catch (Exception e) {
            log.error("ERROR Loading fxml", e);
            return null;
        }
    }

    public Parent getParent() {
        return fxmlLoader("/fxml/StageScene.fxml");
    }
}
