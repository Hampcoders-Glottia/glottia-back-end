package com.hampcoders.glottia.platform.api.venues.interfaces.rest.transform.fromentity;

import com.hampcoders.glottia.platform.api.venues.domain.model.aggregates.TableRegistry;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.tables.TableRegistryResource;

public class TableRegistryResourceFromEntityAssembler {
    public static TableRegistryResource toResourceFromEntity(TableRegistry entity) {
        var tableResources = entity.getTableList().getTables().stream()
            .map(TableResourceFromEntityAssembler::toResourceFromEntity)
            .toList();
        return new TableRegistryResource(
            entity.getId(),
            entity.getVenue().getId(),
            tableResources
        );
    }
}