package com.hampcoders.glottia.platform.api.partner.interfaces.rest.transform;

import com.hampcoders.glottia.platform.api.partner.domain.model.commands.AddVenueToPartnerCommand;
import com.hampcoders.glottia.platform.api.partner.domain.model.entities.TableVenue;
import com.hampcoders.glottia.platform.api.partner.domain.model.valueobjects.MinimumConsumption;
import com.hampcoders.glottia.platform.api.partner.domain.model.valueobjects.TableCapacity;
import com.hampcoders.glottia.platform.api.partner.interfaces.rest.resources.AddVenueToPartnerResource;
import com.hampcoders.glottia.platform.api.partner.interfaces.rest.resources.CreateTableResource;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Assembler para convertir AddVenueToPartnerResource a AddVenueToPartnerCommand.
 */
public class AddVenueToPartnerCommandFromResourceAssembler {

    private AddVenueToPartnerCommandFromResourceAssembler() {
        // Constructor privado para clase utilitaria
    }

    public static AddVenueToPartnerCommand toCommandFromResource(Long partnerId, AddVenueToPartnerResource resource) {
        List<TableVenue> tables = resource.initialTables() != null
                ? resource.initialTables().stream()
                    .map(AddVenueToPartnerCommandFromResourceAssembler::toTableEntity)
                    .collect(Collectors.toList())
                : Collections.emptyList();

        return new AddVenueToPartnerCommand(
                partnerId,
                resource.venueName(),
                resource.venueAddress(),
                tables
        );
    }

    private static TableVenue toTableEntity(CreateTableResource resource) {
        return new TableVenue(
                resource.name(),
                new TableCapacity(resource.capacity()),
                new MinimumConsumption(resource.minimumConsumption(), resource.name()),
                resource.startReservationTime(),
                resource.endReservationTime()
        );
    }
}

