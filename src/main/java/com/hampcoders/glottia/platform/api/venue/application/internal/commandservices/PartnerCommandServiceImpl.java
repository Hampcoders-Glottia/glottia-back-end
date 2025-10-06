package com.hampcoders.glottia.platform.api.venue.application.internal.commandservices;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hampcoders.glottia.platform.api.venue.domain.exceptions.PartnerNotFoundException;
import com.hampcoders.glottia.platform.api.venue.domain.exceptions.VenueNotFoundException;
import com.hampcoders.glottia.platform.api.venue.domain.model.aggregates.Partner;
import com.hampcoders.glottia.platform.api.venue.domain.model.commands.*;
import com.hampcoders.glottia.platform.api.venue.domain.model.entities.TableVenue;
import com.hampcoders.glottia.platform.api.venue.domain.model.entities.Venue;
import com.hampcoders.glottia.platform.api.venue.domain.model.valueobjects.MinimumConsumption;
import com.hampcoders.glottia.platform.api.venue.domain.model.valueobjects.TableCapacity;
import com.hampcoders.glottia.platform.api.venue.domain.services.PartnerCommandService;
import com.hampcoders.glottia.platform.api.venue.infrastructure.persistence.jpa.repositories.PartnerRepository;

import java.util.Optional;

/**
 * Implementación del servicio de comandos para el Partner.
 * Responsable de:
 * - Orquestación de casos de uso
 * - Validaciones a nivel de aplicación (anti-corrupción, duplicados)
 * - Gestión de transacciones
 * - Delegación de lógica de negocio al Aggregate Root
 */
@Service
public class PartnerCommandServiceImpl implements PartnerCommandService {

    private final PartnerRepository partnerRepository;

    public PartnerCommandServiceImpl(PartnerRepository partnerRepository) {
        this.partnerRepository = partnerRepository;
    }

    /**
     * Crea un nuevo Partner.
     * Validaciones de aplicación: verificar que no exista un Partner con el mismo userId.
     */
    @Override
    @Transactional
    public Partner handle(CreatePartnerCommand command) {

        // 1. Crear el Aggregate Root (las validaciones de dominio están en el constructor)
        Partner partner = new Partner(
                command.userId(),
                command.legalName(),
                command.initialVenues()
        );

        // 2. Persistir el agregado
        return partnerRepository.save(partner);
    }

    /**
     * Actualiza la información básica de un Partner existente.
     */
    @Override
    @Transactional
    public Optional<Partner> handle(UpdatePartnerCommand command) {
        // 1. Recuperar el agregado
        Partner partner = partnerRepository.findById(command.partnerId())
                .orElseThrow(() -> new PartnerNotFoundException(command.partnerId()));

        // 2. Delegar la lógica de negocio al agregado
        partner.updateBasicInfo(command.legalName());

        // 3. Persistir los cambios
        Partner updatedPartner = partnerRepository.save(partner);

        return Optional.of(updatedPartner);
    }

    /**
     * Elimina un Partner.
     */
    @Override
    @Transactional
    public boolean handle(DeletePartnerCommand command) {
        // 1. Buscar el agregado
        Optional<Partner> partnerOptional = partnerRepository.findById(command.partnerId());

        if (partnerOptional.isEmpty()) {
            return false;
        }

        // 2. Eliminar (las reglas de cascade se manejan en el agregado)
        partnerRepository.delete(partnerOptional.get());

        return true;
    }

    /**
     * Agrega un nuevo Venue a un Partner existente.
     */
    @Override
    @Transactional
    public Optional<Partner> handle(AddVenueToPartnerCommand command) {
        // 1. Recuperar el agregado
        Partner partner = partnerRepository.findById(command.partnerId())
                .orElseThrow(() -> new PartnerNotFoundException(command.partnerId()));

        // 2. Crear la entidad Venue
        Venue newVenue = new Venue(
                command.venueName(),
                command.venueAddress(),
                command.initialTables()
        );

        // 3. Delegar al agregado (él valida las reglas de negocio)
        partner.addVenue(newVenue);

        // 4. Persistir
        Partner savedPartner = partnerRepository.save(partner);

        return Optional.of(savedPartner);
    }

