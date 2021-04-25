package de.trs.javafx.dbcontroller;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.trs.javafx.model.Event;
import de.trs.javafx.model.Mitglied;
import javafx.collections.ObservableList;

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

    public void deleteMember(Mitglied selectedItems) {
        memberRepository.delete(selectedItems);
    }

    public Optional<Mitglied> getReservationList(Event event) {
        Optional<Mitglied> reservationList = memberRepository.findById(event.getReservationsList().getId());
        if (!reservationList.isPresent()) {
            // TODO: Prüfen
        }
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

    public void updateReservationList(Event event, ObservableList<Mitglied> mitglied) {

        for (Mitglied mitglied2 : mitglied) {
            event.setReservationsList(mitglied2);
        }
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
