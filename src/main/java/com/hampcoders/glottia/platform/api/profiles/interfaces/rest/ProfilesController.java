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
import com.hampcoders.glottia.platform.api.profiles.domain.model.valueobjects.BusinessRoles;
import com.hampcoders.glottia.platform.api.profiles.domain.services.ProfileCommandService;
import com.hampcoders.glottia.platform.api.profiles.domain.services.ProfileQueryService;
import com.hampcoders.glottia.platform.api.profiles.domain.services.BusinessRoleQueryService;
import com.hampcoders.glottia.platform.api.profiles.interfaces.rest.resources.*;
import com.hampcoders.glottia.platform.api.profiles.interfaces.rest.transform.*;
import com.hampcoders.glottia.platform.api.iam.interfaces.acl.IamContextFacade;
import org.springframework.security.core.Authentication;

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
    private final IamContextFacade iamContextFacade;

    public ProfilesController(ProfileCommandService profileCommandService,
                             ProfileQueryService profileQueryService,
                             BusinessRoleQueryService businessRoleQueryService,
                             IamContextFacade iamContextFacade) {
        this.profileCommandService = profileCommandService;
        this.profileQueryService = profileQueryService;
        this.businessRoleQueryService = businessRoleQueryService;
        this.iamContextFacade = iamContextFacade;
    }

    /**
     * Create a complete profile with role assignment
     */
    @PostMapping
    @Operation(summary = "Create a complete profile with role assignment", description = "Creates a complete profile with automatic role assignment based on provided data")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Profile created successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProfileResource.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "409", description = "Profile with email already exists")
    })
    public ResponseEntity<ProfileResource> createProfile(
            @RequestBody CreateProfileResource resource) {
        
        try {
            var command = CreateProfileCommandFromResourceAssembler
                .toCommandFromResource(resource);
            var profileId = profileCommandService.handle(command);

            var optionalProfile = profileQueryService.handle(new GetProfileByIdQuery(profileId));

            if (optionalProfile.isEmpty()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            
            // Convert to resource
            var profileResource = ProfileResourceFromEntityAssembler.toResourceFromEntity(optionalProfile.get());
            
            return new ResponseEntity<>(profileResource, HttpStatus.CREATED);
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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
                addressResource.country(),
                addressResource.latitude(),
                addressResource.longitude()
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
     * Assign partner role to existing profile - RESTful sub-resource
     */
    @PostMapping("/{profileId}/partner-assignment")
    @Operation(summary = "Assign partner role to profile", description = "Assigns partner role with business information to an existing profile")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Partner role assigned successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid profile ID or profile already has a role"),
        @ApiResponse(responseCode = "404", description = "Profile not found")
    })
    public ResponseEntity<ProfileResource> assignPartnerRole(
            @Parameter(description = "Profile ID") @PathVariable Long profileId,
            @RequestBody PartnerBusinessInfoResource businessInfo) {
        try {
            var profile = profileQueryService.handle(new GetProfileByIdQuery(profileId));
            if (profile.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            if (profile.get().isLearner() || profile.get().isPartner()) {
                return ResponseEntity.badRequest().build(); // Profile already has a role
            }
            
            profile.get().assignAsPartner(
                businessInfo.legalName(),
                businessInfo.businessName(),
                businessInfo.taxId(),
                businessInfo.contactEmail(),
                businessInfo.contactPhone(),
                businessInfo.contactPersonName(),
                businessInfo.description()
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
            BusinessRole businessRoleEnum = BusinessRole.toBusinessRoleFromName(role);
            
            // Query profiles directly using the BusinessRoles enum
            var profiles = profileQueryService.handle(new GetProfilesByBusinessRoleQuery(businessRoleEnum));
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

    // --- Learner Language Management Endpoints ---

    /**
     * Add language to learner
     */
    @PostMapping("/{profileId}/learner/languages")
    @Operation(summary = "Add language to learner", description = "Adds a new language with CEFR level to a learner profile")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Language added successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request or profile not found"),
        @ApiResponse(responseCode = "404", description = "Profile not found or not a learner")
    })
    public ResponseEntity<LearnerLanguageItemResource> addLanguageToLearner(
            @Parameter(description = "Profile ID") @PathVariable Long profileId,
            @RequestBody AddLanguageResource languageResource) {
        try {
            // First, get the profile to find the learner ID
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
            
            var result = profileCommandService.handle(command);
            if (result.isPresent()) {
                // Convert to resource (you'd need to create this assembler)
                // var resource = LearnerLanguageItemResourceFromEntityAssembler.toResourceFromEntity(result.get());
                // return ResponseEntity.status(HttpStatus.CREATED).body(resource);
                return ResponseEntity.status(HttpStatus.CREATED).build(); // TODO: Return actual resource
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Remove language from learner
     */
    @DeleteMapping("/{profileId}/learner/languages/{languageId}")
    @Operation(summary = "Remove language from learner", description = "Removes a language from a learner profile")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Language removed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "404", description = "Profile or language not found")
    })
    public ResponseEntity<Void> removeLanguageFromLearner(
            @Parameter(description = "Profile ID") @PathVariable Long profileId,
            @Parameter(description = "Language ID") @PathVariable Long languageId) {
        try {
            // First, get the profile to find the learner ID
            var profile = profileQueryService.handle(new GetProfileByIdQuery(profileId));
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

    /**
     * Update learner language
     */
    @PutMapping("/{profileId}/learner/languages/{languageId}")
    @Operation(summary = "Update learner language", description = "Updates a language's CEFR level or learning status for a learner")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Language updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "404", description = "Profile or language not found")
    })
    public ResponseEntity<LearnerLanguageItemResource> updateLearnerLanguage(
            @Parameter(description = "Profile ID") @PathVariable Long profileId,
            @Parameter(description = "Language ID") @PathVariable Long languageId,
            @RequestBody UpdateLanguageResource updateResource) {
        try {
            // First, get the profile to find the learner ID
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
            
            var result = profileCommandService.handle(command);
            if (result.isPresent()) {
                // Convert to resource (you'd need to create this assembler)
                // var resource = LearnerLanguageItemResourceFromEntityAssembler.toResourceFromEntity(result.get());
                // return ResponseEntity.ok(resource);
                return ResponseEntity.ok().build(); // TODO: Return actual resource
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}