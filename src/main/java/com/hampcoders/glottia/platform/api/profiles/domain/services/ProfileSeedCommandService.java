package com.hampcoders.glottia.platform.api.profiles.domain.services;

import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.SeedBusinessRolesCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.SeedSubscriptionStatusCommand;

/**
 * Service interface for seeding reference data
 */
public interface ProfileSeedCommandService {
    void handle(SeedBusinessRolesCommand command);
    void handle(SeedSubscriptionStatusCommand command);
}