package de.trs.javafx.dbcontroller;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

    /** not working jet */
    @Query(value = "select * from mitglied m inner join reservation r on m.member_id = r.member_id  where r.Event_id = ?1)", nativeQuery = true)
    List<Mitglied> getReservationList(Long id);

    /**
     * Member deletion sql request from specific event reservaionlist by member id
     * 
     * @param id
     */
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM RESERVATION WHERE MEMBER_ID = :ID", nativeQuery = true)
    void deleteMemberformReservations(@Param("ID") Long id);

}
