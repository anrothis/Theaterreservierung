package de.trs.javafx.memberview;

import java.io.IOException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;

import de.trs.javafx.JavafxApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import lombok.extern.slf4j.Slf4j;

@Slf4j
// @Component
public class MemberViewScene extends Scene {

    /**
     * Autowired test
     * 
     * sollte automatisch initialisiert und gemanaged werden
     */
    @Autowired
    private MemberViewPane memberViewPane;

    /**
     * Initialisiert im Konstruktor die entsprechende FXML Datei
     * 
     * @throws IOException
     */
    public MemberViewScene() {

        super(loadFXML("/fxml/MainFrame"));
        // this.memberViewPane = (MemberViewPane) loadFXML("MainFrame");
        // this.memberViewPane = (MemberViewPane) getRoot();

    }

    public void refreshDate() {
        memberViewPane.refreshDate();
    }

    /*
     * FXML loader Funktion zum laden einer Layout FXML Datei
     */
    private static Parent loadFXML(String fxml) {
        try {

            log.info("START LOADING FXML: " + fxml);
            URL fxml_path = JavafxApplication.class.getResource(fxml + ".fxml");
            log.info("LOADING FXML: " + fxml_path);
            FXMLLoader fxmlLoader = new FXMLLoader(fxml_path);
            log.info("LOADING FXML PARENT ROOT");
            Parent root = fxmlLoader.load();
            log.info("DONE LOADING FXML PARENT ROOT");
            return root;
        } catch (IOException e) {
            log.error("ERROR LOADING FXML", e);
        }
        return null;
    }
}
