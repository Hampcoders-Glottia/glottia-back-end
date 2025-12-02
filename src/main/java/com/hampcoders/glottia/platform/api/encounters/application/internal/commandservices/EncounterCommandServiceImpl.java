package com.hampcoders.glottia.platform.api.encounters.application.internal.commandservices;

import com.hampcoders.glottia.platform.api.encounters.domain.model.aggregates.Encounter;
import com.hampcoders.glottia.platform.api.encounters.domain.model.commands.*;
import com.hampcoders.glottia.platform.api.encounters.domain.model.entities.AttendanceStatus;
import com.hampcoders.glottia.platform.api.encounters.domain.model.entities.EncounterStatus;
import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.AttendanceStatuses;
import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.EncounterStatuses;
import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.TableId;
import com.hampcoders.glottia.platform.api.encounters.domain.services.EncounterCommandService;
import com.hampcoders.glottia.platform.api.encounters.infrastructure.persistence.jpa.repository.AttendanceStatusRepository;
import com.hampcoders.glottia.platform.api.encounters.infrastructure.persistence.jpa.repository.EncounterRepository;
import com.hampcoders.glottia.platform.api.encounters.infrastructure.persistence.jpa.repository.EncounterStatusRepository;
import com.hampcoders.glottia.platform.api.profiles.interfaces.acl.ProfilesContextFacade;
import com.hampcoders.glottia.platform.api.shared.domain.model.valueobjects.CEFRLevels;
import com.hampcoders.glottia.platform.api.shared.domain.model.valueobjects.Languages;
import com.hampcoders.glottia.platform.api.shared.infrastructure.persistence.jpa.repository.CEFRLevelRepository;
import com.hampcoders.glottia.platform.api.shared.infrastructure.persistence.jpa.repository.LanguageRepository;
import com.hampcoders.glottia.platform.api.venues.interfaces.acl.VenuesContextFacade;

import jakarta.transaction.Transactional;

// import com.hampcoders.glottia.platform.api.venues.interfaces.acl.VenuesContextFacade; // ACL
// import com.hampcoders.glottia.platform.api.profiles.interfaces.acl.ProfilesContextFacade; // ACL

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implements the EncounterCommandService to handle commands related to
 * encounters.
 * This service manages the creation, updating, and cancellation of encounters,
 * enforcing business rules and interacting with necessary repositories.
 * It utilizes ACLs and Query Services for cross-boundary validations (to-do: in
 * progress).
 */
@Service
public class EncounterCommandServiceImpl implements EncounterCommandService {

    private final EncounterRepository encounterRepository;
    private final EncounterStatusRepository encounterStatusRepository;
    private final AttendanceStatusRepository attendanceStatusRepository;
    private final LanguageRepository languageRepository;
    private final CEFRLevelRepository cefrLevelRepository;
    private final VenuesContextFacade venuesContextFacade; // ACL
    private final ProfilesContextFacade profilesContextFacade; // ACL

    // Constructor simplificado (sin ACLs ni QueryServices por ahora)
    public EncounterCommandServiceImpl(EncounterRepository encounterRepository,
            EncounterStatusRepository encounterStatusRepository, AttendanceStatusRepository attendanceStatusRepository,
            LanguageRepository languageRepository, CEFRLevelRepository cefrLevelRepository,
            VenuesContextFacade venuesContextFacade, ProfilesContextFacade profilesContextFacade) {
        this.encounterRepository = encounterRepository;
        this.encounterStatusRepository = encounterStatusRepository;
        this.attendanceStatusRepository = attendanceStatusRepository;
        this.languageRepository = languageRepository;
        this.cefrLevelRepository = cefrLevelRepository;
        this.venuesContextFacade = venuesContextFacade;
        this.profilesContextFacade = profilesContextFacade;
    }

