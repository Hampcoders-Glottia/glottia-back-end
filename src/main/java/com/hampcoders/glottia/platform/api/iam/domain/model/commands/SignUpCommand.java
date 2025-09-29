package com.hampcoders.glottia.platform.api.iam.domain.model.commands;

import com.hampcoders.glottia.platform.api.iam.domain.model.entities.AccessRole;

import java.util.List;

public record SignUpCommand(String username, String password, List<AccessRole> roles) {
}