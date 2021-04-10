package de.trs.javafx;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App /* extends Application */ {

    private static Scene scene;

    /*
     * Ablauf nach main() Funktion. 1. lauch(args) 2. start() 3. erzeugen der Root
     * Node aus der MainFrame.fxml und initialisieren als scene 4. anschließend
     * initialisieren der scene als stage (natives Fenster) und anzeigen
     */
    // @Override
    // public void start(Stage stage) throws IOException {
    // scene = new Scene(loadFXML("MainFrame"));
    // stage.setScene(scene);
    // stage.show();

    // stage.setOnCloseRequest(event -> { // Sicherheitsabfrage beim Beenden der
    // Applikation mit Lambda Funktion
    // logout(stage);
    // event.consume();
    // });
    // }

    // @Override
    // public void stop() throws Exception {
    // System.out.println("Closing");
    // super.stop();
    // }

    /*
     * Funktion zum wechseln der scene in neuer Layout Pane(root)
     */
    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    /*
     * FXML loader Funktion zum laden einer Layout FXML Datei
     */
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    /*
     * logout Funktion zur Initalisierung der "Schließen" Abfrage
     */
    public void logout(Stage stage) {

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Closing App?");
        alert.setHeaderText("Sie sind dabei die App zu beenden.");
        alert.setContentText("Fortfahren? ");

        if (alert.showAndWait().get() == ButtonType.OK) {

            System.out.println("Logout complete");
            stage.close();
        }
    }

    // public static void main(String[] args) {
    // launch(args);

    // }

}