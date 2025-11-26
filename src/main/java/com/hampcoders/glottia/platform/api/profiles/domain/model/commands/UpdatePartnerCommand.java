package com.hampcoders.glottia.platform.api.profiles.domain.model.commands;

public record UpdatePartnerCommand(
        Long profileId,
        String legalName,
        String businessName,
        String taxId,
        String contactEmail,
        String contactPhone,
        String contactPersonName,
        String description
) {
}