package com.hampcoders.glottia.platform.api.profiles.domain.model.commands;

public record UpdatePartnerSubscriptionCommand(
    Long partnerId,
    Long subscriptionStatusId
) {
}