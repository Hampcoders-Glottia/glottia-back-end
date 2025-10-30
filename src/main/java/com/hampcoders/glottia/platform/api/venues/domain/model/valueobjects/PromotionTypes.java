package com.hampcoders.glottia.platform.api.venues.domain.model.valueobjects;

/**
 * PromotionType Value Object
 * Represents the different types of promotions available in the system
 */
public enum PromotionTypes {
    DISCOUNT_PERCENT(1),
    FIXED_AMOUNT_DISCOUNT(2),
    FREE_ITEM(3),
    TWO_FOR_ONE(4),
    COMPLIMENTARY_DRINK(5);
    
    private final int value;
    
    PromotionTypes(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static PromotionTypes fromValue(int value) {
        for (PromotionTypes type : values()) {
            if (type.value == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid PromotionTypes value: " + value);
    }
}
