package de.trs.javafx.dbcontroller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.trs.javafx.model.Event;
import de.trs.javafx.model.Mitglied;
import javafx.collections.ObservableList;
import lombok.extern.slf4j.Slf4j;

/**
 * DbService
 * 
 * Serviceclass for userelement interaction with the datebase repository
 */
@Slf4j
@Service
public class DbService {

    private final MemberRepository memberRepository;
    private final EventRepository eventRepository;

    @Autowired
    public DbService(MemberRepository memberRepository, EventRepository eventRepository) {
        this.memberRepository = memberRepository;
        this.eventRepository = eventRepository;
    }

    /**
     * Member request of all members
     * 
     * @return List<Mitglied> of all members in database
     */
    public List<Mitglied> getMembers() {
        return memberRepository.findAll();
    }

    public List<Mitglied> getReservationTable(Event event) {

        List<Mitglied> reservationList = memberRepository.getReservationList(event.getId());
        return reservationList;
    }

    /**
     * Member additio request
     * 
     * @param mitglied new member object
     */
    public void addMember(Mitglied mitglied) {
        memberRepository.save(mitglied);
    }

    /**
     * Member update request by id
     * 
     * @param mitglied updated member object
     * @param id       id of member to update
     */
    @Transactional
    public void updateMember(Mitglied mitglied, Long id) {
        // TODO: update Memeber
    }

    /**
     * Member deletion request by member
     * 
     * @param selectedItem Member object
     */
    public void deleteMember(Mitglied selectedItem) {
        log.info("DELETE MEMBER");

        memberRepository.deleteMemberformReservations(selectedItem.getId());
        memberRepository.delete(selectedItem);
    }

    /**
     * Event request for the reservationlist by event
     * 
     * @param event Event object
     * @return List of members
     */
    public List<Mitglied> getReservationList(Event event) {

        List<Mitglied> reservationList = event.getReservationsList();
        return reservationList;
    }

    /**
     * Event deletion Database requests by Event
     */
    public void deleteEvent(Event selectedEvent) {
        log.info("DELETE EVENT");
        // eventRepository.deleteEventformReservations(selectedEvent.getId());
        eventRepository.delete(selectedEvent);
    }

    /**
     * Event List request by date
     * 
     * @param date SQL Date
     * @return Event list on date
     */
    public List<Event> getEventsByDate(Date date) {
        return eventRepository.findByPerformanceDate(date);
    }

    /**
     * Event addition to database by event
     * 
     * @param event
     */
    public void addEvent(Event event) {
        eventRepository.save(event);
    }

    /**
     * Event reservationlist update by event
     * 
     * @param event
     * @param reservationList
     */
    @Transactional
    public void updateReservationList(Event event, ObservableList<Mitglied> reservationList) {
        log.info("UPDATING RESERVATIONLIST");
        List<Mitglied> reservationArrayList = new ArrayList<>();
        for (Mitglied mitglied2 : reservationList) {
            reservationArrayList.add(mitglied2);
        }
        log.info("UPDATING RESERVATIONLIST List " + reservationArrayList);
        event.setReservationsList(reservationArrayList);
        eventRepository.saveAndFlush(event);
    }

    /**
     * Event request of all events
     * 
     * @return List<Event> of all events
     */
    public List<Event> getEvents() {
        return eventRepository.findAll();
    }
}
