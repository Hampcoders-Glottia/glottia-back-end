package com.hampcoders.glottia.platform.api.iam.domain.services;

import com.hampcoders.glottia.platform.api.iam.domain.model.commands.SeedRolesCommand;

public interface RoleCommandService {
    void handle(SeedRolesCommand command);
}