    /**
     * Actualiza la información de un Venue.
     */
    @Override
    @Transactional
    public boolean handle(UpdateVenueCommand command) {
        // 1. Recuperar el agregado Partner
        Partner partner = partnerRepository.findById(command.partnerId())
                .orElseThrow(() -> new PartnerNotFoundException(command.partnerId()));

        // 2. Buscar el Venue dentro del agregado
        Venue venue = partner.findVenueById(command.venueId())
                .orElseThrow(() -> new VenueNotFoundException(command.venueId()));

        // 3. Delegar la actualización a la entidad Venue
        venue.updateInfo(command.venueName(), command.venueAddress());

        // 4. Persistir el agregado completo
        partnerRepository.save(partner);

        return true;
    }

    /**
     * Elimina un Venue de un Partner.
     */
    @Override
    @Transactional
    public boolean handle(DeleteVenueCommand command) {
        // 1. Recuperar el agregado Partner
        Partner partner = partnerRepository.findById(command.partnerId())
                .orElseThrow(() -> new PartnerNotFoundException(command.partnerId()));

        // 2. Delegar la eliminación al agregado (él mantiene los invariantes)
        partner.removeVenueById(command.venueId());

        // 3. Persistir
        partnerRepository.save(partner);

        return true;
    }

    /**
     * Agrega una Table a un Venue específico.
     */
    @Override
    @Transactional
    public boolean handle(AddTableToVenueCommand command) {
        // 1. Recuperar el agregado Partner
        Partner partner = partnerRepository.findById(command.partnerId())
                .orElseThrow(() -> new PartnerNotFoundException(command.partnerId()));

        // 2. Buscar el Venue dentro del agregado
        Venue venue = partner.findVenueById(command.venueId())
                .orElseThrow(() -> new VenueNotFoundException(command.venueId()));

        // 3. Crear la nueva tabla (necesitas adaptar según tus value objects)
        // Nota: Esta es una simplificación. Deberías usar tus value objects correctamente
        TableVenue newTable = new TableVenue(
                "Mesa " + command.tableNumber(),
                new TableCapacity(command.capacity()),
                new MinimumConsumption(0F, null),
                java.time.LocalTime.of(9, 0),
                java.time.LocalTime.of(22, 0)
        );

        // 4. Delegar al Venue
        venue.addTable(newTable);

        // 5. Persistir el agregado
        partnerRepository.save(partner);

        return true;
    }

    /**
     * Actualiza el estado de una Table.
     */
    @Override
    @Transactional
    public boolean handle(UpdateTableStatusCommand command) {
        // 1. Recuperar el agregado Partner
        Partner partner = partnerRepository.findById(command.partnerId())
                .orElseThrow(() -> new PartnerNotFoundException(command.partnerId()));

        // 2. Buscar el Venue dentro del agregado
        Venue venue = partner.findVenueById(command.venueId())
                .orElseThrow(() -> new VenueNotFoundException(command.venueId()));

        // 3. Buscar la Table dentro del Venue
        TableVenue table = venue.findTableById(command.tableId())
                .orElseThrow(() -> new IllegalArgumentException("Table con ID " + command.tableId() + " no encontrada."));

        // 4. Delegar la actualización de estado a la entidad Table
        com.hampcoders.glottia.platform.api.venue.domain.model.valueobjects.TableStatus newStatus =
                com.hampcoders.glottia.platform.api.venue.domain.model.valueobjects.TableStatus.valueOf(command.newStatus());
        table.updateStatus(newStatus);

        // 5. Persistir el agregado completo
        partnerRepository.save(partner);

        return true;
    }
}
