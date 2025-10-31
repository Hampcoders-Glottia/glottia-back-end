package com.hampcoders.glottia.platform.api.profiles.domain.model.valueobjects;

/**
 * Enumeration of subscription statuses.
 * @summary
 * Each status is associated with a unique integer value.
 * It can be used to standardize subscription status representation across the system.
 * @see IllegalArgumentException
 */
public enum SubscriptionStatuses {
    PENDING(1),    // Pending - awaiting approval
    ACTIVE(2),     // Active - subscription is active
    SUSPENDED(3),  // Suspended - access temporarily disabled
    CANCELLED(4),  // Cancelled - subscription cancelled by user
    EXPIRED(5);    // Expired - requires renewal

    private final int value;

    SubscriptionStatuses(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static SubscriptionStatuses fromValue(int value) {
        for (SubscriptionStatuses status : values()) {
            if (status.value == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid SubscriptionStatuses value: " + value);
    }
}