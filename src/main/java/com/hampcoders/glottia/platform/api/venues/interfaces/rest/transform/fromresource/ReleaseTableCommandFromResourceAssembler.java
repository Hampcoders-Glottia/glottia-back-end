package com.hampcoders.glottia.platform.api.venues.interfaces.rest.transform.fromresource;

import com.hampcoders.glottia.platform.api.venues.domain.model.commands.tables.ReleaseTableCommand;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.tables.ReleaseTableResource;

public class ReleaseTableCommandFromResourceAssembler {
    public static ReleaseTableCommand toCommandFromResource(Long tableId, ReleaseTableResource resource) {
        return new ReleaseTableCommand(
                tableId,
                resource.releaseDate(),
                resource.startHour(),
                resource.endHour());
    }
}
