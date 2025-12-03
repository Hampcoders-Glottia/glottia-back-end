package com.hampcoders.glottia.platform.api.profiles.application.acl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.hampcoders.glottia.platform.api.profiles.domain.model.aggregates.Profile;
import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.GetLearnerByIdQuery;
import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.GetLearnerLanguageItemsByLearnerIdQuery;
import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.GetPartnerByIdQuery;
import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.GetProfileByIdQuery;
import com.hampcoders.glottia.platform.api.profiles.domain.services.ProfileQueryService;
import com.hampcoders.glottia.platform.api.profiles.infrastructure.persistence.jpa.repository.ProfileRepository;
import com.hampcoders.glottia.platform.api.profiles.interfaces.acl.ProfilesContextFacade;

import lombok.AllArgsConstructor;

/**
 * Implementation of ProfilesContextFacade.
 */
@Service
@AllArgsConstructor
public class ProfilesContextFacadeImpl implements ProfilesContextFacade {
    private final ProfileQueryService profileQueryService;
    private final ProfileRepository profileRepository;


    @Override
    public Optional<Long> fetchProfileById(Long profileId) {
        return profileQueryService.handle(new GetProfileByIdQuery(profileId)).map(Profile::getId);
    }
    
    /**
     * Fetches the learner ID by profile ID.
     * @param profileId The profile ID.
     * @return The learner ID, or 0L if not found or not a learner.
     */
    public Long fetchLearnerIdByProfileId(Long profileId) {
        try {
            var query = new GetProfileByIdQuery(profileId);
            var result = profileQueryService.handle(query);
            return result
                .filter(profile -> profile.isLearner())
                .map(profile -> profile.getLearner().getId())
                .orElse(0L);
        } catch (Exception e) {
            return 0L;
        }
    }

    /**
     * Fetches the partner ID by profile ID.
     * @param profileId The profile ID.
     * @return The partner ID, or 0L if not found or not a partner.
     */
    public Long fetchPartnerIdByProfileId(Long profileId) {
        try {
            var query = new GetProfileByIdQuery(profileId);
            var result = profileQueryService.handle(query);
            return result
                .filter(profile -> profile.isPartner())
                .map(profile -> profile.getPartner().getId())
                .orElse(0L);
        } catch (Exception e) {
            return 0L;
        }
    }

    /**
     * Checks if a profile is a learner.
     * @param profileId The profile ID.
     * @return true if learner, false otherwise.
     */
    public boolean isLearnerProfile(Long profileId) {
        try {
            var query = new GetProfileByIdQuery(profileId);
            var result = profileQueryService.handle(query);
            return result.map(profile -> profile.isLearner()).orElse(false);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if a profile is a partner.
     * @param profileId The profile ID.
     * @return true if partner, false otherwise.
     */
    public boolean isPartnerProfile(Long profileId) {
        try {
            var query = new GetProfileByIdQuery(profileId);
            var result = profileQueryService.handle(query);
            return result.map(profile -> profile.isPartner()).orElse(false);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if a learner profile is active and complete.
     * @param learnerId The learner ID.
     * @return true if active and has at least one language, false otherwise.
     */
    public boolean isLearnerActiveAndComplete(Long learnerId) {
        try {
            var query = new GetLearnerByIdQuery(learnerId);
            var result = profileQueryService.handle(query);
            return result
                .map(learner -> learner.getLanguages().size() > 0)
                .orElse(false);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if a partner profile is active.
     * @param partnerId The partner ID.
     * @return true if active, false otherwise.
     */
    public boolean isPartnerActive(Long partnerId) {
        try {
            var query = new GetPartnerByIdQuery(partnerId);
            var result = profileQueryService.handle(query);
            return result.isPresent();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Fetches learner languages count.
     * @param learnerId The learner ID.
     * @return The number of languages, or 0 if not found.
     */
    public int fetchLearnerLanguagesCount(Long learnerId) {
        try {
            var query = new GetLearnerLanguageItemsByLearnerIdQuery(learnerId);
            var result = profileQueryService.handle(query);
            return result.size();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Checks if learner has a specific language.
     * @param learnerId The learner ID.
     * @param languageId The language ID.
     * @return true if learner has the language, false otherwise.
     */
    public boolean learnerHasLanguage(Long learnerId, Long languageId) {
        try {
            var query = new GetLearnerLanguageItemsByLearnerIdQuery(learnerId);
            var result = profileQueryService.handle(query);
            return result.stream()
                .anyMatch(item -> item.getLanguage().getId().equals(languageId));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Fetches partner contact email.
     * @param partnerId The partner ID.
     * @return The contact email, or empty string if not found.
     */
    public String fetchPartnerContactEmail(Long partnerId) {
        try {
            var query = new GetPartnerByIdQuery(partnerId);
            var result = profileQueryService.handle(query);
            return result.map(partner -> partner.getContactEmail()).orElse("");
        } catch (Exception e) {
            return "";
        }
    }


    @Override
    public Long fetchProfileIdByUserId(Long userId) {
        var profile = profileRepository.findByUserId(userId);
        return profile.map(Profile::getId).orElse(0L);
    }

    @Override
    public Long fetchLearnerIdByUserId(Long userId) {
        var profile = profileRepository.findByUserId(userId);
        if (profile.isPresent() && profile.get().isLearner()) {
            return profile.get().getLearner().getId();
        }
        return 0L;
    }

    @Override
    public Long fetchPartnerIdByUserId(Long userId) {
        var profile = profileRepository.findByUserId(userId);
        if (profile.isPresent() && profile.get().isPartner()) {
            return profile.get().getPartner().getId();
        }
        return 0L;
    }

    @Override
    public boolean isUserLearner(Long userId) {
        var profile = profileRepository.findByUserId(userId);
        return profile.map(Profile::isLearner).orElse(false);
    }

    @Override
    public boolean isUserPartner(Long userId) {
        var profile = profileRepository.findByUserId(userId);
        return profile.map(Profile::isPartner).orElse(false);
    }
}
