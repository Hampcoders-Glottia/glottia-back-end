package com.hampcoders.glottia.platform.api.partner.interfaces.rest.resources;

import java.util.List;

/**
 * Recurso de respuesta para la información de un Local.
 */
public record VenueResource(
        Long id,
        String name,
        String address,
        String externalVenueId,
        List<TableResource> tables
) {
}
