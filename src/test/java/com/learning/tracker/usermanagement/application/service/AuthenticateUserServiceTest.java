package com.learning.tracker.usermanagement.application.service;

import com.learning.tracker.shared.domain.vo.Email;
import com.learning.tracker.shared.domain.vo.UserId;
import com.learning.tracker.shared.infrastructure.exception.ValidationException;
import com.learning.tracker.shared.TestFixtures;
import com.learning.tracker.usermanagement.application.usecase.AuthenticateUserUseCase.*;
import com.learning.tracker.usermanagement.domain.port.out.AuthenticationPort;
import com.learning.tracker.usermanagement.domain.port.out.AuthenticationPort.AuthenticationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthenticateUserService Tests")
class AuthenticateUserServiceTest {

    @Mock
    private AuthenticationPort authenticationPort;

    @InjectMocks
    private AuthenticateUserService authenticateUserService;

    private AuthenticateCommand validCommand;

    @BeforeEach
    void setUp() {
        validCommand = new AuthenticateCommand(
                TestFixtures.TEST_EMAIL,
                TestFixtures.TEST_PASSWORD
        );
    }

    @Nested
    @DisplayName("Successful Authentication Tests")
    class SuccessfulAuthenticationTests {

        @Test
        @DisplayName("Should successfully authenticate user with valid credentials")
        void shouldAuthenticateUserWithValidCredentials() {
            // Given
            UUID userId = UUID.randomUUID();
            AuthenticationResult mockResult = new AuthenticationResult(
                    "access-token-123",
                    "refresh-token-456",
                    3600L,
                    UserId.of(userId)
            );

            when(authenticationPort.authenticate(any(Email.class), eq(TestFixtures.TEST_PASSWORD)))
                    .thenReturn(mockResult);

            // When
            AuthenticationResponse response = authenticateUserService.authenticate(validCommand);

            // Then
            assertThat(response).isNotNull();
            assertThat(response.accessToken()).isEqualTo("access-token-123");
            assertThat(response.refreshToken()).isEqualTo("refresh-token-456");
            assertThat(response.expiresIn()).isEqualTo(3600L);
            assertThat(response.userId()).isEqualTo(userId.toString());

            verify(authenticationPort).authenticate(Email.of(TestFixtures.TEST_EMAIL), TestFixtures.TEST_PASSWORD);
        }

        @Test
        @DisplayName("Should return response with all token information")
        void shouldReturnCompleteAuthenticationResponse() {
            // Given
            UUID userId = UUID.randomUUID();
            AuthenticationResult mockResult = new AuthenticationResult(
                    "jwt-access-token",
                    "jwt-refresh-token",
                    7200L,
                    UserId.of(userId)
            );

            when(authenticationPort.authenticate(any(Email.class), anyString()))
                    .thenReturn(mockResult);

            // When
            AuthenticationResponse response = authenticateUserService.authenticate(validCommand);

            // Then
            assertThat(response.accessToken()).isNotBlank();
            assertThat(response.refreshToken()).isNotBlank();
            assertThat(response.expiresIn()).isPositive();
            assertThat(response.userId()).isNotNull();
        }

        @Test
        @DisplayName("Should call authenticationPort with correct email and password")
        void shouldCallAuthenticationPortWithCorrectParameters() {
            // Given
            AuthenticationResult mockResult = new AuthenticationResult(
                    "token",
                    "refresh",
                    3600L,
                    UserId.generate()
            );

            when(authenticationPort.authenticate(any(Email.class), anyString()))
                    .thenReturn(mockResult);

            // When
            authenticateUserService.authenticate(validCommand);

            // Then
            verify(authenticationPort).authenticate(
                    eq(Email.of(TestFixtures.TEST_EMAIL)),
                    eq(TestFixtures.TEST_PASSWORD)
            );
        }
    }

    @Nested
    @DisplayName("Validation Tests")
    class ValidationTests {

        @Test
        @DisplayName("Should throw ValidationException when command is null")
        void shouldFailWhenCommandIsNull() {
            // When/Then
            assertThatThrownBy(() -> authenticateUserService.authenticate(null))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Authentication command cannot be null");

            verify(authenticationPort, never()).authenticate(any(), any());
        }

