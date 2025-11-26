package com.hampcoders.glottia.platform.api.venues.interfaces.rest;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hampcoders.glottia.platform.api.shared.interfaces.rest.resources.MessageResource;
import com.hampcoders.glottia.platform.api.venues.domain.model.commands.tables.MarkTableAvailableCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.commands.tables.MarkTableDateUnavailableCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.commands.tables.MarkTableUnavailableCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.commands.tables.RemoveTableCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.tables.GetTableAvailabilityFromDateToDateQuery;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.tables.GetTableByIdQuery;
import com.hampcoders.glottia.platform.api.venues.domain.services.TableRegistryCommandService;
import com.hampcoders.glottia.platform.api.venues.domain.services.TableRegistryQueryService;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.tables.AvailabilityCalendarResource;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.tables.CreateAvailabilitySlotResource;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.tables.MarkDateUnavailableResource;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.tables.ReleaseTableResource;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.tables.ReserveTableResource;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.tables.TableResource;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.tables.UpdateTableResource;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.transform.fromentity.AvailabilityCalendarResourceFromEntityAssembler;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.transform.fromentity.TableResourceFromEntityAssembler;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.transform.fromresource.CreateAvailabilitySlotCommandFromResourceAssembler;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.transform.fromresource.ReleaseTableCommandFromResourceAssembler;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.transform.fromresource.ReserveTableCommandFromResourceAssembler;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.transform.fromresource.UpdateTableCommandFromResourceAssembler;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * TablesController
 * <p>
 * RESTful controller for individual table operations.
 * Used when you need to access a table directly without venue context.
 * </p>
 */
@RestController
@RequestMapping(value = "/api/v1/tables", produces = "application/json")
@Tag(name = "Tables", description = "Available Table Endpoints")
public class TablesController {

    private final TableRegistryCommandService tableCommandService;
    private final TableRegistryQueryService tableQueryService;

    public TablesController(TableRegistryCommandService tableCommandService,
            TableRegistryQueryService tableQueryService) {
        this.tableCommandService = tableCommandService;
        this.tableQueryService = tableQueryService;
    }

