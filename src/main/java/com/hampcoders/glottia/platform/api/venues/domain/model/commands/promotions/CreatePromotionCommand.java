package com.hampcoders.glottia.platform.api.venues.domain.model.commands.promotions;

import com.hampcoders.glottia.platform.api.venues.domain.model.valueobjects.PartnerId;

public record CreatePromotionCommand(PartnerId partnerId, String name, String description, String promotionType, Integer value) {
    
}
