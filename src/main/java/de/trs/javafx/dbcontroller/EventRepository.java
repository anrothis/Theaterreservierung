package de.trs.javafx.dbcontroller;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import de.trs.javafx.model.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByName(String name);

    List<Event> findByPerformanceDate(Date performanceDate);

    List<Event> findByLocation(String location);

    @Query("SELECT name, location FROM Event")
    List<String> getEventsAsStrings();
}
