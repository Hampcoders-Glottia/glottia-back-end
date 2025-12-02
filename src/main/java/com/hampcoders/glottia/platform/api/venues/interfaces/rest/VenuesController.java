package com.hampcoders.glottia.platform.api.venues.interfaces.rest;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hampcoders.glottia.platform.api.shared.interfaces.rest.resources.MessageResource;
import com.hampcoders.glottia.platform.api.venues.domain.model.commands.venues.ActivateVenueCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.commands.venues.DeactivateVenueCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.venues.GetActiveVenuesQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.venues.GetAllVenuesQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.venues.GetAvailableSlotsForVenueQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.venues.GetTotalVenuesCountQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.venues.GetVenueByIdQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.venues.GetVenueEncounterStatisticsQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.venues.GetVenuesByCityQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.venues.GetVenuesByVenueTypeIdQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.venues.GetVenuesWithActivePromotionsQuery;
import com.hampcoders.glottia.platform.api.venues.domain.services.VenueCommandService;
import com.hampcoders.glottia.platform.api.venues.domain.services.VenueQueryService;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.CountResource;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.VenueEncounterStatisticsResource;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.tables.AvailableSlotResource;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.venues.CreateVenueResource;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.venues.UpdateVenueDetailsResource;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.venues.VenueResource;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.transform.fromentity.AvailableSlotResourceFromEntityAssembler;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.transform.fromentity.VenueResourceFromEntityAssembler;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.transform.fromresource.CreateVenueCommandFromResourceAssembler;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.transform.fromresource.UpdateVenueDetailsCommandFromResourceAssembler;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


/**
 * VenuesController
 * <p>
 * RESTful controller for managing venues.
 * Handles venue registration, updates, activation/deactivation, and queries.
 * </p>
 */
@RestController
@RequestMapping(value = "/api/v1/venues", produces = "application/json")
@Tag(name = "Venues", description = "Available Venue Endpoints")
public class VenuesController {
    
    private final VenueCommandService venueCommandService;
    private final VenueQueryService venueQueryService;

    public VenuesController(VenueCommandService venueCommandService, 
                           VenueQueryService venueQueryService) {
        this.venueCommandService = venueCommandService;
        this.venueQueryService = venueQueryService;
    }

