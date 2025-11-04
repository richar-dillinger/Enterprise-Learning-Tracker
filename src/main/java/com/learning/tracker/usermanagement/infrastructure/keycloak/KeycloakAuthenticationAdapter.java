package com.learning.tracker.usermanagement.infrastructure.keycloak;

import com.learning.tracker.shared.domain.vo.Email;
import com.learning.tracker.shared.domain.vo.UserId;
import com.learning.tracker.shared.infrastructure.exception.UnauthorizedException;
import com.learning.tracker.shared.infrastructure.exception.ValidationException;
import com.learning.tracker.usermanagement.domain.port.out.AuthenticationPort;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.context.annotation.Profile;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Keycloak implementation of the AuthenticationPort.
 * <p>
 * This adapter integrates with Keycloak for user authentication and management.
 */
@Component
@Profile("dev")
@Slf4j
public class KeycloakAuthenticationAdapter implements AuthenticationPort {

    private final Keycloak keycloakAdminClient;
    private final KeycloakConfigProperties properties;
    private final RestTemplate restTemplate;

    public KeycloakAuthenticationAdapter(Keycloak keycloakAdminClient,
                                         KeycloakConfigProperties properties) {
        this.keycloakAdminClient = keycloakAdminClient;
        this.properties = properties;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public String createUser(UserId userId, Email email, String firstName, String lastName, String password) {
        try {
            RealmResource realmResource = getRealmResource();
            UsersResource usersResource = realmResource.users();

            // Create user representation
            UserRepresentation user = new UserRepresentation();
            user.setUsername(email.value());
            user.setEmail(email.value());
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEnabled(true);
            user.setEmailVerified(true);

            // Add custom attributes
            user.setAttributes(Map.of("userId", List.of(userId.toString())));

            // Create user
            Response response = usersResource.create(user);

            if (response.getStatus() != 201) {
                log.error("Failed to create user in Keycloak. Status: {}", response.getStatus());
                throw new ValidationException("Failed to create user in authentication system");
            }

            // Extract user ID from location header
            String locationHeader = response.getHeaderString("Location");
            String keycloakUserId = locationHeader.substring(locationHeader.lastIndexOf('/') + 1);

            // Set password
            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(password);
            credential.setTemporary(false);

            usersResource.get(keycloakUserId).resetPassword(credential);

            log.info("User created in Keycloak with ID: {}", keycloakUserId);
            return keycloakUserId;

        } catch (Exception e) {
            log.error("Error creating user in Keycloak", e);
            throw new ValidationException("Failed to create user in authentication system: " + e.getMessage());
        }
    }

    @Override
    public AuthenticationResult authenticate(Email email, String password) {
        try {
            String tokenUrl = properties.getAuthServerUrl() + "/realms/" + properties.getRealm() + "/protocol/openid-connect/token";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("grant_type", "password");
            body.add("client_id", properties.getResource());
            body.add("client_secret", properties.getCredentials().getSecret());
            body.add("username", email.value());
            body.add("password", password);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> tokenResponse = response.getBody();

                String accessToken = (String) tokenResponse.get("access_token");
                String refreshToken = (String) tokenResponse.get("refresh_token");
                long expiresIn = ((Number) tokenResponse.get("expires_in")).longValue();

                // Extract user ID from token or fetch from Keycloak
                UserId userId = getUserIdByEmail(email);

                log.info("User authenticated successfully: {}", email.value());
                return new AuthenticationResult(accessToken, refreshToken, expiresIn, userId);
            }

            throw new UnauthorizedException("Authentication failed");

        } catch (HttpClientErrorException e) {
            log.error("Authentication failed for user: {}", email.value(), e);
            throw new UnauthorizedException("Invalid email or password");
        } catch (Exception e) {
            log.error("Error during authentication", e);
            throw new UnauthorizedException("Authentication failed: " + e.getMessage());
        }
    }

    @Override
    public Optional<UserId> validateToken(String token) {
        try {
            String introspectUrl = properties.getAuthServerUrl() + "/realms/" + properties.getRealm() + "/protocol/openid-connect/token/introspect";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("token", token);
            body.add("client_id", properties.getResource());
            body.add("client_secret", properties.getCredentials().getSecret());

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(introspectUrl, request, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> introspectionResponse = response.getBody();
                Boolean active = (Boolean) introspectionResponse.get("active");

                if (Boolean.TRUE.equals(active)) {
                    String email = (String) introspectionResponse.get("email");
                    if (email != null) {
                        return Optional.of(getUserIdByEmail(Email.of(email)));
                    }
                }
            }

            return Optional.empty();

        } catch (Exception e) {
            log.error("Error validating token", e);
            return Optional.empty();
        }
    }

    @Override
    public void updatePassword(UserId userId, String newPassword) {
        try {
            String keycloakUserId = findKeycloakUserIdByUserId(userId);

            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(newPassword);
            credential.setTemporary(false);

            getRealmResource().users().get(keycloakUserId).resetPassword(credential);

            log.info("Password updated for user: {}", userId.value());

        } catch (Exception e) {
            log.error("Error updating password for user: {}", userId.value(), e);
            throw new ValidationException("Failed to update password: " + e.getMessage());
        }
    }

    @Override
    public void deleteUser(UserId userId) {
        try {
            String keycloakUserId = findKeycloakUserIdByUserId(userId);

            Response response = getRealmResource().users().delete(keycloakUserId);

            if (response.getStatus() != 204) {
                log.error("Failed to delete user from Keycloak. Status: {}", response.getStatus());
                throw new ValidationException("Failed to delete user from authentication system");
            }

            log.info("User deleted from Keycloak: {}", userId.value());

        } catch (Exception e) {
            log.error("Error deleting user from Keycloak: {}", userId.value(), e);
            throw new ValidationException("Failed to delete user: " + e.getMessage());
        }
    }

    // Helper methods

    private RealmResource getRealmResource() {
        return keycloakAdminClient.realm(properties.getRealm());
    }

    private UserId getUserIdByEmail(Email email) {
        List<UserRepresentation> users = getRealmResource()
                .users()
                .search(email.value(), true);

        if (users.isEmpty()) {
            throw new ValidationException("User not found in authentication system");
        }

        UserRepresentation user = users.get(0);
        Map<String, List<String>> attributes = user.getAttributes();

        if (attributes != null && attributes.containsKey("userId")) {
            String userIdValue = attributes.get("userId").get(0);
            return UserId.of(userIdValue);
        }

        throw new ValidationException("User ID attribute not found");
    }

    private String findKeycloakUserIdByUserId(UserId userId) {
        List<UserRepresentation> users = getRealmResource()
                .users()
                .searchByAttributes("userId:" + userId.value());

        if (users.isEmpty()) {
            throw new ValidationException("User not found in authentication system");
        }

        return users.get(0).getId();
    }
}
