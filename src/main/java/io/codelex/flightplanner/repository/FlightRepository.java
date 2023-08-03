package io.codelex.flightplanner.repository;

import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.domain.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {

    @Query("SELECT f FROM Flight f WHERE f.from.airport = :fromAirport " +
            "AND f.to.airport = :toAirport " +
            "AND f.carrier = :carrier " +
            "AND f.departureTime = :departureTime " +
            "AND f.arrivalTime = :arrivalTime ")
    List<Flight> findFlightByCriteria(String fromAirport, String toAirport, String carrier, LocalDateTime departureTime, LocalDateTime arrivalTime);

    @Query("SELECT f FROM Flight f " +
            "WHERE LOWER(f.from.airport) = LOWER(:from) " +
            "AND LOWER(f.to.airport) = LOWER(:to) " +
            "AND f.departureTime >= :departureStartTime " +
            "AND f.departureTime <= :departureEndTime ")
    List<Flight> searchFlight(String from, String to, LocalDateTime departureStartTime, LocalDateTime departureEndTime);

    @Query("SELECT DISTINCT f.from FROM Flight f WHERE LOWER(f.from.airport) LIKE %:search%" +
            "OR LOWER(f.from.city) LIKE %:search%" +
            "OR LOWER(f.from.country) LIKE %:search%")
    List<Airport> findAirportByCriteria(String search);
}
