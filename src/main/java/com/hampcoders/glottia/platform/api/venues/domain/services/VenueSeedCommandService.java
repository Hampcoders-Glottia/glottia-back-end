package com.hampcoders.glottia.platform.api.venues.domain.services;

import com.hampcoders.glottia.platform.api.venues.domain.model.commands.types.SeedPromotionTypesCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.commands.types.SeedTableStatusesCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.commands.types.SeedTableTypesCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.commands.types.SeedVenueTypesCommand;

public interface VenueSeedCommandService {

    void handle(SeedVenueTypesCommand command);
    void handle(SeedPromotionTypesCommand command);
    void handle(SeedTableTypesCommand command);
    void handle(SeedTableStatusesCommand command);
    
}
