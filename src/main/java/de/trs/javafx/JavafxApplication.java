package de.trs.javafx;

import de.trs.javafx.create.CreateScene;
import de.trs.javafx.searchpane.SearchPaneScene;
import javafx.application.Application;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JavafxApplication extends Application {

    private SearchPaneScene searchPaneScene = null;
    private CreateScene createScene = null;
    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Theater Reservation Service");
        this.primaryStage.setScene(getSearchPaneScene());
        this.primaryStage.show();
    }

    private SearchPaneScene getSearchPaneScene() {
        if (searchPaneScene == null) {
            searchPaneScene = new SearchPaneScene();
            searchPaneScene.addEventFilter(SceneChangeEvent.GO_TO_CREATE_SCENE, e -> {
                log.info("BEGIN Changeing scene to create");
                primaryStage.setScene(getCreateScene());
                log.info("END Changeing scene to create");

            });
        }
        return searchPaneScene;
    }

    public void setSearchPaneScene(SearchPaneScene searchPaneScene) {
        this.searchPaneScene = searchPaneScene;
    }

    public CreateScene getCreateScene() {
        if (createScene == null) {
            createScene = new CreateScene();
            createScene.addEventFilter(SceneChangeEvent.GO_TO_OVERVIEW_SCENE, e -> {
                log.info("BEGIN changeing scene to search");
                primaryStage.setScene(getSearchPaneScene());
                searchPaneScene.refreshDate();
                log.info("END changeing scene to search");
            });
        }
        return createScene;
    }

    public void setCreateScene(CreateScene createScene) {
        this.createScene = createScene;
    }

}
