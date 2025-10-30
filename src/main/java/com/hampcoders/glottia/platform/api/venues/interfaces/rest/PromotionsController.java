package com.hampcoders.glottia.platform.api.venues.interfaces.rest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hampcoders.glottia.platform.api.venues.domain.model.aggregates.Promotion;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.promotions.GetAllPromotionsQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.promotions.GetPromotionByIdQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.promotions.GetPromotionCatalogQuery;
import com.hampcoders.glottia.platform.api.venues.domain.services.PromotionCommandService;
import com.hampcoders.glottia.platform.api.venues.domain.services.PromotionQueryService;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.promotions.CreatePromotionResource;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.promotions.PromotionResource;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.transform.fromentity.PromotionResourceFromEntityAssembler;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.transform.fromresource.CreatePromotionCommandFromResourceAssembler;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * PromotionsController
 * <p>
 * RESTful controller for managing partner promotions.
 * Handles promotion catalog operations (CRUD).
 * </p>
 */
@RestController
@RequestMapping(value = "/api/v1/promotions", produces = "application/json")
@Tag(name = "Promotions", description = "Available Promotion Endpoints")
public class PromotionsController {
    
    private final PromotionCommandService promotionCommandService;
    private final PromotionQueryService promotionQueryService;

    public PromotionsController(PromotionCommandService promotionCommandService,
                               PromotionQueryService promotionQueryService) {
        this.promotionCommandService = promotionCommandService;
        this.promotionQueryService = promotionQueryService;
    }

    /**
     * Create a new promotion
     * 
     * @param resource The promotion data
     * @return The created promotion resource
     */
    @PostMapping
    @Operation(
        summary = "Create promotion", 
        description = "Creates a new promotion for the authenticated partner"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Promotion created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data or duplicate promotion name")
    })
    public ResponseEntity<PromotionResource> createPromotion(
            @RequestBody CreatePromotionResource resource) {
        
        var createCommand = CreatePromotionCommandFromResourceAssembler
                .toCommandFromResource(resource);
        
        var promotionId = promotionCommandService.handle(createCommand);
        
        if (promotionId == null || promotionId == 0L) {
            return ResponseEntity.badRequest().build();
        }
        var getPromotionByIdQuery = new GetPromotionByIdQuery(promotionId);
        var promotion = promotionQueryService.handle(getPromotionByIdQuery);
        
        if (promotion.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        var promotionResource = PromotionResourceFromEntityAssembler
                .toResourceFromEntity(promotion.get());
        
        return new ResponseEntity<>(promotionResource, HttpStatus.CREATED);
    }

    /**
     * Get all promotions
     * 
     * @param active Optional filter by active status
     * @return List of promotions
     */
    @GetMapping
    @Operation(
        summary = "Get all promotions", 
        description = "Retrieves all promotions with optional active filter"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Promotions retrieved successfully")
    })
    public ResponseEntity<List<PromotionResource>> getAllPromotions(
            @RequestParam(required = false) Boolean active) {
        
        List<Promotion> promotions;
        
        if (Boolean.TRUE.equals(active)) {
            promotions = promotionQueryService.handle(new GetPromotionCatalogQuery());
        } else {
            promotions = promotionQueryService.handle(new GetAllPromotionsQuery());
        }
        
        var resources = promotions.stream()
                .map(PromotionResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        
        return ResponseEntity.ok(resources);
    }

    /**
     * Get promotion by ID
     * 
     * @param promotionId The promotion identifier
     * @return The promotion resource
     */
    @GetMapping("/{promotionId}")
    @Operation(
        summary = "Get promotion by ID", 
        description = "Retrieves a specific promotion"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Promotion found"),
        @ApiResponse(responseCode = "404", description = "Promotion not found")
    })
    public ResponseEntity<PromotionResource> getPromotionById(
            @PathVariable Long promotionId) {
        
        var getPromotionByIdQuery = new GetPromotionByIdQuery(promotionId);
        var promotion = promotionQueryService.handle(getPromotionByIdQuery);
        
        if (promotion.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        var resource = PromotionResourceFromEntityAssembler
                .toResourceFromEntity(promotion.get());
        
        return ResponseEntity.ok(resource);
    }
}