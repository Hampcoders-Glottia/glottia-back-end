package com.hampcoders.glottia.platform.api.partner.domain.services;

import com.hampcoders.glottia.platform.api.partner.domain.model.aggregates.Partner;
import com.hampcoders.glottia.platform.api.partner.domain.model.commands.*;

import java.util.Optional;

/**
 * Servicio de dominio para procesar comandos relacionados con Partner.
 */
public interface PartnerCommandService {

    /**
     * Crea un nuevo Partner.
     * @param command Comando con los datos del nuevo Partner.
     * @return El Partner creado.
     */
    Partner handle(CreatePartnerCommand command);

    /**
     * Actualiza la información de un Partner existente.
     * @param command Comando con los datos actualizados.
     * @return El Partner actualizado.
     */
    Optional<Partner> handle(UpdatePartnerCommand command);

    /**
     * Elimina un Partner.
     * @param command Comando con el ID del Partner a eliminar.
     * @return true si se eliminó correctamente, false en caso contrario.
     */
    boolean handle(DeletePartnerCommand command);

    /**
     * Agrega un nuevo Venue a un Partner.
     * @param command Comando con los datos del nuevo Venue.
     * @return El Partner actualizado con el nuevo Venue.
     */
    Optional<Partner> handle(AddVenueToPartnerCommand command);

    /**
     * Actualiza la información de un Venue.
     * @param command Comando con los datos actualizados del Venue.
     * @return true si se actualizó correctamente.
     */
    boolean handle(UpdateVenueCommand command);

    /**
     * Elimina un Venue.
     * @param command Comando con el ID del Venue a eliminar.
     * @return true si se eliminó correctamente.
     */
    boolean handle(DeleteVenueCommand command);

    /**
     * Agrega una nueva mesa a un Venue.
     * @param command Comando con los datos de la nueva mesa.
     * @return true si se agregó correctamente.
     */
    boolean handle(AddTableToVenueCommand command);

    /**
     * Actualiza el estado de una mesa.
     * @param command Comando con el nuevo estado.
     * @return true si se actualizó correctamente.
     */
    boolean handle(UpdateTableStatusCommand command);
}

