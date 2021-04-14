package de.trs.javafx.dbcontroller;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import de.trs.javafx.model.Mitglied;

/**
 * Repositiory stellt die möglichen Database Query Funktionen bereit.
 * 
 * Durch erwietern mit dem JpaRepository werden ein Reihe von Standardabfragen
 * berietgestellt, wie zum Beistpiel findAll() oder
 */

@Repository
public interface MemberRepository extends JpaRepository<Mitglied, Long> {

    /**
     * Datenbankabfrage durch die im JPA Repository bereitgestellten Funktionen
     * 
     * @Query Annotation gibt die Möglichkeit individuelle SQL Anfragen zu
     *        definieren.
     * @return
     */
    @Query("SELECT m.vName,m.nName FROM Mitglied m")
    List<String> listUsers();

    /**
     * Datenbankabfrage mit automatisch generierter SQL Abfrage
     * 
     * Das JPA Repository erstellt automatisch aus dem Funktionsnamen eine
     * entsprechende Anfrage. Hier wird eine Abfrage erstellt, welche alle
     * Mitglieder in einer Liste zurück gibt, die mit dem gelieferten String in der
     * Entityvariabel seat übereinstimmen.
     * 
     * @param seat - Entity Parameter seat
     * @return
     */
    Mitglied findBySeatOrderBySeatAsc(String seat);
}
