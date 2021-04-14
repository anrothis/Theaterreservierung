package de.trs.javafx.model;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Event implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private Long id;
    private String name;
    // @Column(nullable = false)
    private Date performanceDate;
    private String location;
    @ManyToOne
    private Mitglied reservationsList;

    /** reduzierter Konstruktor zu Testzwecken */
    public Event(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public Event(String name, Date performanceDate, String location) {
        this.name = name;
        this.performanceDate = performanceDate;
        this.location = location;
    }

}