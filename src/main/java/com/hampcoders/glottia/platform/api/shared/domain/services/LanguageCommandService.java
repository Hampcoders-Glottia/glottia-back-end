package com.hampcoders.glottia.platform.api.shared.domain.services;

import com.hampcoders.glottia.platform.api.shared.domain.model.commands.SeedLanguagesCommand;

public interface LanguageCommandService {
    void handle(SeedLanguagesCommand command);

}
