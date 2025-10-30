package com.hampcoders.glottia.platform.api.venues.interfaces.rest.transform.fromresource;

import com.hampcoders.glottia.platform.api.venues.domain.model.commands.tables.ReserveTableCommand;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.tables.ReserveTableResource;

public class ReserveTableCommandFromResourceAssembler {
    public static ReserveTableCommand toCommandFromResource(Long tableId, ReserveTableResource resource) {
        return new ReserveTableCommand(
            tableId,
            resource.reservationDate()
        );
    }
}
