package io.codelex.flightplanner.service;

import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.repository.AirportRepository;
import io.codelex.flightplanner.repository.FlightRepository;
import io.codelex.flightplanner.request.AddFlightRequest;
import io.codelex.flightplanner.request.PageResult;
import io.codelex.flightplanner.request.SearchFlightsRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FlightDatabaseService implements FlightService {

    private final FlightRepository flightRepository;
    private final AirportRepository airportRepository;

    public FlightDatabaseService(FlightRepository flightRepository, AirportRepository airportRepository) {
        this.flightRepository = flightRepository;
        this.airportRepository = airportRepository;
    }

    @Override
    public Flight addFlight(AddFlightRequest request) {
        validateAirports(request);

        LocalDateTime departureTime = request.getDepartureTime();
        LocalDateTime arrivalTime = request.getArrivalTime();
        validateDepartureArrivalTime(departureTime, arrivalTime);

        validateUniqueFlight(request);

        Flight flight = new Flight(request.getFrom(), request.getTo(), request.getCarrier(), departureTime, arrivalTime);
        airportRepository.save(flight.getFrom());
        airportRepository.save(flight.getTo());

        return flightRepository.save(flight);
    }

    private void validateAirports(AddFlightRequest request) {
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
    }

    private void validateDepartureArrivalTime(LocalDateTime departureTime, LocalDateTime arrivalTime) {
        if (arrivalTime.isBefore(departureTime) || arrivalTime.isEqual(departureTime)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid departure and arrival time!");
        }
    }

    private void validateUniqueFlight(AddFlightRequest request) {
        List<Flight> existingFlights = flightRepository.findFlightByCriteria(
                request.getFrom().getAirport(),
                request.getTo().getAirport(),
                request.getCarrier(),
                request.getDepartureTime(),
                request.getArrivalTime()
        );

        if (existingFlights.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This flight already exists!");
        }
    }

    @Override
    public void clearAllFlights() {
        flightRepository.deleteAll();
    }

    @Override
    public synchronized void deleteFlight(long id) {
        if (flightRepository.existsById(id)) {
            flightRepository.deleteById(id);
        }
    }

    @Override
    public List<Airport> searchAirport(String search) {
        return flightRepository.findAirportByCriteria(search.toLowerCase().trim());
    }

    @Override
    public PageResult<Flight> searchFlights(SearchFlightsRequest request) {
        if (request.getFrom().equalsIgnoreCase(request.getTo())) {
            throw new IllegalArgumentException("Departure and arrival airport are the same!");
        }

        LocalDate departureDate = LocalDate.parse(request.getDepartureDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDateTime departureStartTime = departureDate.atStartOfDay();
        LocalDateTime departureEndTime = departureDate.atTime(LocalTime.MAX);

        List<Flight> flights = flightRepository.searchFlight(
                request.getFrom(),
                request.getTo(),
                departureStartTime,
                departureEndTime
        );

        return new PageResult<>(0, flights.size(), flights);
    }

    @Override
    public Flight fetchFlight(long id) {
        return flightRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Failed to fetch flight!"));
    }
}
