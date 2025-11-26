package com.hampcoders.glottia.platform.api.venues.domain.model.entities;

import java.time.LocalDate;
import java.time.LocalTime;
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

    /**
     * Reserve a table for a specific date and time slot
     * 
     * @param date      The reservation date
     * @param startHour Start time of the reservation (must be 2-hour slot)
     * @param endHour   End time of the reservation
     */
    public void reserve(LocalDate date, LocalTime startHour, LocalTime endHour) {
        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Cannot reserve past dates");
        }

        AvailabilityCalendar calendar = findOrCreateCalendar(date, startHour, endHour);
        calendar.reserve();

        // Update status only if reserving for today
        if (date.equals(LocalDate.now())) {
            updateStatus(TableStatus.toTableStatusFromName("RESERVED"));
        }
    }

    /**
     * Release a table reservation for a specific time slot
     */
    public void release(LocalDate date, LocalTime startHour, LocalTime endHour, TableStatus availableStatus) {
        AvailabilityCalendar calendar = findCalendar(date, startHour, endHour);
        if (calendar != null) {
            calendar.release();
        }

        if (date.equals(LocalDate.now())) {
            updateStatus(availableStatus);
        }
    }

    public void updateStatusBasedOnTodaysCalendar() {
        LocalDate today = LocalDate.now();
        List<AvailabilityCalendar> todaysCalendars = findAllCalendarsForDate(today);

        // If any slot is reserved, table is reserved
        boolean anyReserved = todaysCalendars.stream().anyMatch(AvailabilityCalendar::isReserved);
        if (anyReserved) {
            updateStatus(TableStatus.toTableStatusFromName("RESERVED"));
            return;
        }

        // If all slots are available or no slots exist, table is available
        boolean allAvailable = todaysCalendars.isEmpty() ||
                todaysCalendars.stream().allMatch(cal -> cal.getIsAvailable());
        if (allAvailable) {
            updateStatus(TableStatus.toTableStatusFromName("AVAILABLE"));
        } else {
            updateStatus(TableStatus.toTableStatusFromName("UNAVAILABLE"));
        }
    }

    public void markUnavailable(TableStatus unavailableStatus) {
        updateStatus(unavailableStatus);

        LocalDate today = LocalDate.now();
        List<AvailabilityCalendar> todaysCalendars = findAllCalendarsForDate(today);
        todaysCalendars.stream()
                .filter(cal -> !cal.isReserved())
                .forEach(AvailabilityCalendar::markUnavailable);
    }

    /**
     * Mark a specific time slot as unavailable
     */
    public void markDateUnavailable(LocalDate date, LocalTime startHour, LocalTime endHour) {
        AvailabilityCalendar calendar = findOrCreateCalendar(date, startHour, endHour);
        calendar.markUnavailable();

        // Update general status if it's today
        if (date.equals(LocalDate.now())) {
            updateStatus(TableStatus.toTableStatusFromName("UNAVAILABLE"));
        }
    }

    public void markAvailable(TableStatus availableStatus) {
        LocalDate today = LocalDate.now();
        List<AvailabilityCalendar> todaysCalendars = findAllCalendarsForDate(today);

        boolean anyReserved = todaysCalendars.stream().anyMatch(AvailabilityCalendar::isReserved);
        if (anyReserved) {
            throw new IllegalStateException("Cannot mark as available - table has reserved slots for today");
        }

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

    private AvailabilityCalendar findOrCreateCalendar(LocalDate date, LocalTime startHour, LocalTime endHour) {
        return availabilityCalendars.stream()
                .filter(cal -> cal.getAvailabilityDate() != null &&
                        cal.getAvailabilityDate().equals(date) &&
                        cal.getStartHour().equals(startHour) &&
                        cal.getEndHour().equals(endHour))
                .findFirst()
                .orElseGet(() -> {
                    AvailabilityCalendar newCalendar = new AvailabilityCalendar(this, date, startHour, endHour);
                    availabilityCalendars.add(newCalendar);
                    return newCalendar;
                });
    }

    private AvailabilityCalendar findCalendar(LocalDate date, LocalTime startHour, LocalTime endHour) {
        return availabilityCalendars.stream()
                .filter(cal -> cal.getAvailabilityDate() != null &&
                        cal.getAvailabilityDate().equals(date) &&
                        cal.getStartHour().equals(startHour) &&
                        cal.getEndHour().equals(endHour))
                .findFirst()
                .orElse(null);
    }

    private List<AvailabilityCalendar> findAllCalendarsForDate(LocalDate date) {
        return availabilityCalendars.stream()
                .filter(cal -> cal.getAvailabilityDate() != null &&
                        cal.getAvailabilityDate().equals(date))
                .toList();
    }

    public String getTableTypeName() {
        return tableType.getStringTypeName();
    }

    public String getTableStatusName() {
        return tableStatus.getStringStatusName();
    }
}