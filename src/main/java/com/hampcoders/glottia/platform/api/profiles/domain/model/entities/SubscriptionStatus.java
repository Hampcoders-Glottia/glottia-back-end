package com.hampcoders.glottia.platform.api.profiles.domain.model.entities;

import com.hampcoders.glottia.platform.api.profiles.domain.model.valueobjects.SubscriptionStatuses;
import com.hampcoders.glottia.platform.api.shared.domain.model.entities.AuditableModel;
import jakarta.persistence.*;
import lombok.Getter;


@Getter
@Entity
@Table(name = "subscription_statuses")
public class SubscriptionStatus extends AuditableModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", length = 20, nullable = false, unique = true)
    private SubscriptionStatuses name;

    public SubscriptionStatus() {}

    public SubscriptionStatus(SubscriptionStatuses name) {
        this.name = name;
    }

    public String getStringStatusName() {
        return name.name();
    }

    public static SubscriptionStatus toSubscriptionStatusFromName(String name) {
        return new SubscriptionStatus(SubscriptionStatuses.valueOf(name.toUpperCase()));
    }

    public static SubscriptionStatus geSubscriptionStatus() {
        return new SubscriptionStatus(SubscriptionStatuses.PENDING);
    }


}