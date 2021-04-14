package de.trs.javafx.dbcontroller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.trs.javafx.model.Event;
import de.trs.javafx.model.Mitglied;

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
     * used requests
     */
    public List<Mitglied> getMembers() {
        return memberRepository.findAll();
    }

    public List<Event> getEventsByDate(Date date) {
        return eventRepository.findByPerformanceDate(date);
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

    public List<Event> getEventsByName(String name) {
        return eventRepository.findByName(name);
    }

    public List<Event> getEventsByLocation(String location) {
        return eventRepository.findByLocation(location);
    }

}
