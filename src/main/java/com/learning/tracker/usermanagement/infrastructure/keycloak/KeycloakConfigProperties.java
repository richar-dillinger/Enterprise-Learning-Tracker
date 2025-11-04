package com.learning.tracker.usermanagement.infrastructure.keycloak;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration properties for Keycloak integration.
 */
@Component
@ConfigurationProperties(prefix = "keycloak")
@Getter
@Setter
public class KeycloakConfigProperties {

    private String authServerUrl;
    private String realm;
    private String resource;
    private Credentials credentials;
    private Admin admin;

    @Getter
    @Setter
    public static class Credentials {
        private String secret;
    }

    @Getter
    @Setter
    public static class Admin {
        private String username;
        private String password;
        private String clientId;
    }
}
