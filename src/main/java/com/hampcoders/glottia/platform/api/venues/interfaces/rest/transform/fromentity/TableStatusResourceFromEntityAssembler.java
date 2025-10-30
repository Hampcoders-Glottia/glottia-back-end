package com.hampcoders.glottia.platform.api.venues.interfaces.rest.transform.fromentity;

import com.hampcoders.glottia.platform.api.venues.domain.model.entities.TableStatus;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.types.TableStatusResource;

public class TableStatusResourceFromEntityAssembler {

    public static TableStatusResource toResourceFromEntity(TableStatus entity) {
        return new TableStatusResource(
            entity.getId(),
            entity.getStringStatusName()
        );

    }
}
