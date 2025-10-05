package com.hampcoders.glottia.platform.api.profiles.application.internal.commandservices;

import com.hampcoders.glottia.platform.api.profiles.domain.model.aggregates.Partner;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.CreatePartnerCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.UpdatePartnerContactCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.model.commands.UpdatePartnerSubscriptionCommand;
import com.hampcoders.glottia.platform.api.profiles.domain.services.PartnerCommandService;
import com.hampcoders.glottia.platform.api.profiles.infrastructure.persistence.jpa.repository.PartnerRepository;
import com.hampcoders.glottia.platform.api.profiles.infrastructure.persistence.jpa.repository.ProfileRepository;
import com.hampcoders.glottia.platform.api.profiles.infrastructure.persistence.jpa.repository.SubscriptionStatusRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PartnerCommandServiceImpl implements PartnerCommandService {

    private final PartnerRepository partnerRepository;
    private final ProfileRepository profileRepository;
    private final SubscriptionStatusRepository subscriptionStatusRepository;

    public PartnerCommandServiceImpl(PartnerRepository partnerRepository, ProfileRepository profileRepository,
                                   SubscriptionStatusRepository subscriptionStatusRepository) {
        this.partnerRepository = partnerRepository;
        this.profileRepository = profileRepository;
        this.subscriptionStatusRepository = subscriptionStatusRepository;
    }

    @Override
    public Long handle(CreatePartnerCommand command) {
        var profile = this.profileRepository.findById(command.profileId())
                .orElseThrow(() -> new IllegalArgumentException("Profile with id " + command.profileId() + " does not exist"));
        
        if (profile.getPartner() != null) {
            throw new IllegalArgumentException("Profile " + command.profileId() + " is already assigned as a Partner");
        }
        
        if (profile.getLearner() != null) {
            throw new IllegalArgumentException("Profile " + command.profileId() + " is already assigned as a Learner");
        }

        if (this.partnerRepository.existsByTaxId(command.taxId())) {
            throw new IllegalArgumentException("Partner with tax ID " + command.taxId() + " already exists");
        }

        var partner = new Partner(command);
        profile.assignAsPartner(partner);
        
        try {
            this.partnerRepository.save(partner);
            this.profileRepository.save(profile);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while saving partner: " + e.getMessage());
        }
        
        return partner.getId();
    }

    @Override
    public Optional<Partner> handle(UpdatePartnerContactCommand command) {
        var partner = this.partnerRepository.findById(command.partnerId())
                .orElseThrow(() -> new IllegalArgumentException("Partner with id " + command.partnerId() + " does not exist"));
        
        partner.updateContactInfo(command.contactEmail(), command.contactPhone(), command.contactPersonName());
        var updatedPartner = this.partnerRepository.save(partner);
        
        return Optional.of(updatedPartner);
    }

    @Override
    public Optional<Partner> handle(UpdatePartnerSubscriptionCommand command) {
        var partner = this.partnerRepository.findById(command.partnerId())
                .orElseThrow(() -> new IllegalArgumentException("Partner with id " + command.partnerId() + " does not exist"));
        
        var subscriptionStatus = this.subscriptionStatusRepository.findById(command.subscriptionStatusId())
                .orElseThrow(() -> new IllegalArgumentException("Subscription Status with id " + command.subscriptionStatusId() + " does not exist"));
        
        partner.updateSubscriptionStatus(subscriptionStatus);
        var updatedPartner = this.partnerRepository.save(partner);
        
        return Optional.of(updatedPartner);
    }
}