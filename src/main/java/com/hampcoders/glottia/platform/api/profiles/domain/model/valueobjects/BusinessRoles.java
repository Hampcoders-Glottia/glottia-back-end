package com.hampcoders.glottia.platform.api.profiles.domain.model.valueobjects;

/**
 * Enumeration of business roles.
 * @summary
 * Each role is associated with a unique integer value.
 * It can be used to standardize businessrole representation across the system.
 * @see IllegalArgumentException
 */
public enum BusinessRoles {
    Learner(1),
    Partner(2);

    private final int value;

    BusinessRoles(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }   

    public static BusinessRoles fromValue(int value) {
        for (BusinessRoles role : values()) {
            if (role.value == value) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid BusinessRoles value: " + value);
    }
}
