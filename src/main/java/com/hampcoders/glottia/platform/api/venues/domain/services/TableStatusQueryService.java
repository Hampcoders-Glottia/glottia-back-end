package com.hampcoders.glottia.platform.api.venues.domain.services;

import java.util.List;
import java.util.Optional;

import com.hampcoders.glottia.platform.api.venues.domain.model.entities.TableStatus;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.types.GetAllTableStatusesQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.types.GetTableStatusByNameQuery;

public interface TableStatusQueryService {
    List<TableStatus> handle(GetAllTableStatusesQuery query);
    Optional<TableStatus> handle(GetTableStatusByNameQuery query);
}
