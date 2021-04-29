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

    List<Event> findByName(String name);

    List<Event> findByPerformanceDate(Date performanceDate);

    // @Query("SELECT e FROM Event e where performanceDate between '2021-01-01' and
    // '2021-02-01'")
    @Query("SELECT e FROM Event e where performanceDate = '2021-01-01'")
    List<Event> findDate();

    List<Event> findByLocation(String location);

    @Query("SELECT name, location FROM Event")
    List<String> getEventsAsStrings();

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM RESERVATION WHERE EVENT_ID = :ID", nativeQuery = true)
    void deleteEventformReservations(@Param("ID") Long id);

}
