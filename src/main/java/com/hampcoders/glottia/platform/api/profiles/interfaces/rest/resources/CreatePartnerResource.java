package com.hampcoders.glottia.platform.api.profiles.interfaces.rest.resources;

public record CreatePartnerResource(
    Long profileId,
    String legalName,
    String businessName,
    String taxId,
    String contactEmail,
    String contactPhone,
    String contactPersonName,
    String description,
    String websiteUrl,
    String instagramHandle
) {
}
