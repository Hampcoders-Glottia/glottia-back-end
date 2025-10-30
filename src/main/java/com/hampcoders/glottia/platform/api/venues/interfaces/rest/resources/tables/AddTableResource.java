package com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.tables;

/**
 * Add table resource.
 */
public record AddTableResource(String tableNumber, Integer capacity, 
                String tableType, String tableStatus) {
    public AddTableResource {
        if (tableNumber == null) throw new IllegalArgumentException("Table number required");
        if (tableType == null) throw new IllegalArgumentException("Table type required");
        if (capacity == null || capacity <= 0) throw new IllegalArgumentException("Capacity must be positive");
    }
}