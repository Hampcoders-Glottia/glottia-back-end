package com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.tables;

import java.util.Optional;

public record UpdateTableResource(Long tableId, Optional<Integer> capacity, Optional<Long> tableTypeId) {

}
