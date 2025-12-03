package com.hampcoders.glottia.platform.api.encounters.application.internal.eventhandlers;

import com.hampcoders.glottia.platform.api.encounters.domain.model.commands.AwardPointsCommand;
import com.hampcoders.glottia.platform.api.encounters.domain.model.events.EncounterCreatedEvent;
import com.hampcoders.glottia.platform.api.encounters.domain.model.events.LearnerCheckedInEvent;
import com.hampcoders.glottia.platform.api.encounters.domain.services.LoyaltyAccountCommandService;
import com.hampcoders.glottia.platform.api.encounters.infrastructure.persistence.jpa.repository.LoyaltyAccountRepository;
import com.hampcoders.glottia.platform.api.venues.interfaces.acl.VenuesContextFacade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class LoyaltyPointsEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoyaltyPointsEventHandler.class);

    private final LoyaltyAccountRepository loyaltyAccountRepository;
    private final VenuesContextFacade venuesContextFacade;

    public LoyaltyPointsEventHandler(LoyaltyAccountRepository loyaltyAccountRepository,
                                     VenuesContextFacade venuesContextFacade) {
        this.loyaltyAccountRepository = loyaltyAccountRepository;
        this.venuesContextFacade = venuesContextFacade;
    }

    @EventListener
    public void on(EncounterCreatedEvent event) {
        LOGGER.info("Awarding points for encounter creation. CreatorId: {}", event.creatorId().learnerId());
        
        var account = loyaltyAccountRepository.findByLearnerId(event.creatorId())
            .orElseThrow(() -> new IllegalStateException("Loyalty account not found"));
        
        String venueName = venuesContextFacade.fetchVenueName(event.venueId().venueId());
        if (venueName == null || venueName.isBlank()) {
            venueName = "Venue desconocido";
        }
        
        account.awardPointsForCreation(event.encounterId(), event.venueId(), venueName);
        loyaltyAccountRepository.save(account);
    }

    @EventListener
    public void on(LearnerCheckedInEvent event) {
        LOGGER.info("Awarding points for check-in. LearnerId: {}", event.learnerId().learnerId());
        
        var account = loyaltyAccountRepository.findByLearnerId(event.learnerId())
            .orElseThrow(() -> new IllegalStateException("Loyalty account not found"));
        
        String venueName = venuesContextFacade.fetchVenueName(event.venueId().venueId());
        if (venueName == null || venueName.isBlank()) {
            venueName = "Venue desconocido";
        }
        
        account.awardPointsForAttendance(10, event.encounterId(), event.venueId(), venueName);
        loyaltyAccountRepository.save(account);
    }
}