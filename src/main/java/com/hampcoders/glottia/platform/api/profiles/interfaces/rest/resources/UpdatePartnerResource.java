package com.hampcoders.glottia.platform.api.profiles.interfaces.rest.resources;

public record UpdatePartnerResource(
    String contactEmail,
    String contactPhone,
    String contactPersonName,
    String description,
    String websiteUrl,
    String instagramHandle
) {
}
