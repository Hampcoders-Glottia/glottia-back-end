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
import com.hampcoders.glottia.platform.api.profiles.domain.services.LearnerCommandService;
import com.hampcoders.glottia.platform.api.profiles.domain.services.LearnerQueryService;
import com.hampcoders.glottia.platform.api.profiles.interfaces.rest.resources.*;
import com.hampcoders.glottia.platform.api.profiles.interfaces.rest.transform.*;

import java.util.List;

/**
 * Learners Controller
 * Handles REST endpoints for Learner aggregate operations
 */
@RestController
@RequestMapping(value = "/api/v1/learners", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Learners", description = "Learner Management Endpoints")
public class LearnersController {

    private final LearnerCommandService learnerCommandService;
    private final LearnerQueryService learnerQueryService;

    public LearnersController(LearnerCommandService learnerCommandService,
                             LearnerQueryService learnerQueryService) {
        this.learnerCommandService = learnerCommandService;
        this.learnerQueryService = learnerQueryService;
    }

    /**
     * Create a new learner
     */
    @PostMapping
    @Operation(summary = "Create a new learner", description = "Creates a new learner with the provided profile and address information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Learner created successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = LearnerResource.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Profile not found")
    })
    public ResponseEntity<LearnerResource> createLearner(@RequestBody CreateLearnerResource resource) {
        var command = CreateLearnerCommandFromResourceAssembler.toCommandFromResource(resource);
        var learnerId = learnerCommandService.handle(command);
        
        if (learnerId == null || learnerId == 0) {
            return ResponseEntity.badRequest().build();
        }

        var learner = learnerQueryService.handle(new GetLearnerByIdQuery(learnerId));
        if (learner.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        var learnerResource = LearnerResourceFromEntityAssembler.toResource(learner.get());
        return new ResponseEntity<>(learnerResource, HttpStatus.CREATED);
    }

    /**
     * Get all learners
     */
    @GetMapping
    @Operation(summary = "Get all learners", description = "Retrieves all learners in the system")
    @ApiResponse(responseCode = "200", description = "Learners retrieved successfully")
    public ResponseEntity<List<LearnerResource>> getAllLearners() {
        var learners = learnerQueryService.handle(new GetAllLearnersQuery());
        var learnerResources = learners.stream()
                .map(LearnerResourceFromEntityAssembler::toResource)
                .toList();
        return ResponseEntity.ok(learnerResources);
    }

    /**
     * Get learner by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get learner by ID", description = "Retrieves a specific learner by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Learner found"),
        @ApiResponse(responseCode = "404", description = "Learner not found")
    })
    public ResponseEntity<LearnerResource> getLearnerById(
            @Parameter(description = "Learner ID") @PathVariable Long id) {
        var learner = learnerQueryService.handle(new GetLearnerByIdQuery(id));
        
        if (learner.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var learnerResource = LearnerResourceFromEntityAssembler.toResource(learner.get());
        return ResponseEntity.ok(learnerResource);
    }

    /**
     * Get learner by profile ID
     */
    @GetMapping("/profile/{profileId}")
    @Operation(summary = "Get learner by profile ID", description = "Retrieves a learner by their profile ID")
    public ResponseEntity<LearnerResource> getLearnerByProfileId(
            @Parameter(description = "Profile ID") @PathVariable Long profileId) {
        var learner = learnerQueryService.handle(new GetLearnerByProfileIdQuery(profileId));
        
        if (learner.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var learnerResource = LearnerResourceFromEntityAssembler.toResource(learner.get());
        return ResponseEntity.ok(learnerResource);
    }

    /**
     * Add language to learner
     */
    @PostMapping("/{id}/languages")
    @Operation(summary = "Add language to learner", description = "Adds a new language with proficiency level to a learner")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Language added successfully"),
        @ApiResponse(responseCode = "404", description = "Learner not found"),
        @ApiResponse(responseCode = "400", description = "Invalid language or level data")
    })
    public ResponseEntity<LearnerResource> addLanguageToLearner(
            @Parameter(description = "Learner ID") @PathVariable Long id,
            @RequestBody AddLanguageResource resource) {
        var command = new AddLanguageToLearnerCommand(id, resource.languageId(), resource.cefrLevelId(), resource.isLearning());
        var updatedLearner = learnerCommandService.handle(command);
        
        if (updatedLearner.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var learnerResource = LearnerResourceFromEntityAssembler.toResource(updatedLearner.get());
        return ResponseEntity.ok(learnerResource);
    }

    /**
     * Update learner language
     */
    @PutMapping("/{id}/languages/{languageId}")
    @Operation(summary = "Update learner language", description = "Updates the proficiency level of a learner's language")
    public ResponseEntity<LearnerResource> updateLearnerLanguage(
            @Parameter(description = "Learner ID") @PathVariable Long id,
            @Parameter(description = "Language ID") @PathVariable Long languageId,
            @RequestBody UpdateLanguageResource resource) {
        var command = new UpdateLearnerLanguageCommand(id, languageId, resource.cefrLevelId(), resource.isLearning());
        var updatedLearner = learnerCommandService.handle(command);
        
        if (updatedLearner.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var learnerResource = LearnerResourceFromEntityAssembler.toResource(updatedLearner.get());
        return ResponseEntity.ok(learnerResource);
    }

    /**
     * Remove language from learner
     */
    @DeleteMapping("/{id}/languages/{languageId}")
    @Operation(summary = "Remove language from learner", description = "Removes a language from a learner's profile")
    public ResponseEntity<LearnerResource> removeLanguageFromLearner(
            @Parameter(description = "Learner ID") @PathVariable Long id,
            @Parameter(description = "Language ID") @PathVariable Long languageId) {
        var command = new RemoveLanguageFromLearnerCommand(id, languageId);
        var updatedLearner = learnerCommandService.handle(command);
        
        if (updatedLearner.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var learnerResource = LearnerResourceFromEntityAssembler.toResource(updatedLearner.get());
        return ResponseEntity.ok(learnerResource);
    }

    /**
     * Get learner language items
     */
    @GetMapping("/{id}/languages")
    @Operation(summary = "Get learner languages", description = "Retrieves all languages associated with a learner")
    public ResponseEntity<List<LearnerLanguageItemResource>> getLearnerLanguages(
            @Parameter(description = "Learner ID") @PathVariable Long id) {
        var languageItems = learnerQueryService.handle(new GetLearnerLanguageItemsByLearnerIdQuery(id));
        var languageItemResources = languageItems.stream()
                .map(LearnerLanguageItemResourceFromEntityAssembler::toResource)
                .toList();
        return ResponseEntity.ok(languageItemResources);
    }
}