    /**
     * Get table by ID
     * 
     * @param tableId The table identifier
     * @return The table resource
     */
    @GetMapping("/{tableId}")
    @Operation(summary = "Get table by ID", description = "Retrieves a specific table by its identifier")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Table found"),
            @ApiResponse(responseCode = "404", description = "Table not found") })
    public ResponseEntity<TableResource> getTableById(@PathVariable Long tableId) {
        var table = tableQueryService.handle(new GetTableByIdQuery(tableId));

        if (table.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var resource = TableResourceFromEntityAssembler.toResourceFromEntity(table.get());
        return ResponseEntity.ok(resource);
    }

    /**
     * Get table availability for a date range
     * 
     * @param tableId  The table identifier
     * @param fromDate Start date
     * @param toDate   End date
     * @return Availability calendar entries
     */
    @GetMapping("/{tableId}/availability")
    @Operation(summary = "Get table availability", description = "Retrieves availability for a table in a date range")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Availability retrieved"),
            @ApiResponse(responseCode = "404", description = "Table not found") })
    public ResponseEntity<List<AvailabilityCalendarResource>> getTableAvailability(@PathVariable Long tableId,
            @Parameter(description = "Start date (format: yyyy-MM-dd)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @Parameter(description = "End date (format: yyyy-MM-dd)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        var query = new GetTableAvailabilityFromDateToDateQuery(tableId, fromDate, toDate);
        var availability = tableQueryService.handle(query);
        var resources = availability.stream().map(AvailabilityCalendarResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    /**
     * Update table information
     * 
     * @param tableId  The table identifier
     * @param resource The updated table data
     * @return Success message
     */
    @PutMapping("/{tableId}")
    @Operation(summary = "Update table", description = "Updates table capacity and/or type")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Table updated successfully"),
            @ApiResponse(responseCode = "404", description = "Table not found") })
    public ResponseEntity<TableResource> updateTable(@PathVariable Long tableId,
            @RequestBody UpdateTableResource resource) {
        var updateCommand = UpdateTableCommandFromResourceAssembler.toCommandFromResource(tableId, resource);
        tableCommandService.handle(updateCommand);
        var updatedTable = tableQueryService.handle(new GetTableByIdQuery(tableId));
        return ResponseEntity.ok(TableResourceFromEntityAssembler.toResourceFromEntity(updatedTable.get()));
    }

    /**
     * Create availability slot
     * Partners create available time slots for booking (specific dates or recurring
     * patterns)
     * 
     * @param tableId  The table identifier
     * @param resource Slot data (date/dayOfWeek, startHour, endHour)
     * @return Success message
     */
    @PostMapping("/{tableId}/availability-slots")
    @Operation(summary = "Create availability slot", description = "Partners create available time slots for bookings (specific dates or recurring patterns)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Slot created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid slot data or overlap detected")
    })
    public ResponseEntity<MessageResource> createAvailabilitySlot(
            @PathVariable Long tableId,
            @RequestBody CreateAvailabilitySlotResource resource) {

        var command = CreateAvailabilitySlotCommandFromResourceAssembler
                .toCommandFromResource(tableId, resource);

        tableCommandService.handle(command);

        String slotType = resource.availabilityDate() != null ? "on " + resource.availabilityDate()
                : "every " + resource.dayOfWeek();

        return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED)
                .body(new MessageResource("Availability slot created " + slotType));
    }

    /**
     * Delete (remove) a table
     * 
     * @param tableId The table identifier
     * @return Success message
     */
    @DeleteMapping("/{tableId}")
    @Operation(summary = "Delete table", description = "Removes a table from the system (cannot have active reservations)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Table deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Cannot delete reserved table")
    })
    public ResponseEntity<MessageResource> deleteTable(@PathVariable Long tableId) {
        var removeCommand = new RemoveTableCommand(tableId);
        tableCommandService.handle(removeCommand);
        return ResponseEntity.ok(new MessageResource("Table with ID " + tableId + " successfully deleted"));
    }

    /**
     * Reserve a table (system use - typically called by Encounters BC)
     * 
     * @param tableId  The table iden identifier
     * @param resource Reservation data (date, startHour, endHour)
     * @return Success message
     */
    @PatchMapping("/{tableId}/reservations")
    @Operation(summary = "Reserve table", description = "Reserves a table for a specific date and time slot (internal system use)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Table reserved successfully"),
            @ApiResponse(responseCode = "400", description = "Table not available")
    })
    public ResponseEntity<MessageResource> reserveTable(
            @PathVariable Long tableId,
            @RequestBody ReserveTableResource resource) {

        var reserveCommand = ReserveTableCommandFromResourceAssembler
                .toCommandFromResource(tableId, resource);

        tableCommandService.handle(reserveCommand);

        return ResponseEntity.ok(new MessageResource("Table reserved successfully"));
    }

    /**
     * Release a table reservation
     * 
     * @param tableId  The table identifier
     * @param resource Release data (date, startHour, endHour)
     * @return Success message
     */
    @PatchMapping("/{tableId}/releases")
    @Operation(summary = "Release table", description = "Releases a table reservation for a specific time slot")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Table released successfully")
    })
    public ResponseEntity<MessageResource> releaseTable(
            @PathVariable Long tableId,
            @RequestBody ReleaseTableResource resource) {

        var releaseCommand = ReleaseTableCommandFromResourceAssembler.toCommandFromResource(tableId, resource);

        tableCommandService.handle(releaseCommand);

        return ResponseEntity.ok(new MessageResource("Table released successfully"));
    }

    /**
     * Mark table as available
     * 
     * @param tableId The table identifier
     * @return Success message
     */
    @PatchMapping("/{tableId}/mark-available")
    @Operation(summary = "Mark table as available", description = "Marks a table as generally available")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Table marked as available")
    })
    public ResponseEntity<MessageResource> markTableAvailable(@PathVariable Long tableId) {
        var command = new MarkTableAvailableCommand(tableId);
        tableCommandService.handle(command);

        return ResponseEntity.ok(new MessageResource("Table marked as available"));
    }

    /**
     * Mark table as unavailable
     * 
     * @param tableId The table identifier
     * @return Success message
     */
    @PatchMapping("/{tableId}/mark-unavailable")
    @Operation(summary = "Mark table as unavailable", description = "Marks a table as generally unavailable")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Table marked as unavailable")
    })
    public ResponseEntity<MessageResource> markTableUnavailable(@PathVariable Long tableId) {
        var command = new MarkTableUnavailableCommand(tableId);
        tableCommandService.handle(command);

        return ResponseEntity.ok(new MessageResource("Table marked as unavailable"));
    }

    /**
     * Mark specific date and time slot as unavailable for table
     * 
     * @param tableId  The table identifier
     * @param resource Date and time slot to mark unavailable
     * @return Success message
     */
    @PatchMapping("/{tableId}/mark-date-unavailable")
    @Operation(summary = "Mark date unavailable", description = "Marks a specific date and time slot as unavailable for the table")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Time slot marked as unavailable")
    })
    public ResponseEntity<MessageResource> markDateUnavailable(
            @PathVariable Long tableId,
            @RequestBody MarkDateUnavailableResource resource) {

        var command = new MarkTableDateUnavailableCommand(
                tableId,
                resource.date(),
                resource.startHour(),
                resource.endHour());
        tableCommandService.handle(command);

        return ResponseEntity.ok(new MessageResource("Time slot on " + resource.date() + " marked as unavailable"));
    }
}