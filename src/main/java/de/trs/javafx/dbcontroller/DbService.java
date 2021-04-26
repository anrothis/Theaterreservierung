package de.trs.javafx.dbcontroller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.trs.javafx.model.Event;
import de.trs.javafx.model.Mitglied;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.extern.slf4j.Slf4j;

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
     * Member Database requests
     */

    public List<Mitglied> getMembers() {
        return memberRepository.findAll();
    }

    public void addMember(Mitglied mitglied) {
        memberRepository.save(mitglied);
    }

    @Transactional
    public void updateMember(Mitglied mitglied, Long id) {
        // TODO: update Memeber
    }

    public void deleteMember(Mitglied selectedItems) {
        List<Event> allEvents = eventRepository.findAll();
        if (!allEvents.isEmpty()) {
            for (Event event : allEvents) {
                List<Mitglied> reservationList = event.getReservationsList();
                for (Mitglied mitglied : reservationList) {
                    if (mitglied.equals(selectedItems)) {
                        log.info("FOUND MEMBER: " + mitglied);
                        reservationList.remove(mitglied);
                        this.updateReservationList(event, FXCollections.observableList(reservationList));
                    }
                }
            }

        }

        memberRepository.delete(selectedItems);
    }

    public List<Mitglied> getReservationList(Event event) {
        List<Mitglied> reservationList = event.getReservationsList();
        return reservationList;
    }

    /**
     * Event Database requests
     */

    public List<Event> getEventsByDate(Date date) {
        return eventRepository.findByPerformanceDate(date);
    }

    public void addEvent(Event event) {
        eventRepository.save(event);
    }

    @Transactional
    public void updateReservationList(Event event, ObservableList<Mitglied> reservationList) {
        List<Mitglied> reservationArrayList = new ArrayList<>();
        for (Mitglied mitglied2 : reservationList) {
            reservationArrayList.add(mitglied2);
        }
        event.setReservationsList(reservationArrayList);
        eventRepository.save(event);
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
