package io.codelex.flightplanner.repository;

import io.codelex.flightplanner.domain.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AirportRepository extends JpaRepository<Airport, String> {
    @Query("SELECT a FROM Airport a " +
            "WHERE LOWER(a.airport) = LOWER(:search) " +
            "OR LOWER(a.city) = LOWER(:search) " +
            "OR LOWER(a.country) = LOWER(:search)")
    List<Airport> findAirportByCriteria(@Param("search") String search);


}
