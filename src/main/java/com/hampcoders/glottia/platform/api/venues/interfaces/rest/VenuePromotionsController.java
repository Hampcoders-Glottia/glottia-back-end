package com.hampcoders.glottia.platform.api.venues.interfaces.rest;

import java.util.List;

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
import com.hampcoders.glottia.platform.api.venues.domain.model.commands.promotions.ActivateVenuePromotionCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.commands.promotions.DeactivateVenuePromotionCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.entities.VenuePromotion;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.promotions.GetActivePromotionsByVenueIdQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.promotions.GetAllPromotionsByVenueIdQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.promotions.GetExpiredPromotionsByVenueIdQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.promotions.GetVenuePromotionByVenueIdAndPromotionIdQuery;
import com.hampcoders.glottia.platform.api.venues.domain.services.PromotionCommandService;
import com.hampcoders.glottia.platform.api.venues.domain.services.PromotionQueryService;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.promotions.AddPromotionToVenueResource;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.promotions.DeactivateVenuePromotionResource;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.promotions.VenuePromotionResource;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.transform.fromentity.VenuePromotionResourceFromEntityAssembler;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.transform.fromresource.AddPromotionToVenueCommandFromResourceAssembler;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * VenuePromotionsController
 * <p>
 * RESTful controller for managing promotions assigned to venues.
 * Handles the many-to-many relationship between venues and promotions.
 * </p>
 */
@RestController
@RequestMapping(value = "/api/v1/venues/{venueId}/promotions", produces = "application/json")
@Tag(name = "Venues")
public class VenuePromotionsController {
    
    private final PromotionCommandService promotionCommandService;
    private final PromotionQueryService promotionQueryService;

    public VenuePromotionsController(PromotionCommandService promotionCommandService,
                                    PromotionQueryService promotionQueryService) {
        this.promotionCommandService = promotionCommandService;
        this.promotionQueryService = promotionQueryService;
    }

    /**
     * Assign a promotion to a venue
     * 
     * @param venueId The venue identifier
     * @param resource The promotion assignment data
     * @return Success message
     */
    @PostMapping
    @Operation(
        summary = "Assign promotion to venue", 
        description = "Assigns an existing promotion to a venue with specific dates and limits"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Promotion assigned successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data or promotion already assigned"),
        @ApiResponse(responseCode = "404", description = "Venue or promotion not found")
    })
    public ResponseEntity<VenuePromotionResource> addPromotionToVenue(
            @PathVariable Long venueId,
            @RequestBody AddPromotionToVenueResource resource) {
        
        var addPromotionCommand = AddPromotionToVenueCommandFromResourceAssembler
                .toCommandFromResource(venueId, resource);
        
        promotionCommandService.handle(addPromotionCommand);
        var venuePromotion = promotionQueryService.handle(new GetVenuePromotionByVenueIdAndPromotionIdQuery(venueId, resource.promotionId()));
        return new ResponseEntity<>(VenuePromotionResourceFromEntityAssembler
                .toResourceFromEntity(venuePromotion.get()), HttpStatus.CREATED);
    }

    /**
     * Get all promotions for a venue
     * 
     * @param venueId The venue identifier
     * @param active Optional filter for active promotions only
     * @param expired Optional filter for expired promotions only
     * @return List of venue promotions
     */
    @GetMapping
    @Operation(
        summary = "Get venue promotions", 
        description = "Retrieves all promotions assigned to a specific venue"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Promotions retrieved successfully")
    })
    public ResponseEntity<List<VenuePromotionResource>> getVenuePromotions(
            @PathVariable Long venueId,
            @Parameter(description = "Filter by active status")
            @RequestParam(required = false) Boolean active,
            @Parameter(description = "Show only expired promotions")
            @RequestParam(required = false) Boolean expired) {
        
        List<VenuePromotion> venuePromotions;
        
        if (Boolean.TRUE.equals(active)) {
            venuePromotions = promotionQueryService.handle(
                new GetActivePromotionsByVenueIdQuery(venueId));
        } else if (Boolean.TRUE.equals(expired)) {
            venuePromotions = promotionQueryService.handle(
                new GetExpiredPromotionsByVenueIdQuery(venueId));
        } else {
            venuePromotions = promotionQueryService.handle(
                new GetAllPromotionsByVenueIdQuery(venueId));
        }
        
        var resources = venuePromotions.stream()
                .map(VenuePromotionResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        
        return ResponseEntity.ok(resources);
    }

    /**
     * Deactivate a venue promotion
     * 
     * @param venueId The venue identifier
     * @param venuePromotionId The venue promotion identifier
     * @param resource Deactivation reason (optional)
     * @return Success message
     */
    @DeleteMapping("/{venuePromotionId}")
    @Operation(
        summary = "Deactivate venue promotion", 
        description = "Deactivates a promotion assignment for a venue"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Venue promotion deactivated"),
        @ApiResponse(responseCode = "404", description = "Venue promotion not found")
    })
    public ResponseEntity<MessageResource> deactivateVenuePromotion(
            @PathVariable Long venueId,
            @PathVariable Long venuePromotionId,
            @RequestBody(required = false) DeactivateVenuePromotionResource resource) {
        
        String reason = (resource != null && resource.reason() != null) 
            ? resource.reason() 
            : "No reason provided";

        var deactivateCommand = new DeactivateVenuePromotionCommand(venueId, venuePromotionId, reason);
        promotionCommandService.handle(deactivateCommand);
        
        return ResponseEntity.ok(
            new MessageResource("Venue promotion successfully deactivated"));
    }

    /**
     * Activate a venue promotion
     * 
     * @param venueId The venue identifier
     * @param venuePromotionId The venue promotion identifier
     * @return Success message
     */
    @PostMapping("/{venuePromotionId}/activations")
    @Operation(
        summary = "Activate venue promotion", 
        description = "Activates a previously deactivated promotion assignment"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Venue promotion activated"),
        @ApiResponse(responseCode = "404", description = "Venue promotion not found"),
        @ApiResponse(responseCode = "400", description = "Promotion already active or expired")
    })
    public ResponseEntity<MessageResource> activateVenuePromotion(
            @PathVariable Long venueId,
            @PathVariable Long venuePromotionId) {

        var activateCommand = new ActivateVenuePromotionCommand(venueId, venuePromotionId);
        promotionCommandService.handle(activateCommand);
        
        return ResponseEntity.ok(
            new MessageResource("Venue promotion successfully activated"));
    }
}