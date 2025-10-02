package com.hampcoders.glottia.platform.api.profiles.interfaces.rest;

import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.DeleteProfileCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.*;
import com.hampcoders.glottia.platform.api.profiles.domain.model.valueobjects.Languages;
import com.hampcoders.glottia.platform.api.profiles.domain.model.valueobjects.NivelCefr;
import com.hampcoders.glottia.platform.api.profiles.domain.services.ProfileCommandService;
import com.hampcoders.glottia.platform.api.profiles.domain.services.ProfileQueryService;
import com.hampcoders.glottia.platform.api.profiles.interfaces.rest.resources.CreateProfileResource;
import com.hampcoders.glottia.platform.api.profiles.interfaces.rest.resources.ProfileResource;
import com.hampcoders.glottia.platform.api.profiles.interfaces.rest.resources.UpdateProfileResource;
import com.hampcoders.glottia.platform.api.profiles.interfaces.rest.transform.CreateProfileCommandFromResourceAssembler;
import com.hampcoders.glottia.platform.api.profiles.interfaces.rest.transform.ProfileResourceFromEntityAssembler;
import com.hampcoders.glottia.platform.api.profiles.interfaces.rest.transform.UpdateProfileCommandFromResourceAssembler;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", methods = { RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE })
@RestController
@RequestMapping(value = "/api/v1/profiles", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Profiles", description = "Profile Management Endpoints")
public class ProfilesController {

    private final ProfileQueryService profileQueryService;
    private final ProfileCommandService profileCommandService;

    public ProfilesController(ProfileQueryService profileQueryService, ProfileCommandService profileCommandService) {
        this.profileQueryService = profileQueryService;
        this.profileCommandService = profileCommandService;
    }

    @Operation(summary = "Create a new profile")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createProfile(@RequestBody CreateProfileResource resource) {
        try {
            var createProfileCommand = CreateProfileCommandFromResourceAssembler.toCommandFromResource(resource);
            var profileId = this.profileCommandService.handle(createProfileCommand);
            var optionalProfile = this.profileQueryService.handle(new GetProfileByIdQuery(profileId));
            if (optionalProfile.isEmpty()) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Created but not found");
            var profileResource = ProfileResourceFromEntityAssembler.toResourceFromEntity(optionalProfile.get());
            var location = URI.create("/api/v1/profiles/" + profileId);
            return ResponseEntity.created(location).body(profileResource);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @Operation(summary = "List profiles with optional filters: age, language, level (all combined with AND)")
    @GetMapping
    public ResponseEntity<List<ProfileResource>> getAllProfiles(@RequestParam(required = false) Integer age,
                                                                @RequestParam(required = false) String language,
                                                                @RequestParam(required = false) String level) {
        var profiles = this.profileQueryService.handle(new GetAllProfilesQuery());

        if (age != null) {
            profiles = profiles.stream().filter(p -> p.getAge() == age).collect(Collectors.toList());
        }
        if (language != null) {
            Languages langEnum;
            try { langEnum = Languages.valueOf(language.toUpperCase()); }
            catch (IllegalArgumentException e) { return ResponseEntity.badRequest().build(); }
            var finalLangEnum = langEnum;
            profiles = profiles.stream().filter(p -> p.getLanguage().equalsIgnoreCase(finalLangEnum.name())).collect(Collectors.toList());
        }
        if (level != null) {
            NivelCefr lvlEnum;
            try { lvlEnum = NivelCefr.valueOf(level.toUpperCase()); }
            catch (IllegalArgumentException e) { return ResponseEntity.badRequest().build(); }
            var finalLvlEnum = lvlEnum;
            profiles = profiles.stream().filter(p -> p.getLevel().equalsIgnoreCase(finalLvlEnum.name())).collect(Collectors.toList());
        }

        var resources = ProfileResourceFromEntityAssembler.toResourceListFromEntities(profiles);
        return ResponseEntity.ok(resources);
    }

    @Operation(summary = "Get profile by id")
    @GetMapping("/{profileId}")
    public ResponseEntity<?> getProfileById(@PathVariable Long profileId) {
        var optionalProfile = this.profileQueryService.handle(new GetProfileByIdQuery(profileId));
        if (optionalProfile.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Profile not found");
        var profileResource = ProfileResourceFromEntityAssembler.toResourceFromEntity(optionalProfile.get());
        return ResponseEntity.ok(profileResource);
    }

    @Operation(summary = "Update an existing profile")
    @PutMapping(value = "/{profileId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateProfile(@PathVariable Long profileId, @RequestBody UpdateProfileResource resource) {
        try {
            var updateProfileCommand = UpdateProfileCommandFromResourceAssembler.toCommandFromResource(profileId, resource);
            var optionalProfile = this.profileCommandService.handle(updateProfileCommand);
            if (optionalProfile.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Profile not found");
            var profileResource = ProfileResourceFromEntityAssembler.toResourceFromEntity(optionalProfile.get());
            return ResponseEntity.ok(profileResource);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @Operation(summary = "Delete a profile by id")
    @DeleteMapping("/{profileId}")
    public ResponseEntity<?> deleteProfile(@PathVariable Long profileId) {
        try {
            this.profileCommandService.handle(new DeleteProfileCommand(profileId));
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
}
