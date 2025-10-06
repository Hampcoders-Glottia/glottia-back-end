package com.hampcoders.glottia.platform.api.partner.interfaces.rest.transform;

import com.hampcoders.glottia.platform.api.partner.domain.model.aggregates.Partner;
import com.hampcoders.glottia.platform.api.partner.domain.model.entities.TableVenue;
import com.hampcoders.glottia.platform.api.partner.domain.model.entities.Venue;
import com.hampcoders.glottia.platform.api.partner.interfaces.rest.resources.PartnerResource;
import com.hampcoders.glottia.platform.api.partner.interfaces.rest.resources.TableResource;
import com.hampcoders.glottia.platform.api.partner.interfaces.rest.resources.VenueResource;

import java.util.stream.Collectors;

/**
 * Assembler para transformar la entidad de Dominio Partner a PartnerResource.
 * Sigue el patrón de transformación Entity -> Resource.
 */
public class PartnerResourceFromEntityAssembler {

    private PartnerResourceFromEntityAssembler() {
        // Constructor privado para clase utilitaria
    }

    public static PartnerResource toResourceFromEntity(Partner entity) {
        if (entity == null) {
            return null;
        }

        return new PartnerResource(
                entity.getId(),
                entity.getUserId(),
                entity.getLegalName(),
                "ACTIVE", // Por ahora un valor fijo, puedes agregar un campo status en Partner si lo necesitas
                entity.getVenues().stream()
                        .map(PartnerResourceFromEntityAssembler::venueToResource)
                        .collect(Collectors.toList())
        );
    }

    private static VenueResource venueToResource(Venue entity) {
        if (entity == null) {
            return null;
        }
        return new VenueResource(
                entity.getId(),
                entity.getName(),
                entity.getAddress(),
                entity.getExternalVenueId(),
                entity.getTables().stream()
                        .map(PartnerResourceFromEntityAssembler::tableToResource)
                        .collect(Collectors.toList())
        );
    }

    private static TableResource tableToResource(TableVenue entity) {
        if (entity == null) {
            return null;
        }
        return new TableResource(
                entity.getId(),
                entity.getName(),
                entity.getCapacity().capacity(),
                entity.getMinimumConsumption().amount().floatValue(),
                entity.getStartReservationTime(),
                entity.getEndReservationTime(),
                entity.getStatus().name()
        );
    }
}