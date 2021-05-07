package de.trs.javafx.dbcontroller;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.trs.javafx.model.CsvHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * Configuration class for testing
 */
@Slf4j
@Configuration
public class DbConfig {

    @Bean
    CommandLineRunner commandLineRunner(MemberRepository memberRepository, EventRepository eventRepository) {
        return args -> {

            // memberRepository.saveAll(CsvHandler.ParseMemberList.getMemberfromCSV("members.csv",
            // true));
            memberRepository.saveAll(CsvHandler.ParseMemberList.getMemberfromCSV("members.csv", true, ','));
            eventRepository.saveAll(CsvHandler.ParseEventList.getEventfromCSV("eventsLong.csv", true, ','));

        };
    }
}
