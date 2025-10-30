package com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.venues;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;

public record CreatePartnerVenueRegistryResource(@NotNull @JsonProperty("partnerId") Long partnerId) {
}