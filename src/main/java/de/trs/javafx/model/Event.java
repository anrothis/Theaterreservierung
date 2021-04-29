package de.trs.javafx.model;

import java.sql.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Datenentität zum anlegen der Theateraufführungen
 * 
 * @Annotationen sorgen für das automatische generieren der Konstruktoren und
 *               der Getter/Setter
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Event {

    // TODO: annotieren der Columns
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "EVENT_ID", unique = true, nullable = false)
    private Long id;
    private String name;
    // @Column(nullable = false)
    private Date performanceDate;
    private String location;
    @ManyToMany(targetEntity = Mitglied.class, fetch = FetchType.EAGER)
    @JoinTable(name = "RESERVATION", joinColumns = @JoinColumn(name = "EVENT_ID"), inverseJoinColumns = @JoinColumn(name = "MEMBER_ID"))
    private List<Mitglied> reservationsList;

    public Event(String name, Date performanceDate, String location) {
        this.name = name;
        this.performanceDate = performanceDate;
        this.location = location;
    }

    public Event(String name, Date performanceDate, String location, List<Mitglied> reservationsList) {
        this.name = name;
        this.performanceDate = performanceDate;
        this.location = location;
        this.reservationsList = reservationsList;
    }

    /** reduzierter Konstruktor zu Testzwecken */
    public Event(String name, String location) {
        this.name = name;
        this.location = location;
    }

}
