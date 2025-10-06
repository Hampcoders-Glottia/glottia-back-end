package com.hampcoders.glottia.platform.api.partner.interfaces.rest.resources;


import java.util.List;

/**
 * Recurso de respuesta para la información completa del Partner.
 */
public record PartnerResource(
        Long id,
        Long userId, // El ID que viene del BC IAM
        String legalName,
        String status,
        List<VenueResource> venues
) {
}