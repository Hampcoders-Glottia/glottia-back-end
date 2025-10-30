package com.hampcoders.glottia.platform.api.venues.domain.model.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.hampcoders.glottia.platform.api.venues.domain.model.aggregates.TableRegistry;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

/**
 * Table Entity
 * Represents a table within a venue
 * Contains its own availability calendars
 */
@Entity
@Getter
@jakarta.persistence.Table(name = "tables")
public class Table {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_registry_id")
    private TableRegistry tableRegistry;

    @Column(name = "table_number", nullable = false, length = 50)
    private String tableNumber;

    @Min(4)
    @Max(6)
    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "table_type_id", nullable = false)
    private TableType tableType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "table_status_id", nullable = false)
    private TableStatus tableStatus;

    @Getter
    @OneToMany(mappedBy = "table", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<AvailabilityCalendar> availabilityCalendars;

    // Constructor
    protected Table() {
        super();
        this.availabilityCalendars = new ArrayList<>();
    }

    /**
     * Constructor used by TableList.addTable()
     */
    public Table(TableRegistry tableRegistry, String tableNumber, Integer capacity, 
                TableType tableType, TableStatus tableStatus) {
        this();
        if (tableRegistry == null) {
            throw new IllegalArgumentException("Table registry cannot be null");
        }
        validateTableNumber(tableNumber);
        validateCapacity(capacity);
        validateTableType(tableType);
        validateTableStatus(tableStatus);

        this.tableRegistry = tableRegistry;
        this.tableNumber = tableNumber;
        this.capacity = capacity;
        this.tableType = tableType;
        this.tableStatus = tableStatus;
    }

    // Validation
    private void validateTableNumber(String tableNumber) {
        if (tableNumber == null || tableNumber.isBlank()) {
            throw new IllegalArgumentException("Table number cannot be null or empty");
        }
    }

    private void validateCapacity(Integer capacity) {
        if (capacity == null || capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
    }

    private void validateTableType(TableType tableType) {
        if (tableType == null) {
            throw new IllegalArgumentException("Table type cannot be null");
        }
    }

    private void validateTableStatus(TableStatus tableStatus) {
        if (tableStatus == null) {
            throw new IllegalArgumentException("Table status cannot be null");
        }
    }

    public void reserve(LocalDate date) {
        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Cannot reserve past dates");
        }

        AvailabilityCalendar calendar = findOrCreateCalendar(date);
        calendar.reserve();
        
        // Update status only if reserving for today
        if (date.equals(LocalDate.now())) {
            updateStatus(TableStatus.toTableStatusFromName("RESERVED"));
        }
    }

    public void release(LocalDate date, TableStatus availableStatus) {
        AvailabilityCalendar calendar = findCalendar(date);
        if (calendar != null) {
            calendar.release();
        }

        if (date.equals(LocalDate.now())) {
            updateStatus(availableStatus);
        }
    }

    public void updateStatusBasedOnTodaysCalendar() {
        LocalDate today = LocalDate.now();
        AvailabilityCalendar todaysCalendar = findCalendar(today);
        
        if (todaysCalendar == null || todaysCalendar.getIsAvailable()) {
            updateStatus(TableStatus.toTableStatusFromName("AVAILABLE"));
        } else if (todaysCalendar.isReserved()) {
            updateStatus(TableStatus.toTableStatusFromName("RESERVED"));
        } else {
            updateStatus(TableStatus.toTableStatusFromName("UNAVAILABLE"));
        }
    }

    public void markUnavailable(TableStatus unavailableStatus) {
        updateStatus(unavailableStatus);

        LocalDate today = LocalDate.now();
        AvailabilityCalendar todaysCalendar = findCalendar(today);
        if (todaysCalendar != null && !todaysCalendar.isReserved()) {
            todaysCalendar.markUnavailable();
        }
    }

    public void markDateUnavailable(LocalDate date) {
        AvailabilityCalendar calendar = findOrCreateCalendar(date);
        calendar.markUnavailable();
        
        // Update general status if it's today
        if (date.equals(LocalDate.now())) {
            updateStatus(TableStatus.toTableStatusFromName("UNAVAILABLE"));
        }
    }

    public void markAvailable(TableStatus availableStatus) {
        LocalDate today = LocalDate.now();
        AvailabilityCalendar todaysCalendar = findCalendar(today);

        if (todaysCalendar != null && todaysCalendar.isReserved())
            throw new IllegalStateException("Cannot mark as available - table is reserved for today");
        
        updateStatus(availableStatus); 
}

    public boolean isAvailable() {
        return tableStatus.getStringStatusName().equals("AVAILABLE");
    }

    public boolean isReserved() {
        return tableStatus.getStringStatusName().equals("RESERVED");
    }

    public boolean canBeDeleted() {
        return availabilityCalendars.stream().noneMatch(cal -> cal.isReserved());
    }

    public void updateCapacity(Integer newCapacity) {
        validateCapacity(newCapacity);
        this.capacity = newCapacity;
    }

    public void updateTableType(TableType newType) {
        validateTableType(newType);
        this.tableType = newType;
    }

    private void updateStatus(TableStatus newStatus) {
        validateTableStatus(newStatus);
        this.tableStatus = newStatus;
    }

    private AvailabilityCalendar findOrCreateCalendar(LocalDate date) {
        return availabilityCalendars.stream()
                .filter(cal -> cal.getAvailabilityDate().equals(date))
                .findFirst()
                .orElseGet(() -> {
                    AvailabilityCalendar newCalendar = new AvailabilityCalendar(this, date);
                    availabilityCalendars.add(newCalendar);
                    return newCalendar;
                });
    }

    private AvailabilityCalendar findCalendar(LocalDate date) {
        return availabilityCalendars.stream()
                .filter(cal -> cal.getAvailabilityDate().equals(date))
                .findFirst()
                .orElse(null);
    }

    public String getTableTypeName() {
        return tableType.getStringTypeName();
    }

    public String getTableStatusName() {
        return tableStatus.getStringStatusName();
    }
}