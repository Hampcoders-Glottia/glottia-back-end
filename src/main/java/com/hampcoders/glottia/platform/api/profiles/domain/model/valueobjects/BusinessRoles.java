package com.hampcoders.glottia.platform.api.profiles.domain.model.valueobjects;

/**
 * Enumeration of business roles.
 * @summary
 * Each role is associated with a unique integer value.
 * It can be used to standardize businessrole representation across the system.
 * @see IllegalArgumentException
 */
public enum BusinessRoles {
    LEARNER(1),
    PARTNER(2);


    /**
     * The integer value associated with the BusinessRole
     */
    private final int value;

    /**
     * Constructor for BusinessRoles enum
     * @param value
     */
    BusinessRoles(int value) {
        this.value = value;
    }

    /**
     * Gets the integer value of the BusinessRole
     * @return the integer value of the BusinessRole
     */
    public int getValue() {
        return value;
    }

    /**
     * Gets the BusinessRoles enum from an integer value
     * @param value
     * @return
     */
    public static BusinessRoles fromValue(int value) {
        for (BusinessRoles role : values()) {
            if (role.value == value) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid BusinessRoles value: " + value);
    }
}
