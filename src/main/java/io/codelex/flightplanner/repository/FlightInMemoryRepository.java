package io.codelex.flightplanner.repository;

import io.codelex.flightplanner.domain.Flight;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class FlightInMemoryRepository {

    private final List<Flight> savedFlights = new ArrayList<>();

    public synchronized void saveFlight(Flight flight) {
        savedFlights.add(flight);
    }

    public List<Flight> listFlights() {
        return savedFlights;
    }

    public synchronized void deleteAllFlights() {
        savedFlights.clear();
    }

    public synchronized void deleteFlight(long id) {
        savedFlights.remove(savedFlights.stream().filter(flight1 -> flight1.getId() == id).findAny().orElse(null));
    }

}
