package com.hampcoders.glottia.platform.api.iam.interfaces.rest.transform;

import com.hampcoders.glottia.platform.api.iam.domain.model.aggregates.User;
import com.hampcoders.glottia.platform.api.iam.interfaces.rest.resources.UserResource;

public class UserResourceFromEntityAssembler {

    public static UserResource toResourceFromEntity(User user) {
        return new UserResource(user.getId(), user.getUsername());
    }
}