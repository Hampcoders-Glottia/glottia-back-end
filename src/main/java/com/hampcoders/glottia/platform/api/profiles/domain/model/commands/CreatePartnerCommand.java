package com.hampcoders.glottia.platform.api.profiles.domain.model.commands;

public record CreatePartnerCommand(
    Long profileId,
    String legalName,
    String businessName,
    String taxId,
    String contactEmail,
    String contactPhone,
    String contactPersonName,
    String description,
    Long subscriptionStatusId
) {
    public CreatePartnerCommand {
        if (profileId == null) {
            throw new IllegalArgumentException("Profile ID is required");
        }
    }
}