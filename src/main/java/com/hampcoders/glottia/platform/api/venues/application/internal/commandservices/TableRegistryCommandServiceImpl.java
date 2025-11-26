package com.hampcoders.glottia.platform.api.venues.application.internal.commandservices;

import org.springframework.stereotype.Service;

import com.hampcoders.glottia.platform.api.venues.domain.model.commands.tables.AddTableCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.commands.tables.MarkTableAvailableCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.commands.tables.MarkTableDateUnavailableCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.commands.tables.MarkTableUnavailableCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.commands.tables.ReleaseTableCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.commands.tables.RemoveTableCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.commands.tables.ReserveTableCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.commands.tables.UpdateTableCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.valueobjects.TableStatuses;
import com.hampcoders.glottia.platform.api.venues.domain.model.valueobjects.TableTypes;
import com.hampcoders.glottia.platform.api.venues.domain.services.TableRegistryCommandService;
import com.hampcoders.glottia.platform.api.venues.infrastructure.persistence.jpa.repositories.TableRegistryRepository;
import com.hampcoders.glottia.platform.api.venues.infrastructure.persistence.jpa.repositories.TableRepository;
import com.hampcoders.glottia.platform.api.venues.infrastructure.persistence.jpa.repositories.TableStatusRepository;
import com.hampcoders.glottia.platform.api.venues.infrastructure.persistence.jpa.repositories.TableTypeRepository;
import com.hampcoders.glottia.platform.api.venues.domain.model.commands.tables.CreateAvailabilitySlotCommand;

/**
 * Command Service implementation for TableRegistry aggregate
 * Handles all table-related commands following DDD patterns
 */
@Service
public class TableRegistryCommandServiceImpl implements TableRegistryCommandService {

	private final TableRegistryRepository tableRegistryRepository;
	private final TableRepository tableRepository;
	private final TableTypeRepository tableTypeRepository;
	private final TableStatusRepository tableStatusRepository;

	public TableRegistryCommandServiceImpl(TableRegistryRepository tableRegistryRepository,
			TableRepository tableRepository, TableTypeRepository tableTypeRepository,
			TableStatusRepository tableStatusRepository) {
		this.tableRegistryRepository = tableRegistryRepository;
		this.tableRepository = tableRepository;
		this.tableTypeRepository = tableTypeRepository;
		this.tableStatusRepository = tableStatusRepository;
	}

	/**
	 * Handle AddTableCommand
	 * Creates a new table in the specified registry
	 */
	@Override
	public void handle(AddTableCommand command) {
		var tableRegistry = tableRegistryRepository.findById(command.tableId())
				.orElseThrow(() -> new IllegalArgumentException(
						"Table registry not found with ID: " + command.tableId()));

		var tableType = this.tableTypeRepository.findByName(TableTypes.valueOf(command.tableType()))
				.orElseThrow(() -> new IllegalArgumentException(
						"Table type not found with name: " + command.tableType()));

		var tableStatus = this.tableStatusRepository.findByName(TableStatuses.valueOf(command.tableStatus()))
				.orElseThrow(() -> new IllegalArgumentException(
						"Table status not found with name: " + command.tableStatus()));

		tableRegistry.addTable(
				command.tableNumber(),
				command.capacity(),
				tableType,
				tableStatus);

		tableRegistryRepository.save(tableRegistry);
	}

	/**
	 * Handle CreateAvailabilitySlotCommand
	 * Creates an availability slot for a table (Partner creates available time
	 * slots for booking)
	 */
	@Override
	public void handle(CreateAvailabilitySlotCommand command) {
		var table = tableRepository.findById(command.tableId())
				.orElseThrow(() -> new IllegalArgumentException("Table not found with ID: " + command.tableId()));

		var tableRegistry = table.getTableRegistry();

		tableRegistry.createAvailabilitySlot(command.tableId(), command.availabilityDate(), command.dayOfWeek(),
				command.startHour(), command.endHour());

		tableRegistryRepository.save(tableRegistry);

		// TODO: Emit TableAvailabilitySlotCreatedEvent for analytics
	}

