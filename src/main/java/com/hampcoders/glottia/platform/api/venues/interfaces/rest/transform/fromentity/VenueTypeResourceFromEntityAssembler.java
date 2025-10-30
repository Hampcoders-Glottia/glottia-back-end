package com.hampcoders.glottia.platform.api.venues.interfaces.rest.transform.fromentity;

import com.hampcoders.glottia.platform.api.venues.domain.model.entities.VenueType;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.types.VenueTypeResource;

public class VenueTypeResourceFromEntityAssembler {

    public static VenueTypeResource toResourceFromEntity(VenueType entity) {
        return new VenueTypeResource(
            entity.getId(),
            entity.getStringTypeName()
        );

    }
}
