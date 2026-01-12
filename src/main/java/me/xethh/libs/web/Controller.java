package me.xethh.libs.web;

import me.xethh.libs.web.annotations.GlobalCache;
import me.xethh.libs.web.annotations.WithCrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping("api/v1")
public class Controller {
    static final String HKG = "HKG";

    public Controller(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public enum TimeRange {
        All, EarlyMorning, Morning, Afternoon, Evening;
        public int getStart(){
            return switch (this){
                case All, EarlyMorning -> 0;
                case Morning -> 601;
                case Afternoon -> 1201;
                case Evening -> 1801;
            };
        }
        public int getEnd(){
            return switch (this){
                case All, Evening -> 2359;
                case EarlyMorning -> 600;
                case Morning -> 1200;
                case Afternoon -> 1800;
            };
        }
        public static TimeRange fromInteger(Integer value) {
            if(value==0){
                return All;
            } else if(value==1){
                return EarlyMorning;
            } else if (value == 2){
                return Morning;
            } else if (value == 3){
                return Afternoon;
            } else if (value ==4){
                return Evening;
            } else {
                throw new RuntimeException("Fail to match");
            }
        }
    }

    public enum SearchStatus {
        Departing, Arriving;
        public static SearchStatus fromString(String status) {
            return switch (status){
                case "Departing" -> Departing;
                case "Arriving" -> Arriving;
                default -> throw new RuntimeException("Failed to extract the SearchStatus");
            };
        }
    }

    final FlightRepository flightRepository;

    @GlobalCache
    @WithCrossOrigin
    @GetMapping("/search/by-airport/{airport}/{status}/{date}/{options}")
    public List<Flight> searchByAirport(
            @PathVariable("airport") String airport,
            @PathVariable("status") String status,
            @PathVariable("date") String date,
            @PathVariable("options") String options
    ) {
        final var statusEnum = SearchStatus.fromString(status);
        final var formater = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return Arrays.stream(options.split(",")).map(Integer::parseInt)
                .map(TimeRange::fromInteger)
                .map(range -> switch (statusEnum) {
                    case Arriving -> flightRepository.findByDestinationAndFlightDateEqualsAndArrivalTimeBetween(airport, LocalDate.parse(date, formater), range.getStart(), range.getEnd());
                    case Departing -> flightRepository.findByOriginAndFlightDateEqualsAndDepartureTimeBetween(airport, LocalDate.parse(date, formater), range.getStart(), range.getEnd());
                })
                .flatMap(List::stream)
                .toList();
    }

    @GlobalCache
    @WithCrossOrigin
    @GetMapping("/search/by-route/{from}/{to}/{date}")
    public List<Flight> searchByFlightRoute(
            @PathVariable("from") String from,
            @PathVariable("to") String to,
            @PathVariable("date") String date
    ) {
        final var formater = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return flightRepository.findByOriginAndDestinationAndFlightDate(
                from,
                to,
                LocalDate.parse(date, formater)
        );
    }

    @GlobalCache
    @WithCrossOrigin
    @GetMapping("/search/by-number/{number}/{status}/{date}")
    public List<Flight> searchByFlightNumber(
            @PathVariable("number") String number,
            @PathVariable("status") String status,
            @PathVariable("date") String date
    ) {
        final var statusEnum = SearchStatus.fromString(status);
        final var formater = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return switch (statusEnum) {
            case Departing -> flightRepository.findByFlightNoAndOriginAndFlightDate(number, Controller.HKG, LocalDate.parse(date, formater));
            case Arriving -> flightRepository.findByFlightNoAndOriginNotAndFlightDate(number, Controller.HKG, LocalDate.parse(date, formater));
            case null -> throw new RuntimeException("Search status not as expected");
        };
    }

    @GlobalCache
    @WithCrossOrigin
    @GetMapping("/flight/airports")
    public List<String> flightsAirports(
    ) {
        return Stream.concat(
                        flightRepository.findAll()
                                .stream()
                                .map(Flight::getOrigin)
                                .distinct(),
                        flightRepository.findAll()
                                .stream()
                                .map(Flight::getDestination)
                                .distinct()

                )
                .toList()
                    ;
    }
}
