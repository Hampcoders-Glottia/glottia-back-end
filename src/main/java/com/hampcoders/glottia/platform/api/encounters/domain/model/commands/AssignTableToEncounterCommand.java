package com.hampcoders.glottia.platform.api.encounters.domain.model.commands;

import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.TableId;

public record AssignTableToEncounterCommand(Long encounterId, TableId tableId) {}
