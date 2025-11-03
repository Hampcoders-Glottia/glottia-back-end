package com.hampcoders.glottia.platform.api.encounters.interfaces.rest;

import com.hampcoders.glottia.platform.api.encounters.domain.model.commands.CancelEncounterCommand;
import com.hampcoders.glottia.platform.api.encounters.domain.model.queries.GetEncounterByIdQuery;
import com.hampcoders.glottia.platform.api.encounters.domain.model.queries.SearchEncountersQuery;
import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.LearnerId;
import com.hampcoders.glottia.platform.api.encounters.domain.services.EncounterCommandService;
import com.hampcoders.glottia.platform.api.encounters.domain.services.EncounterQueryService;
import com.hampcoders.glottia.platform.api.encounters.interfaces.rest.resources.CancelEncounterResource;
import com.hampcoders.glottia.platform.api.encounters.interfaces.rest.resources.EncounterResource;
import com.hampcoders.glottia.platform.api.encounters.interfaces.rest.resources.CreateEncounterResource;
import com.hampcoders.glottia.platform.api.encounters.interfaces.rest.transform.CancelEncounterCommandFromResourceAssembler;
import com.hampcoders.glottia.platform.api.encounters.interfaces.rest.transform.CreateEncounterCommandFromResourceAssembler;
import com.hampcoders.glottia.platform.api.encounters.interfaces.rest.transform.EncounterResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/encounters", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Encounters", description = "Encounter Management Endpoints")
public class EncountersController {

    private final EncounterCommandService encounterCommandService;
    private final EncounterQueryService encounterQueryService;

    public EncountersController(EncounterCommandService encounterCommandService, EncounterQueryService encounterQueryService) {
        this.encounterCommandService = encounterCommandService;
        this.encounterQueryService = encounterQueryService;
    }

    @Operation(summary = "Crear un nuevo encounter")
    @PostMapping
    public ResponseEntity<EncounterResource> createEncounter(@RequestBody CreateEncounterResource resource) {
        var createEncounterCommand = CreateEncounterCommandFromResourceAssembler.toCommandFromResource(resource);
        var encounter = encounterCommandService.handle(createEncounterCommand);
        
        return encounter.map(e -> new ResponseEntity<>(EncounterResourceFromEntityAssembler.toResourceFromEntity(e), HttpStatus.CREATED))
                .orElse(ResponseEntity.badRequest().build());
    }

    @Operation(summary = "Obtener un encounter por ID")
    @GetMapping("/{encounterId}")
    public ResponseEntity<EncounterResource> getEncounterById(@PathVariable Long encounterId) {
        var getEncounterByIdQuery = new GetEncounterByIdQuery(encounterId);
        var encounter = encounterQueryService.handle(getEncounterByIdQuery);
        
        return encounter.map(e -> ResponseEntity.ok(EncounterResourceFromEntityAssembler.toResourceFromEntity(e)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Buscar encounters (para learners)")
    @GetMapping("/search")
    public ResponseEntity<List<EncounterResource>> searchEncounters(
            @RequestParam String location, // Simplificado, idealmente VenueId o coordenadas
            @RequestParam Long languageId,
            @RequestParam Long cefrLevelId,
            @RequestParam LocalDate date,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        var query = new SearchEncountersQuery(location, languageId, cefrLevelId, date, page, size);
        var encounters = encounterQueryService.handle(query);
        var resources = encounters.stream()
                .map(EncounterResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resources);
    }

    @Operation(summary = "Publicar un encounter (hecho por el creador)")
    @PostMapping("/{encounterId}/publish")
    public ResponseEntity<EncounterResource> publishEncounter(@PathVariable Long encounterId) {
        // TODO: Verificar que el usuario actual es el creador (Security)
        var command = new com.hampcoders.glottia.platform.api.encounters.domain.model.commands.PublishEncounterCommand(encounterId);
        var encounter = encounterCommandService.handle(command);
        
        return encounter.map(e -> ResponseEntity.ok(EncounterResourceFromEntityAssembler.toResourceFromEntity(e)))
                .orElse(ResponseEntity.badRequest().build());
    }

    @Operation(summary = "Cancelar un encounter (hecho por el creador o admin)")
    @DeleteMapping("/{encounterId}")
    public ResponseEntity<EncounterResource> cancelEncounter(@PathVariable Long encounterId, @RequestBody CancelEncounterResource resource) {
        // TODO: Obtener LearnerId del contexto de seguridad
        LearnerId cancellerId = new LearnerId(1L); // Placeholder
        
        var command = CancelEncounterCommandFromResourceAssembler.toCommandFromResource(encounterId, cancellerId, resource);
        var encounter = encounterCommandService.handle(command);
        
        return encounter.map(e -> ResponseEntity.ok(EncounterResourceFromEntityAssembler.toResourceFromEntity(e)))
                .orElse(ResponseEntity.badRequest().build());
    }
    
    // NOTA: Los endpoints de START y COMPLETE están marcados como (SYSTEM) en el MD,
    // por lo que podrían no ser expuestos públicamente, sino llamados por Schedulers o eventos internos.
    // Los agrego aquí por completitud.
    
    @Operation(summary = "Marcar un encounter como iniciado (Sistema)")
    @PostMapping("/{encounterId}/start")
    public ResponseEntity<EncounterResource> startEncounter(@PathVariable Long encounterId) {
        var command = new com.hampcoders.glottia.platform.api.encounters.domain.model.commands.StartEncounterCommand(encounterId);
        var encounter = encounterCommandService.handle(command);
         return encounter.map(e -> ResponseEntity.ok(EncounterResourceFromEntityAssembler.toResourceFromEntity(e)))
                .orElse(ResponseEntity.badRequest().build());
    }
    
    @Operation(summary = "Marcar un encounter como completado (Sistema)")
    @PostMapping("/{encounterId}/complete")
    public ResponseEntity<EncounterResource> completeEncounter(@PathVariable Long encounterId) {
        var command = new com.hampcoders.glottia.platform.api.encounters.domain.model.commands.CompleteEncounterCommand(encounterId);
        var encounter = encounterCommandService.handle(command);
         return encounter.map(e -> ResponseEntity.ok(EncounterResourceFromEntityAssembler.toResourceFromEntity(e)))
                .orElse(ResponseEntity.badRequest().build());
    }
}
