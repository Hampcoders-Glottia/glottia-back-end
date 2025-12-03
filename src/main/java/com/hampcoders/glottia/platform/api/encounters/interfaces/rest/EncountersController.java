package com.hampcoders.glottia.platform.api.encounters.interfaces.rest;

import com.hampcoders.glottia.platform.api.encounters.domain.model.aggregates.Encounter;
import com.hampcoders.glottia.platform.api.encounters.domain.model.queries.*;
import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.LearnerId;
import com.hampcoders.glottia.platform.api.encounters.domain.services.EncounterCommandService;
import com.hampcoders.glottia.platform.api.encounters.domain.services.EncounterQueryService;
import com.hampcoders.glottia.platform.api.encounters.interfaces.rest.resources.CancelEncounterResource;
import com.hampcoders.glottia.platform.api.encounters.interfaces.rest.resources.CreateEncounterResource;
import com.hampcoders.glottia.platform.api.encounters.interfaces.rest.resources.EncounterResource;
import com.hampcoders.glottia.platform.api.encounters.interfaces.rest.transform.CancelEncounterCommandFromResourceAssembler;
import com.hampcoders.glottia.platform.api.encounters.interfaces.rest.transform.CreateEncounterCommandFromResourceAssembler;
import com.hampcoders.glottia.platform.api.encounters.interfaces.rest.transform.EncounterResourceFromEntityAssembler;
import com.hampcoders.glottia.platform.api.venues.interfaces.acl.VenuesContextFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/encounters", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Encounters", description = "Encounter Management Endpoints")
public class EncountersController {

  private final EncounterCommandService encounterCommandService;
  private final EncounterQueryService encounterQueryService;
  private final VenuesContextFacade venuesContextFacade;

  public EncountersController(EncounterCommandService encounterCommandService,
                              EncounterQueryService encounterQueryService,
                              VenuesContextFacade venuesContextFacade) {
    this.encounterCommandService = encounterCommandService;
    this.encounterQueryService = encounterQueryService;
    this.venuesContextFacade = venuesContextFacade;
  }

  // --- MÉTODOS AUXILIARES ---

  private EncounterResource toResource(Encounter encounter) {
    // Obtenemos datos enriquecidos del módulo de Venues
    String venueName = venuesContextFacade.fetchVenueName(encounter.getVenueId().venueId());
    String venueAddress = venuesContextFacade.fetchVenueAddress(encounter.getVenueId().venueId());

    // Usamos el assembler actualizado con 3 argumentos
    return EncounterResourceFromEntityAssembler.toResourceFromEntity(encounter, venueName, venueAddress);
  }

  // --- ENDPOINTS ---

  @Operation(summary = "Crear un nuevo encounter")
  @PostMapping
  public ResponseEntity<EncounterResource> createEncounter(@RequestBody CreateEncounterResource resource) {
    var createEncounterCommand = CreateEncounterCommandFromResourceAssembler.toCommandFromResource(resource);
    var encounter = encounterCommandService.handle(createEncounterCommand);

    return encounter.map(e -> new ResponseEntity<>(toResource(e), HttpStatus.CREATED))
        .orElse(ResponseEntity.badRequest().build());
  }

  @Operation(summary = "Obtener un encounter por ID")
  @GetMapping("/{encounterId}")
  public ResponseEntity<EncounterResource> getEncounterById(@PathVariable Long encounterId) {
    var getEncounterByIdQuery = new GetEncounterByIdQuery(encounterId);
    var encounter = encounterQueryService.handle(getEncounterByIdQuery);

    return encounter.map(e -> ResponseEntity.ok(toResource(e)))
        .orElse(ResponseEntity.notFound().build());
  }

  @Operation(summary = "Buscar encounters (para learners)")
  @GetMapping("/search")
  public ResponseEntity<List<EncounterResource>> searchEncounters(
      @RequestParam String location,
      @RequestParam Long languageId,
      @RequestParam Long cefrLevelId,
      @RequestParam LocalDate date,
      @RequestParam(defaultValue = "0") Integer page,
      @RequestParam(defaultValue = "10") Integer size
  ) {
    var query = new SearchEncountersQuery(location, languageId, cefrLevelId, date, page, size);
    var encounters = encounterQueryService.handle(query);

    var resources = encounters.stream()
        .map(this::toResource) // Usamos el método auxiliar para incluir nombres de Venues
        .collect(Collectors.toList());

    return ResponseEntity.ok(resources);
  }

