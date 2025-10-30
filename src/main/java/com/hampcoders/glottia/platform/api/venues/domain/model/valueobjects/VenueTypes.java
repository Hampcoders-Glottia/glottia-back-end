package com.hampcoders.glottia.platform.api.venues.domain.model.valueobjects;


/**
 * VenueTypes Value Object 
 * Represents the different types of venues available in the system
 */
public enum VenueTypes {
    COWORKING(1),
    RESTAURANT(2),
    CAFE(3);

    private final int value;

    VenueTypes(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }

    public static VenueTypes fromValue(int value) {
        for (VenueTypes type : values()) {
            if (type.value == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid VenueTypes value: " + value);
    }
}