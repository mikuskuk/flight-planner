package io.codelex.flightplanner.service;

import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.repository.FlightRepository;
import io.codelex.flightplanner.request.PageResult;
import io.codelex.flightplanner.request.SearchFlightsRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {

    @Mock
    private FlightRepository flightRepository;
    @InjectMocks
    private FlightService flightService;
    private final Airport airportRix = new Airport("Latvia", "Riga", "RIX");
    private final Airport airportLgw = new Airport("UK", "London", "LGW");
    LocalDateTime departureTime = LocalDateTime.of(2023, 7, 12, 12, 0);
    private final List<Flight> flights = Arrays.asList(
            new Flight(1, airportRix, airportLgw, "AirBaltic", departureTime, departureTime.plusHours(2)),
            new Flight(2, airportRix, airportLgw, "AirBaltic", departureTime.plusHours(3), departureTime.plusHours(5))
    );


    @Test
    void testSearchFlights() {
        String from = "RIX";
        String to = "LGW";
        String departureDate = "2023-07-12";
        SearchFlightsRequest request = new SearchFlightsRequest(from, to, departureDate);

        Mockito.when(flightRepository.listFlights()).thenReturn(flights);

        PageResult<Flight> result = flightService.searchFlights(request);

        Mockito.verify(flightRepository, Mockito.times(1)).listFlights();

        Assertions.assertEquals(2, result.getTotalItems());
        Assertions.assertEquals(flights, result.getItems());
    }

    @Test
    void testSearchFlightsWithSameAirports() {
        SearchFlightsRequest request = new SearchFlightsRequest("RIX", "RIX", "2023-07-12 12:00");

        Assertions.assertThrows(IllegalArgumentException.class, () -> flightService.searchFlights(request), "Departure and arrival airport are the same!");
    }

    @Test
    void testFetchFlights() {
        Mockito.when(flightRepository.listFlights()).thenReturn(flights);

        Flight result = flightService.fetchFlight(1);

        Mockito.verify(flightRepository, Mockito.times(1)).listFlights();
        Assertions.assertEquals(flights.get(0), result);
    }

    @Test
    void testFetchFlightsNotFound() {
        Mockito.when(flightRepository.listFlights()).thenReturn(flights);

        Assertions.assertThrows(IllegalArgumentException.class, () -> flightService.fetchFlight(3), "Failed to fetch flight!");
    }
}