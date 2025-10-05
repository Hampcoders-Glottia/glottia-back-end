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
import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.*;
import com.hampcoders.glottia.platform.api.profiles.domain.model.valueobjects.BusinessRoles;
import com.hampcoders.glottia.platform.api.profiles.domain.services.BusinessRoleQueryService;
import com.hampcoders.glottia.platform.api.profiles.domain.services.ProfileCommandService;
import com.hampcoders.glottia.platform.api.profiles.domain.services.ProfileQueryService;
import com.hampcoders.glottia.platform.api.profiles.interfaces.rest.resources.*;
import com.hampcoders.glottia.platform.api.profiles.interfaces.rest.transform.*;

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
    private final BusinessRoleQueryService businessRoleQueryService;

    public ProfilesController(ProfileCommandService profileCommandService,
                             ProfileQueryService profileQueryService,
                             BusinessRoleQueryService businessRoleQueryService) {
        this.profileCommandService = profileCommandService;
        this.profileQueryService = profileQueryService;
        this.businessRoleQueryService = businessRoleQueryService;
    }

    /**
     * Create a new profile
     */
    @PostMapping
    @Operation(summary = "Create a new profile", description = "Creates a new profile with the provided information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Profile created successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProfileResource.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "409", description = "Profile with email already exists")
    })
    public ResponseEntity<ProfileResource> createProfile(@RequestBody CreateProfileResource resource) {
        var command = CreateProfileCommandFromResourceAssembler.toCommandFromResource(resource);
        var profileId = profileCommandService.handle(command);
        
        if (profileId == null || profileId == 0) {
            return ResponseEntity.badRequest().build();
        }

        var profile = profileQueryService.handle(new GetProfileByIdQuery(profileId));
        if (profile.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        var profileResource = ProfileResourceFromEntityAssembler.toResourceFromEntity(profile.get());
        return new ResponseEntity<>(profileResource, HttpStatus.CREATED);
    }

    /**
     * Get all profiles
     */
    @GetMapping
    @Operation(summary = "Get all profiles", description = "Retrieves all profiles in the system")
    @ApiResponse(responseCode = "200", description = "Profiles retrieved successfully")
    public ResponseEntity<List<ProfileResource>> getAllProfiles() {
        var profiles = profileQueryService.handle(new GetAllProfilesQuery());
        var profileResources = profiles.stream()
                .map(ProfileResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
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
        var profile = profileQueryService.handle(new GetProfileByIdQuery(id));
        
        if (profile.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var profileResource = ProfileResourceFromEntityAssembler.toResourceFromEntity(profile.get());
        return ResponseEntity.ok(profileResource);
    }

    /**
     * Get profile by email
     */
    @GetMapping("/email/{email}")
    @Operation(summary = "Get profile by email", description = "Retrieves a profile by email address")
    public ResponseEntity<ProfileResource> getProfileByEmail(
            @Parameter(description = "Email address") @PathVariable String email) {
        var profile = profileQueryService.handle(new GetProfileByEmailQuery(email));
        
        if (profile.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var profileResource = ProfileResourceFromEntityAssembler.toResourceFromEntity(profile.get());
        return ResponseEntity.ok(profileResource);
    }

    /**
     * Update profile
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update profile", description = "Updates an existing profile")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Profile updated successfully"),
        @ApiResponse(responseCode = "404", description = "Profile not found")
    })
    public ResponseEntity<ProfileResource> updateProfile(
            @Parameter(description = "Profile ID") @PathVariable Long id,
            @RequestBody UpdateProfileResource resource) {
        var command = UpdateProfileCommandFromResourceAssembler.toCommandFromResource(id, resource);
        var updatedProfile = profileCommandService.handle(command);
        
        if (updatedProfile.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var profileResource = ProfileResourceFromEntityAssembler.toResourceFromEntity(updatedProfile.get());
        return ResponseEntity.ok(profileResource);
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
        var profile = profileQueryService.handle(new GetProfileByIdQuery(id));
        
        if (profile.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        profileCommandService.handle(new DeleteProfileCommand(id));
        return ResponseEntity.noContent().build();
    }

    /**
     * Get profiles by business role
     */
    @GetMapping("/business-role/{role}")
    @Operation(summary = "Get profiles by business role", description = "Retrieves profiles filtered by business role")
    public ResponseEntity<List<ProfileResource>> getProfilesByBusinessRole(
            @Parameter(description = "Business role (LEARNER, PARTNER, ADMIN)") @PathVariable String role) {
        try {
            // Convert string to enum
            BusinessRoles businessRoleEnum = BusinessRoles.valueOf(role.toUpperCase());
            
            // Find the BusinessRole entity by the enum value
            var businessRole = businessRoleQueryService.handle(new GetBusinessRoleByNameQuery(businessRoleEnum));
            if (businessRole.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            // Query profiles using the BusinessRole entity
            var profiles = profileQueryService.handle(new GetProfilesByBusinessRoleQuery(businessRole.get()));
            var profileResources = profiles.stream()
                    .map(ProfileResourceFromEntityAssembler::toResourceFromEntity)
                    .toList();
            return ResponseEntity.ok(profileResources);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get profiles by age
     */
    @GetMapping("/age/{age}")
    @Operation(summary = "Get profiles by age", description = "Retrieves profiles filtered by age")
    public ResponseEntity<List<ProfileResource>> getProfilesByAge(
            @Parameter(description = "Age") @PathVariable Integer age) {
        var profiles = profileQueryService.handle(new GetProfileByAgeQuery(age));
        var profileResources = profiles.stream()
                .map(ProfileResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(profileResources);
    }
}