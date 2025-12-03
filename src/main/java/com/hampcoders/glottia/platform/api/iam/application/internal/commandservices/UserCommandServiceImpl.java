package com.hampcoders.glottia.platform.api.iam.application.internal.commandservices;

import com.hampcoders.glottia.platform.api.iam.application.internal.outboundservices.hashing.HashingService;
import com.hampcoders.glottia.platform.api.iam.application.internal.outboundservices.tokens.TokenService;
import com.hampcoders.glottia.platform.api.iam.domain.model.aggregates.User;
import com.hampcoders.glottia.platform.api.iam.domain.model.commands.SignInCommand;
import com.hampcoders.glottia.platform.api.iam.domain.model.commands.SignUpCommand;
import com.hampcoders.glottia.platform.api.iam.domain.services.UserCommandService;
import com.hampcoders.glottia.platform.api.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import com.hampcoders.glottia.platform.api.profiles.interfaces.acl.ProfilesContextFacade;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * User command service implementation
 * <p>
 *     This class implements the {@link UserCommandService} interface and provides the implementation for the
 *     {@link SignInCommand} and {@link SignUpCommand} commands.
 * </p>
 */
@Service
public class UserCommandServiceImpl implements UserCommandService {

    private final UserRepository userRepository;
    private final HashingService hashingService;
    private final TokenService tokenService;
    private final ProfilesContextFacade profilesContextFacade;


    public UserCommandServiceImpl(UserRepository userRepository, HashingService hashingService,
                                  TokenService tokenService, ProfilesContextFacade profilesContextFacade) {
        this.userRepository = userRepository;
        this.hashingService = hashingService;
        this.tokenService = tokenService;
        this.profilesContextFacade = profilesContextFacade;
    }

    /**
     * Handle the sign-in command
     * <p>
     *     This method handles the {@link SignInCommand} command and returns the user and the token.
     * </p>
     * @param command the sign-in command containing the username and password
     * @return and optional containing the user matching the username and the generated token
     * @throws RuntimeException if the user is not found or the password is invalid
     */
    @Override
    public Optional<ImmutablePair<User, String>> handle(SignInCommand command) {
        var user = userRepository.findByUsername(command.username());

        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        if (!hashingService.matches(command.password(), user.get().getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        var authenticatedUser = user.get();
        
        // Determinar rol y obtener IDs específicos
        String role = determineUserRole(authenticatedUser.getId());
        Long roleSpecificId = getRoleSpecificId(authenticatedUser.getId(), role);

        // Generar token CON los claims adicionales
        var token = tokenService.generateToken(
                authenticatedUser.getUsername(),
                authenticatedUser.getId(),
                role,
                roleSpecificId
        );

        return Optional.of(ImmutablePair.of(authenticatedUser, token));
    }

    @Override
    public Optional<User> handle(SignUpCommand command) {
        if (userRepository.existsByUsername(command.username())) {
            throw new RuntimeException("Username already exists");
        }

        var hashedPassword = hashingService.encode(command.password());
        var user = new User(command.username(), hashedPassword);
        userRepository.save(user);

        return Optional.of(user);
    }

    /**
     * Determines the user's business role (LEARNER, PARTNER, or UNASSIGNED)
     */
    private String determineUserRole(Long userId) {
        if (profilesContextFacade.isUserLearner(userId)) {
            return "LEARNER";
        } else if (profilesContextFacade.isUserPartner(userId)) {
            return "PARTNER";
        }
        return "UNASSIGNED";
    }

    /**
     * Gets the specific ID (learnerId or partnerId) based on the role
     */
    private Long getRoleSpecificId(Long userId, String role) {
        if ("LEARNER".equals(role)) {
            return profilesContextFacade.fetchLearnerIdByUserId(userId);
        } else if ("PARTNER".equals(role)) {
            return profilesContextFacade.fetchPartnerIdByUserId(userId);
        }
        return 0L;
    }
}