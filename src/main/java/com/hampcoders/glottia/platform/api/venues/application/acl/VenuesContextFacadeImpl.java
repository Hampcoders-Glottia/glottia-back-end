package com.hampcoders.glottia.platform.api.venues.application.acl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hampcoders.glottia.platform.api.venues.domain.model.commands.venues.CreatePartnerVenueRegistryCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.promotions.GetActivePromotionsByVenueIdQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.promotions.GetPromotionByIdQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.promotions.GetVenuePromotionByIdQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.tables.GetTableAvailabilityFromDateToDateQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.tables.GetTableByIdQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.venues.GetPartnerVenueRegistryByPartnerIdQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.venues.GetVenueByIdQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.venues.GetVenuesByPartnerIdQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.valueobjects.PartnerId;
import com.hampcoders.glottia.platform.api.venues.domain.services.PartnerVenueRegistryCommandService;
import com.hampcoders.glottia.platform.api.venues.domain.services.PartnerVenueRegistryQueryService;
import com.hampcoders.glottia.platform.api.venues.domain.services.PromotionQueryService;
import com.hampcoders.glottia.platform.api.venues.domain.services.TableRegistryQueryService;
import com.hampcoders.glottia.platform.api.venues.domain.services.VenueQueryService;
import com.hampcoders.glottia.platform.api.venues.interfaces.acl.VenuesContextFacade;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class VenuesContextFacadeImpl implements VenuesContextFacade {

    private final PartnerVenueRegistryCommandService partnerVenueRegistryCommandService;
    private final VenueQueryService venueQueryService;
    private final TableRegistryQueryService tableRegistryQueryService;
    private final PromotionQueryService promotionQueryService;
    private final PartnerVenueRegistryQueryService partnerVenueRegistryQueryService;

    @Override
    public Long createPartnerVenueRegistry(Long partnerId) {
        var command = new CreatePartnerVenueRegistryCommand(new PartnerId(partnerId));
        return partnerVenueRegistryCommandService.handle(command); 
    }

    @Override
    public boolean isVenueActive(Long venueId) {
        try {
            return venueQueryService.handle(new GetVenueByIdQuery(venueId)).map(venue -> venue.isActive()).orElse(false);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean exitsVenueRegistryByPartnerId(Long partnerId) {
        try {
            var query = new GetPartnerVenueRegistryByPartnerIdQuery(new PartnerId(partnerId));
            return partnerVenueRegistryQueryService.handle(query).isPresent();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isTableAvailable(Long tableId, LocalDate date) {
        try {
            var query = new GetTableAvailabilityFromDateToDateQuery(tableId, date, date);
            var result = tableRegistryQueryService.handle(query);
            return !result.isEmpty() && result.get(0).getIsAvailable();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Long fetchVenueIdByTableId(Long tableId) {
        try {
            return tableRegistryQueryService.handle(new GetTableByIdQuery(tableId)).map(table -> table.getTableRegistry().getVenue().getId()).orElse(0L);
        } catch (Exception e) {
            return 0L;
        }
    }

    @Override
    public boolean venueBelongsToPartner(Long venueId, Long partnerId) {
        try {
            var query = new GetVenuesByPartnerIdQuery(new PartnerId(partnerId));
            var result = venueQueryService.handle(query);
            return result.stream()
                .anyMatch(venue -> venue.getId().equals(venueId));
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public int fetchTableCapacity(Long tableId) {
        try {
            var query = new GetTableByIdQuery(tableId);
            var result = tableRegistryQueryService.handle(query);
            return result.map(table -> table.getCapacity()).orElse(0);
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public String fetchVenueName(Long venueId) {
        try {
            var query = new GetVenueByIdQuery(venueId);
            var result = venueQueryService.handle(query);
            return result.map(venue -> venue.getName()).orElse("");
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public String fetchVenueAddress(Long venueId) {
        try {
            var query = new GetVenueByIdQuery(venueId);
            var result = venueQueryService.handle(query);
            return result
                .map(venue -> String.format("%s, %s, %s",
                    venue.getAddress().street(),
                    venue.getAddress().city(),
                    venue.getAddress().country()))
                .orElse("");
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Checks if a promotion exists and is active.
     * @param promotionId The promotion ID.
     * @return true if promotion is active, false otherwise.
     */
    public boolean isPromotionActive(Long promotionId) {
        try {
            var query = new GetPromotionByIdQuery(promotionId);
            var result = promotionQueryService.handle(query);
            return result.map(promotion -> promotion.getIsActive()).orElse(false);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if a promotion belongs to a specific partner.
     * @param promotionId The promotion ID.
     * @param partnerId The partner ID.
     * @return true if promotion belongs to partner, false otherwise.
     */
    public boolean promotionBelongsToPartner(Long promotionId, Long partnerId) {
        try {
            var query = new GetPromotionByIdQuery(promotionId);
            var result = promotionQueryService.handle(query);
            return result
                .map(promotion -> promotion.getPartnerId().partnerId().equals(partnerId))
                .orElse(false);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Fetches active promotions for a venue.
     * @param venueId The venue ID.
     * @return List of active promotion IDs.
     */
    public List<Long> fetchActivePromotionIdsByVenue(Long venueId) {
        try {
            var query = new GetActivePromotionsByVenueIdQuery(venueId);
            var result = promotionQueryService.handle(query);
            return result.stream()
                .map(vp -> vp.getPromotion().getId())
                .toList();
        } catch (Exception e) {
            return List.of();
        }
    }

    /**
     * Checks if a venue promotion can be redeemed.
     * @param venuePromotionId The venue promotion ID.
     * @return true if can be redeemed, false otherwise.
     */
    public boolean canRedeemVenuePromotion(Long venuePromotionId) {
        try {
            var query = new GetVenuePromotionByIdQuery(venuePromotionId);
            var result = promotionQueryService.handle(query);
            return result.map(vp -> vp.canBeRedeemed()).orElse(false);
        } catch (Exception e) {
            return false;
        }
    }
}
