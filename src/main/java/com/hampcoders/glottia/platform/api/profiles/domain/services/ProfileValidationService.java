package com.hampcoders.glottia.platform.api.profiles.domain.services;

import com.hampcoders.glottia.platform.api.profiles.domain.model.aggregates.Learner;
import com.hampcoders.glottia.platform.api.profiles.domain.model.aggregates.Partner;
import com.hampcoders.glottia.platform.api.profiles.domain.model.aggregates.Profile;

/**
 * Domain service for business validation rules
 */
public interface ProfileValidationService {
    
    // Profile validations
    boolean isEmailUnique(String email, Long excludeProfileId);
    boolean canProfileAssignLearner(Profile profile);
    boolean canProfileAssignPartner(Profile profile);
    
    // Profile type consistency validations
    boolean isProfileAssignmentConsistent(Profile profile);
    boolean hasConflictingAssignments(Profile profile);
    
    // Partner validations
    boolean isTaxIdUnique(String taxId, Long excludePartnerId);
    boolean isPartnerEligibleForActivation(Partner partner);
    
    // Learner validations
    boolean canLearnerAddLanguage(Learner learner, Long languageId);
    boolean isLanguageCombinationValid(Learner learner, Long languageId, Long cefrLevelId);
    
    // Business rule validations
    boolean canProfileBeDeleted(Profile profile);
    boolean isAgeValidForRegistration(int age);
}