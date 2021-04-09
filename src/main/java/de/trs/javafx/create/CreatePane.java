package de.trs.javafx.create;

import javafx.scene.layout.BorderPane;

public class CreatePane extends BorderPane {

    private CreateInputPane createInputPane = null;
    private CreateBottomPane createBottomPane = null;

    public CreatePane() {
        initiate();
    }

    private void initiate() {
        setCenter(getCreateInputPane());
        setBottom(getCreateBottomPane());
        setPrefSize(200, 150);

    }

    public CreateInputPane getCreateInputPane() {
        if (createInputPane == null) {
            createInputPane = new CreateInputPane();
        }
        return createInputPane;
    }

    public void setCreateInputPane(CreateInputPane createInputPane) {
        this.createInputPane = createInputPane;
    }

    public CreateBottomPane getCreateBottomPane() {
        if (createBottomPane == null) {
            createBottomPane = new CreateBottomPane(getCreateInputPane());
        }
        return createBottomPane;
    }

    public void setCreateBottomPane(CreateBottomPane createBottomPane) {
        this.createBottomPane = createBottomPane;
    }

}
