package io.codelex.flightplanner.request;

import io.codelex.flightplanner.domain.Airport;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class AddFlightRequest {

    @Valid
    @NotNull
    private Airport from;
    @Valid
    @NotNull
    private Airport to;
    @NotBlank
    private String carrier;
    @NotNull
    private final LocalDateTime departureTime;
    @NotNull
    private final LocalDateTime arrivalTime;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public AddFlightRequest(Airport from, Airport to, String carrier, String departureTime, String arrivalTime) {
        this.from = from;
        this.to = to;
        this.carrier = carrier;
        this.departureTime = LocalDateTime.parse(departureTime, formatter);
        this.arrivalTime = LocalDateTime.parse(arrivalTime, formatter);
    }

    public Airport getFrom() {
        return from;
    }

    public Airport getTo() {
        return to;
    }

    public String getCarrier() {
        return carrier;
    }

    public @NotNull LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public @NotNull LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AddFlightRequest that)) return false;
        return Objects.equals(from, that.from) && Objects.equals(to, that.to) && Objects.equals(carrier, that.carrier) && Objects.equals(departureTime, that.departureTime) && Objects.equals(arrivalTime, that.arrivalTime) && Objects.equals(formatter, that.formatter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, carrier, departureTime, arrivalTime, formatter);
    }
}
