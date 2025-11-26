package com.hampcoders.glottia.platform.api.encounters.interfaces.rest;

import com.hampcoders.glottia.platform.api.encounters.domain.model.aggregates.LoyaltyAccount;
import com.hampcoders.glottia.platform.api.encounters.domain.model.queries.GetLeaderboardQuery;
import com.hampcoders.glottia.platform.api.encounters.domain.model.queries.GetLoyaltyAccountByLearnerQuery;
import com.hampcoders.glottia.platform.api.encounters.domain.model.queries.GetUnlockedBadgesQuery;
import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.LearnerId;
import com.hampcoders.glottia.platform.api.encounters.domain.services.LoyaltyAccountQueryService;
import com.hampcoders.glottia.platform.api.encounters.interfaces.rest.resources.LoyaltyAccountResource;
import com.hampcoders.glottia.platform.api.encounters.interfaces.rest.resources.LoyaltyLeaderboardResource;
import com.hampcoders.glottia.platform.api.encounters.interfaces.rest.transform.LoyaltyAccountResourceFromEntityAssembler;
// import com.hampcoders.glottia.platform.api.profiles.interfaces.acl.ProfilesContextFacade; // Necesario para nombres
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/loyalty", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Loyalty", description = "Loyalty (Gamification) Endpoints")
public class LoyaltyController {

    private final LoyaltyAccountQueryService loyaltyAccountQueryService;
    // private final ProfilesContextFacade profilesContextFacade; // Inyectar para
    // obtener nombres

    public LoyaltyController(LoyaltyAccountQueryService loyaltyAccountQueryService /*
                                                                                    * , ProfilesContextFacade
                                                                                    * profilesContextFacade
                                                                                    */) {
        this.loyaltyAccountQueryService = loyaltyAccountQueryService;
        // this.profilesContextFacade = profilesContextFacade;
    }

    @Operation(summary = "Obtener mi cuenta de lealtad (puntos)")
    @GetMapping("/me")
    public ResponseEntity<LoyaltyAccountResource> getMyLoyaltyAccount() {
        // TODO: Obtener LearnerId del contexto de seguridad
        LearnerId learnerId = new LearnerId(1L); // Placeholder

        var query = new GetLoyaltyAccountByLearnerQuery(learnerId);
        var account = loyaltyAccountQueryService.handle(query);

        return account.map(a -> ResponseEntity.ok(LoyaltyAccountResourceFromEntityAssembler.toResourceFromEntity(a)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Obtener el leaderboard")
    @GetMapping("/leaderboard")
    public ResponseEntity<List<LoyaltyLeaderboardResource>> getLeaderboard(
            @RequestParam(defaultValue = "10") Integer top,
            @RequestParam(defaultValue = "ALL_TIME") String period) {
        var query = new GetLeaderboardQuery(top, period);
        var accounts = loyaltyAccountQueryService.handle(query); // Devuelve List<Object> que es List<LoyaltyAccount>

        var resources = accounts.stream()
                .map(obj -> (LoyaltyAccount) obj)
                .map(account -> {
                    // TODO: Usar ACL para obtener el nombre del Learner
                    // String learnerName =
                    // profilesContextFacade.fetchLearnerName(account.getLearnerId());
                    String learnerName = "Learner " + account.getLearnerId().learnerId(); // Placeholder
                    return new LoyaltyLeaderboardResource(account.getLearnerId().learnerId(), account.getPoints(),
                            learnerName);
                }).collect(Collectors.toList());

        return ResponseEntity.ok(resources);
    }

    @Operation(summary = "Obtener mis badges desbloqueados")
    @GetMapping("/me/badges")
    public ResponseEntity<List<?>> getMyBadges() {
        // TODO: Obtener LearnerId del contexto de seguridad
        LearnerId learnerId = new LearnerId(1L); // Placeholder

        var query = new GetUnlockedBadgesQuery(learnerId);
        var badges = loyaltyAccountQueryService.handle(query);

        // La lógica de badges no está implementada, devuelve vacío
        return ResponseEntity.ok(badges);
    }
}
