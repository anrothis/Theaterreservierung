package de.trs.javafx.model;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * DataHandler übernimmt das lesen und schrieben der Daten
 */
public enum DataHandler {
    INSTANCE;

    private List<Member> member = null;

    private List<Member> getMember() {
        if (member == null) {
            member = new ArrayList<Member>();
            member.add(new Member("Seb", "Ried", "3d"));
            member.add(new Member("Jul", "Zwei", "6g"));

        }
        return member;
    }

    public void createMember(Member member) {
        getMember().add(member);
    }

    public void deleteMember(Member member) {
        getMember().remove(member);
    }

    /**
     * ObservableList wird von JavaFX TableView erwartet
     */
    public ObservableList<Member> membersAsObservableList() {
        ObservableList<Member> observableList = FXCollections.observableList(getMember());
        return observableList;

    }

    private void setMember(List<Member> member) {
        this.member = member;
    }

}