    /**
     * Create a new venue
     * 
     * @param resource The venue creation data
     * @return The created venue resource
     */
    @PostMapping
    @Operation(
        summary = "Create a new venue", 
        description = "Creates a new venue with address and type"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Venue created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data or duplicate venue"),
        @ApiResponse(responseCode = "404", description = "Venue not found after creation")
    })
    public ResponseEntity<VenueResource> createVenue(@RequestBody CreateVenueResource resource) {
        var createVenueCommand = CreateVenueCommandFromResourceAssembler.toCommandFromResource(resource);
        var venueId = venueCommandService.handle(createVenueCommand);
        
        if (venueId == null || venueId == 0L) {
            return ResponseEntity.badRequest().build();
        }
        
        var getVenueByIdQuery = new GetVenueByIdQuery(venueId);
        var venue = venueQueryService.handle(getVenueByIdQuery);
        
        if (venue.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        var venueResource = VenueResourceFromEntityAssembler.toResourceFromEntity(venue.get());
        return new ResponseEntity<>(venueResource, HttpStatus.CREATED);
    }

    /**
     * Get venue by ID
     * 
     * @param venueId The venue identifier
     * @return The venue resource
     */
    @GetMapping("/{venueId}")
    @Operation(
        summary = "Get venue by ID", 
        description = "Retrieves a specific venue by its identifier"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Venue found"),
        @ApiResponse(responseCode = "404", description = "Venue not found")
    })
    public ResponseEntity<VenueResource> getVenueById(@PathVariable Long venueId) {
        var getVenueByIdQuery = new GetVenueByIdQuery(venueId);
        var venue = venueQueryService.handle(getVenueByIdQuery);
        
        if (venue.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        var venueResource = VenueResourceFromEntityAssembler.toResourceFromEntity(venue.get());
        return ResponseEntity.ok(venueResource);
    }

    /**
     * Get all venues with optional filters
     * 
     * @param city Optional city filter
     * @param typeId Optional venue type ID filter
     * @param active Optional active status filter
     * @return List of venues matching filters
     */
    @GetMapping
    @Operation(
        summary = "Get all venues", 
        description = "Retrieves all venues with optional filters (city, type, active status)"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Venues retrieved successfully")
    })
    public ResponseEntity<List<VenueResource>> getAllVenues(
            @Parameter(description = "Filter by city name")
            @RequestParam(required = false) String city,
            @Parameter(description = "Filter by venue type ID")
            @RequestParam(required = false) Long typeId,
            @Parameter(description = "Filter by active status")
            @RequestParam(required = false) Boolean active) {
        
        List<com.hampcoders.glottia.platform.api.venues.domain.model.aggregates.Venue> venues;
        
        if (city != null && !city.isBlank()) {
            venues = venueQueryService.handle(new GetVenuesByCityQuery(city));
        } else if (typeId != null) {
            venues = venueQueryService.handle(new GetVenuesByVenueTypeIdQuery(typeId));
        } else if (Boolean.TRUE.equals(active)) {
            venues = venueQueryService.handle(new GetActiveVenuesQuery());
        } else {
            venues = venueQueryService.handle(new GetAllVenuesQuery());
        }
        
        var venueResources = venues.stream()
                .map(VenueResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        
        return ResponseEntity.ok(venueResources);
    }

    /**
     * Update venue details
     * 
     * @param venueId The venue identifier
     * @param resource The updated venue data
     * @return The updated venue resource
     */
    @PutMapping("/{venueId}")
    @Operation(
        summary = "Update venue details", 
        description = "Updates venue information (name, address, type)"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Venue updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Venue not found")
    })
    public ResponseEntity<VenueResource> updateVenue(
            @PathVariable Long venueId,
            @RequestBody UpdateVenueDetailsResource resource) {
        
        var updateCommand = UpdateVenueDetailsCommandFromResourceAssembler
                .toCommandFromResource(venueId, resource);
        
        venueCommandService.handle(updateCommand);
        
        var getVenueByIdQuery = new GetVenueByIdQuery(venueId);
        var venue = venueQueryService.handle(getVenueByIdQuery);
        
        if (venue.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        var venueResource = VenueResourceFromEntityAssembler.toResourceFromEntity(venue.get());
        return ResponseEntity.ok(venueResource);
    }

    /**
     * Deactivate a venue
     * 
     * @param venueId The venue identifier
     * @return Success message
     */
    @DeleteMapping("/{venueId}")
    @Operation(
        summary = "Deactivate venue", 
        description = "Deactivates a venue (soft delete)"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Venue deactivated successfully"),
        @ApiResponse(responseCode = "404", description = "Venue not found")
    })
    public ResponseEntity<MessageResource> deactivateVenue(@PathVariable Long venueId) {
        var deactivateCommand = new DeactivateVenueCommand(venueId);
        venueCommandService.handle(deactivateCommand);
        
        return ResponseEntity.ok(
            new MessageResource("Venue with ID " + venueId + " successfully deactivated"));
    }

    /**
     * Activate a venue
     * 
     * @param venueId The venue identifier
     * @return Success message
     */
    @PostMapping("/{venueId}/activations")
    @Operation(
        summary = "Activate venue", 
        description = "Activates a previously deactivated venue"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Venue activated successfully"),
        @ApiResponse(responseCode = "404", description = "Venue not found")
    })
    public ResponseEntity<MessageResource> activateVenue(@PathVariable Long venueId) {
        var activateCommand = new ActivateVenueCommand(venueId);
        venueCommandService.handle(activateCommand);
        
        return ResponseEntity.ok(
            new MessageResource("Venue with ID " + venueId + " successfully activated"));
    }

    /**
     * Get total venues count
     * 
     * @return Total count
     */
    @GetMapping("/count")
    @Operation(
        summary = "Get total venues count", 
        description = "Returns the total number of venues in the system"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Count retrieved")
    })
    public ResponseEntity<CountResource> getTotalVenuesCount() {
        var count = venueQueryService.handle(new GetTotalVenuesCountQuery());
        return ResponseEntity.ok(new CountResource(count));
    }

    /**
     * Get venues with active promotions
     * 
     * @return List of venues with active promotions
     */
    @GetMapping("/with-promotions")
    @Operation(
        summary = "Get venues with active promotions", 
        description = "Returns all venues that have active promotions"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Venues retrieved")
    })
    public ResponseEntity<List<VenueResource>> getVenuesWithActivePromotions() {
        var venues = venueQueryService.handle(new GetVenuesWithActivePromotionsQuery());
        var resources = venues.stream()
                .map(VenueResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    /**
     * Get available slots for a venue.
     * 
     * @param venueId   The venue ID
     * @param startDate Start date filter (optional, defaults to today)
     * @param endDate   End date filter (optional, null = all future slots)
     * @return List of available slots
     */
    @GetMapping("/{venueId}/available-slots")
    public ResponseEntity<List<AvailableSlotResource>> getAvailableSlots(
            @PathVariable Long venueId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        var query = new GetAvailableSlotsForVenueQuery(venueId, startDate, endDate);
        var slots = venueQueryService.handle(query);
        
        var resources = slots.stream()
                .map(AvailableSlotResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        
        return ResponseEntity.ok(resources);
    }

    /**
     * Get encounter statistics for a venue.
     * 
     * @param venueId  The venue ID
     * @param month    Month number (1-12) - used with year
     * @param year     Year - used with month
     * @param lastDays Number of last days to fetch (alternative to month/year)
     * @return Venue encounter statistics grouped by week
     */
    @GetMapping("/{venueId}/encounter-statistics")
    public ResponseEntity<VenueEncounterStatisticsResource> getEncounterStatistics(
            @PathVariable Long venueId,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false, defaultValue = "30") Integer lastDays
    ) {
        GetVenueEncounterStatisticsQuery query;
        
        if (month != null && year != null) {
            query = GetVenueEncounterStatisticsQuery.forMonth(venueId, month, year);
        } else {
            query = GetVenueEncounterStatisticsQuery.forLastDays(venueId, lastDays);
        }
        
        var statistics = venueQueryService.handle(query);
        return ResponseEntity.ok(statistics);
    }
}