package com.hampcoders.glottia.platform.api.venues.application.internal.eventhandlers;

import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.hampcoders.glottia.platform.api.venues.domain.model.commands.types.SeedPromotionTypesCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.commands.types.SeedTableStatusesCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.commands.types.SeedTableTypesCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.commands.types.SeedVenueTypesCommand;
import com.hampcoders.glottia.platform.api.venues.domain.services.VenueSeedCommandService;

@Service
public class VenueReadyEventHandler {

    private final VenueSeedCommandService venueSeedCommandService;
    private static final Logger LOGGER = LoggerFactory.getLogger(VenueReadyEventHandler.class);

    public VenueReadyEventHandler(VenueSeedCommandService venueSeedCommandService) {
        this.venueSeedCommandService = venueSeedCommandService;
    }

    @EventListener
    public void on(ApplicationReadyEvent event) {
        var applicationName = event.getApplicationContext().getId();
        LOGGER.info("Venue Service '{}' is ready to handle requests.", applicationName, currentTimeStamp());

        var seedVenueTypesCommand = new SeedVenueTypesCommand();
        var seedTableTypesCommand = new SeedTableTypesCommand();
        var seedTableStatusesCommand = new SeedTableStatusesCommand();
        var seedPromotionTypesCommand = new SeedPromotionTypesCommand();

        venueSeedCommandService.handle(seedVenueTypesCommand);
        venueSeedCommandService.handle(seedTableTypesCommand);
        venueSeedCommandService.handle(seedTableStatusesCommand);
        venueSeedCommandService.handle(seedPromotionTypesCommand);
        LOGGER.info("Completed ApplicationReadyEvent handler for Venues & Promotions Management Bounded Context.", applicationName, currentTimeStamp());
    }

    private Timestamp currentTimeStamp() {
        return new Timestamp(System.currentTimeMillis());
    }
}
