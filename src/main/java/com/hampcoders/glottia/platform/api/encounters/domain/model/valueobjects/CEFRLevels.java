package com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects;

/**
 * Enumeration of CEFR language proficiency levels.
 * @summary
 * This enum defines the Common European Framework of Reference for Languages (CEFR) levels.
 * Each level is associated with a unique integer value.
 * It can be used to standardize language proficiency representation across the system.
 * @see IllegalArgumentException
 */
public enum CEFRLevels {
    /**
     * A1 level of language proficiency.
     */
    A1(1),
    /**
     * A2 level of language proficiency.
     */
    A2(2),
    /**
     * B1 level of language proficiency.
     */
    B1(3),
    /**
     * B2 level of language proficiency.
     */
    B2(4),
    /**
     * C1 level of language proficiency.
     */
    C1(5),
    /**
     * C2 level of language proficiency.
     */
    C2(6),
    /**
     * Native level of language proficiency.
     */
    NATIVE(7);

    /**
     * Integer value associated with the CEFR level.
     */
    private final int value;

    /**
     * Constructor for CEFRLevels enum.
     * @param value
     */
    CEFRLevels(int value) {
        this.value = value;
    }

    /**
     * Get the integer value of the CEFR level.
     * @return int
     */
    public int getValue() {
        return value;
    }

    /**
     * Create a CEFRLevels from an integer value.
     * @param value
     * @return CEFRLevels
     */
    public static CEFRLevels fromValue(int value) {
        for (CEFRLevels level : values()) {
            if (level.value == value) {
                return level;
            }
        }
        throw new IllegalArgumentException("Invalid CEFRLevels value: " + value);
    }
}