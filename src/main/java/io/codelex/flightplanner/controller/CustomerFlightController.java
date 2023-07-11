package io.codelex.flightplanner.controller;

import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.request.PageResult;
import io.codelex.flightplanner.request.SearchFlightsRequest;
import io.codelex.flightplanner.service.FlightService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CustomerFlightController {

    private final FlightService flightService;

    public CustomerFlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping("/airports")
    @ResponseStatus(HttpStatus.OK)
    public List<Airport> searchAirports(@RequestParam String search) {
        return flightService.searchAirport(search);
    }

    @GetMapping("/flights/{id}")
    public Flight findFlightById(@PathVariable long id) {
        try {
            return flightService.fetchFlight(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Can not find flight by ID!");
        }
    }

    @PostMapping("/flights/search")
    @ResponseStatus(HttpStatus.OK)
    public PageResult<Flight> searchFlights(@RequestBody @Valid SearchFlightsRequest request) {
        try {
            return flightService.searchFlights(request);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can not find flight!");
        }
    }



}
