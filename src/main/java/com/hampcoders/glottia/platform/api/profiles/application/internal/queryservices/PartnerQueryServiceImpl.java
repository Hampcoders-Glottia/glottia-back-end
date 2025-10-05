package com.hampcoders.glottia.platform.api.profiles.application.internal.queryservices;

import com.hampcoders.glottia.platform.api.profiles.domain.model.aggregates.Partner;
import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.*;
import com.hampcoders.glottia.platform.api.profiles.domain.services.PartnerQueryService;
import com.hampcoders.glottia.platform.api.profiles.infrastructure.persistence.jpa.repository.PartnerRepository;
import com.hampcoders.glottia.platform.api.profiles.infrastructure.persistence.jpa.repository.ProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PartnerQueryServiceImpl implements PartnerQueryService {

    private final PartnerRepository partnerRepository;
    private final ProfileRepository profileRepository;

    public PartnerQueryServiceImpl(PartnerRepository partnerRepository, ProfileRepository profileRepository) {
        this.partnerRepository = partnerRepository;
        this.profileRepository = profileRepository;
    }

    @Override
    public List<Partner> handle(GetAllPartnersQuery query) {
        return this.partnerRepository.findAll();
    }

    @Override
    public Optional<Partner> handle(GetPartnerByIdQuery query) {
        return this.partnerRepository.findById(query.partnerId());
    }

    @Override
    public Optional<Partner> handle(GetPartnerByProfileIdQuery query) {
        var profile = this.profileRepository.findById(query.profileId());
        return profile.map(p -> Optional.ofNullable(p.getPartner()))
                     .orElse(Optional.empty());
    }

    @Override
    public List<Partner> handle(GetPartnersBySubscriptionStatusQuery query) {
        return this.partnerRepository.findBySubscriptionStatus(query.subscriptionStatus());
    }
}