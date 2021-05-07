package de.trs.javafx.dbcontroller;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import de.trs.javafx.model.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    /**
     * 
     * @param performanceDate
     * @return
     */
    List<Event> findByPerformanceDate(Date performanceDate);

    /**
     * Event sql request to delete reservationlist for specific event id
     * 
     * @param id
     */
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM RESERVATION WHERE EVENT_ID = :ID", nativeQuery = true)
    void deleteEventformReservations(@Param("ID") Long id);

}
