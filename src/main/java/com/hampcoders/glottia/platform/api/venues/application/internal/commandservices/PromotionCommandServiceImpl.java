package com.hampcoders.glottia.platform.api.venues.application.internal.commandservices;

import org.springframework.stereotype.Service;

import com.hampcoders.glottia.platform.api.venues.domain.model.aggregates.Promotion;
import com.hampcoders.glottia.platform.api.venues.domain.model.aggregates.Venue;
import com.hampcoders.glottia.platform.api.venues.domain.model.commands.promotions.ActivateVenuePromotionCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.commands.promotions.AddPromotionToVenueCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.commands.promotions.CreatePromotionCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.commands.promotions.DeactivateVenuePromotionCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.entities.VenueRegistration;
import com.hampcoders.glottia.platform.api.venues.domain.model.valueobjects.PartnerId;
import com.hampcoders.glottia.platform.api.venues.domain.model.valueobjects.PromotionTypes;
import com.hampcoders.glottia.platform.api.venues.domain.services.PromotionCommandService;
import com.hampcoders.glottia.platform.api.venues.infrastructure.persistence.jpa.repositories.PartnerVenueRegistryRepository;
import com.hampcoders.glottia.platform.api.venues.infrastructure.persistence.jpa.repositories.PromotionRepository;
import com.hampcoders.glottia.platform.api.venues.infrastructure.persistence.jpa.repositories.PromotionTypeRepository;
import com.hampcoders.glottia.platform.api.venues.infrastructure.persistence.jpa.repositories.VenuePromotionRepository;
import com.hampcoders.glottia.platform.api.venues.infrastructure.persistence.jpa.repositories.VenueRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class PromotionCommandServiceImpl implements PromotionCommandService {

    private final VenueRepository venueRepository;
    private final PromotionRepository promotionRepository;  
    private final PromotionTypeRepository promotionTypeRepository;
    private final VenuePromotionRepository venuePromotionRepository;
    private final PartnerVenueRegistryRepository partnerVenueRegistryRepository;  

    public PromotionCommandServiceImpl(VenueRepository venueRepository, PromotionRepository promotionRepository, PromotionTypeRepository promotionTypeRepository, VenuePromotionRepository venuePromotionRepository, PartnerVenueRegistryRepository partnerVenueRegistryRepository) {
        this.venueRepository = venueRepository;
        this.promotionRepository = promotionRepository;
        this.venuePromotionRepository = venuePromotionRepository;
        this.partnerVenueRegistryRepository = partnerVenueRegistryRepository;
        this.promotionTypeRepository = promotionTypeRepository;
    }

    /**
     * Handles the creation of a new promotion.
     * Validates uniqueness of promotion name per partner.
     * 
     * @param command The command containing promotion details
     * @return The ID of the created promotion
     * @throws IllegalArgumentException if promotion name already exists for partner
     */
    @Override
    @Transactional
    public Long handle(CreatePromotionCommand command) {
        if (promotionRepository.existsByNameAndPartnerId(command.name(), command.partnerId())) {
            throw new IllegalArgumentException("A promotion with the same name already exists for this partner.");
        }

        var promotionType = promotionTypeRepository.findByName(PromotionTypes.valueOf(command.promotionType())).orElseThrow(() -> new IllegalArgumentException("Promotion type not found"));

        Promotion promotion = new Promotion(command, promotionType);
        promotionRepository.save(promotion);
        return promotion.getId();
    }

    @Override
    @Transactional
    public void handle(AddPromotionToVenueCommand command) {
        Venue venue = venueRepository.findById(command.venueId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Venue not found with ID: " + command.venueId()));

        Promotion promotion = promotionRepository.findById(command.promotionId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Promotion not found with ID: " + command.promotionId()));

        VenueRegistration venueRegistration = partnerVenueRegistryRepository
                .findActiveRegistrationByVenueId(command.venueId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Venue is not registered to any active partner"));
        
        PartnerId venuePartnerId = venueRegistration.getPartnerVenueRegistry().getPartnerId();
        PartnerId promotionPartnerId = promotion.getPartnerId();

        // 4. ✅ Validar ownership del Partner
        if (!venuePartnerId.equals(promotionPartnerId)) {
            throw new IllegalArgumentException(
                String.format("Cannot add promotion: Promotion belongs to partner %d but venue belongs to partner %d",
                    promotionPartnerId.partnerId(), venuePartnerId.partnerId()));
        }

        // 5. ✅ Validar duplicados ANTES de delegar (early validation)
        if (venuePromotionRepository.existsByVenueIdAndPromotionIdAndIsActive(
                command.venueId(), command.promotionId(), true)) {
            throw new IllegalStateException(
                "Promotion is already active in this venue");
        }

        // 6. ✅ Delegar al agregado (similar a inventory.addToStock())
        venue.addPromotion(
                promotion, 
                command.validFrom(), 
                command.validUntil(), 
                command.maxRedemptions()
        );

        // 7. ✅ Guardar el agregado (JPA cascade guardará VenuePromotion)
        venueRepository.save(venue);

        // TODO: Emit VenuePromotionAddedEvent(venuePromotionId, venueId, promotionId)
    }


    /** 
     * Handle DeactivateVenuePromotionCommand
     * Deactivates a specific promotion for a venue
     * 
     * @param command The command containing venuePromotionId and reason
     * @throws EntityNotFoundException if venue or venue promotion not found
     */
    @Override
    @Transactional
    public void handle(DeactivateVenuePromotionCommand command) {
        var venuePromotion = venuePromotionRepository.findById(command.venuePromotionId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "VenuePromotion not found with ID: " + command.venuePromotionId()));

        Venue venue = venueRepository.findById(venuePromotion.getVenue().getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Venue not found for VenuePromotion"));

        venue.deactivatePromotion(command.venuePromotionId(), command.reason());

        venueRepository.save(venue);

        // TODO: Emit PromotionDeactivatedEvent(venuePromotionId, venueId, reason)
    }

        

    /**
     * Handle ActivateVenuePromotionCommand
     * Reactivates a previously deactivated promotion.
     * @param command The command containing venuePromotionId
     * @throws EntityNotFoundException if venue or venue promotion not found
     * @throws IllegalStateException if promotion is already active or expired
     */
    @Override
    @Transactional
    public void handle(ActivateVenuePromotionCommand command) {
        var venuePromotion = venuePromotionRepository.findById(command.venuePromotionId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "VenuePromotion not found with ID: " + command.venuePromotionId()));

        Venue venue = venueRepository.findById(venuePromotion.getVenue().getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Venue not found for VenuePromotion"));

        venue.activatePromotion(command.venuePromotionId());

        venueRepository.save(venue);

        // TODO: Emit PromotionActivatedEvent(venuePromotionId, venueId)
    }

}
