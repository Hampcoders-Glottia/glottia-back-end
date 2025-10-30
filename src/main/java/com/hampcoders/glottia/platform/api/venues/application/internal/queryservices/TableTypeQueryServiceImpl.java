package com.hampcoders.glottia.platform.api.venues.application.internal.queryservices;

import com.hampcoders.glottia.platform.api.venues.domain.model.entities.TableType;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.types.GetAllTableTypesQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.types.GetTableTypeByNameQuery;
import com.hampcoders.glottia.platform.api.venues.domain.services.TableTypeQueryService;
import com.hampcoders.glottia.platform.api.venues.infrastructure.persistence.jpa.repositories.TableTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TableTypeQueryServiceImpl implements TableTypeQueryService {
    private final TableTypeRepository tableTypeRepository;

    public TableTypeQueryServiceImpl(TableTypeRepository tableTypeRepository) {
        this.tableTypeRepository = tableTypeRepository;
    }

    @Override
    public List<TableType> handle(GetAllTableTypesQuery query) {
        return tableTypeRepository.findAll();
    }

    @Override
    public Optional<TableType> handle(GetTableTypeByNameQuery query) {
        return tableTypeRepository.findByName(query.name());
    }
}