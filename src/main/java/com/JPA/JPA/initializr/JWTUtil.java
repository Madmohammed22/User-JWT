package com.JPA.JPA.initializr;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTUtil {

//    @Value("${jwt_secret}")
    @Value("${jwt_secret:default-secret-key-change-in-production}")
    private String secret;

    // Token expiration time (24 hours in milliseconds)
    private static final long JWT_TOKEN_VALIDITY = 24 * 60 * 60 * 1000;

    public String generateToken(String username) throws
            IllegalArgumentException, JWTCreationException {
        return JWT.create()
                .withSubject("User Details")
                .withClaim("username", username)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .withIssuer("JOB TRACKER APPLICATION")
                .sign(Algorithm.HMAC256(secret));
    }

    public String validateTokenAndRetrieveSubject(String token) throws
            JWTVerificationException{
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("User Details")
                .withIssuer("JOB TRACKER APPLICATION")
                .build();

        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("username").asString();
    }

    // Method called by JWTFilter to extract username from token
    public String extractUsername(String token) {
        try {
            return validateTokenAndRetrieveSubject(token);
        } catch (JWTVerificationException e) {
            return null;
        }
    }

    // Method called by JWTFilter to validate token against user details
    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            String username = extractUsername(token);
            return username != null &&
                    username.equals(userDetails.getUsername()) &&
                    !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    // Helper method to check if token is expired
    private boolean isTokenExpired(String token) {
        try {
            DecodedJWT jwt = JWT.require(Algorithm.HMAC256(secret))
                    .withSubject("User Details")
                    .withIssuer("JOB TRACKER APPLICATION")
                    .build()
                    .verify(token);

            Date expiresAt = jwt.getExpiresAt();
            return expiresAt != null && expiresAt.before(new Date());
        } catch (JWTVerificationException e) {
            return true; // If we can't verify, consider it expired
        }
    }

    // Additional utility methods that might be useful

    // Extract expiration date from token
    public Date extractExpiration(String token) {
        try {
            DecodedJWT jwt = JWT.require(Algorithm.HMAC256(secret))
                    .withSubject("User Details")
                    .withIssuer("JOB TRACKER APPLICATION")
                    .build()
                    .verify(token);
            return jwt.getExpiresAt();
        } catch (JWTVerificationException e) {
            return null;
        }
    }

    // Extract issued at date from token
    public Date extractIssuedAt(String token) {
        try {
            DecodedJWT jwt = JWT.require(Algorithm.HMAC256(secret))
                    .withSubject("User Details")
                    .withIssuer("JOB TRACKER APPLICATION")
                    .build()
                    .verify(token);
            return jwt.getIssuedAt();
        } catch (JWTVerificationException e) {
            return null;
        }
    }

    // Check if token can be refreshed (not expired for too long)
    public boolean canTokenBeRefreshed(String token) {
        return !isTokenExpired(token) || ignoreTokenExpiration(token);
    }

    // Helper method for refresh logic - customize as needed
    private boolean ignoreTokenExpiration(String token) {
        // You can implement logic here to allow refreshing of recently expired tokens
        // For example, allow refresh within 1 hour of expiration
        return false;
    }
}