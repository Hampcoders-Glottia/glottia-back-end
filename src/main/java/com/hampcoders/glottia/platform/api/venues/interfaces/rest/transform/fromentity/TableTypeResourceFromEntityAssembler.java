package com.hampcoders.glottia.platform.api.venues.interfaces.rest.transform.fromentity;

import com.hampcoders.glottia.platform.api.venues.domain.model.entities.TableType;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.types.TableTypeResource;

public class TableTypeResourceFromEntityAssembler {
    public static TableTypeResource toResourceFromEntity(TableType entity) {
        return new TableTypeResource(
            entity.getId(),
            entity.getStringTypeName()
        );
    }
}
