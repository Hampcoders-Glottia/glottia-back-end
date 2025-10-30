package com.hampcoders.glottia.platform.api.venues.interfaces.rest;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hampcoders.glottia.platform.api.venues.domain.model.queries.tables.GetAvailableTablesByVenueIdAndDateAndMinimumCapacityQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.tables.GetTableCountByVenueIdQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.tables.GetTableRegistryByVenueIdQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.tables.GetTablesByVenueIdQuery;
import com.hampcoders.glottia.platform.api.venues.domain.services.TableRegistryCommandService;
import com.hampcoders.glottia.platform.api.venues.domain.services.TableRegistryQueryService;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.CountResource;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.tables.AddTableResource;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.tables.TableRegistryResource;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.tables.TableResource;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.transform.fromentity.TableRegistryResourceFromEntityAssembler;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.transform.fromentity.TableResourceFromEntityAssembler;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.transform.fromresource.AddTableCommandFromResourceAssembler;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * VenueTablesController
 * <p>
 * RESTful controller for managing tables within a venue.
 * Tables are sub-resources of venues, hence the nested URL structure.
 * </p>
 */
@RestController
@RequestMapping(value = "/api/v1/venues/{venueId}/tables", produces = "application/json")
@Tag(name = "Venues")
public class VenueTablesController {
    
    private final TableRegistryCommandService tableCommandService;
    private final TableRegistryQueryService tableQueryService;

    public VenueTablesController(TableRegistryCommandService tableCommandService,
                                TableRegistryQueryService tableQueryService) {
        this.tableCommandService = tableCommandService;
        this.tableQueryService = tableQueryService;
    }

    /**
     * Add a table to a venue
     * 
     * @param venueId The venue identifier
     * @param resource The table data
     * @return Success message
     */
    @PostMapping
    @Operation(
        summary = "Add table to venue", 
        description = "Adds a new table to the specified venue's registry"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Table created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Venue or table registry not found")
    })
    public ResponseEntity<TableRegistryResource> addTableToVenue(@PathVariable Long venueId, @RequestBody AddTableResource resource) {
        
        var command = AddTableCommandFromResourceAssembler
                .toCommandFromResource(venueId, resource);
        
        tableCommandService.handle(command);

        var tableRegistry = tableQueryService.handle(
            new GetTableRegistryByVenueIdQuery(venueId));

        return tableRegistry.map(reg -> new ResponseEntity<>(TableRegistryResourceFromEntityAssembler.toResourceFromEntity(reg), HttpStatus.OK)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Get all tables for a venue
     * 
     * @param venueId The venue identifier
     * @return List of tables
     */
    @GetMapping
    @Operation(
        summary = "Get venue tables", 
        description = "Retrieves all tables for a specific venue"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tables retrieved successfully")
    })
    public ResponseEntity<List<TableResource>> getTablesByVenue(@PathVariable Long venueId) {
        var tables = tableQueryService.handle(new GetTablesByVenueIdQuery(venueId));
        var resources = tables.stream()
                .map(TableResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    /**
     * Get available tables for a specific date
     * 
     * @param venueId The venue identifier
     * @param date The date to check availability
     * @param minCapacity Optional minimum capacity filter
     * @return List of available tables
     */
    @GetMapping("/available")
    @Operation(
        summary = "Get available tables", 
        description = "Retrieves available tables for a specific date and optional minimum capacity"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Available tables retrieved")
    })
    public ResponseEntity<List<TableResource>> getAvailableTables(
            @PathVariable Long venueId,
            @Parameter(description = "Date to check availability (format: yyyy-MM-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @Parameter(description = "Minimum table capacity")
            @RequestParam(required = false, defaultValue = "1") Integer minCapacity) {
        
        var query = new GetAvailableTablesByVenueIdAndDateAndMinimumCapacityQuery(
            venueId, date, minCapacity);
        
        var tables = tableQueryService.handle(query);
        var resources = tables.stream()
                .map(TableResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        
        return ResponseEntity.ok(resources);
    }

    /**
     * Get table count for venue
     * 
     * @param venueId The venue identifier
     * @return Table count
     */
    @GetMapping("/count")
    @Operation(
        summary = "Get table count", 
        description = "Returns the total number of tables in the venue"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Count retrieved")
    })
    public ResponseEntity<CountResource> getTableCountByVenue(@PathVariable Long venueId) {
        var count = tableQueryService.handle(new GetTableCountByVenueIdQuery(venueId));
        return ResponseEntity.ok(new CountResource(count));
    }
}