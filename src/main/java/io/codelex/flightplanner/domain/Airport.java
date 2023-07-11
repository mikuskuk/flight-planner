package io.codelex.flightplanner.domain;

import jakarta.validation.constraints.NotBlank;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class Airport {

    @NotBlank
    private String country;
    @NotBlank
    private String city;
    @NotBlank
    private String airport;

    public Airport(String country, String city, String airport) {
        this.country = formatInput(country);
        this.city = formatInput(city);
        this.airport = airport.toLowerCase().trim();
    }

    public Airport() {
    }

    private String formatInput(String input) {
        return Arrays.stream(input.split(""))
                .map(originalInput -> originalInput.substring(0, 1).toUpperCase() + originalInput.substring(1).toLowerCase())
                .collect(Collectors.joining(" ")).trim();
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getAirport() {
        return airport;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Airport airport1)) return false;
        return Objects.equals(country, airport1.country) && Objects.equals(city, airport1.city) && Objects.equals(airport, airport1.airport);
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, city, airport);
    }
}
