package com.hampcoders.glottia.platform.api.encounters.application.internal.commandservices;

import com.hampcoders.glottia.platform.api.encounters.domain.model.aggregates.Encounter;
import com.hampcoders.glottia.platform.api.encounters.domain.model.commands.*;
import com.hampcoders.glottia.platform.api.encounters.domain.model.entities.AttendanceStatus;
import com.hampcoders.glottia.platform.api.encounters.domain.model.entities.EncounterStatus;
import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.AttendanceStatuses;
import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.EncounterStatuses;
import com.hampcoders.glottia.platform.api.encounters.domain.services.EncounterCommandService;
import com.hampcoders.glottia.platform.api.encounters.infrastructure.persistence.jpa.repository.AttendanceStatusRepository;
import com.hampcoders.glottia.platform.api.encounters.infrastructure.persistence.jpa.repository.EncounterRepository;
import com.hampcoders.glottia.platform.api.encounters.infrastructure.persistence.jpa.repository.EncounterStatusRepository;

// import com.hampcoders.glottia.platform.api.venues.interfaces.acl.VenuesContextFacade; // ACL
// import com.hampcoders.glottia.platform.api.profiles.interfaces.acl.ProfilesContextFacade; // ACL

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/** 
 * Implements the EncounterCommandService to handle commands related to encounters.
 * This service manages the creation, updating, and cancellation of encounters,
 * enforcing business rules and interacting with necessary repositories.
 * It utilizes ACLs and Query Services for cross-boundary validations (to-do: in progress).
 */
@Service
public class EncounterCommandServiceImpl implements EncounterCommandService {

    private final EncounterRepository encounterRepository;
    private final EncounterStatusRepository encounterStatusRepository;
    private final AttendanceStatusRepository attendanceStatusRepository;
    // private final VenuesContextFacade venuesContextFacade; // ACL
    // private final ProfilesContextFacade profilesContextFacade; // ACL
    // private final LoyaltyAccountQueryService loyaltyAccountQueryService; // Para Regla 12
    // private final EncounterQueryService encounterQueryService; // Para Regla 14

    // Constructor simplificado (sin ACLs ni QueryServices por ahora)
    public EncounterCommandServiceImpl(EncounterRepository encounterRepository, EncounterStatusRepository encounterStatusRepository, AttendanceStatusRepository attendanceStatusRepository) {
        this.encounterRepository = encounterRepository;
        this.encounterStatusRepository = encounterStatusRepository;
        this.attendanceStatusRepository = attendanceStatusRepository;
    }

    @Override
    public Optional<Encounter> handle(CreateEncounterCommand command) {
        // --- Validación de Reglas de Negocio (Usando ACLs/QueryServices) ---
        // 1. (Regla 12) Verificar si el learner puede participar (puntos >= 0)
        // if (!loyaltyAccountQueryService.handle(new CanParticipateQuery(command.creatorId()))) {
        //     throw new IllegalStateException("Creator cannot participate due to low loyalty points.");
        // }

        // 2. (Regla 14) Verificar conflictos de horario
        // LocalDateTime start = command.scheduledAt().minusHours(2);
        // LocalDateTime end = command.scheduledAt().plusHours(2);
        // if (encounterQueryService.handle(new HasConflictingEncounterQuery(command.creatorId(), start, end))) {
        //     throw new IllegalStateException("Creator has a conflicting encounter within 2 hours.");
        // }
        
        // 3. (Regla 5) Solicitar reserva de mesa al Venues BC
        // TableId tableId = venuesContextFacade.requestTableReservation(command.venueId(), command.scheduledAt());
        // if (tableId == null) {
        //     throw new IllegalStateException("No tables available at the selected venue and time.");
        // }

        // --- Creación del Agregado (con dependencias gestionadas) ---

        // 4. Crear el Agregado (Asumiendo un constructor que acepta entidades gestionadas)
        // Nota: Modificaremos el Encounter.java para que acepte esto
        var encounter = new Encounter(command);
        
        // 5. Asignar la mesa (si se obtuvo en el paso 3)
        // encounter.assignTable(tableId); // Asignar la mesa obtenida del Venues BC

        // 6. Persistir
        encounterRepository.save(encounter);
        
        // 7. (Opcional) Publicar Evento de Dominio
        // encounter.registerEvent(new EncounterCreatedEvent(...));
        
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
        encounter.publish();
        encounterRepository.save(encounter);
        return Optional.of(encounter);
    }

