package com.hampcoders.glottia.platform.api.iam.application.internal.outboundservices.tokens;

/**
 * TokenService interface
 * This interface is used to generate and validate tokens
 */
public interface TokenService {

    /**
     * Generate a token for a given username
     * @param username the username
     * @return String the token
     */
    String generateToken(String username);

    /**
     * Generate a token with user claims
     * @param username the username
     * @param userId the user id
     * @param role the business role (LEARNER, PARTNER, or UNASSIGNED)
     * @param roleSpecificId the learner or partner id
     * @return String the token
     */
    String generateToken(String username, Long userId, String role, Long roleSpecificId);

    /**
     * Extract the username from a token
     * @param token the token
     * @return String the username
     */
    String getUsernameFromToken(String token);

    /**
     * Validate a token
     * @param token the token
     * @return boolean true if the token is valid, false otherwise
     */
    boolean validateToken(String token);
}