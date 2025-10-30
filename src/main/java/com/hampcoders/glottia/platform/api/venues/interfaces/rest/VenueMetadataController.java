package com.hampcoders.glottia.platform.api.venues.interfaces.rest;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hampcoders.glottia.platform.api.venues.domain.model.queries.types.GetAllPromotionTypesQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.types.GetAllTableStatusesQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.types.GetAllTableTypesQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.types.GetAllVenueTypesQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.types.GetPromotionTypeByNameQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.types.GetTableStatusByNameQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.types.GetTableTypeByNameQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.types.GetVenueTypeByNameQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.valueobjects.PromotionTypes;
import com.hampcoders.glottia.platform.api.venues.domain.model.valueobjects.TableStatuses;
import com.hampcoders.glottia.platform.api.venues.domain.model.valueobjects.TableTypes;
import com.hampcoders.glottia.platform.api.venues.domain.model.valueobjects.VenueTypes;
import com.hampcoders.glottia.platform.api.venues.domain.services.PromotionTypeQueryService;
import com.hampcoders.glottia.platform.api.venues.domain.services.TableStatusQueryService;
import com.hampcoders.glottia.platform.api.venues.domain.services.TableTypeQueryService;
import com.hampcoders.glottia.platform.api.venues.domain.services.VenueTypeQueryService;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.types.PromotionTypeResource;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.types.TableStatusResource;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.types.TableTypeResource;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.types.VenueTypeResource;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.transform.fromentity.PromotionTypeResourceFromEntityAssembler;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.transform.fromentity.TableStatusResourceFromEntityAssembler;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.transform.fromentity.TableTypeResourceFromEntityAssembler;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.transform.fromentity.VenueTypeResourceFromEntityAssembler;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * VenueMetadataController
 * <p>
 * Exposes catalog/reference data (enums) needed for venue operations.
 * These are read-only resources seeded at application startup.
 * Supports fetching all items or querying by name.
 * </p>
 */
@RestController
@RequestMapping(value = "/api/v1/venues/metadata", produces = "application/json")
@Tag(name = "Venues", description = "Venue Metadata and Catalogs")
public class VenueMetadataController {

    private final TableTypeQueryService tableTypeQueryService;
    private final PromotionTypeQueryService promotionTypeQueryService;
    private final TableStatusQueryService tableStatusQueryService;
    private final VenueTypeQueryService venueTypeQueryService;

    public VenueMetadataController(
            TableTypeQueryService tableTypeQueryService,
            PromotionTypeQueryService promotionTypeQueryService,
            TableStatusQueryService tableStatusQueryService,
            VenueTypeQueryService venueTypeQueryService) {
        this.tableTypeQueryService = tableTypeQueryService;
        this.promotionTypeQueryService = promotionTypeQueryService;
        this.tableStatusQueryService = tableStatusQueryService;
        this.venueTypeQueryService = venueTypeQueryService;
    }

