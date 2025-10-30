package com.hampcoders.glottia.platform.api.encounters.domain.model.events;

import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.*;

public record EncounterTableAssignedEvent(
    Long encounterId, 
    TableId tableId) {} // Nuevo evento cuando Venues confirma
