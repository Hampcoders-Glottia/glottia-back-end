package com.hampcoders.glottia.platform.api.encounters.application.internal.eventhandlers;

import com.hampcoders.glottia.platform.api.encounters.domain.model.commands.AwardPointsCommand;
import com.hampcoders.glottia.platform.api.encounters.domain.model.events.EncounterCreatedEvent;
import com.hampcoders.glottia.platform.api.encounters.domain.model.events.LearnerCheckedInEvent;
import com.hampcoders.glottia.platform.api.encounters.domain.services.LoyaltyAccountCommandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class LoyaltyPointsEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoyaltyPointsEventHandler.class);
    private final LoyaltyAccountCommandService loyaltyAccountCommandService;

    public LoyaltyPointsEventHandler(LoyaltyAccountCommandService loyaltyAccountCommandService) {
        this.loyaltyAccountCommandService = loyaltyAccountCommandService;
    }

    @EventListener
    public void on(EncounterCreatedEvent event) {
        LOGGER.info("Awarding points for encounter creation. CreatorId: {}, EncounterId: {}", 
            event.creatorId().learnerId(), event.encounterId());
        
        var command = new AwardPointsCommand(event.creatorId(), 5, "CREATION");
        loyaltyAccountCommandService.handle(command);
        
        LOGGER.info("Points awarded successfully for encounter creation.");
    }

    @EventListener
    public void on(LearnerCheckedInEvent event) {
        LOGGER.info("Awarding points for check-in. LearnerId: {}, EncounterId: {}", 
            event.learnerId().learnerId(), event.encounterId());
        
        var command = new AwardPointsCommand(event.learnerId(), 10, "ATTENDANCE");
        loyaltyAccountCommandService.handle(command);
        
        LOGGER.info("Points awarded successfully for check-in.");
    }
}