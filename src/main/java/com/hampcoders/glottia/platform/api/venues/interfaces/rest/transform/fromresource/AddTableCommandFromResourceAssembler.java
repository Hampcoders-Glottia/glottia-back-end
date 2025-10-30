package com.hampcoders.glottia.platform.api.venues.interfaces.rest.transform.fromresource;

import com.hampcoders.glottia.platform.api.venues.domain.model.commands.tables.AddTableCommand;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.tables.AddTableResource;

public class AddTableCommandFromResourceAssembler {

    public static AddTableCommand toCommandFromResource(Long tableId, AddTableResource resource){
        return new AddTableCommand(
                tableId,
                resource.tableNumber(),
                resource.capacity(),
                resource.tableType(),
                resource.tableStatus()
        );
    }
}
