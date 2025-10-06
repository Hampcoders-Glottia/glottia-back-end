package com.hampcoders.glottia.platform.api.profiles.application.internal.commandservices;

import org.springframework.stereotype.Service;

import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.CreatePartnerCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.UpdatePartnerContactCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.UpdatePartnerSubscriptionCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.GetSubscriptionStatusByIdQuery;
import com.hampcoders.glottia.platform.api.profiles.domain.services.PartnerCommandService;
import com.hampcoders.glottia.platform.api.profiles.domain.services.SubscriptionStatusQueryService;
import com.hampcoders.glottia.platform.api.profiles.infrastructure.persistence.jpa.repository.ProfileRepository;

import jakarta.transaction.Transactional;

@Service
public class PartnerCommandServiceImpl implements PartnerCommandService {

    private final ProfileRepository profileRepository;
    private final SubscriptionStatusQueryService subscriptionStatusQueryService;

    public PartnerCommandServiceImpl(ProfileRepository profileRepository,
                                   SubscriptionStatusQueryService subscriptionStatusQueryService) {
        this.profileRepository = profileRepository;
        this.subscriptionStatusQueryService = subscriptionStatusQueryService;
    }

    @Override
    @Transactional
    public Long handle(CreatePartnerCommand command) {
        // Validate if profile exists
        var profile = profileRepository.findById(command.profileId())
            .orElseThrow(() -> new IllegalArgumentException("Profile with id " + command.profileId() + " not found"));
        
        // Validate if profile is not already a partner
        if (profile.isPartner()) {
            throw new IllegalArgumentException("Profile with id " + command.profileId() + " is already a partner");
        }
        
        // Validate if subscription status exists
        var subscriptionStatusId = command.subscriptionStatusId() != null ? command.subscriptionStatusId() : 1L; // Default: PENDING
        var subscriptionStatus = subscriptionStatusQueryService.handle(new GetSubscriptionStatusByIdQuery(subscriptionStatusId))
            .orElseThrow(() -> new IllegalArgumentException("Subscription status with id " + subscriptionStatusId + " not found"));
        
        // Assign as partner
        profile.assignAsPartner(
            command.legalName() != null ? command.legalName() : "Default Legal Name",
            command.businessName() != null ? command.businessName() : "Default Business",
            command.taxId() != null ? command.taxId() : "DEFAULT-TAX-ID",
            command.contactEmail() != null ? command.contactEmail() : profile.getEmail(),
            command.contactPhone() != null ? command.contactPhone() : "000-000-0000",
            command.contactPersonName() != null ? command.contactPersonName() : profile.getFirstName() + " " + profile.getLastName(),
            command.description() != null ? command.description() : "Default description"
        );
        
        try {
            profileRepository.save(profile);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while saving partner: " + e.getMessage());
        }
        
        return profile.getPartner().getId();
    }

    @Override
    @Transactional
    public void handle(UpdatePartnerContactCommand command) {
        // Validate if profile exists
        var profile = profileRepository.findById(command.partnerId())
            .orElseThrow(() -> new IllegalArgumentException("Profile with id " + command.partnerId() + " not found"));
        
        // Validate if profile is a partner
        if (!profile.isPartner()) {
            throw new IllegalArgumentException("Profile with id " + command.partnerId() + " is not a partner");
        }
        
        // Update partner contact information
        var partner = profile.getPartner();
        partner.updateContactInformation(
            command.contactEmail(),
            command.contactPhone(),
            command.contactPersonName()
        );
        
        try {
            profileRepository.save(profile);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while updating partner contact: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void handle(UpdatePartnerSubscriptionCommand command) {
        // Validate if profile exists
        var profile = profileRepository.findById(command.partnerId())
            .orElseThrow(() -> new IllegalArgumentException("Profile with id " + command.partnerId() + " not found"));
        
        // Validate if profile is a partner
        if (!profile.isPartner()) {
            throw new IllegalArgumentException("Profile with id " + command.partnerId() + " is not a partner");
        }
        
        // Validate if subscription status exists
        var subscriptionStatus = subscriptionStatusQueryService.handle(new GetSubscriptionStatusByIdQuery(command.subscriptionStatusId()))
            .orElseThrow(() -> new IllegalArgumentException("Subscription status with id " + command.subscriptionStatusId() + " not found"));
        
        // Update partner subscription
        var partner = profile.getPartner();
        partner.updateSubscriptionStatus(subscriptionStatus);
        
        try {
            profileRepository.save(profile);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while updating partner subscription: " + e.getMessage());
        }
    }
}