package com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.tables;

import java.util.List;

public record TableRegistryResource(Long tableRegistryId, Long venueId, List<TableResource> tables) {

}
