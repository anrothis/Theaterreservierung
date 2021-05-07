package de.trs.javafx.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entität zum Abbilden der Sitznummerierung
 * 
 * --- tbd ---
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Seating {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    // @OneToOne
    private String seat;

    public Seating(String seat) {
        this.seat = seat;
    }
}