        @Test
        @DisplayName("Should throw ValidationException when email is null")
        void shouldFailWhenEmailIsNull() {
            // Given
            AuthenticateCommand command = new AuthenticateCommand(null, TestFixtures.TEST_PASSWORD);

            // When/Then
            assertThatThrownBy(() -> authenticateUserService.authenticate(command))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Email is required");

            verify(authenticationPort, never()).authenticate(any(), any());
        }

        @Test
        @DisplayName("Should throw ValidationException when email is blank")
        void shouldFailWhenEmailIsBlank() {
            // Given
            AuthenticateCommand command = new AuthenticateCommand("   ", TestFixtures.TEST_PASSWORD);

            // When/Then
            assertThatThrownBy(() -> authenticateUserService.authenticate(command))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Email is required");

            verify(authenticationPort, never()).authenticate(any(), any());
        }

        @Test
        @DisplayName("Should throw ValidationException when email is empty string")
        void shouldFailWhenEmailIsEmpty() {
            // Given
            AuthenticateCommand command = new AuthenticateCommand("", TestFixtures.TEST_PASSWORD);

            // When/Then
            assertThatThrownBy(() -> authenticateUserService.authenticate(command))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Email is required");

            verify(authenticationPort, never()).authenticate(any(), any());
        }

        @Test
        @DisplayName("Should throw ValidationException when password is null")
        void shouldFailWhenPasswordIsNull() {
            // Given
            AuthenticateCommand command = new AuthenticateCommand(TestFixtures.TEST_EMAIL, null);

            // When/Then
            assertThatThrownBy(() -> authenticateUserService.authenticate(command))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Password is required");

            verify(authenticationPort, never()).authenticate(any(), any());
        }

        @Test
        @DisplayName("Should throw ValidationException when password is blank")
        void shouldFailWhenPasswordIsBlank() {
            // Given
            AuthenticateCommand command = new AuthenticateCommand(TestFixtures.TEST_EMAIL, "   ");

            // When/Then
            assertThatThrownBy(() -> authenticateUserService.authenticate(command))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Password is required");

            verify(authenticationPort, never()).authenticate(any(), any());
        }

        @Test
        @DisplayName("Should throw ValidationException when password is empty string")
        void shouldFailWhenPasswordIsEmpty() {
            // Given
            AuthenticateCommand command = new AuthenticateCommand(TestFixtures.TEST_EMAIL, "");

            // When/Then
            assertThatThrownBy(() -> authenticateUserService.authenticate(command))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Password is required");

            verify(authenticationPort, never()).authenticate(any(), any());
        }
    }

    @Nested
    @DisplayName("Email Format Validation Tests")
    class EmailFormatValidationTests {

        @Test
        @DisplayName("Should throw exception for invalid email format")
        void shouldFailForInvalidEmailFormat() {
            // Given
            AuthenticateCommand command = new AuthenticateCommand(
                    "invalid-email-format",
                    TestFixtures.TEST_PASSWORD
            );

            // When/Then
            assertThatThrownBy(() -> authenticateUserService.authenticate(command))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Invalid email format");

            verify(authenticationPort, never()).authenticate(any(), any());
        }

        @Test
        @DisplayName("Should accept valid email format")
        void shouldAcceptValidEmailFormat() {
            // Given
            AuthenticateCommand command = new AuthenticateCommand(
                    "valid.email@example.com",
                    TestFixtures.TEST_PASSWORD
            );
            AuthenticationResult mockResult = new AuthenticationResult(
                    "token",
                    "refresh",
                    3600L,
                    UserId.generate()
            );

            when(authenticationPort.authenticate(any(Email.class), anyString()))
                    .thenReturn(mockResult);

            // When
            AuthenticationResponse response = authenticateUserService.authenticate(command);

            // Then
            assertThat(response).isNotNull();
            verify(authenticationPort).authenticate(
                    eq(Email.of("valid.email@example.com")),
                    eq(TestFixtures.TEST_PASSWORD)
            );
        }
    }