    @Override
    public Optional<Encounter> handle(JoinEncounterCommand command) {
        // (Regla 12) Verificar si el learner puede participar (puntos >= 0)
        // ...

        var encounter = encounterRepository.findById(command.encounterId())
                .orElseThrow(() -> new IllegalArgumentException("Encounter not found"));
        
        // Modificamos el .join para que acepte el status gestionado
        encounter.join(command.learnerId());
        
        encounterRepository.save(encounter);
        return Optional.of(encounter);
    }

    @Override
    public Optional<Encounter> handle(CheckInEncounterCommand command) {
        var encounter = encounterRepository.findById(command.encounterId())
                .orElseThrow(() -> new IllegalArgumentException("Encounter not found"));
        
        // Modificamos el .checkIn para que acepte el status gestionado
        encounter.checkIn(command.learnerId());
        
        encounterRepository.save(encounter);
        // TODO: Publicar evento LearnerCheckedInEvent para que Loyalty BC otorgue puntos
        return Optional.of(encounter);
    }

    @Override
    public Optional<Encounter> handle(StartEncounterCommand command) {
        var encounter = encounterRepository.findById(command.encounterId())
                .orElseThrow(() -> new IllegalArgumentException("Encounter not found"));

        encounter.start();
        encounterRepository.save(encounter);
        return Optional.of(encounter);
    }

    @Override
    public Optional<Encounter> handle(CompleteEncounterCommand command) {
        var encounter = encounterRepository.findById(command.encounterId())
                .orElseThrow(() -> new IllegalArgumentException("Encounter not found"));
        
        encounter.complete();
        encounterRepository.save(encounter);
        // TODO: Publicar evento EncounterCompletedEvent para que Loyalty BC penalice NO_SHOWS
        return Optional.of(encounter);
    }

    @Override
    public Optional<Encounter> handle(CancelEncounterCommand command) {
        var encounter = encounterRepository.findById(command.encounterId())
                .orElseThrow(() -> new IllegalArgumentException("Encounter not found"));

        encounter.cancel(command.reason());
        encounterRepository.save(encounter);
        // TODO: Publicar evento EncounterCancelledEvent para Venues BC (liberar mesa) y Loyalty BC (penalizar)
        return Optional.of(encounter);
    }

    @Override
    public Optional<Encounter> handle(CancelAttendanceCommand command) {
        var encounter = encounterRepository.findById(command.encounterId())
                .orElseThrow(() -> new IllegalArgumentException("Encounter not found"));

        AttendanceStatus cancelledStatus = attendanceStatusRepository.findByName(AttendanceStatuses.CANCELLED)
                .orElseThrow(() -> new IllegalStateException("CANCELLED status not found"));
        
        encounter.cancel(cancelledStatus.toString());
        encounterRepository.save(encounter);
        // TODO: Publicar evento AttendanceCancelledEvent para Loyalty BC (penalizar si es tardía)
        return Optional.of(encounter);
    }

    @Override
    public void handle(AutoCancelEncountersCommand command) {
        EncounterStatus readyStatus = encounterStatusRepository.findByName(EncounterStatuses.READY).orElse(null);
        EncounterStatus publishedStatus = encounterStatusRepository.findByName(EncounterStatuses.PUBLISHED).orElse(null);

        if (readyStatus == null || publishedStatus == null) return; // No se pueden buscar

        List<Encounter> encountersToAutoCancel = encounterRepository.findAllByStatusInAndScheduledAtBefore(
            publishedStatus != null && readyStatus != null ?
                List.of(AttendanceStatuses.CHECKED_IN, AttendanceStatuses.RESERVED) :
                List.of(),
            command.thresholdTime());

        for (Encounter encounter : encountersToAutoCancel) {
            if (encounter.needsAutoCancellation()) {
                encounter.cancel("Auto-cancelled: insufficient attendees 30 mins prior.");
                encounterRepository.save(encounter);
                // TODO: Publicar evento EncounterCancelledEvent
            }
        }
    }
}