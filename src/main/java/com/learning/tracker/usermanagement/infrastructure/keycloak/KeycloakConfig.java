package com.learning.tracker.usermanagement.infrastructure.keycloak;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Configuration class for Keycloak Admin Client.
 */
@Configuration
@Profile("dev")
public class KeycloakConfig {

    private final KeycloakConfigProperties properties;

    public KeycloakConfig(KeycloakConfigProperties properties) {
        this.properties = properties;
    }

    /**
     * Creates a Keycloak Admin Client bean for managing users and realm configuration.
     *
     * @return configured Keycloak admin client
     */
    @Bean
    public Keycloak keycloakAdminClient() {
        return KeycloakBuilder.builder()
                .serverUrl(properties.getAuthServerUrl())
                .realm("master") // Admin operations use master realm
                .clientId(properties.getAdmin().getClientId())
                .username(properties.getAdmin().getUsername())
                .password(properties.getAdmin().getPassword())
                .build();
    }
}
