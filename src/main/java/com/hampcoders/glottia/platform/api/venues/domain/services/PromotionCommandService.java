package com.hampcoders.glottia.platform.api.venues.domain.services;

import com.hampcoders.glottia.platform.api.venues.domain.model.commands.promotions.ActivateVenuePromotionCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.commands.promotions.AddPromotionToVenueCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.commands.promotions.CreatePromotionCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.commands.promotions.DeactivateVenuePromotionCommand;

public interface PromotionCommandService {
    Long handle(CreatePromotionCommand command);
    void handle(AddPromotionToVenueCommand command);
    void handle(DeactivateVenuePromotionCommand command);
    void handle(ActivateVenuePromotionCommand command);
}

