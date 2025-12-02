package com.hampcoders.glottia.platform.api.venues.application.internal.queryservices;

import com.hampcoders.glottia.platform.api.venues.application.internal.outboundservices.ExternalEncounterService;
import com.hampcoders.glottia.platform.api.venues.domain.model.aggregates.Venue;
import com.hampcoders.glottia.platform.api.venues.domain.model.entities.AvailabilityCalendar;
import com.hampcoders.glottia.platform.api.venues.domain.model.queries.venues.*;
import com.hampcoders.glottia.platform.api.venues.domain.services.VenueQueryService;
import com.hampcoders.glottia.platform.api.venues.infrastructure.persistence.jpa.repositories.AvailabilityCalendarRepository;
import com.hampcoders.glottia.platform.api.venues.infrastructure.persistence.jpa.repositories.VenueRepository;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.DailyEncounterStatResource;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.VenueEncounterStatisticsResource;
import com.hampcoders.glottia.platform.api.venues.interfaces.rest.resources.WeeklyEncounterStatResource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VenueQueryServiceImpl implements VenueQueryService {
    private final VenueRepository venueRepository;
    private final AvailabilityCalendarRepository availabilityCalendarRepository;
    private final ExternalEncounterService externalEncounterService;


    public VenueQueryServiceImpl(VenueRepository venueRepository, AvailabilityCalendarRepository availabilityCalendarRepository, @Qualifier("venuesExternalEncounterService") ExternalEncounterService externalEncounterService) {
        this.venueRepository = venueRepository;
        this.availabilityCalendarRepository = availabilityCalendarRepository;
        this.externalEncounterService = externalEncounterService;
    }

    @Override
    public Optional<Venue> handle(GetVenueByIdQuery query) {
        return venueRepository.findById(query.venueId());
    }

    @Override
    public Optional<Venue> handle(GetVenueByNameQuery query) {
        return venueRepository.findByName(query.name());
    }

    @Override
    public List<Venue> handle(GetVenuesByCityQuery query) {
        return venueRepository.findByAddressCity(query.city());
    }

    @Override
    public List<Venue> handle(GetVenuesByVenueTypeIdQuery query) {
        return venueRepository.findByVenueTypeId(query.venueTypeId());
    }

    @Override
    public List<Venue> handle(GetActiveVenuesQuery query) {
        return venueRepository.findByIsActiveTrue();
    }

    @Override
    public List<Venue> handle(GetAllVenuesQuery query) {
        return venueRepository.findAll();
    }

    @Override
    public int handle(GetTotalVenuesCountQuery query) {
        return (int) venueRepository.count();
    }

    @Override
    public List<Venue> handle(GetVenuesWithActivePromotionsQuery query) {
        return venueRepository.findAllWithActivePromotions();
    }

    @Override
    public List<Venue> handle(GetVenuesByPartnerIdQuery query) {
        return venueRepository.findByPartnerId(query.partnerId());
    }

    @Override
    public List<Venue> handle(GetActiveVenuesByPartnerIdQuery query) {
        return venueRepository.findByPartnerId(query.partnerId()).stream()
                .filter(Venue::isActive)
                .toList();
    }

    @Override
    public List<AvailabilityCalendar> handle(GetAvailableSlotsForVenueQuery query) {
        if (query.endDate() == null) {
            // No end date - fetch all future slots
            return availabilityCalendarRepository.findAllFutureAvailableSlotsByVenue(
                query.venueId(),
                query.startDate()
            );
        }
        // Both dates provided - fetch within range
        return availabilityCalendarRepository.findAvailableSlotsByVenueAndDateRange(
            query.venueId(),
            query.startDate(),
            query.endDate()
        );
    }

    @Override
    public VenueEncounterStatisticsResource handle(GetVenueEncounterStatisticsQuery query) {
        // Get data from Encounters BC via External Service (ACL wrapper)
        var dailyStats = externalEncounterService.fetchEncounterStatsByVenueAndDateRange(
            query.venueId(), query.startDate(), query.endDate()
        );
        
        // Transform to resources with formatted day names
        List<DailyEncounterStatResource> dailyResources = dailyStats.stream()
            .map(stat -> new DailyEncounterStatResource(
                stat.date(),
                formatDayOfWeek(stat.date()),
                getWeekNumber(stat.date()),
                stat.scheduledCount(),
                stat.completedCount()
            ))
            .toList();
        
        // Group by week number
        Map<Integer, List<DailyEncounterStatResource>> byWeek = dailyResources.stream()
            .collect(Collectors.groupingBy(DailyEncounterStatResource::weekNumber));
        
        // Create weekly statistics
        List<WeeklyEncounterStatResource> weeklyStats = byWeek.entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .map(entry -> {
                List<DailyEncounterStatResource> weekDays = entry.getValue();
                long totalScheduled = weekDays.stream()
                    .mapToLong(DailyEncounterStatResource::scheduledCount).sum();
                long totalCompleted = weekDays.stream()
                    .mapToLong(DailyEncounterStatResource::completedCount).sum();
                
                LocalDate firstDay = weekDays.stream()
                    .map(DailyEncounterStatResource::date)
                    .min(LocalDate::compareTo)
                    .orElse(query.startDate());
                LocalDate lastDay = weekDays.stream()
                    .map(DailyEncounterStatResource::date)
                    .max(LocalDate::compareTo)
                    .orElse(query.endDate());
                
                String weekLabel = formatWeekLabel(entry.getKey(), firstDay, lastDay);
                
                return new WeeklyEncounterStatResource(
                    entry.getKey(),
                    weekLabel,
                    weekDays,
                    totalScheduled,
                    totalCompleted
                );
            })
            .toList();
        
        // Calculate grand totals
        long grandTotalScheduled = weeklyStats.stream()
            .mapToLong(WeeklyEncounterStatResource::totalScheduled).sum();
        long grandTotalCompleted = weeklyStats.stream()
            .mapToLong(WeeklyEncounterStatResource::totalCompleted).sum();
        
        return new VenueEncounterStatisticsResource(
            query.venueId(),
            query.startDate(),
            query.endDate(),
            weeklyStats,
            new VenueEncounterStatisticsResource.TotalsResource(grandTotalScheduled, grandTotalCompleted)
        );
    }

    /**
     * Format date as "Lunes 01 de Diciembre"
     */
    private String formatDayOfWeek(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE dd 'de' MMMM", new Locale("es", "ES"));
        String formatted = date.format(formatter);
        return formatted.substring(0, 1).toUpperCase() + formatted.substring(1);
    }

    /**
     * Get week number of the month
     */
    private int getWeekNumber(LocalDate date) {
        WeekFields weekFields = WeekFields.of(new Locale("es", "ES"));
        return date.get(weekFields.weekOfMonth());
    }

    /**
     * Format week label as "Semana 1 (01 Dic - 07 Dic)"
     */
    private String formatWeekLabel(int weekNumber, LocalDate firstDay, LocalDate lastDay) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM", new Locale("es", "ES"));
        return String.format("Semana %d (%s - %s)", 
            weekNumber, 
            firstDay.format(formatter), 
            lastDay.format(formatter)
        );
    }
}