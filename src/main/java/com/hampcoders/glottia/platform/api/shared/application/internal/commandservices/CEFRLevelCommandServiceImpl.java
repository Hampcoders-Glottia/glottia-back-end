package com.hampcoders.glottia.platform.api.shared.application.internal.commandservices;

import java.util.Arrays;

import org.springframework.stereotype.Service;

import com.hampcoders.glottia.platform.api.shared.domain.model.commands.SeedCEFRLevelsCommand;
import com.hampcoders.glottia.platform.api.shared.domain.model.entities.CEFRLevel;
import com.hampcoders.glottia.platform.api.shared.domain.model.valueobjects.CEFRLevels;
import com.hampcoders.glottia.platform.api.shared.domain.services.CEFRLevelCommandService;
import com.hampcoders.glottia.platform.api.shared.infrastructure.persistence.jpa.repository.CEFRLevelRepository;

import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
public class CEFRLevelCommandServiceImpl implements CEFRLevelCommandService  {

    private final CEFRLevelRepository cefrLevelRepository;

    @Override
    public void handle(SeedCEFRLevelsCommand command) {
        Arrays.stream(CEFRLevels.values()).forEach(cefrLevel -> {
            if(!cefrLevelRepository.existsByName(cefrLevel)) {
                cefrLevelRepository.save(new CEFRLevel(CEFRLevels.valueOf(cefrLevel.name())));
            }
        });
    }

}
