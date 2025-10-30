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

@Embeddable
@Getter
public class AttendanceList {

    @OneToMany(mappedBy = "encounter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attendance> items;

    public AttendanceList() {
        this.items = new ArrayList<>();
    }

    public List<Attendance> getItems() {
        return Collections.unmodifiableList(items); // Devuelve copia inmutable
    }

    public Attendance addItem(Encounter encounter, LearnerId learnerId) {
        Attendance attendance = new Attendance(encounter, learnerId);
        this.items.add(attendance);
        return attendance;
    }

    public boolean hasLearner(LearnerId learnerId) {
        return items.stream()
                .anyMatch(item -> item.getLearnerId().equals(learnerId) &&
                                 (item.getStatus() == AttendanceStatus.RESERVED || item.getStatus() == AttendanceStatus.CHECKED_IN)); // Solo contar activos
    }

    public Optional<Attendance> findByLearnerId(LearnerId learnerId) {
        return items.stream()
                .filter(item -> item.getLearnerId().equals(learnerId))
                .findFirst();
    }

    public long getConfirmedCount() {
        return items.stream()
                .filter(item -> item.getStatus() == AttendanceStatus.RESERVED || item.getStatus() == AttendanceStatus.CHECKED_IN)
                .count();
    }

    public long getCheckedInCount() {
        return items.stream()
                .filter(item -> item.getStatus() == AttendanceStatus.CHECKED_IN)
                .count();
    }

     // Llamado al completar el encounter
    public void markNoShows() {
        items.stream()
                .filter(item -> item.getStatus() == AttendanceStatus.RESERVED)
                .forEach(Attendance::markAsNoShow);
                // La penalización se manejaría en el servicio que llama a esto
    }

     // Llamado al cancelar el encounter
     public void cancelAllReserved(LocalDateTime cancellationTime, LocalDateTime scheduledAt) {
         items.stream()
             .filter(item -> item.getStatus() == AttendanceStatus.RESERVED)
             .forEach(item -> item.cancel(cancellationTime, scheduledAt));
             // La penalización se manejaría en el servicio que llama a esto
     }

}