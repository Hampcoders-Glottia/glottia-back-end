package com.hampcoders.glottia.platform.api.venues.domain.services;

import java.util.List;
import java.util.Optional;

import com.hampcoders.glottia.platform.api.venues.domain.model.entities.TableType;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.types.GetAllTableTypesQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.types.GetTableTypeByNameQuery;

public interface TableTypeQueryService {

    List<TableType> handle(GetAllTableTypesQuery query);
    Optional<TableType> handle(GetTableTypeByNameQuery query);
}
