package com.hampcoders.glottia.platform.api.profiles.application.internal.eventhandlers;

import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.hampcoders.glottia.platform.api.profiles.domain.services.ProfileSeedCommandService;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.SeedSubscriptionStatusCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.SeedBusinessRolesCommand;

/**
 * ProfilesReadyEventHandler class
 * This class is used to handle the ApplicationReadyEvent for the Profiles Bounded Context.
 */
@Service
public class ProfilesReadyEventHandler {

    private final ProfileSeedCommandService profileSeedCommandService;
    private static final Logger LOGGER = LoggerFactory.getLogger(ProfilesReadyEventHandler.class);

    public ProfilesReadyEventHandler(ProfileSeedCommandService profileSeedCommandService) {
        this.profileSeedCommandService = profileSeedCommandService;
    }


    /**
     * Handle the Application Ready Event for Profiles Bounded Context.
     * This method is used to seed the reference data for the Profiles Bounded Context.
     * It seeds Business Roles and Subscription Statuses.
     */
    @EventListener
    public void on(ApplicationReadyEvent event) {
        var applicationName = event.getApplicationContext().getId();
        LOGGER.info("Starting ApplicationReadyEvent handler for Profiles Bounded Context: {}", applicationName, currentTimeStamp());

        var SeedBusinessRolesCommand = new SeedBusinessRolesCommand();
        var SeedSubscriptionStatusCommand = new SeedSubscriptionStatusCommand();

        profileSeedCommandService.handle(SeedBusinessRolesCommand);
        profileSeedCommandService.handle(SeedSubscriptionStatusCommand);
        LOGGER.info("Completed ApplicationReadyEvent handler for Profiles Bounded Context: {}", applicationName, currentTimeStamp());
    }

    private Timestamp currentTimeStamp() {
        return new Timestamp(System.currentTimeMillis());
    }
}
