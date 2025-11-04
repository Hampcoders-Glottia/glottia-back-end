package com.hampcoders.glottia.platform.api.encounters.interfaces.rest;

import com.hampcoders.glottia.platform.api.encounters.domain.model.commands.CancelAttendanceCommand;
import com.hampcoders.glottia.platform.api.encounters.domain.model.queries.GetAttendancesByEncounterQuery;
import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.LearnerId;
import com.hampcoders.glottia.platform.api.encounters.domain.services.EncounterCommandService;
import com.hampcoders.glottia.platform.api.encounters.domain.services.EncounterQueryService;
import com.hampcoders.glottia.platform.api.encounters.interfaces.rest.resources.AttendanceResource;
import com.hampcoders.glottia.platform.api.encounters.interfaces.rest.resources.CheckInEncounterResource;
import com.hampcoders.glottia.platform.api.encounters.interfaces.rest.resources.JoinEncounterResource;
import com.hampcoders.glottia.platform.api.encounters.interfaces.rest.transform.AttendanceResourceFromEntityAssembler;
import com.hampcoders.glottia.platform.api.encounters.interfaces.rest.transform.CheckInEncounterCommandFromResourceAssembler;
import com.hampcoders.glottia.platform.api.encounters.interfaces.rest.transform.JoinEncounterCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/encounters/{encounterId}/attendances", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Encounters")
public class EncounterAttendancesController {

    private final EncounterCommandService encounterCommandService;
    private final EncounterQueryService encounterQueryService;

    public EncounterAttendancesController(EncounterCommandService encounterCommandService, EncounterQueryService encounterQueryService) {
        this.encounterCommandService = encounterCommandService;
        this.encounterQueryService = encounterQueryService;
    }

    @Operation(summary = "Unirse a un encounter (Inscribir asistencia)")
    @PostMapping
    public ResponseEntity<AttendanceResource> joinEncounter(
            @PathVariable Long encounterId,
            @RequestBody JoinEncounterResource resource
    ) {
        var command = JoinEncounterCommandFromResourceAssembler.toCommandFromResource(encounterId, resource);
        var encounter = encounterCommandService.handle(command);
        
        // Devolvemos la asistencia recién creada
        return encounter.flatMap(e -> e.getAttendances().findByLearnerId(command.learnerId())
                .map(a -> new ResponseEntity<>(AttendanceResourceFromEntityAssembler.toResourceFromEntity(a), HttpStatus.CREATED)))
                .orElse(ResponseEntity.badRequest().build());
    }

    @Operation(summary = "Hacer Check-in en un encounter")
    @PostMapping("/check-in")
    public ResponseEntity<AttendanceResource> checkInEncounter(
            @PathVariable Long encounterId,
            @RequestBody CheckInEncounterResource resource
    ) {
        var command = CheckInEncounterCommandFromResourceAssembler.toCommandFromResource(encounterId, resource);
        var encounter = encounterCommandService.handle(command);

        return encounter.flatMap(e -> e.getAttendances().findByLearnerId(command.learnerId())
                .map(a -> ResponseEntity.ok(AttendanceResourceFromEntityAssembler.toResourceFromEntity(a))))
                .orElse(ResponseEntity.badRequest().build());
    }

    @Operation(summary = "Cancelar asistencia a un encounter")
    @DeleteMapping("/me") // Asume que se cancela la asistencia del usuario autenticado
    public ResponseEntity<?> cancelAttendance(
            @PathVariable Long encounterId
            // @AuthenticationPrincipal UserDetails userDetails // Así obtendrías el usuario
    ) {
        // TODO: Obtener LearnerId del contexto de seguridad (o de un ProfileContextFacade)
        LearnerId learnerId = new LearnerId(1L); // Placeholder
        
        var command = new CancelAttendanceCommand(encounterId, learnerId);
        encounterCommandService.handle(command);
        return ResponseEntity.ok("Attendance cancelled");
    }

    @Operation(summary = "Obtener todas las asistencias de un encounter")
    @GetMapping
    public ResponseEntity<List<AttendanceResource>> getAttendancesForEncounter(@PathVariable Long encounterId) {
        var query = new GetAttendancesByEncounterQuery(encounterId);
        var attendances = encounterQueryService.handle(query);
        var resources = attendances.stream()
                .map(AttendanceResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resources);
    }
}