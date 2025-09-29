package com.hampcoders.glottia.platform.api.iam.interfaces.rest.transform;

import com.hampcoders.glottia.platform.api.iam.domain.model.commands.SignInCommand;
import com.hampcoders.glottia.platform.api.iam.interfaces.rest.resources.SignInResource;

public class SignInCommandFromResourceAssembler {

    public static SignInCommand toCommandFromResource(SignInResource signInResource) {
        return new SignInCommand(signInResource.username(), signInResource.password());
    }
}