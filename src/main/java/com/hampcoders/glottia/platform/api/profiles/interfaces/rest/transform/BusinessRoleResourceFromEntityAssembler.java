package com.hampcoders.glottia.platform.api.profiles.interfaces.rest.transform;

import com.hampcoders.glottia.platform.api.profiles.domain.model.entities.BusinessRole;
import com.hampcoders.glottia.platform.api.profiles.interfaces.rest.resources.BusinessRoleResource;

public class BusinessRoleResourceFromEntityAssembler {

    public static BusinessRoleResource toResource(BusinessRole entity) {
        return new BusinessRoleResource(
            entity.getId(),
            entity.getRole().name()
        );
    }
}
