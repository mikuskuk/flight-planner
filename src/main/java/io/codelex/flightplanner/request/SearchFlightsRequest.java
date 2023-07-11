package io.codelex.flightplanner.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public class SearchFlightsRequest {

    @NotBlank
    private final String from;
    @NotBlank
    private final String to;
    @NotNull
    private final String departureDate;

    public SearchFlightsRequest(String from, String to, String departureDate) {
        this.from = from;
        this.to = to;
        this.departureDate = departureDate;
    }


    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SearchFlightsRequest that)) return false;
        return Objects.equals(from, that.from) && Objects.equals(to, that.to) && Objects.equals(departureDate, that.departureDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, departureDate);
    }
}
