package com.hampcoders.glottia.platform.api.profiles.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.GetProfileByIdQuery;
import com.hampcoders.glottia.platform.api.profiles.domain.services.ProfileCommandService;
import com.hampcoders.glottia.platform.api.profiles.domain.services.ProfileQueryService;
import com.hampcoders.glottia.platform.api.profiles.interfaces.rest.resources.*;
import com.hampcoders.glottia.platform.api.profiles.interfaces.rest.transform.*;

/**
 * User Registration Controller - RESTful API for user registration
 * Handles user registration with profile creation
 */
@RestController
@RequestMapping(value = "/api/v1/registrations", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "User Registration", description = "User Registration Management Endpoints")
public class UserRegistrationsController {

    private final ProfileCommandService profileCommandService;
    private final ProfileQueryService profileQueryService;

    public UserRegistrationsController(ProfileCommandService profileCommandService,
                                     ProfileQueryService profileQueryService) {
        this.profileCommandService = profileCommandService;
        this.profileQueryService = profileQueryService;
    }

    /**
     * Register a new user with profile - Primary RESTful registration endpoint
     */
    @PostMapping
    @Operation(summary = "Register new user", description = "Creates a user account and associated profile with role assignment in a single transaction")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User registered successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProfileResource.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data or username already exists"),
        @ApiResponse(responseCode = "409", description = "Username or email already exists")
    })
    public ResponseEntity<ProfileResource> registerUser(@RequestBody UserRegistrationRequest request) {
        try {
            // Convert to internal resource format for compatibility
            var resource = new UserRegistrationResource(
                request.username(), request.password(),
                request.firstName(), request.lastName(), request.age(), request.email(),
                request.businessRole(),
                request.street(), request.number(), request.city(), request.postalCode(),
                request.country(), request.latitude(), request.longitude(),
                request.legalName(), request.businessName(), request.taxId(),
                request.contactEmail(), request.contactPhone(), request.contactPersonName(),
                request.description(), request.websiteUrl(), request.instagramHandle()
            );
            
            var command = UserRegistrationResourceToCompleteRegistrationCommandAssembler.toCommandFromResource(resource);
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
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // Username/email conflict
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}