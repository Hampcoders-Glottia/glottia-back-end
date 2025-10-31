package com.hampcoders.glottia.platform.api.profiles.domain.model.valueobjects;

/**
 * Enumeration of languages.
 * @summary
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

    private final int value;

    Languages(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Languages fromValue(int value) {
        for (Languages language : values()) {
            if (language.value == value) {
                return language;
            }
        }
        throw new IllegalArgumentException("Invalid Languages value: " + value);
    }

}