	/**
	 * Handle ReserveTableCommand
	 * Reserves a table for a specific date and time slot (typically from Encounters
	 * BC)
	 */
	@Override
	public void handle(ReserveTableCommand command) {
		var table = tableRepository.findById(command.tableId())
				.orElseThrow(() -> new IllegalArgumentException("Table not found with ID: " + command.tableId()));

		var tableRegistry = table.getTableRegistry();

		tableRegistry.reserveTable(command.tableId(), command.reservationDate(), command.startHour(),
				command.endHour());
		tableRegistryRepository.save(tableRegistry);

		// TODO: Emit TableReservedEvent(tableId, venueId, date, startHour, endHour,
		// encounterId)
		// Example: domainEventPublisher.publish(new TableReservedEvent(...));
	}

	/**
	 * Handle ReleaseTableCommand
	 * Releases a table reservation for a specific time slot
	 */
	@Override
	public void handle(ReleaseTableCommand command) {
		var table = tableRepository.findById(command.tableId())
				.orElseThrow(() -> new IllegalArgumentException(
						"Table not found with ID: " + command.tableId()));
		var tableStatus = tableStatusRepository.findByName(TableStatuses.AVAILABLE)
				.orElseThrow(() -> new IllegalArgumentException("Table status 'AVAILABLE' not found"));

		table.release(
				command.reservationDate(),
				command.startHour(),
				command.endHour(),
				tableStatus);

		tableRegistryRepository.save(table.getTableRegistry());

		// TODO: Emit TableReleasedEvent(tableId, venueId, date, startHour, endHour)
	}

	/**
	 * Handle RemoveTableCommand
	 * Removes a table if it has no active reservations
	 */
	@Override
	public void handle(RemoveTableCommand command) {
		var table = tableRepository.findById(command.tableId())
				.orElseThrow(() -> new IllegalArgumentException("Table not found with ID: " + command.tableId()));
		var tableRegistry = table.getTableRegistry();
		tableRegistry.removeTable(command.tableId());
		tableRegistryRepository.save(tableRegistry);
	}

	/**
	 * Handle UpdateTableCommand
	 * Updates table capacity and/or type
	 */
	@Override
	public void handle(UpdateTableCommand command) {
		var table = tableRepository.findById(command.tableId())
				.orElseThrow(() -> new IllegalArgumentException("Table not found with ID: " + command.tableId()));
		command.capacity().ifPresent(table::updateCapacity);
		command.tableTypeId().ifPresent(typeId -> {
			var tableType = tableTypeRepository.findById(typeId)
					.orElseThrow(() -> new IllegalArgumentException("Table type not found with ID: " + typeId));
			table.updateTableType(tableType);
		});
		tableRepository.save(table);
	}

	/**
	 * Handle MarkTableAvailableCommand
	 * Marks a table as generally available
	 */
	@Override
	public void handle(MarkTableAvailableCommand command) {
		var table = tableRepository.findById(command.tableId())
				.orElseThrow(() -> new IllegalArgumentException("Table not found with ID: " + command.tableId()));
		var availableStatus = tableStatusRepository.findByName(TableStatuses.AVAILABLE)
				.orElseThrow(() -> new IllegalArgumentException("Table status 'AVAILABLE' not found"));
		table.markAvailable(availableStatus);
		tableRepository.save(table);
	}

	/**
	 * Handle MarkTableUnavailableCommand
	 * Marks a table as generally unavailable
	 */
	@Override
	public void handle(MarkTableUnavailableCommand command) {
		var table = tableRepository.findById(command.tableId())
				.orElseThrow(() -> new IllegalArgumentException("Table not found with ID: " + command.tableId()));
		var unavailableStatus = tableStatusRepository.findByName(TableStatuses.UNAVAILABLE)
				.orElseThrow(() -> new IllegalArgumentException("Table status 'UNAVAILABLE' not found"));
		table.markUnavailable(unavailableStatus);
		tableRepository.save(table);
	}

	/**
	 * Handle MarkTableDateUnavailableCommand
	 * Marks a specific date and time slot as unavailable for a table
	 */
	@Override
	public void handle(MarkTableDateUnavailableCommand command) {
		var table = tableRepository.findById(command.tableId())
				.orElseThrow(() -> new IllegalArgumentException("Table not found with ID: " + command.tableId()));
		table.markDateUnavailable(command.date(), command.startHour(), command.endHour());
		tableRepository.save(table);
	}
}