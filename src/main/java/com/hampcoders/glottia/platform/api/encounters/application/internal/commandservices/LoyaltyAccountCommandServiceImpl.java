package com.hampcoders.glottia.platform.api.encounters.application.internal.commandservices;

import com.hampcoders.glottia.platform.api.encounters.domain.model.aggregates.LoyaltyAccount;
import com.hampcoders.glottia.platform.api.encounters.domain.model.commands.AwardPointsCommand;
import com.hampcoders.glottia.platform.api.encounters.domain.model.commands.CreateLoyaltyAccountCommand;
import com.hampcoders.glottia.platform.api.encounters.domain.model.commands.PenalizeCommand;
import com.hampcoders.glottia.platform.api.encounters.domain.services.LoyaltyAccountCommandService;
import com.hampcoders.glottia.platform.api.encounters.infrastructure.persistence.jpa.repository.LoyaltyAccountRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implements the LoyaltyAccountCommandService to handle commands related to loyalty accounts.
 * This service manages the creation of loyalty accounts, awarding points, and penalizing accounts,
 * enforcing business rules and interacting with the necessary repository.
 */
@Service
public class LoyaltyAccountCommandServiceImpl implements LoyaltyAccountCommandService {

    private final LoyaltyAccountRepository loyaltyAccountRepository;

    public LoyaltyAccountCommandServiceImpl(LoyaltyAccountRepository loyaltyAccountRepository) {
        this.loyaltyAccountRepository = loyaltyAccountRepository;
    }

    @Override
    public Optional<LoyaltyAccount> handle(CreateLoyaltyAccountCommand command) {
        if (loyaltyAccountRepository.existsByLearnerId(command.learnerId())) {
            throw new IllegalArgumentException("Loyalty account already exists for this learner.");
        }
        var account = new LoyaltyAccount(command.learnerId());
        loyaltyAccountRepository.save(account);
        return Optional.of(account);
    }

    @Override
    public Optional<LoyaltyAccount> handle(AwardPointsCommand command) {
        var account = loyaltyAccountRepository.findByLearnerId(command.learnerId())
                .orElseThrow(() -> new IllegalArgumentException("Loyalty account not found for this learner."));
        
        // Usamos los métodos específicos del agregado
        if ("CREATION".equals(command.reason())) {
            account.awardPointsForCreation();
        } else if ("ATTENDANCE".equals(command.reason())) {
             // Asumimos un valor estándar de 10 para asistencia
            account.awardPointsForAttendance(10); 
        } else {
            // Un método genérico que deberíamos agregar al Agregado
            // account.awardGenericPoints(command.points()); 
            
            // Por ahora, usamos el de asistencia como fallback
             account.awardPointsForAttendance(command.points());
        }

        loyaltyAccountRepository.save(account);
        return Optional.of(account);
    }

    @Override
    public Optional<LoyaltyAccount> handle(PenalizeCommand command) {
        var account = loyaltyAccountRepository.findByLearnerId(command.learnerId())
                .orElseThrow(() -> new IllegalArgumentException("Loyalty account not found for this learner."));

        if ("NO_SHOW".equals(command.reason())) {
            account.penalizeNoShow(15); // Penalidad estándar de 15
        } else if ("LATE_CANCELLATION".equals(command.reason())) {
            account.penalizeLateCancellation(5); // Penalidad estándar de 5
        } else {
             // Método genérico
             // account.penalizeGeneric(command.penalty());
             
             // Fallback
             account.penalizeNoShow(command.penalty());
        }

        loyaltyAccountRepository.save(account);
        return Optional.of(account);
    }
}
