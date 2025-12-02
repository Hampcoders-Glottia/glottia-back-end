package com.hampcoders.glottia.platform.api.venues.application.internal.commandservices;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Arrays;

import org.springframework.stereotype.Service;

import com.hampcoders.glottia.platform.api.venues.domain.model.aggregates.Venue;
import com.hampcoders.glottia.platform.api.venues.domain.model.commands.venues.ActivateVenueCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.commands.venues.CreateVenueCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.commands.venues.DeactivateVenueCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.commands.venues.UpdateVenueDetailsCommand;
import com.hampcoders.glottia.platform.api.venues.domain.model.entities.Table;
import com.hampcoders.glottia.platform.api.venues.domain.model.valueobjects.TableStatuses;
import com.hampcoders.glottia.platform.api.venues.domain.model.valueobjects.TableTypes;
import com.hampcoders.glottia.platform.api.venues.domain.services.VenueCommandService;
import com.hampcoders.glottia.platform.api.venues.infrastructure.persistence.jpa.repositories.TableRegistryRepository;
import com.hampcoders.glottia.platform.api.venues.infrastructure.persistence.jpa.repositories.TableStatusRepository;
import com.hampcoders.glottia.platform.api.venues.infrastructure.persistence.jpa.repositories.TableTypeRepository;
import com.hampcoders.glottia.platform.api.venues.infrastructure.persistence.jpa.repositories.VenueRepository;
import com.hampcoders.glottia.platform.api.venues.infrastructure.persistence.jpa.repositories.VenueTypeRepository;

@Service
public class VenueCommandServiceImpl implements VenueCommandService {

    private final VenueRepository venueRepository;
    private final VenueTypeRepository venueTypeRepository;
    // Repositorios adicionales necesarios para crear la mesa por defecto
    private final TableRegistryRepository tableRegistryRepository;
    private final TableTypeRepository tableTypeRepository;
    private final TableStatusRepository tableStatusRepository;

    public VenueCommandServiceImpl(
        VenueRepository venueRepository,
        VenueTypeRepository venueTypeRepository,
        TableRegistryRepository tableRegistryRepository,
        TableTypeRepository tableTypeRepository,
        TableStatusRepository tableStatusRepository) {
        this.venueRepository = venueRepository;
        this.venueTypeRepository = venueTypeRepository;
        this.tableRegistryRepository = tableRegistryRepository;
        this.tableTypeRepository = tableTypeRepository;
        this.tableStatusRepository = tableStatusRepository;
    }

    @Override
    public Long handle(CreateVenueCommand command) {
        if (venueRepository.existsByName(command.name()) && venueRepository.existsByAddress(command.address())) {
            throw new IllegalArgumentException("Venue with the same name and address already exists.");
        }

        var venueType = venueTypeRepository.findById(command.venueTypeId())
            .orElseThrow(() -> new IllegalArgumentException("VenueType not found"));

        // 1. Crear y guardar el Venue
        var venue = new Venue(command, venueType);
        venue = venueRepository.save(venue);

        // 2. Inicializar el registro de mesas
        venue.initializeTableRegistry();
        venue = venueRepository.save(venue); // Guardar para que el registry tenga ID

        // 3. --- LÓGICA AÑADIDA: Crear Mesa por Defecto ---
        createDefaultTableForVenue(venue);

        return venue.getId();
    }

    private void createDefaultTableForVenue(Venue venue) {
        var registry = venue.getTableRegistry();

        // Obtener tipos y estados por defecto
        var defaultType = tableTypeRepository.findByName(TableTypes.ENCOUNTER_TABLE)
            .orElseThrow(() -> new IllegalStateException("Default TableType not seeded"));
        var defaultStatus = tableStatusRepository.findByName(TableStatuses.AVAILABLE)
            .orElseThrow(() -> new IllegalStateException("Default TableStatus not seeded"));

        // Agregar mesa #1 con capacidad para 6 personas
        registry.addTable("Mesa 1", 6, defaultType, defaultStatus);

        // Guardamos para persistir la mesa antes de agregarle horarios
        tableRegistryRepository.save(registry);

        // Obtener la mesa recién creada (está en memoria en el registry)
        Table table = registry.getTableList().findByTableNumber("Mesa 1");

        // Agregar disponibilidad para todos los días de la semana (08:00 - 23:00)
        LocalTime openTime = LocalTime.of(8, 0);
        LocalTime closeTime = LocalTime.of(23, 0);

        Arrays.stream(DayOfWeek.values()).forEach(day -> {
            LocalTime slotStart = openTime;
            // Esto evita que 22:00 + 2h = 00:00 entre al bucle, ya que 00:00 NO es desépus de 22:00
            while(true) {
                LocalTime slotEnd = slotStart.plusHours(2);

                // Si cruzamos la medianoche (end < start) o nos pasamos de la hora de cierre
                if (slotEnd.isBefore(slotStart) || slotEnd.isAfter(closeTime)) {
                    break;
                }

                table.createAvailabilitySlot(null, day, slotStart, slotEnd);
                slotStart = slotEnd;

                // Seguridad extra para no buclear infinitamente si llegamos justo al cierre
                if (slotStart.equals(closeTime)) break;
            }
        });

        // Guardar finalmente con los horarios
        tableRegistryRepository.save(registry);
    }

    @Override
    public Long handle(UpdateVenueDetailsCommand command) {
        if (venueRepository.existsByAddressAndIdIsNot(command.address(), command.venueId())) {
            throw new IllegalArgumentException("Another venue with the same address already exists.");
        }

        var venue = venueRepository.findById(command.venueId())
            .orElseThrow(() -> new IllegalArgumentException("Venue with ID " + command.venueId() + " does not exist."));

        venue.updateDetails(command.address(), command.name(), command.venueTypeId());
        venueRepository.save(venue);
        return venue.getId();
    }

    @Override
    public void handle(DeactivateVenueCommand command) {
        var venue = venueRepository.findById(command.venueId())
            .orElseThrow(() -> new IllegalArgumentException("Venue with ID " + command.venueId() + " does not exist."));

        venue.deactivate();
        venueRepository.save(venue);
    }

    @Override
    public void handle(ActivateVenueCommand command) {
        var venue = venueRepository.findById(command.venueId())
            .orElseThrow(() -> new IllegalArgumentException("Venue with ID " + command.venueId() + " does not exist."));

        venue.activate();
        venueRepository.save(venue);
    }
}