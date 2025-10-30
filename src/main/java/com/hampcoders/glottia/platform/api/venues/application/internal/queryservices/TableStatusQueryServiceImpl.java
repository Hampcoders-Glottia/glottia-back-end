package com.hampcoders.glottia.platform.api.venues.application.internal.queryservices;

import com.hampcoders.glottia.platform.api.venues.domain.model.entities.TableStatus;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.types.GetAllTableStatusesQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.types.GetTableStatusByNameQuery;
import com.hampcoders.glottia.platform.api.venues.domain.services.TableStatusQueryService;
import com.hampcoders.glottia.platform.api.venues.infrastructure.persistence.jpa.repositories.TableStatusRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TableStatusQueryServiceImpl implements TableStatusQueryService {
    private final TableStatusRepository tableStatusRepository;

    public TableStatusQueryServiceImpl(TableStatusRepository tableStatusRepository) {
        this.tableStatusRepository = tableStatusRepository;
    }

    @Override
    public List<TableStatus> handle(GetAllTableStatusesQuery query) {
        return tableStatusRepository.findAll();
    }

    @Override
    public Optional<TableStatus> handle(GetTableStatusByNameQuery query) {
        return tableStatusRepository.findByName(query.name());
    }
}