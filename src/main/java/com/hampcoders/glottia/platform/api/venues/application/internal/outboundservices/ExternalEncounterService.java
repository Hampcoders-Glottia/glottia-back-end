package com.hampcoders.glottia.platform.api.venues.application.internal.outboundservices;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hampcoders.glottia.platform.api.encounters.interfaces.acl.EncountersContextFacade;

/**
 * External service to interact with Encounters Bounded Context.
 * This is part of the Anti-Corruption Layer (ACL) for the Venues BC.
 * Transforms primitive types from the facade into domain-friendly structures.
 */
@Service("venuesExternalEncounterService")
public class ExternalEncounterService {

    private final EncountersContextFacade encountersContextFacade;

    public ExternalEncounterService(EncountersContextFacade encountersContextFacade) {
        this.encountersContextFacade = encountersContextFacade;
    }

    /**
     * Fetches encounter statistics for a venue within a date range.
     * Transforms primitive data from ACL into structured records.
     * 
     * @param venueId   The venue ID
     * @param startDate Start date (inclusive)
     * @param endDate   End date (inclusive)
     * @return List of daily encounter statistics
     */
    public List<DailyEncounterStats> fetchEncounterStatsByVenueAndDateRange(
            Long venueId, 
            LocalDate startDate, 
            LocalDate endDate
    ) {
        List<Object[]> rawData = encountersContextFacade.getEncounterCountsByVenueAndDateRange(
            venueId, startDate, endDate
        );

        if (rawData == null || rawData.isEmpty()) {
            return new ArrayList<>();
        }

        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        
        return rawData.stream()
            .map(row -> new DailyEncounterStats(
                LocalDate.parse((String) row[0], formatter),
                (long) row[1],
                (long) row[2]
            ))
            .toList();
    }

    /**
     * Record to hold daily encounter statistics.
     * This is a simple DTO used within the Venues BC.
     */
    public record DailyEncounterStats(
        LocalDate date,
        long scheduledCount,
        long completedCount
    ) {}
}