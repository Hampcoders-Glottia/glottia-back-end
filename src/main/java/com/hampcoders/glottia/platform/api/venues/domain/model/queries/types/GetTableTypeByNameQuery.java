package com.hampcoders.glottia.platform.api.venues.domain.model.queries.types;

import com.hampcoders.glottia.platform.api.venues.domain.model.valueobjects.TableTypes;

public record GetTableTypeByNameQuery(
    TableTypes name
) {}