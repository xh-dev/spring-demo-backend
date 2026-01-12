package me.xethh.libs.web;

import me.xethh.libs.l2cache.L2Cache;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

public interface FlightRepository extends MongoRepository<Flight, String> {
    @L2Cache
    List<Flight> findAll();
    List<Flight> findByFlightNoAndOriginAndFlightDate(String flightNo, String origin, LocalDate flightDate);
    @L2Cache
    List<Flight> findByFlightNoAndOriginNotAndFlightDate(String flightNo, String origin, LocalDate flightDate);
    @L2Cache
    List<Flight> findByOriginAndDestinationAndFlightDate(String from, String to, LocalDate flightDate);
    @L2Cache
    List<Flight> findByOriginAndFlightDateEqualsAndDepartureTimeBetween(String origin, LocalDate flightDate, Integer departureTimeAfter, Integer departureTimeBefore);
    @L2Cache
    List<Flight> findByDestinationAndFlightDateEqualsAndArrivalTimeBetween(String destination, LocalDate flightDate, Integer arrivalTimeAfter, Integer arrivalTimeBefore);
}