    /**
     * Get venue types catalog
     * Supports optional filtering by name query parameter
     * 
     * @param name Optional name filter (e.g., "CAFE", "RESTAURANT")
     * @return List of venue types or single venue type if name is provided
     */
    @GetMapping("/venue-types")
    @Operation(
        summary = "Get venue types", 
        description = "Returns all venue types or filters by name if provided"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Venue types retrieved"),
        @ApiResponse(responseCode = "404", description = "Venue type not found (when filtering by name)")
    })
    public ResponseEntity<List<VenueTypeResource>> getVenueTypes(@Parameter(description = "Filter by venue type name (e.g., CAFE, RESTAURANT, BAR)") @RequestParam(required = false) String name) {
        if (name != null && !name.isBlank()) {
            VenueTypes venueTypeEnum = VenueTypes.valueOf(name.toUpperCase());
            var getVenueTypeByNameQuery = new GetVenueTypeByNameQuery(venueTypeEnum);
            var venueType = venueTypeQueryService.handle(getVenueTypeByNameQuery);
            
            if (venueType.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            var resource = VenueTypeResourceFromEntityAssembler
                .toResourceFromEntity(venueType.get());
            
            return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS))
                .body(List.of(resource));
        }
        
        var venueTypes = venueTypeQueryService.handle(new GetAllVenueTypesQuery());
        var resources = venueTypes.stream()
                .map(VenueTypeResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        
        return ResponseEntity.ok()
            .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS))
            .body(resources); 
    }

    /**
     * Get table types catalog
     * Supports optional filtering by name query parameter
     * 
     * @param name Optional name filter (e.g., "ENCOUNTER_TABLE", "GENERAL_TABLE")
     * @return List of table types or single table type if name is provided
     */
    @GetMapping("/table-types")
    @Operation(
        summary = "Get table types", 
        description = "Returns all table types or filters by name if provided"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Table types retrieved"),
        @ApiResponse(responseCode = "404", description = "Table type not found (when filtering by name)")
    })
    public ResponseEntity<List<TableTypeResource>> getTableTypes(@Parameter(description = "Filter by table type name (e.g., ENCOUNTER_TABLE, GENERAL_TABLE)") @RequestParam(required = false) String name) {
        if (name != null && !name.isBlank()) {
            TableTypes tableTypeEnum = TableTypes.valueOf(name.toUpperCase());
            var getTableTypeByNameQuery = new GetTableTypeByNameQuery(tableTypeEnum);
            var tableType = tableTypeQueryService.handle(getTableTypeByNameQuery);
            
            if (tableType.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            var resource = TableTypeResourceFromEntityAssembler
                .toResourceFromEntity(tableType.get());
            
            return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS))
                .body(List.of(resource));
        }
        
        // Get all
        var tableTypes = tableTypeQueryService.handle(new GetAllTableTypesQuery());
        var resources = tableTypes.stream()
                .map(TableTypeResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        
        return ResponseEntity.ok()
            .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS))
            .body(resources);
    }

    /**
     * Get promotion types catalog
     * Supports optional filtering by name query parameter
     * 
     * @param name Optional name filter (e.g., "HAPPY_HOUR", "SEASONAL_OFFER")
     * @return List of promotion types or single promotion type if name is provided
     */
    @GetMapping("/promotion-types")
    @Operation(
        summary = "Get promotion types", 
        description = "Returns all promotion types or filters by name if provided"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Promotion types retrieved"),
        @ApiResponse(responseCode = "404", description = "Promotion type not found (when filtering by name)")
    })
    public ResponseEntity<List<PromotionTypeResource>> getPromotionTypes(@Parameter(description = "Filter by promotion type name (e.g., HAPPY_HOUR, SEASONAL_OFFER)") @RequestParam(required = false) String name) {
        if (name != null && !name.isBlank()) {
            PromotionTypes promotionTypeEnum = PromotionTypes.valueOf(name.toUpperCase());
            var getPromotionTypeByNameQuery = new GetPromotionTypeByNameQuery(promotionTypeEnum);
            var promotionType = promotionTypeQueryService.handle(getPromotionTypeByNameQuery);

            if (promotionType.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            var resource = PromotionTypeResourceFromEntityAssembler
                .toResourceFromEntity(promotionType.get());
            
            return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS))
                .body(List.of(resource));
        }

        var promotionTypes = promotionTypeQueryService.handle(new GetAllPromotionTypesQuery());
        var resources = promotionTypes.stream()
                .map(PromotionTypeResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok()
            .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS))
            .body(resources);
    }

    /**
     * Get table statuses catalog
     * Supports optional filtering by name query parameter
     * @param name Optional name filter (e.g., "AVAILABLE", "OCCUPIED", "RESERVED")
     * @return List of table statuses or single table status if name is provided
     */
    @GetMapping("/table-statuses")
    @Operation(
        summary = "Get table statuses", 
        description = "Returns all table statuses or filters by name if provided"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Table statuses retrieved"),
        @ApiResponse(responseCode = "404", description = "Table status not found (when filtering by name)")
    })
    public ResponseEntity<List<TableStatusResource>> getTableStatuses(@Parameter(description = "Filter by table status name (e.g., AVAILABLE, OCCUPIED, RESERVED)") @RequestParam(required = false) String name) {
        if (name != null && !name.isBlank()) {
            TableStatuses tableStatusEnum = TableStatuses.valueOf(name.toUpperCase());
            var getTableStatusByNameQuery = new GetTableStatusByNameQuery(tableStatusEnum);
            var tableStatus = tableStatusQueryService.handle(getTableStatusByNameQuery);

            if (tableStatus.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            var resource = TableStatusResourceFromEntityAssembler
                .toResourceFromEntity(tableStatus.get());

            return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS))
                .body(List.of(resource));
        }

        var tableStatuses = tableStatusQueryService.handle(new GetAllTableStatusesQuery());
        var resources = tableStatuses.stream()
                .map(TableStatusResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok()
            .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS))
            .body(resources);
    }
}
