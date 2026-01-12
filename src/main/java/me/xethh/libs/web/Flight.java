package me.xethh.libs.web;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Data
@Document(collection = "flights")
// Indexing carrier + flightNo + flightDate is recommended for flight lookups
@CompoundIndex(name = "flight_unique_idx", def = "{'carrierCode': 1, 'flightNo': 1, 'flightDate': 1}")
public class Flight {

    @Id
    private String id; // MongoDB will auto-generate this

    private String carrierCode;
    private String flightNo;
    private String origin;
    private String destination;

    private LocalDate flightDate; // Maps to "2025-12-15"

    private String aircraftType;
    private String aircraftCatg;
    private String flightStatus;
    private Integer departureTime;
    private Integer arrivalTime;
    private boolean flightCancelled;

    // Time-zone aware timestamps
    private OffsetDateTime STD;
    private OffsetDateTime ETD;
    private OffsetDateTime ATD;
    private OffsetDateTime STA;
    private OffsetDateTime ETA;
    private OffsetDateTime ATA;
}