    @Override
    @Transactional
    public Optional<Encounter> handle(CreateEncounterCommand command) {
        // ... Validation logic (Venue, Conflicts) ...

        // 1. FETCH REFERENCE DATA (The "Ingredients")
        var language = languageRepository.findByName(Languages.valueOf(command.language()))
                .orElseThrow(() -> new IllegalArgumentException("Language not found"));

        var level = cefrLevelRepository.findByName(CEFRLevels.valueOf(command.cefrLevel()))
                .orElseThrow(() -> new IllegalArgumentException("CEFR Level not found"));

        // --- THE MISSING LINK ---
        // Fetch the specific rows for "DRAFT" and "RESERVED"
        var draftStatus = encounterStatusRepository.findByName(EncounterStatuses.DRAFT)
                .orElseThrow(() -> new IllegalStateException("Status DRAFT not found in DB"));

        var reservedStatus = attendanceStatusRepository.findByName(AttendanceStatuses.RESERVED)
                .orElseThrow(() -> new IllegalStateException("Status RESERVED not found in DB"));

        Long tableIdValue = venuesContextFacade.findAvailableTableAtTime(
                command.venueId().venueId(),
                command.scheduledAt());

        if (tableIdValue == null || tableIdValue == 0L) {
            throw new IllegalStateException("No tables available at the selected venue and time.");
        }

        // 3. Create Encounter
        var encounter = new Encounter(command, language, level, draftStatus, reservedStatus);

        // 4. Assign the Found Table
        encounter.assignTable(new TableId(tableIdValue));

        encounterRepository.save(encounter);

        return Optional.of(encounter);
    }

    @Override
    public void handle(AssignTableToEncounterCommand command) {
        var encounter = encounterRepository.findById(command.encounterId())
                .orElseThrow(() -> new IllegalArgumentException("Encounter not found"));
        encounter.assignTable(command.tableId());
        encounterRepository.save(encounter);
    }

    @Override
    public Optional<Encounter> handle(PublishEncounterCommand command) {
        var encounter = encounterRepository.findById(command.encounterId())
                .orElseThrow(() -> new IllegalArgumentException("Encounter not found"));

        // Fetch published status entity from DB and pass the managed instance
        EncounterStatus publishedStatus = encounterStatusRepository.findByName(EncounterStatuses.PUBLISHED)
                .orElseThrow(() -> new IllegalStateException("Status PUBLISHED not found in DB"));

        // pass managed entity
        encounter.publish(publishedStatus);
        encounterRepository.save(encounter);
        return Optional.of(encounter);
    }

    @Override
    @Transactional
    public Optional<Encounter> handle(JoinEncounterCommand command) {
        // 1. Fetch the Aggregate
        var encounter = encounterRepository.findById(command.encounterId())
                .orElseThrow(() -> new IllegalArgumentException("Encounter not found"));

        // 2. FETCH THE STATUS ENTITY (The Fix)
        var reservedStatus = attendanceStatusRepository.findByName(AttendanceStatuses.RESERVED)
                .orElseThrow(() -> new IllegalStateException("Status RESERVED not found in DB"));

        EncounterStatus readyStatus = encounterStatusRepository.findByName(EncounterStatuses.READY)
                .orElseThrow(() -> new IllegalStateException("Status READY not found in DB"));
        // 3. Pass the Entity to the Aggregate
        encounter.join(command.learnerId(), reservedStatus, readyStatus);

        // 4. Save
        encounterRepository.save(encounter);
        return Optional.of(encounter);
    }

    @Override
    public Optional<Encounter> handle(CheckInEncounterCommand command) {
        var encounter = encounterRepository.findById(command.encounterId())
                .orElseThrow(() -> new IllegalArgumentException("Encounter not found"));

        AttendanceStatus checkedInStatus = attendanceStatusRepository.findByName(AttendanceStatuses.CHECKED_IN)
                .orElseThrow(() -> new IllegalStateException("Status CHECKED_IN not found in DB"));
        // Modificamos el .checkIn para que acepte el status gestionado
        encounter.checkIn(command.learnerId(), checkedInStatus);

        encounterRepository.save(encounter);
        // TODO: Publicar evento LearnerCheckedInEvent para que Loyalty BC otorgue
        // puntos
        return Optional.of(encounter);
    }

