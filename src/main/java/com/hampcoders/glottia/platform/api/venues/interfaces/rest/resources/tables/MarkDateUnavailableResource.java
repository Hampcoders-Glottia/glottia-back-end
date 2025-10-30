package com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.tables;

import java.time.LocalDate;

public record MarkDateUnavailableResource(
    LocalDate date
) {}
