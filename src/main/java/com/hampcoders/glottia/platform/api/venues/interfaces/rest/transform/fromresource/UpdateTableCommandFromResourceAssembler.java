package com.hampcoders.glottia.platform.api.venues.interfaces.rest.transform.fromresource;

import com.hampcoders.glottia.platform.api.venues.domain.model.commands.tables.UpdateTableCommand;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.tables.UpdateTableResource;

public class UpdateTableCommandFromResourceAssembler {
    public static UpdateTableCommand toCommandFromResource(Long tableId, UpdateTableResource resource) {
        return new UpdateTableCommand(
            tableId,
            resource.capacity(),
            resource.tableTypeId()
        );
    }
}
