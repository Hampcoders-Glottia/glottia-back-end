package com.hampcoders.glottia.platform.api.profiles.domain.model.commands;

public record UpdatePartnerContactCommand(
    Long partnerId,
    String contactEmail,
    String contactPhone,
    String contactPersonName
) {
}