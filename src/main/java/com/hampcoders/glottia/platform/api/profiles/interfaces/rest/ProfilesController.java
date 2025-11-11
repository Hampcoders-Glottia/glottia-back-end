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

import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.DeleteProfileCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.AddLanguageToLearnerCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.RemoveLanguageFromLearnerCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.UpdateLearnerLanguageCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.entities.BusinessRole;
import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.*;
import com.hampcoders.glottia.platform.api.profiles.domain.services.ProfileCommandService;
import com.hampcoders.glottia.platform.api.profiles.domain.services.ProfileQueryService;
import com.hampcoders.glottia.platform.api.profiles.domain.services.BusinessRoleQueryService;
import com.hampcoders.glottia.platform.api.profiles.interfaces.rest.resources.*;
import com.hampcoders.glottia.platform.api.profiles.interfaces.rest.transform.*;
import com.hampcoders.glottia.platform.api.shared.interfaces.rest.resources.MessageResource;
import com.hampcoders.glottia.platform.api.iam.interfaces.acl.IamContextFacade;

import java.util.List;

/**
 * Profiles Controller
 * Handles REST endpoints for Profile aggregate operations
 */
@RestController
@RequestMapping(value = "/api/v1/profiles", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Profiles", description = "Profile Management Endpoints")
public class ProfilesController {

    private final ProfileCommandService profileCommandService;
    private final ProfileQueryService profileQueryService;

    public ProfilesController(ProfileCommandService profileCommandService,
                             ProfileQueryService profileQueryService,
                             BusinessRoleQueryService businessRoleQueryService,
                             IamContextFacade iamContextFacade) {
        this.profileCommandService = profileCommandService;
        this.profileQueryService = profileQueryService;
    }

    /**
     * Create a new profile
     * POST /api/v1/profiles
     */
    @PostMapping
    @Operation(summary = "Create a new profile", description = "Creates a new profile with basic information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Profile created successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProfileResource.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "409", description = "Profile with email already exists")
    })
    public ResponseEntity<?> createProfile(@RequestBody CreateProfileResource resource) {
        try {
            var command = CreateProfileCommandFromResourceAssembler.toCommandFromResource(resource);
            var profileId = profileCommandService.handle(command);

            var profile = profileQueryService.handle(new GetProfileByIdQuery(profileId))
                .orElseThrow(() -> new IllegalStateException("Profile not found after creation"));
            
            var profileResource = ProfileResourceFromEntityAssembler.toResourceFromEntity(profile);
            return ResponseEntity.status(HttpStatus.CREATED).body(profileResource);
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResource(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MessageResource("An error occurred while creating the profile"));
        }
    }

    /**
     * Assign learner role to existing profile - RESTful sub-resource
     */
    @PostMapping("/{profileId}/learner-assignment")
    @Operation(summary = "Assign learner role to profile", description = "Assigns learner role with address information to an existing profile")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Learner role assigned successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid profile ID or profile already has a role"),
        @ApiResponse(responseCode = "404", description = "Profile not found")
    })
    public ResponseEntity<ProfileResource> assignLearnerRole(
            @Parameter(description = "Profile ID") @PathVariable Long profileId,
            @RequestBody AddressResource addressResource) {
        try {
            var profile = profileQueryService.handle(new GetProfileByIdQuery(profileId));
            if (profile.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            if (profile.get().isLearner() || profile.get().isPartner()) {
                return ResponseEntity.badRequest().build(); // Profile already has a role
            }
            
            profile.get().assignAsLearner(
                addressResource.street(),
                addressResource.number(),
                addressResource.city(),
                addressResource.postalCode(),
                addressResource.country()
            );
            
            // Save profile (this would normally be done through a command service)
            var updatedProfile = profileQueryService.handle(new GetProfileByIdQuery(profileId)).get();
            var profileResource = ProfileResourceFromEntityAssembler.toResourceFromEntity(updatedProfile);
            return ResponseEntity.ok(profileResource);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get all profiles with optional filtering
     */
    @GetMapping
    @Operation(summary = "Get all profiles", description = "Retrieves all profiles with optional filters")
    @ApiResponse(responseCode = "200", description = "Profiles retrieved successfully")
    public ResponseEntity<List<ProfileResource>> getAllProfiles(
            @Parameter(description = "Filter by business role") @RequestParam(required = false) String role,
            @Parameter(description = "Filter by age") @RequestParam(required = false) Integer age) {
        
        List<ProfileResource> profileResources;
        
        if (role != null) {
            try {
                BusinessRole businessRoleEnum = BusinessRole.toBusinessRoleFromName(role);
                var profiles = profileQueryService.handle(new GetProfilesByBusinessRoleQuery(businessRoleEnum.getRole()));
                profileResources = profiles.stream()
                    .map(ProfileResourceFromEntityAssembler::toResourceFromEntity)
                    .toList();
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build();
            }
        } else if (age != null) {
            var profiles = profileQueryService.handle(new GetProfileByAgeQuery(age));
            profileResources = profiles.stream()
                .map(ProfileResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        } else {
            var profiles = profileQueryService.handle(new GetAllProfilesQuery());
            profileResources = profiles.stream()
                .map(ProfileResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        }
        
        return ResponseEntity.ok(profileResources);
    }

    /**
     * Get profile by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get profile by ID", description = "Retrieves a specific profile by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Profile found"),
        @ApiResponse(responseCode = "404", description = "Profile not found")
    })
    public ResponseEntity<ProfileResource> getProfileById(
            @Parameter(description = "Profile ID") @PathVariable Long id) {
        return profileQueryService.handle(new GetProfileByIdQuery(id))
            .map(profile -> ResponseEntity.ok(
                ProfileResourceFromEntityAssembler.toResourceFromEntity(profile)))
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Search profile by email
     */
    @GetMapping("/search")
    @Operation(summary = "Search profile by email", description = "Searches for a profile by email address")
    @ApiResponse(responseCode = "200", description = "Search completed")
    public ResponseEntity<ProfileResource> searchProfileByEmail(
            @Parameter(description = "Email address") @RequestParam String email) {
        return profileQueryService.handle(new GetProfileByEmailQuery(email))
            .map(profile -> ResponseEntity.ok(
                ProfileResourceFromEntityAssembler.toResourceFromEntity(profile)))
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Update profile
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update profile", description = "Updates an existing profile's basic and role-specific information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Profile updated successfully"),
        @ApiResponse(responseCode = "404", description = "Profile not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<?> updateProfile(
            @Parameter(description = "Profile ID") @PathVariable Long id,
            @RequestBody UpdateProfileResource resource) {  // Cambiar a UpdateProfileResource
        try {
            // Usar el assembler para crear el comando (CQRS compliant)
            var command = UpdateProfileCommandFromResourceAssembler.toCommandFromResource(id, resource);
            
            // Ejecutar el comando
            var updatedProfile = profileCommandService.handle(command);
            if (updatedProfile.isPresent()) {
                var profileResource = ProfileResourceFromEntityAssembler.toResourceFromEntity(updatedProfile.get());
                return ResponseEntity.ok(profileResource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResource(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MessageResource("An error occurred while updating the profile"));
        }
    }
    /**
     * Delete profile
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete profile", description = "Deletes an existing profile")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Profile deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Profile not found")
    })
    public ResponseEntity<Void> deleteProfile(
            @Parameter(description = "Profile ID") @PathVariable Long id) {
        if (profileQueryService.handle(new GetProfileByIdQuery(id)).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        profileCommandService.handle(new DeleteProfileCommand(id));
        return ResponseEntity.noContent().build();
    }

    /**
     * Add language to learner
     */
    @PostMapping("/{profileId}/learner/languages")
    @Operation(summary = "Add language to learner", description = "Adds a new language with CEFR level to a learner profile")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Language added successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "404", description = "Profile not found or not a learner")
    })
    public ResponseEntity<?> addLanguageToLearner(
            @Parameter(description = "Profile ID") @PathVariable Long profileId,
            @RequestBody AddLanguageResource languageResource) {
        try {
            var profile = profileQueryService.handle(new GetProfileByIdQuery(profileId));
            if (profile.isEmpty() || !profile.get().isLearner()) {
                return ResponseEntity.notFound().build();
            }

            var command = new AddLanguageToLearnerCommand(
                profile.get().getLearner().getId(),
                languageResource.languageId(),
                languageResource.cefrLevelId(),
                languageResource.isLearning()
            );
            
            return profileCommandService.handle(command)
                .map(item -> ResponseEntity.status(HttpStatus.CREATED).body(
                    LearnerLanguageItemResourceFromEntityAssembler.toResourceFromEntity(item)))
                .orElse(ResponseEntity.badRequest().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResource(e.getMessage()));
        }
    }

    /**
     * Update learner language
     */
    @PutMapping("/{profileId}/learner/languages/{languageId}")
    @Operation(summary = "Update learner language", description = "Updates a language's CEFR level or learning status for a learner")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Language updated successfully"),
        @ApiResponse(responseCode = "404", description = "Profile or language not found")
    })
    public ResponseEntity<?> updateLearnerLanguage(
            @Parameter(description = "Profile ID") @PathVariable Long profileId,
            @Parameter(description = "Language ID") @PathVariable Long languageId,
            @RequestBody UpdateLanguageResource updateResource) {
        try {
            var profile = profileQueryService.handle(new GetProfileByIdQuery(profileId));
            if (profile.isEmpty() || !profile.get().isLearner()) {
                return ResponseEntity.notFound().build();
            }

            var command = new UpdateLearnerLanguageCommand(
                profile.get().getLearner().getId(),
                languageId,
                updateResource.cefrLevelId(),
                updateResource.isLearning()
            );
            
            return profileCommandService.handle(command)
                .map(item -> ResponseEntity.ok(
                    LearnerLanguageItemResourceFromEntityAssembler.toResourceFromEntity(item)))
                .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResource(e.getMessage()));
        }
    }
    
    /**
     * Remove language from learner
     */
    @DeleteMapping("/{id}/learner/languages/{languageId}")
    @Operation(summary = "Remove language from learner", description = "Removes a language from a learner profile")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Language removed successfully"),
        @ApiResponse(responseCode = "404", description = "Profile or language not found")
    })
    public ResponseEntity<Void> removeLanguageFromLearner(
            @Parameter(description = "Profile ID") @PathVariable Long id,
            @Parameter(description = "Language ID") @PathVariable Long languageId) {
        try {
            var profile = profileQueryService.handle(new GetProfileByIdQuery(id));
            if (profile.isEmpty() || !profile.get().isLearner()) {
                return ResponseEntity.notFound().build();
            }

            var command = new RemoveLanguageFromLearnerCommand(
                profile.get().getLearner().getId(),
                languageId
            );
            
            profileCommandService.handle(command);
            return ResponseEntity.noContent().build();
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
}