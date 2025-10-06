package com.hampcoders.glottia.platform.api.partner.domain.services;

import com.hampcoders.glottia.platform.api.partner.domain.model.aggregates.Partner;
import com.hampcoders.glottia.platform.api.partner.domain.model.entities.TableVenue;
import com.hampcoders.glottia.platform.api.partner.domain.model.entities.Venue;
import com.hampcoders.glottia.platform.api.partner.domain.model.queries.*;

import java.util.List;
import java.util.Optional;

/**
 * Servicio de dominio para procesar consultas relacionadas con Partner.
 */
public interface PartnerQueryService {

    /**
     * Obtiene todos los Partners.
     * @param query Consulta para obtener todos los Partners.
     * @return Lista de todos los Partners.
     */
    List<Partner> handle(GetAllPartnersQuery query);

    /**
     * Obtiene un Partner por su ID.
     * @param query Consulta con el ID del Partner.
     * @return El Partner si existe.
     */
    Optional<Partner> handle(GetPartnerByIdQuery query);

    /**
     * Obtiene un Partner por el User ID.
     * @param query Consulta con el User ID.
     * @return El Partner si existe.
     */
    Optional<Partner> handle(GetPartnerByUserIdQuery query);

    /**
     * Obtiene un Venue por su ID.
     * @param query Consulta con el ID del Venue.
     * @return El Venue si existe.
     */
    Optional<Venue> handle(GetVenueByIdQuery query);

    /**
     * Obtiene todos los Venues de un Partner.
     * @param query Consulta con el ID del Partner.
     * @return Lista de Venues del Partner.
     */
    List<Venue> handle(GetVenuesByPartnerIdQuery query);

    /**
     * Obtiene una mesa por su ID.
     * @param query Consulta con el ID de la mesa.
     * @return La mesa si existe.
     */
    Optional<TableVenue> handle(GetTableByIdQuery query);

    /**
     * Obtiene todas las mesas de un Venue.
     * @param query Consulta con el ID del Venue.
     * @return Lista de mesas del Venue.
     */
    List<TableVenue> handle(GetTablesByVenueIdQuery query);

    /**
     * Obtiene todas las mesas disponibles de un Venue.
     * @param query Consulta con el ID del Venue.
     * @return Lista de mesas disponibles del Venue.
     */
    List<TableVenue> handle(GetAvailableTablesByVenueIdQuery query);
}

