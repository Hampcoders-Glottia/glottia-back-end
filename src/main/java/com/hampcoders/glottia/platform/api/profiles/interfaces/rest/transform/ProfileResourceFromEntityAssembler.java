package com.hampcoders.glottia.platform.api.profiles.interfaces.rest.transform;

import com.hampcoders.glottia.platform.api.profiles.domain.model.aggregates.Profile;
import com.hampcoders.glottia.platform.api.profiles.interfaces.rest.resources.ProfileResource;
import java.util.List;
import java.util.stream.Collectors;

public class ProfileResourceFromEntityAssembler {
    public static ProfileResource toResourceFromEntity(Profile entity) {
        // Get the first (primary) business role name
        String businessRole = entity.getBusinessRoles().isEmpty() ? 
            "NONE" : 
            entity.getBusinessRoles().iterator().next().getStringRoleName();
            
        // Determine profile type
        String profileType;
        String street = null;
        String city = null; 
        String country = null;
        String languageInfo = null;
        String businessInfo = null;
        Long learnerId = null;
        Long partnerId = null;
        
        if (entity.isLearner()) {
            profileType = "LEARNER";
            var learner = entity.getLearner();
            if (learner != null) {
                learnerId = learner.getId();
                if (learner.getAddress() != null) {
                    street = learner.getAddress().street();
                    city = learner.getAddress().city();
                    country = learner.getAddress().country();
                }
                // Build language info
                languageInfo = learner.getLanguages().stream()
                    .map(lang -> lang.getLanguage().getStringLanguageName() + ":" + lang.getCefrLevel().getStringCefrLevelName())
                    .collect(Collectors.joining(", "));
            }
        } else if (entity.isPartner()) {
            profileType = "PARTNER";
            var partner = entity.getPartner();
            if (partner != null) {
                partnerId = partner.getId();
                businessInfo = partner.getBusinessName() + " - " + partner.getContactEmail();
            }
        } else {
            profileType = "UNASSIGNED";
        }
        
        return new ProfileResource(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getAge(),
                entity.getEmail(),
                businessRole,
                profileType,
                street,
                city,
                country,
                languageInfo,
                businessInfo,
                learnerId,
                partnerId
        );
    }

    public static List<ProfileResource> toResourceListFromEntities(List<Profile> entities) {
        return entities.stream().map(ProfileResourceFromEntityAssembler::toResourceFromEntity).collect(Collectors.toList());
    }
}
