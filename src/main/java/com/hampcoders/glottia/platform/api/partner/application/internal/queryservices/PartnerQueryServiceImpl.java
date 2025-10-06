package com.hampcoders.glottia.platform.api.partner.application.internal.queryservices;

import com.hampcoders.glottia.platform.api.partner.domain.model.aggregates.Partner;
import com.hampcoders.glottia.platform.api.partner.domain.model.entities.TableVenue;
import com.hampcoders.glottia.platform.api.partner.domain.model.entities.Venue;
import com.hampcoders.glottia.platform.api.partner.domain.model.queries.*;
import com.hampcoders.glottia.platform.api.partner.domain.services.PartnerQueryService;
import com.hampcoders.glottia.platform.api.partner.infrastructure.persistence.jpa.repositories.PartnerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de consulta para el Partner (Query Service).
 * Responsable de:
 * - Obtener información de lectura desde el repositorio
 * - Proyecciones y transformaciones de datos
 * - Optimizaciones de lectura (readOnly transactions)
 * - No modifica el estado del agregado
 */
@Service
public class PartnerQueryServiceImpl implements PartnerQueryService {

    private final PartnerRepository partnerRepository;

    public PartnerQueryServiceImpl(PartnerRepository partnerRepository) {
        this.partnerRepository = partnerRepository;
    }

    /**
     * Obtiene todos los Partners.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Partner> handle(GetAllPartnersQuery query) {
        return partnerRepository.findAll();
    }

    /**
     * Obtiene un Partner por su ID.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Partner> handle(GetPartnerByIdQuery query) {
        // Validación del query
        if (query.partnerId() == null || query.partnerId() <= 0) {
            return Optional.empty();
        }

        return partnerRepository.findById(query.partnerId());
    }

    /**
     * Obtiene un Partner por el User ID.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Partner> handle(GetPartnerByUserIdQuery query) {
        // Validación del query
        if (query.userId() == null || query.userId() <= 0) {
            return Optional.empty();
        }

        // Delegación directa al repositorio de dominio
        return partnerRepository.findByUserId(query.userId());
    }

    /**
     * Obtiene un Venue por su ID.
     * Nota: Esta operación atraviesa el agregado Partner.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Venue> handle(GetVenueByIdQuery query) {
        if (query.venueId() == null || query.venueId() <= 0) {
            return Optional.empty();
        }

        // Buscar en todos los Partners (no es óptimo, considera un repositorio específico si es necesario)
        return partnerRepository.findAll().stream()
                .flatMap(partner -> partner.getVenues().stream())
                .filter(venue -> venue.getId() != null && venue.getId().equals(query.venueId()))
                .findFirst();
    }

    /**
     * Obtiene todos los Venues de un Partner específico.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Venue> handle(GetVenuesByPartnerIdQuery query) {
        if (query.partnerId() == null || query.partnerId() <= 0) {
            return Collections.emptyList();
        }

        return partnerRepository.findById(query.partnerId())
                .map(Partner::getVenues)
                .orElse(Collections.emptyList());
    }

    /**
     * Obtiene una Table por su ID.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TableVenue> handle(GetTableByIdQuery query) {
        if (query.tableId() == null || query.tableId() <= 0) {
            return Optional.empty();
        }

        // Navegar a través del agregado: Partner -> Venue -> Table
        return partnerRepository.findAll().stream()
                .flatMap(partner -> partner.getVenues().stream())
                .flatMap(venue -> venue.getTables().stream())
                .filter(table -> table.getId() != null && table.getId().equals(query.tableId()))
                .findFirst();
    }

    /**
     * Obtiene todas las Tables de un Venue específico.
     */
    @Override
    @Transactional(readOnly = true)
    public List<TableVenue> handle(GetTablesByVenueIdQuery query) {
        if (query.venueId() == null || query.venueId() <= 0) {
            return Collections.emptyList();
        }

        // Buscar el venue y retornar sus tables
        return partnerRepository.findAll().stream()
                .flatMap(partner -> partner.getVenues().stream())
                .filter(venue -> venue.getId() != null && venue.getId().equals(query.venueId()))
                .findFirst()
                .map(Venue::getTables)
                .orElse(Collections.emptyList());
    }

    /**
     * Obtiene las Tables disponibles de un Venue.
     * Aplica filtros de negocio en la capa de aplicación.
     */
    @Override
    @Transactional(readOnly = true)
    public List<TableVenue> handle(GetAvailableTablesByVenueIdQuery query) {
        if (query.venueId() == null || query.venueId() <= 0) {
            return Collections.emptyList();
        }

        // Buscar el venue y filtrar por estado AVAILABLE
        return partnerRepository.findAll().stream()
                .flatMap(partner -> partner.getVenues().stream())
                .filter(venue -> venue.getId() != null && venue.getId().equals(query.venueId()))
                .findFirst()
                .map(venue -> venue.getTables().stream()
                        .filter(table -> "AVAILABLE".equals(table.getStatus()))
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }
}