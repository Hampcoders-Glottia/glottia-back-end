package com.hampcoders.glottia.platform.api.shared.application.internal.commandservices;

import java.util.Arrays;

import org.springframework.stereotype.Service;

import com.hampcoders.glottia.platform.api.shared.domain.model.commands.SeedLanguagesCommand;
import com.hampcoders.glottia.platform.api.shared.domain.model.entities.Language;
import com.hampcoders.glottia.platform.api.shared.domain.model.valueobjects.Languages;
import com.hampcoders.glottia.platform.api.shared.domain.services.LanguageCommandService;
import com.hampcoders.glottia.platform.api.shared.infrastructure.persistence.jpa.repository.LanguageRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LanguageCommandServiceImpl implements LanguageCommandService {

    private final LanguageRepository languageRepository;

    @Override
    public void handle(SeedLanguagesCommand command) {
        Arrays.stream(Languages.values()).forEach(language -> {
            if(!languageRepository.existsByName(language)) {
                languageRepository.save(new Language(Languages.valueOf(language.name())));
            }
        });
    }

}
