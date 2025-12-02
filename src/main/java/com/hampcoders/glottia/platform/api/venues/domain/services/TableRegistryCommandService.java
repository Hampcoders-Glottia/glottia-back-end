package com.hampcoders.glottia.platform.api.venues.domain.services;

import com.hampcoders.glottia.platform.api.venues.domain.model.commands.tables.AddTableCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.commands.tables.CreateAvailabilitySlotCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.commands.tables.MarkTableAvailableCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.commands.tables.MarkTableDateUnavailableCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.commands.tables.MarkTableUnavailableCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.commands.tables.ReleaseTableCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.commands.tables.RemoveTableCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.commands.tables.ReserveTableCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.commands.tables.UpdateTableCommand;

public interface TableRegistryCommandService {
    void handle(AddTableCommand command);

    void handle(CreateAvailabilitySlotCommand command);

    void handle(MarkTableAvailableCommand command);

    void handle(MarkTableDateUnavailableCommand command);

    void handle(MarkTableUnavailableCommand command);

    void handle(ReleaseTableCommand command);

    void handle(RemoveTableCommand command);

    void handle(ReserveTableCommand command);

    void handle(UpdateTableCommand command);
}
