package com.hampcoders.glottia.platform.api.encounters.application.internal.commandservices;

import java.util.Arrays;

import org.springframework.stereotype.Service;

import com.hampcoders.glottia.platform.api.encounters.domain.model.commands.SeedAttendanceStatusesCommand;
import com.hampcoders.glottia.platform.api.encounters.domain.model.commands.SeedEncounterStatusesCommand;
import com.hampcoders.glottia.platform.api.encounters.domain.model.entities.AttendanceStatus;
import com.hampcoders.glottia.platform.api.encounters.domain.model.entities.EncounterStatus;
import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.AttendanceStatuses;
import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.EncounterStatuses;
import com.hampcoders.glottia.platform.api.encounters.domain.services.EncounterSeedCommandService;
import com.hampcoders.glottia.platform.api.encounters.infrastructure.persistence.jpa.repository.AttendanceStatusRepository;
import com.hampcoders.glottia.platform.api.encounters.infrastructure.persistence.jpa.repository.EncounterStatusRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EncounterSeedCommandServiceImpl implements EncounterSeedCommandService {

    private final EncounterStatusRepository encounterStatusRepository;
    private final AttendanceStatusRepository attendanceStatusRepository;

    @Override
    public void handle(SeedEncounterStatusesCommand command) {
        Arrays.stream(EncounterStatuses.values()).forEach(encounterStatus -> {
            if (!encounterStatusRepository.existsByName(encounterStatus)) {
                encounterStatusRepository.save(new EncounterStatus(EncounterStatuses.valueOf(encounterStatus.name())));
            }
        });
    }

    @Override
    public void handle(SeedAttendanceStatusesCommand command) {
        Arrays.stream(AttendanceStatuses.values()).forEach(attendanceStatus -> {
            if (!attendanceStatusRepository.existsByName(attendanceStatus)) {
                attendanceStatusRepository.save(new AttendanceStatus(AttendanceStatuses.valueOf(attendanceStatus.name())));
            }
        });
    }



}
