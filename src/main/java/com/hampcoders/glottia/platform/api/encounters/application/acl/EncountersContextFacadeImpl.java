package com.hampcoders.glottia.platform.api.encounters.application.acl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;
import com.hampcoders.glottia.platform.api.encounters.application.internal.queryservices.LoyaltyAccountQueryServiceImpl;
import com.hampcoders.glottia.platform.api.encounters.domain.model.commands.CreateLoyaltyAccountCommand;
import com.hampcoders.glottia.platform.api.encounters.domain.model.queries.GetEncounterStatsByVenueQuery;
import com.hampcoders.glottia.platform.api.encounters.domain.model.queries.GetLoyaltyAccountByLearnerQuery;
import com.hampcoders.glottia.platform.api.encounters.domain.model.queries.HasConflictingEncounterQuery;
import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.LearnerId;
import com.hampcoders.glottia.platform.api.encounters.domain.services.EncounterQueryService;
import com.hampcoders.glottia.platform.api.encounters.domain.services.LoyaltyAccountCommandService;
import com.hampcoders.glottia.platform.api.encounters.interfaces.acl.EncountersContextFacade;

@Service
public class EncountersContextFacadeImpl implements EncountersContextFacade {

    private static final DateTimeFormatter ISO_DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    private final LoyaltyAccountQueryServiceImpl loyaltyAccountQueryServiceImpl;
    private final LoyaltyAccountCommandService loyaltyAccountCommandService;
    private final EncounterQueryService encounterQueryService;

    public EncountersContextFacadeImpl(
            LoyaltyAccountCommandService loyaltyAccountCommandService,
            EncounterQueryService encounterQueryService,
            LoyaltyAccountQueryServiceImpl loyaltyAccountQueryServiceImpl) {
        this.loyaltyAccountCommandService = loyaltyAccountCommandService;
        this.encounterQueryService = encounterQueryService;
        this.loyaltyAccountQueryServiceImpl = loyaltyAccountQueryServiceImpl;
    }

    @Override
    public boolean hasConflictingEncounter(Long learnerId, LocalDateTime scheduledAt) {
        try {
            var query = new HasConflictingEncounterQuery(
                    new LearnerId(learnerId),
                    scheduledAt);
            return encounterQueryService.handle(query);
        } catch (Exception e) {
            return false; // Default to no conflict on error
        }
    }

    @Override
    public Long createLoyaltyAccount(Long learnerId) {
        var command = new CreateLoyaltyAccountCommand(new LearnerId(learnerId));
        return loyaltyAccountCommandService.handle(command);
    }

    @Override
    public boolean existsLoyaltyAccountByLearnerId(Long learnerId) {
        try {
            var query = new GetLoyaltyAccountByLearnerQuery(new LearnerId(learnerId));
            return loyaltyAccountQueryServiceImpl.handle(query).isPresent();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get encounter statistics by venue for a date range.
     * Returns domain value objects (internal use within BC).
     * 
     * @param query The query with venue ID and date range
     * @return List of DailyEncounterStat value objects
     */
    @Override
    public List<Object[]> getEncounterCountsByVenueAndDateRange(Long venueId, LocalDate startDate, LocalDate endDate) {
        var query = new GetEncounterStatsByVenueQuery(venueId, startDate, endDate);
        
        var domainStats = encounterQueryService.handle(query);
        
        // Transform to primitive types at ACL boundary
        return domainStats.stream()
            .map(stat -> new Object[]{
                stat.date().format(ISO_DATE_FORMATTER),  // String: "2025-12-01"
                stat.scheduledCount(),                    // long
                stat.completedCount()                     // long
            })
            .toList();
    }

}
