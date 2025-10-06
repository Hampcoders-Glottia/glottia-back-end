package com.hampcoders.glottia.platform.api.venue.interfaces.rest.transform;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.hampcoders.glottia.platform.api.venue.domain.model.commands.CreatePartnerCommand;
import com.hampcoders.glottia.platform.api.venue.domain.model.entities.TableVenue;
import com.hampcoders.glottia.platform.api.venue.domain.model.entities.Venue;
import com.hampcoders.glottia.platform.api.venue.domain.model.valueobjects.MinimumConsumption;
import com.hampcoders.glottia.platform.api.venue.domain.model.valueobjects.TableCapacity;
import com.hampcoders.glottia.platform.api.venue.interfaces.rest.resources.CreatePartnerResource;
import com.hampcoders.glottia.platform.api.venue.interfaces.rest.resources.CreateTableResource;
import com.hampcoders.glottia.platform.api.venue.interfaces.rest.resources.CreateVenueResource;

/**
 * Assembler para convertir CreatePartnerResource a CreatePartnerCommand.
 * Sigue el patrón de transformación Resource -> Command.
 */
public class CreatePartnerCommandFromResourceAssembler {

    private CreatePartnerCommandFromResourceAssembler() {
        // Constructor privado para clase utilitaria
    }

    public static CreatePartnerCommand toCommandFromResource(CreatePartnerResource resource) {
        // Convertir los venues del resource a entidades Venue
        List<Venue> venues = resource.initialVenues() != null
                ? resource.initialVenues().stream()
                    .map(CreatePartnerCommandFromResourceAssembler::toVenueEntity)
                    .collect(Collectors.toList())
                : Collections.emptyList();

        return new CreatePartnerCommand(
                resource.userId(),
                resource.legalName(),
                venues
        );
    }

    private static Venue toVenueEntity(CreateVenueResource resource) {
        List<TableVenue> tables = resource.initialTables() != null
                ? resource.initialTables().stream()
                    .map(CreatePartnerCommandFromResourceAssembler::toTableEntity)
                    .collect(Collectors.toList())
                : Collections.emptyList();

        return new Venue(
                resource.name(),
                resource.address(),
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

