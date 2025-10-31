package com.hampcoders.glottia.platform.api.profiles.interfaces.rest.resources;

public record CreateLearnerResource(
    Long profileId,
    AddressResource address
) {
}