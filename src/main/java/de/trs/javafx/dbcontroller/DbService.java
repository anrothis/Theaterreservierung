package de.trs.javafx.dbcontroller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.trs.javafx.model.Event;
import de.trs.javafx.model.Mitglied;
import javafx.collections.ObservableList;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
// @Transactional
public class DbService {

    private final MemberRepository memberRepository;
    private final EventRepository eventRepository;

    @Autowired
    public DbService(MemberRepository memberRepository, EventRepository eventRepository) {
        this.memberRepository = memberRepository;
        this.eventRepository = eventRepository;
    }

    /**
     * Member Database requests
     */

    public List<Mitglied> getMembers() {
        return memberRepository.findAll();
    }

    public List<Mitglied> getReservationTable(Event event) {

        List<Mitglied> reservationList = memberRepository.getReservationList(event.getId());
        return reservationList;
    }

    public void addMember(Mitglied mitglied) {
        memberRepository.save(mitglied);
    }

    @Transactional
    public void updateMember(Mitglied mitglied, Long id) {
        // TODO: update Memeber
    }

    // @Modifying
    // @Transactional()
    public void deleteMember(Mitglied selectedItem) {
        log.info("DELETE MEMBER");

        memberRepository.deleteMemberformReservations(selectedItem.getId());
        memberRepository.delete(selectedItem);
    }

    public List<Mitglied> getReservationList(Event event) {

        Event eventNew = eventRepository.getOne(event.getId());
        List<Mitglied> reservationList = event.getReservationsList();
        return reservationList;
    }

    /**
     * Event Database requests
     */
    public void deleteEvent(Event selectedEvent) {
        log.info("DELETE EVENT");
        // eventRepository.deleteEventformReservations(selectedEvent.getId());
        eventRepository.delete(selectedEvent);
    }

    public List<Event> getEventsByDate(Date date) {
        return eventRepository.findByPerformanceDate(date);
    }

    public void addEvent(Event event) {
        eventRepository.save(event);
    }

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
     * Testfunktionen
     */
    public List<String> getMemberName() {
        return memberRepository.listUsers();
    }

    public Mitglied getMemberBySeat(String nName) {
        return memberRepository.findBySeatOrderBySeatAsc(nName);
    }

    public List<Event> getEvents() {
        return eventRepository.findAll();
    }

    public List<Event> getDate() {
        return eventRepository.findDate();
    }

    public List<Event> getEventsByName(String name) {
        return eventRepository.findByName(name);
    }

    public List<Event> getEventsByLocation(String location) {
        return eventRepository.findByLocation(location);
    }

}
