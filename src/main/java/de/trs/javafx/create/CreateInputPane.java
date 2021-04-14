package de.trs.javafx.create;

import de.trs.javafx.model.Mitglied;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;

public class CreateInputPane extends GridPane {

    private Text vnameText;
    private TextField vnameInput;
    private Text nnameText;
    private TextField nnameInput;
    private Text seatText;
    private TextField seatInput;

    public CreateInputPane() {
        initiate();
    }

    private void initiate() {
        getChildren().addAll(getVnameText(), getVnameInput(), getNnameText(), getNnameInput(), getSeatText(),
                getSeatInput());
        ColumnConstraints c1 = new ColumnConstraints(200);
        ColumnConstraints c2 = new ColumnConstraints(300);
        c2.setHgrow(Priority.ALWAYS);
        getColumnConstraints().addAll(c1, c2);
        // getColumnConstraints().add(0);
        int row = 0;
        int column = 0;
        setRowIndex(getVnameText(), row);
        setRowIndex(getVnameInput(), row++);
        setColumnIndex(getVnameText(), column++);
        setColumnIndex(getVnameInput(), column);
        column = 0;
        setRowIndex(getNnameText(), row);
        setRowIndex(getNnameInput(), row++);
        setColumnIndex(getNnameText(), column++);
        setColumnIndex(getNnameInput(), column);
        column = 0;
        setRowIndex(getSeatText(), row);
        setRowIndex(getSeatInput(), row++);
        setColumnIndex(getSeatText(), column++);
        setColumnIndex(getSeatInput(), column);
    }

    private Text getVnameText() {
        if (vnameText == null) {
            vnameText = new Text("Vorname");
        }
        return vnameText;
    }

    private void setVnameText(Text vnameText) {
        this.vnameText = vnameText;
    }

    private TextField getVnameInput() {
        if (vnameInput == null) {
            vnameInput = new TextField();
        }
        return vnameInput;
    }

    private void setVnameInput(TextField vnameInput) {
        this.vnameInput = vnameInput;
    }

    private Text getNnameText() {
        if (nnameText == null) {
            nnameText = new Text("Nachname");
        }
        return nnameText;
    }

    private void setNnameText(Text nnameText) {
        this.nnameText = nnameText;
    }

    private TextField getNnameInput() {
        if (nnameInput == null) {
            nnameInput = new TextField();
        }
        return nnameInput;
    }

    private void setNnameInput(TextField nnameInput) {
        this.nnameInput = nnameInput;
    }

    private Text getSeatText() {
        if (seatText == null) {
            seatText = new Text("Sitzplatz");
        }
        return seatText;
    }

    private void setSeatText(Text seatText) {
        this.seatText = seatText;
    }

    private TextField getSeatInput() {
        if (seatInput == null) {
            seatInput = new TextField();
        }
        return seatInput;
    }

    private void setSeatInput(TextField seatInput) {
        this.seatInput = seatInput;
    }

    public Mitglied memeber() {
        return new Mitglied(getVnameInput().getText(), getNnameInput().getText(), getSeatInput().getText());
    }
}
