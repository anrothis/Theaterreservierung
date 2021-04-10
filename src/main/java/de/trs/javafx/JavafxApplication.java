package de.trs.javafx;

import java.io.IOException;

import org.springframework.stereotype.Component;

import de.trs.javafx.memberview.MemberViewScene;
// import de.trs.javafx.create.CreateScene;
// import de.trs.javafx.memberview.MemberViewScene;
// import de.trs.javafx.searchpane.SearchPaneScene;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JavafxApplication extends Application {

    // private SearchPaneScene searchPaneScene = null;
    // private CreateScene createScene = null;
    // private Stage primaryStage;

    /**
     * Second Window integration
     * 
     * @Autowired erstellt automatisch eine Instanz der Classe und und setellt diese
     *            zur Verfügung
     */
    // @Autowired
    private MemberViewScene memberViewScene;

    // @Autowired
    private Stage memberViewStage;

    /**
     * Standardisierte Methode welche von der JavaFX Parent-Klasse Application
     * überschrieben werden muss
     * 
     * Leitet den Startvorgang und das laden der Oberflächenelemente ein. start()
     * muss in jeder FX Applikation vorhanden sein
     */
    @Override
    public void start(Stage primaryStage) {
        try {

            log.info("START Stage");
            this.memberViewStage = primaryStage;
            this.memberViewStage.setTitle("Theater Reservation Service");
            log.info("START Scene");
            this.memberViewStage.setScene(getMemberViewScene());
            log.info("START .show()");
            this.memberViewStage.show();
            log.info("FINISHED start()");
        } catch (Exception e) {
            log.error("Could not finisch start()", e);
            log.error(e.getMessage());
        }
        /**
         * Beim versuch die Applikation zu schließen wird logout() ausgeführt. Das Event
         * wird bei nicht OK consumiert und der Vorgang somit abgebrochen.
         */
        this.memberViewStage.setOnCloseRequest(event -> {
            logout(memberViewStage);
            event.consume();
        });
    }

    /**
     * Getter Methode des MemberViewScene Objects
     * 
     * Prüft, ob bereits ein Object der zugrunde liegenden Klasse erstellt wurde,
     * initialisiert diese gegebenen falls und gibt sie zurück
     * 
     * @return Object vom Typ MemberViewScene
     * @throws IOException
     */
    private MemberViewScene getMemberViewScene() throws IOException {
        if (memberViewScene == null) {
            memberViewScene = new MemberViewScene();
        }
        return memberViewScene;
    }

    /**
     * Sicherheitsabfrage beim beenden der Applikation
     * 
     * @param stage - Übergabeparameter ist die aktuelle Stage. Wird vom Alert
     *              Buttontype.OK zurückgegeben wird der Befehl stage.close
     *              ausgeführt. Andernfalls wird in der start() Funktion das Closing
     *              Event consumiert.
     */
    public void logout(Stage stage) {
        log.info("CLOSING initiated");
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Closing App?");
        alert.setHeaderText("Sie sind dabei die App zu beenden.");
        alert.setContentText("Fortfahren? ");

        if (alert.showAndWait().get() == ButtonType.OK) {

            log.info("CLOSING confirmed");
            stage.close();
        } else {
            log.info("CLOSING aborted");
        }
    }

    /**
     * In der main() Methode wird lediglich der Start vorbereitet und ruft im
     * Anschluss start() auf
     */
    public static void main(String[] args) {
        launch(args);
    }

    // @Override
    // public void start(Stage primaryStage) throws Exception {
    // this.primaryStage = primaryStage;
    // this.primaryStage.setTitle("Theater Reservation Service");
    // this.primaryStage.setScene(getSearchPaneScene());
    // this.primaryStage.show();
    // }

    // private SearchPaneScene getSearchPaneScene() {
    // if (searchPaneScene == null) {
    // searchPaneScene = new SearchPaneScene();
    // searchPaneScene.addEventFilter(SceneChangeEvent.GO_TO_CREATE_SCENE, e -> {
    // log.info("BEGIN Changeing scene to create");
    // primaryStage.setScene(getCreateScene());
    // log.info("END Changeing scene to create");
    // });
    // }
    // return searchPaneScene;
    // }

    // public CreateScene getCreateScene() {
    // if (createScene == null) {
    // createScene = new CreateScene();
    // createScene.addEventFilter(SceneChangeEvent.GO_TO_OVERVIEW_SCENE, e -> {
    // log.info("BEGIN changeing scene to search");
    // primaryStage.setScene(getSearchPaneScene());
    // searchPaneScene.refreshDate();
    // log.info("END changeing scene to search");
    // });
    // }
    // return createScene;
    // }
}