    @Override
    public Optional<Encounter> handle(StartEncounterCommand command) {
        var encounter = encounterRepository.findById(command.encounterId())
                .orElseThrow(() -> new IllegalArgumentException("Encounter not found"));

        EncounterStatus inProgressStatus = encounterStatusRepository.findByName(EncounterStatuses.IN_PROGRESS)
                .orElseThrow(() -> new IllegalStateException("Status IN_PROGRESS not found in DB"));

        encounter.start(inProgressStatus);
        encounterRepository.save(encounter);
        return Optional.of(encounter);
    }

    @Override
    public Optional<Encounter> handle(CompleteEncounterCommand command) {
        var encounter = encounterRepository.findById(command.encounterId())
                .orElseThrow(() -> new IllegalArgumentException("Encounter not found"));

        EncounterStatus completedStatus = encounterStatusRepository.findByName(EncounterStatuses.COMPLETED)
                .orElseThrow(() -> new IllegalStateException("Status COMPLETED not found in DB"));

        encounter.complete(completedStatus);
        encounterRepository.save(encounter);
        // TODO: Publicar evento EncounterCompletedEvent para que Loyalty BC penalice
        // NO_SHOWS
        return Optional.of(encounter);
    }

    @Override
    public Optional<Encounter> handle(CancelEncounterCommand command) {
        var encounter = encounterRepository.findById(command.encounterId())
                .orElseThrow(() -> new IllegalArgumentException("Encounter not found"));

        EncounterStatus cancelledStatus = encounterStatusRepository.findByName(EncounterStatuses.CANCELLED)
                .orElseThrow(() -> new IllegalStateException("Status CANCELLED not found in DB"));

        encounter.cancel(cancelledStatus, command.reason());
        encounterRepository.save(encounter);
        // TODO: Publicar evento EncounterCancelledEvent para Venues BC (liberar mesa) y
        // Loyalty BC (penalizar)
        return Optional.of(encounter);
    }

     @Override
    public Optional<Encounter> handle(CancelAttendanceCommand command) {
        var encounter = encounterRepository.findById(command.encounterId())
                .orElseThrow(() -> new IllegalArgumentException("Encounter not found"));

        // Find the specific attendance to cancel
        var attendance = encounter.getAttendances().findByLearnerId(command.learnerId())
                .orElseThrow(() -> new IllegalArgumentException("Attendance not found for this learner"));

        // Cancel the attendance (not the encounter)
        attendance.cancel(LocalDateTime.now(), encounter.getScheduledAt());

        encounterRepository.save(encounter);
        // TODO: Publicar evento AttendanceCancelledEvent para Loyalty BC (penalizar si es tardía)
        return Optional.of(encounter);
    }

    @Override
    public void handle(AutoCancelEncountersCommand command) {
        EncounterStatus readyStatus = encounterStatusRepository.findByName(EncounterStatuses.READY).orElse(null);
        EncounterStatus publishedStatus = encounterStatusRepository.findByName(EncounterStatuses.PUBLISHED)
                .orElse(null);
        
        // Fetch CANCELLED status - this was missing!
        EncounterStatus cancelledStatus = encounterStatusRepository.findByName(EncounterStatuses.CANCELLED)
                .orElse(null);

        if (readyStatus == null || publishedStatus == null || cancelledStatus == null)
            return; // Cannot proceed without statuses

        List<Encounter> encountersToAutoCancel = encounterRepository.findAllByStatusInAndScheduledAtBefore(
                List.of(readyStatus, publishedStatus),
                command.thresholdTime());

        for (Encounter encounter : encountersToAutoCancel) {
            if (encounter.needsAutoCancellation()) {
                // Pass the managed EncounterStatus entity
                encounter.cancel(cancelledStatus, "Auto-cancelled: insufficient attendees 30 mins prior.");
                encounterRepository.save(encounter);
                // TODO: Publicar evento EncounterCancelledEvent
            }
        }
    }
}