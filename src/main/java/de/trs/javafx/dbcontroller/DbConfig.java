package de.trs.javafx.dbcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.trs.javafx.model.CsvHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class DbConfig {

    @Autowired
    private DbService dbService;

    @Bean
    CommandLineRunner commandLineRunner(MemberRepository memberRepository, EventRepository eventRepository) {
        return args -> {
            // Mitglied sebastian = new Mitglied("Sebastian", "Riedel", "9c");
            // Mitglied juli = new Mitglied("Juli", "Zwei", "9b");
            // Mitglied dom = new Mitglied("Dom", "Wi", "23g");
            // memberRepository.saveAll(List.of(sebastian, juli, dom));
            // Event event1 = new Event("Superman", "Immenstadt");
            // Event event2 = new Event("Shakespear", "Immenstadt");
            // Event event3 = new Event("Spiderman", "Sonthofen");
            // eventRepository.saveAll(List.of(event1, event2, event3));

            memberRepository.saveAll(CsvHandler.PareseMemberList.getMemberfromCSV("members.csv", true));
            eventRepository.saveAll(CsvHandler.ParseEventList.getEventfromCSV("eventsLong.csv", true));
            // log.info("DB READ getMembers() --- " + dbService.getMembers().toString());
            // log.info("DB READ getMemberName() --- " + dbService.getMemberName());
            log.info("DB READ getMemberBySeat(9c) --- " + dbService.getMemberBySeat(""));
            log.info("DB READ getEventsByName(Superman)--- "
                    + dbService.getEventsByName("Perks of Being a Wallflower, A").toString());
            // log.info("DB READ getEventsByName() --- " + dbService.getEvents());

        };
    }
}
