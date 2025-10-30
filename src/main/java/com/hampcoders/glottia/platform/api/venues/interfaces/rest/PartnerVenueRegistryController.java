package com.hampcoders.glottia.platform.api.venues.interfaces.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hampcoders.glottia.platform.api.shared.interfaces.rest.resources.MessageResource;
import com.hampcoders.glottia.platform.api.venues.domain.model.commands.venues.DeactivateVenueRegistrationCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.commands.venues.ReactivateVenueRegistrationCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.entities.VenueRegistration;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.venues.GetAllVenueRegistrationsByPartnerIdQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.venues.GetPartnerRegistriesWithActiveVenuesQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.venues.GetPartnerVenueRegistryByPartnerIdQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.venues.GetVenueCountByPartnerIdQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.venues.GetVenueRegistrationsByPartnerIdAndStatusQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.valueobjects.PartnerId;
import com.hampcoders.glottia.platform.api.venues.domain.services.PartnerVenueRegistryCommandService;
import com.hampcoders.glottia.platform.api.venues.domain.services.PartnerVenueRegistryQueryService;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.CountResource;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.venues.AddVenueToPartnerRegistryResource;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.venues.CreatePartnerVenueRegistryResource;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.venues.DeactivateVenueRegistrationResource;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.venues.PartnerVenueRegistryResource;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.venues.VenueRegistrationResource;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.transform.fromentity.PartnerVenueRegistryResourceFromEntityAssembler;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.transform.fromentity.VenueRegistrationResourceFromEntityAssembler;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.transform.fromresource.AddVenueToPartnerRegistryCommandFromResourceAssembler;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.transform.fromresource.CreatePartnerVenueRegistryCommandFromResourceAssembler;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/partner-venue-registries")
@Tag(name = "Partner Venue Registries", description = "Partner Venue Registry Endpoints")
public class PartnerVenueRegistryController {

    private final PartnerVenueRegistryCommandService registryCommandService;
    private final PartnerVenueRegistryQueryService registryQueryService;
    private static final Logger logger = LoggerFactory.getLogger(PartnerVenueRegistryController.class);


    public PartnerVenueRegistryController(
            PartnerVenueRegistryCommandService registryCommandService,
            PartnerVenueRegistryQueryService registryQueryService) {
        this.registryCommandService = registryCommandService;
        this.registryQueryService = registryQueryService;
    }

