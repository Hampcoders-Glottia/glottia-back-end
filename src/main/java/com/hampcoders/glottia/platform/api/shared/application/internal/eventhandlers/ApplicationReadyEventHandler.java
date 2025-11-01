package com.hampcoders.glottia.platform.api.shared.application.internal.eventhandlers;

import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.hampcoders.glottia.platform.api.shared.domain.model.commands.SeedCEFRLevelsCommand;
import com.hampcoders.glottia.platform.api.shared.domain.model.commands.SeedLanguagesCommand;
import com.hampcoders.glottia.platform.api.shared.domain.services.CEFRLevelCommandService;
import com.hampcoders.glottia.platform.api.shared.domain.services.LanguageCommandService;

/**
 * Event handler for Application Ready Event.
 * This handler seeds the reference data for CEFR Levels and Languages
 */
@Service
public class ApplicationReadyEventHandler {
    private final CEFRLevelCommandService cefrLevelCommandService;
    private final LanguageCommandService languageCommandService;
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationReadyEventHandler.class);

    public ApplicationReadyEventHandler(CEFRLevelCommandService cefrLevelCommandService,
                                        LanguageCommandService languageCommandService) {
        this.cefrLevelCommandService = cefrLevelCommandService;
        this.languageCommandService = languageCommandService;
    }
    
    /**
     * Handle the Application Ready Event.
     * This method is used to seed the reference data for CEFR Levels and Languages.
     * @param event
     */
    @EventListener
    public void onApplicationReady(ApplicationReadyEvent event) {
        var applicationName = event.getApplicationContext().getApplicationName();
        LOGGER.info("Starting ApplicationReadyEvent handler for Application: {}", applicationName, currentTimeStamp());

        var seedCEFRLevelsCommand = new SeedCEFRLevelsCommand();
        var seedLanguagesCommand = new SeedLanguagesCommand();
        cefrLevelCommandService.handle(seedCEFRLevelsCommand);
        languageCommandService.handle(seedLanguagesCommand);

        LOGGER.info("Completed ApplicationReadyEvent handler for Application: {}", applicationName, currentTimeStamp());
    }

        private Timestamp currentTimeStamp() {
        return new Timestamp(System.currentTimeMillis());
    }
}
