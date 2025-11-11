package com.hampcoders.glottia.platform.api.iam.interfaces.acl;

public interface IamContextFacade {
    /**
     * Creates a user with the given username and password.
     * @param username The username of the user.
     * @param password The password of the user.
     * @return The id of the created user, or 0L if creation failed.
     */
    Long createUser(String username, String password);

    /**
     * Fetches the id of the user with the given username.
     * @param username The username of the user.
     * @return The id of the user, or 0L if not found.
     */
    Long fetchUserIdByUsername(String username);

    /**
     * Checks if a username already exists.
     * @param username The username to check.
     * @return true if username exists, false otherwise.
     */
    boolean existsByUsername(String username);

    /**
     * Fetches the username of the user with the given id.
     * @param userId The id of the user.
     * @return The username of the user, or empty string if not found.
     */
    String fetchUsernameByUserId(Long userId);
}