    /**
     * Create a partner venue registry
     * 
     * @param resource The partner ID
     * @return The created registry resource
     */
    @PostMapping("/registries")
    @Operation(
        summary = "Create partner venue registry", 
        description = "Creates a new venue registry for a partner"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Registry created successfully"),
        @ApiResponse(responseCode = "400", description = "Partner already has a registry")
    })
    public ResponseEntity<PartnerVenueRegistryResource> createPartnerVenueRegistry(@Valid @RequestBody CreatePartnerVenueRegistryResource resource) {
        logger.info("Received resource: {}", resource);
        if (resource.partnerId() == null) {
            logger.warn("Partner ID is null in request");
            return ResponseEntity.badRequest().build();
        }
        var createPartnerVenueRegistryCommand = CreatePartnerVenueRegistryCommandFromResourceAssembler.toCommandFromResource(resource);
        logger.info("Attempting to create registry for partnerId: {}", resource.partnerId());
        
        var registryId = registryCommandService.handle(createPartnerVenueRegistryCommand);
        logger.info("Registry creation returned ID: {}", registryId);

        if (registryId == null || registryId == 0L) {
            logger.error("Registry creation failed for partnerId: {}", resource.partnerId());
            return ResponseEntity.badRequest().build();
        }

        var getRegistryQuery = new GetPartnerVenueRegistryByPartnerIdQuery(new PartnerId(resource.partnerId()));
        var registry = registryQueryService.handle(getRegistryQuery);

        if (registry.isEmpty()) {
            logger.error("Registry not found after creation for partnerId: {}", resource.partnerId());
            return ResponseEntity.notFound().build();
        }

        var registryResource = PartnerVenueRegistryResourceFromEntityAssembler.toResourceFromEntity(registry.get());
        logger.info("Registry created successfully for partnerId: {}", resource.partnerId());
        
        return new ResponseEntity<>(registryResource, HttpStatus.CREATED);
    }

    /**
     * Add venue to partner registry
     * 
     * @param partnerId The partner identifier
     * @param resource The venue assignment data
     * @return Success message
     */
    @PostMapping("/{partnerId}/venues")
    @Operation(
        summary = "Add venue to partner", 
        description = "Registers a venue to a partner's registry"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Venue added to partner successfully"),
        @ApiResponse(responseCode = "400", description = "Venue already registered to partner"),
        @ApiResponse(responseCode = "404", description = "Partner registry or venue not found")
    })
    public ResponseEntity<PartnerVenueRegistryResource> addVenueToPartner(@PathVariable Long partnerId, @RequestBody @Valid AddVenueToPartnerRegistryResource resource) 
    {
        var command = AddVenueToPartnerRegistryCommandFromResourceAssembler.toCommandFromResource(partnerId, resource);

        registryCommandService.handle(command);
        var updatedRegistry = registryQueryService.handle(
            new GetPartnerVenueRegistryByPartnerIdQuery(new PartnerId(partnerId)));


        return updatedRegistry.map(reg -> new ResponseEntity<>(PartnerVenueRegistryResourceFromEntityAssembler.toResourceFromEntity(reg), HttpStatus.OK)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Get partner's venue registry
     * 
     * @param partnerId The partner identifier
     * @return The partner's venue registry
     */
    @GetMapping("/{partnerId}/registry")
    @Operation(
        summary = "Get partner venue registry", 
        description = "Retrieves the venue registry for a specific partner"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Registry retrieved"),
        @ApiResponse(responseCode = "404", description = "Registry not found")
    })
    public ResponseEntity<PartnerVenueRegistryResource> getPartnerRegistry(
            @PathVariable Long partnerId) {
        
        var query = new GetPartnerVenueRegistryByPartnerIdQuery(new PartnerId(partnerId));
        var registry = registryQueryService.handle(query);
        
        if (registry.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        var resource = PartnerVenueRegistryResourceFromEntityAssembler
                .toResourceFromEntity(registry.get());
        
        return ResponseEntity.ok(resource);
    }

    /**
     * Get all venue registrations for a partner
     * 
     * @param partnerId The partner identifier
     * @param active Optional filter for active registrations only
     * @return List of venue registrations
     */
    @GetMapping("/{partnerId}/venues")
    @Operation(
        summary = "Get partner's venues", 
        description = "Retrieves all venue registrations for a partner"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Venues retrieved")
    })
    public ResponseEntity<List<VenueRegistrationResource>> getPartnerVenues(@PathVariable Long partnerId, @Parameter(description = "Filter by active status") @RequestParam(required = false) Boolean active) {
        List<VenueRegistration> registrations;
        
        if (active != null) {
            registrations = registryQueryService.handle(
                new GetVenueRegistrationsByPartnerIdAndStatusQuery(new PartnerId(partnerId), active));
        } else {
            registrations = registryQueryService.handle(
                new GetAllVenueRegistrationsByPartnerIdQuery(new PartnerId(partnerId)));
        }
        
        var resources = registrations.stream()
                .map(VenueRegistrationResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        
        return ResponseEntity.ok(resources);
    }

    /**
     * Get partner's venue count
     * 
     * @param partnerId The partner identifier
     * @return Venue count
     */
    @GetMapping("/{partnerId}/venues/count")
    @Operation(
        summary = "Get partner venue count", 
        description = "Returns the total number of venues for a partner"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Count retrieved")
    })
    public ResponseEntity<CountResource> getPartnerVenueCount(@PathVariable Long partnerId) {
        var count = registryQueryService.handle(new GetVenueCountByPartnerIdQuery(new PartnerId(partnerId)));
        return ResponseEntity.ok(new CountResource(count));
    }

    /**
     * Deactivate venue registration
     * 
     * @param partnerId The partner identifier
     * @param venueId The venue identifier
     * @param resource Deactivation reason
     * @return Success message
     */
    @DeleteMapping("/{partnerId}/venues/{venueId}")
    @Operation(
        summary = "Deactivate venue registration", 
        description = "Deactivates a venue's registration with a partner"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Registration deactivated"),
        @ApiResponse(responseCode = "404", description = "Partner registry not found")
    })
    public ResponseEntity<MessageResource> deactivateVenueRegistration(
            @PathVariable Long partnerId,
            @PathVariable Long venueId,
            @RequestBody(required = false) DeactivateVenueRegistrationResource resource) {
        
        var registryQuery = new GetPartnerVenueRegistryByPartnerIdQuery(new PartnerId(partnerId));
        var registry = registryQueryService.handle(registryQuery);
        
        if (registry.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        String reason = (resource != null && resource.reason() != null) 
            ? resource.reason() 
            : "No reason provided";
        
        var deactivateCommand = new DeactivateVenueRegistrationCommand(
            registry.get().getId(), venueId, reason);
        
        registryCommandService.handle(deactivateCommand);
        
        return ResponseEntity.ok(
            new MessageResource("Venue registration successfully deactivated"));
    }

    /**
     * Reactivate venue registration
     * 
     * @param partnerId The partner identifier
     * @param venueId The venue identifier
     * @return Success message
     */
    @PostMapping("/{partnerId}/venues/{venueId}/activations")
    @Operation(
        summary = "Reactivate venue registration", 
        description = "Reactivates a previously deactivated venue registration"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Registration reactivated"),
        @ApiResponse(responseCode = "404", description = "Partner registry not found")
    })
    public ResponseEntity<MessageResource> reactivateVenueRegistration(
            @PathVariable Long partnerId,
            @PathVariable Long venueId) {
        
        var registryQuery = new GetPartnerVenueRegistryByPartnerIdQuery(new PartnerId(partnerId));
        var registry = registryQueryService.handle(registryQuery);
        
        if (registry.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        var reactivateCommand = new ReactivateVenueRegistrationCommand(
            registry.get().getId(), venueId);
        
        registryCommandService.handle(reactivateCommand);
        
        return ResponseEntity.ok(
            new MessageResource("Venue registration successfully reactivated"));
    }

    /**
     * Get all partner registries with active venues
     * 
     * @return List of partner registries
     */
    @GetMapping("/registries/with-active-venues")
    @Operation(
        summary = "Get registries with active venues", 
        description = "Retrieves all partner registries that have active venues"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Registries retrieved")
    })
    public ResponseEntity<List<PartnerVenueRegistryResource>> getRegistriesWithActiveVenues() {
        var registries = registryQueryService.handle(
            new GetPartnerRegistriesWithActiveVenuesQuery());
        
        var resources = registries.stream()
                .map(PartnerVenueRegistryResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        
        return ResponseEntity.ok(resources);
    }
}
