package de.trs.javafx.memberview;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import de.trs.javafx.App;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class MemberViewScene extends Scene {

    /**
     * Autowired test
     * 
     * sollte automatisch initialisiert und gemanaged werden
     */
    // @Autowired
    private MemberViewPane memberViewPane;

    /**
     * Initialisiert im Konstruktor die entsprechende FXML Datei
     * 
     * @throws IOException
     */
    public MemberViewScene() throws IOException {

        super(loadFXML("MainFrame"));
        // this.memberViewPane = (MemberViewPane) loadFXML("MainFrame");
        // this.memberViewPane = getRoot();

    }

    public void refreshDate() {
        memberViewPane.refreshDate();
    }

    /*
     * FXML loader Funktion zum laden einer Layout FXML Datei
     */
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
}
