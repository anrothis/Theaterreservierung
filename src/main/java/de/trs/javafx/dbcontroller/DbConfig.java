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

            memberRepository.saveAll(CsvHandler.ParseMemberList.getMemberfromCSV("members.csv", true));
            eventRepository.saveAll(CsvHandler.ParseEventList.getEventfromCSV("eventsLong.csv", true));

            // log.info("DB READ getMembers() --- " + dbService.getMembers().toString());
            // log.info("DB READ getMemberName() --- " + dbService.getMemberName());
            // log.info("DB READ getEventsByName() --- " + dbService.getEvents());

            // log.info("DB READ findDate(21-01-01:21-02-01) --- " + dbService.getDate());
            // log.info("DB READ getMemberBySeat(9c) --- " + dbService.getMemberBySeat(""));
            // log.info("DB READ findByDate(1641260465000) --- " +
            // dbService.getEventsByDate(new Date(1641260465000L)));
            // log.info("DB READ getEventsByName(Superman)--- "
            // + dbService.getEventsByName("Perks of Being a Wallflower, A").toString());

        };
    }
}
