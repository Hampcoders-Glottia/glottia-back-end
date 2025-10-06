package com.hampcoders.glottia.platform.api.profiles.domain.services;

import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.CreatePartnerCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.UpdatePartnerContactCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.UpdatePartnerSubscriptionCommand;

public interface PartnerCommandService {
    Long handle(CreatePartnerCommand command);
    void handle(UpdatePartnerContactCommand command);
    void handle(UpdatePartnerSubscriptionCommand command);
}
