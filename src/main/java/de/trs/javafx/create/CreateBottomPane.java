package de.trs.javafx.create;

import de.trs.javafx.SceneChangeEvent;
import de.trs.javafx.model.DataHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CreateBottomPane extends HBox {
    private Button cancle = null;
    private Button ok = null;
    private final CreateInputPane createInputPane;

    public CreateBottomPane(CreateInputPane createInputPane) {
        initiate();
        this.createInputPane = createInputPane;
    }

    public void initiate() {
        getChildren().addAll(getCancle(), getOk());
    }

    public Button getCancle() {
        if (cancle == null) {
            cancle = new Button("Abbrechen");
            cancle.setOnAction(e -> {
                log.info("Anlegen abgebrochen");
                fireEvent(new SceneChangeEvent(SceneChangeEvent.GO_TO_OVERVIEW_SCENE));
            });
        }
        return cancle;
    }

    public void setCancle(Button cancle) {
        this.cancle = cancle;
    }

    public Button getOk() {
        if (ok == null) {
            ok = new Button("Bestätigen");
            ok.setOnAction(e -> {
                log.info("Mitglied angelegt");
                DataHandler.INSTANCE.createMitglied(createInputPane.memeber());
                fireEvent(new SceneChangeEvent(SceneChangeEvent.GO_TO_OVERVIEW_SCENE));
            });
        }
        return ok;
    }

    public void setOk(Button ok) {
        this.ok = ok;
    }

}
