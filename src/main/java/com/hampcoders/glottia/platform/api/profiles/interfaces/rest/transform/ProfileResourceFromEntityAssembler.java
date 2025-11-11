package com.hampcoders.glottia.platform.api.profiles.interfaces.rest.transform;

import com.hampcoders.glottia.platform.api.profiles.domain.model.aggregates.Profile;
import com.hampcoders.glottia.platform.api.profiles.interfaces.rest.resources.ProfileResource;

public class ProfileResourceFromEntityAssembler {

    public static ProfileResource toResourceFromEntity(Profile entity) {
        String street = null;
        String number = null;
        String city = null;
        String postalCode = null;
        String country = null;
        String legalName = null;
        String businessName = null;
        String taxId = null;
        String contactEmail = null;
        String contactPhone = null;
        String description = null;
        String contactPersonName = null;
        Long learnerId = null;
        Long partnerId = null;

        switch (entity.getBusinessRole().getRole()) {
            case LEARNER -> {
                if (entity.getLearner() != null) {
                    street = entity.getLearner().getAddress().street();
                    number = entity.getLearner().getAddress().number();
                    city = entity.getLearner().getAddress().city();
                    postalCode = entity.getLearner().getAddress().postalCode();
                    country = entity.getLearner().getAddress().country();
                    learnerId = entity.getLearner().getId();
                } else {
                    throw new IllegalStateException("Learner details are missing for profile ID: " + entity.getId());
                }
            }
            case PARTNER -> {
                if (entity.getPartner() != null) {
                    legalName = entity.getPartner().getLegalName();
                    businessName = entity.getPartner().getBusinessName();
                    taxId = entity.getPartner().getTaxId();
                    contactEmail = entity.getPartner().getContactEmail();
                    contactPhone = entity.getPartner().getContactPhone();
                    description = entity.getPartner().getDescription();
                    contactPersonName = entity.getPartner().getContactPersonName();
                    partnerId = entity.getPartner().getId();
                } else {
                    throw new IllegalStateException("Partner details are missing for profile ID: " + entity.getId());
                }
            }
        }

        return new ProfileResource(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getAge(),
                entity.getEmail(),
                entity.getBusinessRole().getRole().name(), 
                street,
                number,
                city,
                postalCode,
                country,
                legalName,
                businessName,
                taxId,
                contactEmail,
                contactPhone,
                description,
                contactPersonName,
                learnerId,
                partnerId
        );
    }
}