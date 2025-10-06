package com.hampcoders.glottia.platform.api.profiles.application.internal.queryservices;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.hampcoders.glottia.platform.api.profiles.domain.model.aggregates.Profile;
import com.hampcoders.glottia.platform.api.profiles.domain.model.entities.Partner;
import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.GetAllPartnersQuery;
import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.GetPartnerByBusinessNameQuery;
import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.GetPartnerByIdQuery;
import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.GetPartnerByProfileIdQuery;
import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.GetPartnerByTaxIdQuery;
import com.hampcoders.glottia.platform.api.profiles.domain.model.queries.GetPartnersBySubscriptionStatusQuery;
import com.hampcoders.glottia.platform.api.profiles.domain.services.PartnerQueryService;
import com.hampcoders.glottia.platform.api.profiles.infrastructure.persistence.jpa.repository.PartnerRepository;
import com.hampcoders.glottia.platform.api.profiles.infrastructure.persistence.jpa.repository.ProfileRepository;


@Service
public class PartnerQueryServiceImpl implements PartnerQueryService {

    private final PartnerRepository partnerRepository;

    public PartnerQueryServiceImpl(PartnerRepository partnerRepository) {
        this.partnerRepository = partnerRepository;
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
        return this.partnerRepository.findByProfileId(query.profileId());
    }

    @Override
    public Optional<Partner> handle(GetPartnerByTaxIdQuery query) {
        return this.partnerRepository.findByTaxId(query.taxId());
    }

    @Override
    public Optional<Partner> handle(GetPartnerByBusinessNameQuery query) {
        return this.partnerRepository.findByBusinessNameIgnoreCase(query.businessName());
    }

    @Override
    public List<Partner> handle(GetPartnersBySubscriptionStatusQuery query) {
        return this.partnerRepository.findBySubscriptionStatusId(query.subscriptionStatus().getId());
    }
}