    @Nested
    @DisplayName("AuthenticationPort Integration Tests")
    class AuthenticationPortIntegrationTests {

        @Test
        @DisplayName("Should propagate authentication failure from Keycloak")
        void shouldPropagateAuthenticationFailure() {
            // Given
            when(authenticationPort.authenticate(any(Email.class), anyString()))
                    .thenThrow(new RuntimeException("Invalid credentials"));

            // When/Then
            assertThatThrownBy(() -> authenticateUserService.authenticate(validCommand))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Invalid credentials");

            verify(authenticationPort).authenticate(any(Email.class), eq(TestFixtures.TEST_PASSWORD));
        }

        @Test
        @DisplayName("Should map AuthenticationResult to AuthenticationResponse correctly")
        void shouldMapResultToResponseCorrectly() {
            // Given
            UUID userId = UUID.randomUUID();
            AuthenticationResult mockResult = new AuthenticationResult(
                    "access-token-abc",
                    "refresh-token-xyz",
                    1800L,
                    UserId.of(userId)
            );

            when(authenticationPort.authenticate(any(Email.class), anyString()))
                    .thenReturn(mockResult);

            // When
            AuthenticationResponse response = authenticateUserService.authenticate(validCommand);

            // Then
            assertThat(response.accessToken()).isEqualTo(mockResult.accessToken());
            assertThat(response.refreshToken()).isEqualTo(mockResult.refreshToken());
            assertThat(response.expiresIn()).isEqualTo(mockResult.expiresIn());
            assertThat(response.userId()).isEqualTo(mockResult.userId().toString());
        }

        @Test
        @DisplayName("Should handle different token expiration times")
        void shouldHandleDifferentExpirationTimes() {
            // Given
            long[] expirationTimes = {300L, 1800L, 3600L, 7200L};

            for (long expiresIn : expirationTimes) {
                AuthenticationResult mockResult = new AuthenticationResult(
                        "token",
                        "refresh",
                        expiresIn,
                        UserId.generate()
                );

                when(authenticationPort.authenticate(any(Email.class), anyString()))
                        .thenReturn(mockResult);

                // When
                AuthenticationResponse response = authenticateUserService.authenticate(validCommand);

                // Then
                assertThat(response.expiresIn()).isEqualTo(expiresIn);
            }
        }
    }

    @Nested
    @DisplayName("UserId Mapping Tests")
    class UserIdMappingTests {

        @Test
        @DisplayName("Should convert UserId to String correctly in response")
        void shouldConvertUserIdToString() {
            // Given
            UUID userId = UUID.randomUUID();
            AuthenticationResult mockResult = new AuthenticationResult(
                    "token",
                    "refresh",
                    3600L,
                    UserId.of(userId)
            );

            when(authenticationPort.authenticate(any(Email.class), anyString()))
                    .thenReturn(mockResult);

            // When
            AuthenticationResponse response = authenticateUserService.authenticate(validCommand);

            // Then
            assertThat(response.userId()).isEqualTo(userId.toString());
            assertThatCode(() -> UUID.fromString(response.userId())).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle different user IDs correctly")
        void shouldHandleDifferentUserIds() {
            // Given
            UUID userId1 = UUID.randomUUID();
            UUID userId2 = UUID.randomUUID();

            AuthenticationResult result1 = new AuthenticationResult("token1", "refresh1", 3600L, UserId.of(userId1));
            AuthenticationResult result2 = new AuthenticationResult("token2", "refresh2", 3600L, UserId.of(userId2));

            when(authenticationPort.authenticate(any(Email.class), anyString()))
                    .thenReturn(result1)
                    .thenReturn(result2);

            // When
            AuthenticationResponse response1 = authenticateUserService.authenticate(validCommand);
            AuthenticationResponse response2 = authenticateUserService.authenticate(validCommand);

            // Then
            assertThat(response1.userId()).isEqualTo(userId1.toString());
            assertThat(response2.userId()).isEqualTo(userId2.toString());
            assertThat(response1.userId()).isNotEqualTo(response2.userId());
        }
    }
}