  @Operation(summary = "Obtener próximas clases del alumno")
  @GetMapping("/learner/{learnerId}/upcoming")
  public ResponseEntity<List<EncounterResource>> getUpcomingEncounters(@PathVariable Long learnerId) {
    var query = new GetUpcomingEncountersForLearnerQuery(new LearnerId(learnerId), 7);
    var encounters = encounterQueryService.handle(query);

    var resources = encounters.stream()
        .map(this::toResource)
        .collect(Collectors.toList());

    return ResponseEntity.ok(resources);
  }

  @Operation(summary = "Obtener historial de clases del alumno")
  @GetMapping("/learner/{learnerId}/history")
  public ResponseEntity<List<EncounterResource>> getEncounterHistory(@PathVariable Long learnerId) {
    var query = new GetLearnerEncounterHistoryQuery(new LearnerId(learnerId));
    var encounters = encounterQueryService.handle(query);

    var resources = encounters
        .stream()
        .map(this::toResource)
        .collect(Collectors.toList());

    return ResponseEntity.ok(resources);
  }

  @Operation(summary = "Publicar un encounter (hecho por el creador)")
  @PostMapping("/{encounterId}/publish")
  public ResponseEntity<EncounterResource> publishEncounter(@PathVariable Long encounterId) {
    var command = new com.hampcoders.glottia.platform.api.encounters.domain.model.commands.PublishEncounterCommand(encounterId);
    var encounter = encounterCommandService.handle(command);

    return encounter.map(e -> ResponseEntity.ok(toResource(e)))
        .orElse(ResponseEntity.badRequest().build());
  }

  @Operation(summary = "Cancelar un encounter (hecho por el creador o admin)")
  @DeleteMapping("/{encounterId}")
  public ResponseEntity<EncounterResource> cancelEncounter(@PathVariable Long encounterId, @RequestBody CancelEncounterResource resource) {
    LearnerId cancellerId = new LearnerId(1L); // Placeholder, integrar con SecurityContext

    var command = CancelEncounterCommandFromResourceAssembler.toCommandFromResource(encounterId, cancellerId, resource);
    var encounter = encounterCommandService.handle(command);

    return encounter.map(e -> ResponseEntity.ok(toResource(e)))
        .orElse(ResponseEntity.badRequest().build());
  }

  @Operation(summary = "Marcar un encounter como iniciado (Sistema)")
  @PostMapping("/{encounterId}/start")
  public ResponseEntity<EncounterResource> startEncounter(@PathVariable Long encounterId) {
    var command = new com.hampcoders.glottia.platform.api.encounters.domain.model.commands.StartEncounterCommand(encounterId);
    var encounter = encounterCommandService.handle(command);

    return encounter.map(e -> ResponseEntity.ok(toResource(e)))
        .orElse(ResponseEntity.badRequest().build());
  }

  @Operation(summary = "Marcar un encounter como completado (Sistema)")
  @PostMapping("/{encounterId}/complete")
  public ResponseEntity<EncounterResource> completeEncounter(@PathVariable Long encounterId) {
    var command = new com.hampcoders.glottia.platform.api.encounters.domain.model.commands.CompleteEncounterCommand(encounterId);
    var encounter = encounterCommandService.handle(command);

    return encounter.map(e -> ResponseEntity.ok(toResource(e)))
        .orElse(ResponseEntity.badRequest().build());
  }

  @Operation(summary = "Obtener todos los encounters de un learner")
  @GetMapping("/by-learner/{learnerId}")
  public ResponseEntity<List<EncounterResource>> getEncountersByLearnerId(@PathVariable Long learnerId) {
      var query = new GetEncountersByLearnerIdQuery(new LearnerId(learnerId));
      var encounters = encounterQueryService.handle(query);

      var resources = encounters.stream()
          .map(this::toResource)
          .collect(Collectors.toList());

      return ResponseEntity.ok(resources);
  }

  @Operation(summary = "Búsqueda simple de encounters con filtros opcionales")
  @GetMapping("/search-simple")
  public ResponseEntity<List<EncounterResource>> searchEncountersSimple(
      @RequestParam(required = false) Long languageId,
      @RequestParam(required = false) Long cefrLevelId,
      @RequestParam(required = false) String topic,
      @RequestParam(defaultValue = "0") Integer page,
      @RequestParam(defaultValue = "10") Integer size
  ) {
      var query = new SearchEncountersSimpleQuery(
          Optional.ofNullable(languageId),
          Optional.ofNullable(cefrLevelId),
          Optional.ofNullable(topic),
          page,
          size
      );
      var encounters = encounterQueryService.handle(query);

      var resources = encounters.stream()
          .map(this::toResource)
          .collect(Collectors.toList());

      return ResponseEntity.ok(resources);
  }
}