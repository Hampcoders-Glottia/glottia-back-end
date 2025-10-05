package com.hampcoders.glottia.platform.api.profiles.domain.services;

import com.hampcoders.glottia.platform.api.profiles.domain.model.aggregates.Partner;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.CreatePartnerCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.UpdatePartnerContactCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.UpdatePartnerSubscriptionCommand;

import java.util.Optional;

/**
 * Service interface for Partner-specific domain operations
 */
public interface PartnerCommandService {
    Long handle(CreatePartnerCommand command);
    Optional<Partner> handle(UpdatePartnerContactCommand command);
    Optional<Partner> handle(UpdatePartnerSubscriptionCommand command);
}