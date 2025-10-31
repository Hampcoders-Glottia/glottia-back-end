package com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects;

import com.hampcoders.glottia.platform.api.encounters.domain.model.aggregates.Encounter;
import com.hampcoders.glottia.platform.api.encounters.domain.model.entities.Attendance;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Value object representing a list of Attendances for an Encounter.
 * @summary
 * This class encapsulates a collection of Attendance entities associated with a specific Encounter.
 * It provides methods to manage and query the attendance records, such as adding new attendances, removing existing ones, and retrieving attendance information.
 * It is designed to be embedded within the Encounter aggregate root.
 * @see Attendance
 * @see Encounter
 */
@Embeddable
@Getter
public class AttendanceList {

    @OneToMany(mappedBy = "encounter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attendance> items;

    public AttendanceList() {
        this.items = new ArrayList<>();
    }

    public int size() {
        return items.size();
    }

    public void addItem(Encounter encounter, LearnerId learnerId) {
        this.items.add(new Attendance(encounter, learnerId));
    }

    public boolean hasLearner(LearnerId learnerId) {
        return items.stream().anyMatch(item -> item.getLearnerId().equals(learnerId) && (item.getStatus().getName() == AttendanceStatuses.RESERVED || item.getStatus().getName() == AttendanceStatuses.CHECKED_IN)); // Solo contar activos
    }

    public Optional<Attendance> findByLearnerId(LearnerId learnerId) {
        return items.stream()
                .filter(item -> item.getLearnerId().equals(learnerId))
                .findFirst();
    }

    public long getConfirmedCount() {
        return items.stream()
                .filter(item -> item.getStatus().getName() == AttendanceStatuses.RESERVED || item.getStatus().getName() == AttendanceStatuses.CHECKED_IN)
                .count();
    }

    public long getCheckedInCount() {
        return items.stream()
                .filter(item -> item.getStatus().getName() == AttendanceStatuses.CHECKED_IN)
                .count();
    }

     // Llamado al completar el encounter
    public void markNoShows() {
        items.stream()
                .filter(item -> item.getStatus().getName() == AttendanceStatuses.RESERVED)
                .forEach(Attendance::markAsNoShow);
                // La penalización se manejaría en el servicio que llama a esto
    }

     // Llamado al cancelar el encounter
     public void cancelAllReserved(LocalDateTime cancellationTime, LocalDateTime scheduledAt) {
         items.stream()
             .filter(item -> item.getStatus().getName() == AttendanceStatuses.RESERVED)
             .forEach(item -> item.cancel(cancellationTime, scheduledAt));
             // La penalización se manejaría en el servicio que llama a esto
     }

     public List<Attendance> getReadOnlyItems() {
        return Collections.unmodifiableList(items);
    }

}