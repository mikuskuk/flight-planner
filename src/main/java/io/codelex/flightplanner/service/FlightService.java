package io.codelex.flightplanner.service;

import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.request.AddFlightRequest;
import io.codelex.flightplanner.request.PageResult;
import io.codelex.flightplanner.request.SearchFlightsRequest;

import java.util.List;

public interface FlightService {

    Flight addFlight(AddFlightRequest request);

    void clearAllFlights();

    void deleteFlight(long id);

    List<Airport> searchAirport(String search);

    PageResult<Flight> searchFlights(SearchFlightsRequest request);

    Flight fetchFlight(long id);

}
