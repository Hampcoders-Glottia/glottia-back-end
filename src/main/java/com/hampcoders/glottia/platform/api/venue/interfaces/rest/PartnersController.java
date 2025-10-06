package com.hampcoders.glottia.platform.api.venue.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hampcoders.glottia.platform.api.venue.domain.model.queries.GetAllPartnersQuery;
import com.hampcoders.glottia.platform.api.venue.domain.model.queries.GetPartnerByIdQuery;
import com.hampcoders.glottia.platform.api.venue.domain.model.queries.GetPartnerByUserIdQuery;
import com.hampcoders.glottia.platform.api.venue.domain.services.PartnerCommandService;
import com.hampcoders.glottia.platform.api.venue.domain.services.PartnerQueryService;
import com.hampcoders.glottia.platform.api.venue.interfaces.rest.resources.*;
import com.hampcoders.glottia.platform.api.venue.interfaces.rest.transform.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador REST para gestionar Partners y sus Locales.
 * Actúa como la interfaz del Bounded Context Partner.
 * Sigue el patrón de arquitectura hexagonal con transformadores Resource <-> Command/Entity.
 */
@RestController
@RequestMapping(value = "/api/v1/partners", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Partners", description = "Partner Management Endpoints")
public class PartnersController {

    private final PartnerCommandService partnerCommandService;
    private final PartnerQueryService partnerQueryService;

    public PartnersController(PartnerCommandService partnerCommandService,
                             PartnerQueryService partnerQueryService) {
        this.partnerCommandService = partnerCommandService;
        this.partnerQueryService = partnerQueryService;
    }

    // ==================== ENDPOINTS DE COMANDO ====================

    /**
     * Crear un nuevo Partner.
     * POST /api/v1/partners
     */
    @Operation(summary = "Crear un nuevo Partner",
            description = "Da de alta un nuevo Partner con sus locales y mesas iniciales.")
    @PostMapping
    public ResponseEntity<PartnerResource> createPartner(@RequestBody CreatePartnerResource resource) {
        try {
            // 1. Convertir Resource -> Command usando el assembler
            var command = CreatePartnerCommandFromResourceAssembler.toCommandFromResource(resource);

            // 2. Ejecutar el comando
            var partner = partnerCommandService.handle(command);

            // 3. Convertir Entity -> Resource usando el assembler
            var partnerResource = PartnerResourceFromEntityAssembler.toResourceFromEntity(partner);

            return ResponseEntity.status(HttpStatus.CREATED).body(partnerResource);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Actualizar un Partner existente.
     * PUT /api/v1/partners/{partnerId}
     */
    @Operation(summary = "Actualizar Partner",
            description = "Actualiza la información básica de un Partner.")
    @PutMapping("/{partnerId}")
    public ResponseEntity<PartnerResource> updatePartner(
            @PathVariable Long partnerId,
            @RequestBody UpdatePartnerResource resource) {
        try {
            // 1. Convertir Resource -> Command
            var command = UpdatePartnerCommandFromResourceAssembler.toCommandFromResource(partnerId, resource);

            // 2. Ejecutar el comando
            var partnerOptional = partnerCommandService.handle(command);

            // 3. Convertir Entity -> Resource y retornar
            return partnerOptional
                    .map(PartnerResourceFromEntityAssembler::toResourceFromEntity)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Agregar un Venue a un Partner.
     * POST /api/v1/partners/{partnerId}/venues
     */
    @Operation(summary = "Agregar Venue a Partner",
            description = "Agrega un nuevo local a un Partner existente.")
    @PostMapping("/{partnerId}/venues")
    public ResponseEntity<PartnerResource> addVenueToPartner(
            @PathVariable Long partnerId,
            @RequestBody AddVenueToPartnerResource resource) {
        try {
            // 1. Convertir Resource -> Command
            var command = AddVenueToPartnerCommandFromResourceAssembler.toCommandFromResource(partnerId, resource);

            // 2. Ejecutar el comando
            var partnerOptional = partnerCommandService.handle(command);

            // 3. Convertir Entity -> Resource y retornar
            return partnerOptional
                    .map(PartnerResourceFromEntityAssembler::toResourceFromEntity)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // ==================== ENDPOINTS DE CONSULTA ====================

    /**
     * Obtener todos los Partners.
     * GET /api/v1/partners
     */
    @Operation(summary = "Obtener todos los Partners",
            description = "Retorna la lista completa de Partners registrados.")
    @GetMapping
    public ResponseEntity<List<PartnerResource>> getAllPartners() {
        var query = new GetAllPartnersQuery();
        var partners = partnerQueryService.handle(query);

        var partnerResources = partners.stream()
                .map(PartnerResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(partnerResources);
    }

    /**
     * Obtener Partner por ID.
     * GET /api/v1/partners/{partnerId}
     */
    @Operation(summary = "Obtener Partner por ID",
            description = "Retorna un Partner específico por su ID.")
    @GetMapping("/{partnerId}")
    public ResponseEntity<PartnerResource> getPartnerById(@PathVariable Long partnerId) {
        var query = new GetPartnerByIdQuery(partnerId);
        var partnerOptional = partnerQueryService.handle(query);

        return partnerOptional
                .map(PartnerResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Obtener Partner por User ID.
     * GET /api/v1/partners/by-user/{userId}
     */
    @Operation(summary = "Obtener Partner por ID de Usuario",
            description = "Retorna el Partner asociado a un usuario específico (del BC IAM).")
    @GetMapping("/by-user/{userId}")
    public ResponseEntity<PartnerResource> getPartnerByUserId(@PathVariable Long userId) {
        var query = new GetPartnerByUserIdQuery(userId);
        var partnerOptional = partnerQueryService.handle(query);

        return partnerOptional
                .map(PartnerResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}