package io.codelex.flightplanner.service;

import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.repository.FlightRepository;
import io.codelex.flightplanner.request.AddFlightRequest;
import io.codelex.flightplanner.request.PageResult;
import io.codelex.flightplanner.request.SearchFlightsRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

@Service
public class FlightService {

    private final FlightRepository flightRepository;

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public synchronized Flight addFlight(AddFlightRequest request) {

        String departureAirport = request.getFrom().getAirport().trim();
        String arrivalAirport = request.getTo().getAirport().trim();
        String departureCity = request.getFrom().getCity().trim();
        String arrivalCity = request.getTo().getCity().trim();
        String departureCountry = request.getFrom().getCountry().trim();
        String arrivalCountry = request.getTo().getCountry().trim();

        if (departureAirport.equalsIgnoreCase(arrivalAirport) &&
         departureCity.equalsIgnoreCase(arrivalCity) &&
         departureCountry.equalsIgnoreCase(arrivalCountry)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Departure and arrival airports are the same!");
        }

        LocalDateTime departureTime = request.getDepartureTime();
        LocalDateTime arrivalTime = request.getArrivalTime();

        if (arrivalTime.isBefore(departureTime) || arrivalTime.isEqual(departureTime)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid departure and arrival time!");
        }

        List<Flight> existingFlights = flightRepository.listFlights();
        boolean flightExists = existingFlights.stream().anyMatch(flight -> flight.getFrom().equals(request.getFrom()) &&
                flight.getTo().equals(request.getTo()) &&
                flight.getCarrier().equals(request.getCarrier()) &&
                flight.getDepartureTime().isEqual(departureTime) &&
                flight.getArrivalTime().isEqual(arrivalTime)
        );

        if (flightExists) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This flight already exists!");
        }

        long newId = existingFlights.stream().mapToLong(Flight::getId).max().orElse(0) + 1;
        Flight flight = new Flight(newId, request.getFrom(), request.getTo(), request.getCarrier(), departureTime, arrivalTime);

        flightRepository.saveFlight(flight);
        return flight;
    }
    public void clearAllFlights() {
        flightRepository.deleteAllFlights();
    }

    public synchronized void deleteFlight(long id) {
        flightRepository.deleteFlight(id);
    }

    public List<Airport> searchAirport(String search) {
        List<Airport> airportsFrom = flightRepository.listFlights().stream()
                .map(Flight::getFrom)
                .toList()
                .stream().filter(airport -> airport.getAirport().toLowerCase().contains(search.toLowerCase().trim()) ||
                         airport.getCity().toLowerCase().contains(search.toLowerCase().trim()) ||
                         airport.getCountry().toLowerCase().contains(search.toLowerCase().trim()))
                .toList();

        List<Airport> airportsTo = flightRepository.listFlights().stream()
                .map(Flight::getTo)
                .toList()
                .stream().filter(airport -> airport.getAirport().toLowerCase().contains(search.toLowerCase().trim()) ||
                        airport.getCity().toLowerCase().contains(search.toLowerCase().trim()) ||
                        airport.getCountry().toLowerCase().contains(search.toLowerCase().trim()))
                .toList();

        return Stream.concat(airportsFrom.stream(), airportsTo.stream()).toList();
    }

    public PageResult<Flight> searchFlights(SearchFlightsRequest request) throws IllegalArgumentException{
        if (request.getFrom().equalsIgnoreCase(request.getTo())) {
            throw new IllegalArgumentException("Departure and arrival airport are the same!");
        }
        List<Flight> flights = flightRepository.listFlights().stream()
                .filter(flight -> flight.getFrom().getAirport().equalsIgnoreCase(request.getFrom()))
                .filter(flight -> flight.getTo().getAirport().equalsIgnoreCase(request.getTo()))
                .filter(flight -> flight.getDepartureTime().toLocalDate().isEqual(LocalDate.parse(request.getDepartureDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                .toList();

        return new PageResult<>(0, flights.size(), flights);
    }

    public Flight fetchFlight(long id) throws IllegalArgumentException{
        try {
            return flightRepository.listFlights().stream()
                    .filter(flight -> flight.getId() == id)
                    .toList()
                    .get(0);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to fetch flight!");
        }
    }


}
