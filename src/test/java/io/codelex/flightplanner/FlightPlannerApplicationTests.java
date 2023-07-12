package io.codelex.flightplanner;

import io.codelex.flightplanner.controller.AdminFlightController;
import io.codelex.flightplanner.controller.TestApiController;
import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.repository.FlightRepository;
import io.codelex.flightplanner.request.AddFlightRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class FlightPlannerApplicationTests {

	@Autowired
	AdminFlightController adminFlightController;
	@Autowired
	TestApiController testApiController;
	@Autowired
	FlightRepository flightRepository;


	@AfterEach
	void clearData() {
		flightRepository.deleteAllFlights();
	}

	private final AddFlightRequest flightRequest = new AddFlightRequest(
			new Airport("Latvia", "Riga", "RIX"),
			new Airport("UK", "London", "LGW"),
			"AirBaltic",
			"2023-10-01 12:00",
			"2023-10-01 15:00"
	);

	@Test
	void addFlights() {
		Flight addedFlight = adminFlightController.addFlight(flightRequest);

		Assertions.assertEquals(flightRequest.getFrom(), addedFlight.getFrom());
		Assertions.assertEquals(flightRequest.getTo(), addedFlight.getTo());
		Assertions.assertEquals(flightRequest.getCarrier(), addedFlight.getCarrier());
		Assertions.assertEquals(flightRequest.getDepartureTime(), addedFlight.getDepartureTime());
		Assertions.assertEquals(flightRequest.getArrivalTime(), addedFlight.getArrivalTime());
	}

	@Test
	void clearFlights() {
		Flight addedFlight = adminFlightController.addFlight(flightRequest);

		testApiController.clear();

		List<Flight> flights = flightRepository.listFlights();

		Assertions.assertTrue(flights.isEmpty());
	}

}
