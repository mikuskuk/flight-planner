package io.codelex.flightplanner.configuration;

import io.codelex.flightplanner.repository.AirportRepository;
import io.codelex.flightplanner.repository.FlightInMemoryRepository;
import io.codelex.flightplanner.repository.FlightRepository;
import io.codelex.flightplanner.service.FlightDatabaseService;
import io.codelex.flightplanner.service.FlightInMemoryService;
import io.codelex.flightplanner.service.FlightService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlightPlannerConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "flightplanner", name = "service.version",havingValue = "in-memory")
    public FlightService getInMemoryVersion(FlightInMemoryRepository flightInMemoryRepository) {
        return new FlightInMemoryService(flightInMemoryRepository);
    }

    @Bean
    @ConditionalOnProperty(prefix = "flightplanner", name = "service.version", havingValue = "database")
    public FlightService getDatabaseVersion(FlightRepository flightRepository, AirportRepository airportRepository) {
        return new FlightDatabaseService(flightRepository, airportRepository);
    }

}
