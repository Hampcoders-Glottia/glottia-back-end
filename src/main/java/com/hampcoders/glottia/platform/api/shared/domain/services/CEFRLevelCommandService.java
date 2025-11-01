package com.hampcoders.glottia.platform.api.shared.domain.services;

import com.hampcoders.glottia.platform.api.shared.domain.model.commands.SeedCEFRLevelsCommand;

public interface CEFRLevelCommandService {
    void handle(SeedCEFRLevelsCommand command);
}
