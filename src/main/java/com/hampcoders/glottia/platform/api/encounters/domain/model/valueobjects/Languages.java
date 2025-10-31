package com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects;

/**
 * Enumeration of supported languages.
 * @summary
 * This enum defines the set of supported languages in the Glottia platform.
 * Each language is associated with a unique integer value.
 * It can be used to standardize language representation across the system.
 * @see IllegalArgumentException
 */
public enum Languages {
    ENGLISH(1),
    SPANISH(2),
    FRENCH(3),
    GERMAN(4),
    ITALIAN(5),
    PORTUGUESE(6),
    JAPANESE(7),
    KOREAN(8),
    MANDARIN(9);

    /**
     * The integer value associated with the language.
     */
    private final int value;

    /**
     * Constructor for Languages enum.
     * @param value
     */
    Languages(int value) {
        this.value = value;
    }
    
    /**
     * Gets the integer value associated with the language.
     * @return int
     */
    public int getValue() {
        return value;
    }

    /**
     * Creates a Languages enum from an integer value.
     * @param value
     * @return Languages
     * @throws IllegalArgumentException if the value does not correspond to any language.
     */
    public static Languages fromValue(int value) {
        for (Languages language : values()) {
            if (language.value == value) {
                return language;
            }
        }
        throw new IllegalArgumentException("Invalid Languages value: " + value);
    }
}