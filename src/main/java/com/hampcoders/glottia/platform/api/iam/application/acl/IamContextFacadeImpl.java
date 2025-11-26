package com.hampcoders.glottia.platform.api.iam.application.acl;

import org.springframework.stereotype.Service;

import com.hampcoders.glottia.platform.api.iam.domain.model.commands.SignUpCommand;
import com.hampcoders.glottia.platform.api.iam.domain.model.queries.GetUserByIdQuery;
import com.hampcoders.glottia.platform.api.iam.domain.model.queries.GetUserByUsernameQuery;
import com.hampcoders.glottia.platform.api.iam.domain.services.UserCommandService;
import com.hampcoders.glottia.platform.api.iam.domain.services.UserQueryService;
import com.hampcoders.glottia.platform.api.iam.interfaces.acl.IamContextFacade;

/**
 * Iam Context Facade Implementation
 * This class is a facade for the IAM context. It provides a simple interface for other
 * bounded contexts to interact with the IAM context.
 * This class is a part of the ACL layer.
 */
@Service
public class IamContextFacadeImpl implements IamContextFacade {
    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    public IamContextFacadeImpl(UserCommandService userCommandService, UserQueryService userQueryService) {
        this.userCommandService = userCommandService;
        this.userQueryService = userQueryService;
    }

    /**
     * Creates a user with the given username and password.
     * @param username The username of the user.
     * @param password The password of the user.
     * @return The id of the created user, or 0L if creation failed.
     */
    @Override
    public Long createUser(String username, String password) {
        try {
            var signUpCommand = new SignUpCommand(username, password);
            var result = userCommandService.handle(signUpCommand);
            return result.map(user -> user.getId()).orElse(0L);
        } catch (Exception e) {
            return 0L;
        }
    }

    /**
     * Fetches the id of the user with the given username.
     * @param username The username of the user.
     * @return The id of the user, or 0L if not found.
     */
    @Override
    public Long fetchUserIdByUsername(String username) {
        try {
            var getUserByUsernameQuery = new GetUserByUsernameQuery(username);
            var result = userQueryService.handle(getUserByUsernameQuery);
            return result.map(user -> user.getId()).orElse(0L);
        } catch (Exception e) {
            return 0L;
        }
    }

    /**
     * Checks if a username already exists.
     * @param username The username to check.
     * @return true if username exists, false otherwise.
     */
    @Override
    public boolean existsByUsername(String username) {
        return fetchUserIdByUsername(username) > 0L;
    }

    /**
     * Fetches the username of the user with the given id.
     * @param userId The id of the user.
     * @return The username of the user, or empty string if not found.
     */
    @Override
    public String fetchUsernameByUserId(Long userId) {
        try {
            var getUserByIdQuery = new GetUserByIdQuery(userId);
            var result = userQueryService.handle(getUserByIdQuery);
            return result.map(user -> user.getUsername()).orElse("");
        } catch (Exception e) {
            return "";
        }
    }
}