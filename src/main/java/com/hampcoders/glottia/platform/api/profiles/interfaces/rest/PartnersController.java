package com.hampcoders.glottia.platform.api.profiles.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.*;
import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.*;
import com.hampcoders.glottia.platform.api.profiles.domain.model.valueobjects.SubscriptionStatuses;
import com.hampcoders.glottia.platform.api.profiles.domain.services.PartnerCommandService;
import com.hampcoders.glottia.platform.api.profiles.domain.services.PartnerQueryService;
import com.hampcoders.glottia.platform.api.profiles.domain.services.SubscriptionStatusQueryService;
import com.hampcoders.glottia.platform.api.profiles.interfaces.rest.resources.*;
import com.hampcoders.glottia.platform.api.profiles.interfaces.rest.transform.*;

import java.util.List;

/**
 * Partners Controller
 * Handles REST endpoints for Partner aggregate operations
 */
@RestController
@RequestMapping(value = "/api/v1/partners", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Partners", description = "Partner Management Endpoints")
public class PartnersController {

    private final PartnerCommandService partnerCommandService;
    private final PartnerQueryService partnerQueryService;
    private final SubscriptionStatusQueryService subscriptionStatusQueryService;

    public PartnersController(PartnerCommandService partnerCommandService,
                             PartnerQueryService partnerQueryService,
                             SubscriptionStatusQueryService subscriptionStatusQueryService) {
        this.partnerCommandService = partnerCommandService;
        this.partnerQueryService = partnerQueryService;
        this.subscriptionStatusQueryService = subscriptionStatusQueryService;
    }

    /**
     * Create a new partner
     */
    @PostMapping
    @Operation(summary = "Create a new partner", description = "Creates a new partner with the provided business information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Partner created successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = PartnerResource.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Profile not found"),
        @ApiResponse(responseCode = "409", description = "Partner with tax ID already exists")
    })
    public ResponseEntity<PartnerResource> createPartner(@RequestBody CreatePartnerResource resource) {
        var command = CreatePartnerCommandFromResourceAssembler.toCommandFromResource(resource);
        var partnerId = partnerCommandService.handle(command);
        
        if (partnerId == null || partnerId == 0) {
            return ResponseEntity.badRequest().build();
        }

        var partner = partnerQueryService.handle(new GetPartnerByIdQuery(partnerId));
        if (partner.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        var partnerResource = PartnerResourceFromEntityAssembler.toResource(partner.get());
        return new ResponseEntity<>(partnerResource, HttpStatus.CREATED);
    }

    /**
     * Get all partners
     */
    @GetMapping
    @Operation(summary = "Get all partners", description = "Retrieves all partners in the system")
    @ApiResponse(responseCode = "200", description = "Partners retrieved successfully")
    public ResponseEntity<List<PartnerResource>> getAllPartners() {
        var partners = partnerQueryService.handle(new GetAllPartnersQuery());
        var partnerResources = partners.stream()
                .map(PartnerResourceFromEntityAssembler::toResource)
                .toList();
        return ResponseEntity.ok(partnerResources);
    }

    /**
     * Get partner by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get partner by ID", description = "Retrieves a specific partner by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Partner found"),
        @ApiResponse(responseCode = "404", description = "Partner not found")
    })
    public ResponseEntity<PartnerResource> getPartnerById(
            @Parameter(description = "Partner ID") @PathVariable Long id) {
        var partner = partnerQueryService.handle(new GetPartnerByIdQuery(id));
        
        if (partner.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var partnerResource = PartnerResourceFromEntityAssembler.toResource(partner.get());
        return ResponseEntity.ok(partnerResource);
    }

    /**
     * Get partner by profile ID
     */
    @GetMapping("/profile/{profileId}")
    @Operation(summary = "Get partner by profile ID", description = "Retrieves a partner by their profile ID")
    public ResponseEntity<PartnerResource> getPartnerByProfileId(
            @Parameter(description = "Profile ID") @PathVariable Long profileId) {
        var partner = partnerQueryService.handle(new GetPartnerByProfileIdQuery(profileId));
        
        if (partner.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var partnerResource = PartnerResourceFromEntityAssembler.toResource(partner.get());
        return ResponseEntity.ok(partnerResource);
    }

    /**
     * Update partner contact information
     */
    @PutMapping("/{id}/contact")
    @Operation(summary = "Update partner contact", description = "Updates partner contact information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Partner contact updated successfully"),
        @ApiResponse(responseCode = "404", description = "Partner not found")
    })
    public ResponseEntity<PartnerResource> updatePartnerContact(
            @Parameter(description = "Partner ID") @PathVariable Long id,
            @RequestBody UpdatePartnerResource resource) {
        var command = UpdatePartnerCommandFromResourceAssembler.toCommandFromResource(id, resource);
        var updatedPartner = partnerCommandService.handle(command);
        
        if (updatedPartner.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var partnerResource = PartnerResourceFromEntityAssembler.toResource(updatedPartner.get());
        return ResponseEntity.ok(partnerResource);
    }

    /**
     * Update partner subscription status
     */
    @PutMapping("/{id}/subscription")
    @Operation(summary = "Update partner subscription", description = "Updates partner subscription status")
    public ResponseEntity<PartnerResource> updatePartnerSubscription(
            @Parameter(description = "Partner ID") @PathVariable Long id,
            @RequestBody UpdatePartnerSubscriptionResource resource) {
        var command = new UpdatePartnerSubscriptionCommand(id, resource.subscriptionStatusId());
        var updatedPartner = partnerCommandService.handle(command);
        
        if (updatedPartner.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var partnerResource = PartnerResourceFromEntityAssembler.toResource(updatedPartner.get());
        return ResponseEntity.ok(partnerResource);
    }

    /**
     * Get partners by subscription status
     */
    @GetMapping("/subscription/{status}")
    @Operation(summary = "Get partners by subscription status", description = "Retrieves partners filtered by subscription status")
    public ResponseEntity<List<PartnerResource>> getPartnersBySubscriptionStatus(
            @Parameter(description = "Subscription status (PENDING, ACTIVE, SUSPENDED, CANCELLED, EXPIRED)") @PathVariable String status) {
        try {
            // Convert string to enum
            SubscriptionStatuses subscriptionStatusEnum = SubscriptionStatuses.valueOf(status.toUpperCase());
            
            // Find the SubscriptionStatus entity by the enum value
            var subscriptionStatus = subscriptionStatusQueryService.handle(new GetSubscriptionStatusByNameQuery(subscriptionStatusEnum));
            if (subscriptionStatus.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            // Query partners using the SubscriptionStatus entity
            var partners = partnerQueryService.handle(new GetPartnersBySubscriptionStatusQuery(subscriptionStatus.get()));
            var partnerResources = partners.stream()
                    .map(PartnerResourceFromEntityAssembler::toResource)
                    .toList();
            return ResponseEntity.ok(partnerResources);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}