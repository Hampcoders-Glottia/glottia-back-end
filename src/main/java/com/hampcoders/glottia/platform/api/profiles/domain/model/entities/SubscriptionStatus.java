package com.hampcoders.glottia.platform.api.profiles.domain.model.entities;

import com.hampcoders.glottia.platform.api.profiles.domain.model.valueobjects.SubscriptionStatuses;
import com.hampcoders.glottia.platform.api.shared.domain.model.entities.AuditableModel;
import jakarta.persistence.*;
import lombok.Getter;


/**
 * Entity representing a subscription status in the system.
 * @summary
 * Includes methods to convert between string names and enum values.
 * Extends AuditableModel to include auditing fields.
 */
@Getter
@Entity
@Table(name = "subscription_statuses")
public class SubscriptionStatus extends AuditableModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the subscription status.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "name", length = 20, nullable = false, unique = true)
    private SubscriptionStatuses name;

    /**
     * Default constructor for JPA.
     */
    protected SubscriptionStatus() {}

    /**
     * Constructor to create a SubscriptionStatus with a given name.    
     * @param name
     * @returns SubscriptionStatus instance
     */
    public SubscriptionStatus(SubscriptionStatuses name) {
        this.name = name;
    }

    /**
     * Gets the string representation of the subscription status name.  
     * @return String name of the subscription status
     */
    public String getStringStatusName() {
        return name.name();
    }

    /**
     * Converts a string name to a SubscriptionStatus entity.
     * @param name
     * @return SubscriptionStatus instance
     */
    public static SubscriptionStatus toSubscriptionStatusFromName(String name) {
        return new SubscriptionStatus(SubscriptionStatuses.valueOf(name.toUpperCase()));
    }

    /**
     * Gets a default SubscriptionStatus instance with PENDING status.
     * @return SubscriptionStatus instance with PENDING status
     */
    public static SubscriptionStatus geSubscriptionStatus() {
        return new SubscriptionStatus(SubscriptionStatuses.PENDING);
    }

}