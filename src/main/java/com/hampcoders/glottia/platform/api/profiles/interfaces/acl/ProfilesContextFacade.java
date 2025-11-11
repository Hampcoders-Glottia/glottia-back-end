package com.hampcoders.glottia.platform.api.profiles.interfaces.acl;

import java.util.Optional;

/**
 * Facade interface for Profiles Context operations.
 */
public interface ProfilesContextFacade {
    /**
     * Fetches the profile by its ID.
     * @param profileId The profile ID.
     * @return The profile ID wrapped in Optional, or empty if not found.
     */
    Optional<Long> fetchProfileById(Long profileId);

    /**
     * Fetches the learner ID by profile ID.
     * @param profileId The profile ID.
     * @return The learner ID, or 0L if not found or not a learner.
     */
    Long fetchLearnerIdByProfileId(Long profileId);

    /**
     * Fetches the partner ID by profile ID.
     * @param profileId The profile ID.
     * @return The partner ID, or 0L if not found or not a partner.
     */
    Long fetchPartnerIdByProfileId(Long profileId);

    /**
     * Checks if a profile is a learner.
     * @param profileId The profile ID.
     * @return true if learner, false otherwise.
     */
    boolean isLearnerProfile(Long profileId);

    /**
     * Checks if a profile is a partner.
     * @param profileId The profile ID.
     * @return true if partner, false otherwise.
     */
    boolean isPartnerProfile(Long profileId);

    /**
     * Checks if a learner profile is active and complete.
     * @param learnerId The learner ID.
     * @return true if active and has at least one language, false otherwise.
     */
    boolean isLearnerActiveAndComplete(Long learnerId);

    /**
     * Checks if a partner profile is active.
     * @param partnerId The partner ID.
     * @return true if active, false otherwise.
     */
    boolean isPartnerActive(Long partnerId);

    /**
     * Fetches learner languages count.
     * @param learnerId The learner ID.
     * @return The number of languages, or 0 if not found.
     */
    int fetchLearnerLanguagesCount(Long learnerId);

    /**
     * Checks if learner has a specific language.
     * @param learnerId The learner ID.
     * @param languageId The language ID.
     * @return true if learner has the language, false otherwise.
     */
    boolean learnerHasLanguage(Long learnerId, Long languageId);

    /**
     * Fetches partner contact email.
     * @param partnerId The partner ID.
     * @return The contact email, or empty string if not found.
     */
    String fetchPartnerContactEmail(Long partnerId);

}
