package com.learning.tracker.usermanagement.application.service;

import com.learning.tracker.shared.domain.vo.Email;
import com.learning.tracker.shared.domain.vo.UserId;
import com.learning.tracker.shared.infrastructure.exception.DuplicateEntityException;
import com.learning.tracker.shared.infrastructure.exception.ValidationException;
import com.learning.tracker.shared.TestFixtures;
import com.learning.tracker.usermanagement.application.usecase.RegisterUserUseCase.RegisterUserCommand;
import com.learning.tracker.usermanagement.domain.event.UserRegisteredEvent;
import com.learning.tracker.usermanagement.domain.model.User;
import com.learning.tracker.usermanagement.domain.port.out.AuthenticationPort;
import com.learning.tracker.usermanagement.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RegisterUserService Tests")
class RegisterUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private AuthenticationPort authenticationPort;

    @InjectMocks
    private RegisterUserService registerUserService;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Captor
    private ArgumentCaptor<UserRegisteredEvent> eventCaptor;

    private RegisterUserCommand validCommand;

    @BeforeEach
    void setUp() {
        validCommand = new RegisterUserCommand(
                TestFixtures.TEST_EMAIL,
                TestFixtures.TEST_FIRST_NAME,
                TestFixtures.TEST_LAST_NAME,
                TestFixtures.TEST_PASSWORD
        );
    }

    @Nested
    @DisplayName("Successful Registration Tests")
    class SuccessfulRegistrationTests {

        @Test
        @DisplayName("Should successfully register a new user")
        void shouldRegisterNewUser() {
            // Given
            User mockUser = TestFixtures.createTestUser();
            when(userRepository.existsByEmail(any(Email.class))).thenReturn(false);
            when(userRepository.save(any(User.class))).thenReturn(mockUser);
            when(authenticationPort.createUser(any(), any(), anyString(), anyString(), anyString()))
                    .thenReturn("keycloak-user-id-123");

            // When
            UserId result = registerUserService.register(validCommand);

            // Then
            assertThat(result).isNotNull();
            verify(userRepository).existsByEmail(any(Email.class));
            verify(userRepository).save(userCaptor.capture());
            verify(authenticationPort).createUser(
                    any(UserId.class),
                    any(Email.class),
                    eq(TestFixtures.TEST_FIRST_NAME),
                    eq(TestFixtures.TEST_LAST_NAME),
                    eq(TestFixtures.TEST_PASSWORD)
            );
            verify(eventPublisher).publishEvent(eventCaptor.capture());

            User savedUser = userCaptor.getValue();
            assertThat(savedUser.getEmail().value()).isEqualTo(TestFixtures.TEST_EMAIL);
            assertThat(savedUser.getFirstName()).isEqualTo(TestFixtures.TEST_FIRST_NAME);
            assertThat(savedUser.getLastName()).isEqualTo(TestFixtures.TEST_LAST_NAME);

            UserRegisteredEvent publishedEvent = eventCaptor.getValue();
            assertThat(publishedEvent).isNotNull();
            assertThat(publishedEvent.userId()).isEqualTo(mockUser.getId());
            assertThat(publishedEvent.email()).isEqualTo(mockUser.getEmail());
        }

        @Test
        @DisplayName("Should create user with active status and USER system role")
        void shouldCreateUserWithCorrectDefaults() {
            // Given
            when(userRepository.existsByEmail(any(Email.class))).thenReturn(false);
            when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
            when(authenticationPort.createUser(any(), any(), anyString(), anyString(), anyString()))
                    .thenReturn("keycloak-user-id-123");

            // When
            registerUserService.register(validCommand);

            // Then
            verify(userRepository).save(userCaptor.capture());
            User savedUser = userCaptor.getValue();
            assertThat(savedUser.isActive()).isTrue();
            assertThat(savedUser.getSystemRole().name()).isEqualTo("USER");
        }
    }

    @Nested
    @DisplayName("Validation Tests")
    class ValidationTests {

        @Test
        @DisplayName("Should throw ValidationException when command is null")
        void shouldFailWhenCommandIsNull() {
            // When/Then
            assertThatThrownBy(() -> registerUserService.register(null))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Registration command cannot be null");

            verify(userRepository, never()).save(any());
            verify(authenticationPort, never()).createUser(any(), any(), any(), any(), any());
        }

        @Test
        @DisplayName("Should throw ValidationException when email is null")
        void shouldFailWhenEmailIsNull() {
            // Given
            RegisterUserCommand command = new RegisterUserCommand(
                    null,
                    TestFixtures.TEST_FIRST_NAME,
                    TestFixtures.TEST_LAST_NAME,
                    TestFixtures.TEST_PASSWORD
            );

            // When/Then
            assertThatThrownBy(() -> registerUserService.register(command))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Email is required");
        }

        @Test
        @DisplayName("Should throw ValidationException when email is blank")
        void shouldFailWhenEmailIsBlank() {
            // Given
            RegisterUserCommand command = new RegisterUserCommand(
                    "   ",
                    TestFixtures.TEST_FIRST_NAME,
                    TestFixtures.TEST_LAST_NAME,
                    TestFixtures.TEST_PASSWORD
            );

            // When/Then
            assertThatThrownBy(() -> registerUserService.register(command))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Email is required");
        }

        @Test
        @DisplayName("Should throw ValidationException when firstName is null")
        void shouldFailWhenFirstNameIsNull() {
            // Given
            RegisterUserCommand command = new RegisterUserCommand(
                    TestFixtures.TEST_EMAIL,
                    null,
                    TestFixtures.TEST_LAST_NAME,
                    TestFixtures.TEST_PASSWORD
            );

            // When/Then
            assertThatThrownBy(() -> registerUserService.register(command))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("First name is required");
        }

        @Test
        @DisplayName("Should throw ValidationException when lastName is null")
        void shouldFailWhenLastNameIsNull() {
            // Given
            RegisterUserCommand command = new RegisterUserCommand(
                    TestFixtures.TEST_EMAIL,
                    TestFixtures.TEST_FIRST_NAME,
                    null,
                    TestFixtures.TEST_PASSWORD
            );

            // When/Then
            assertThatThrownBy(() -> registerUserService.register(command))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Last name is required");
        }

        @Test
        @DisplayName("Should throw ValidationException when password is null")
        void shouldFailWhenPasswordIsNull() {
            // Given
            RegisterUserCommand command = new RegisterUserCommand(
                    TestFixtures.TEST_EMAIL,
                    TestFixtures.TEST_FIRST_NAME,
                    TestFixtures.TEST_LAST_NAME,
                    null
            );

            // When/Then
            assertThatThrownBy(() -> registerUserService.register(command))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Password is required");
        }

        @Test
        @DisplayName("Should throw ValidationException when password is too short")
        void shouldFailWhenPasswordIsTooShort() {
            // Given
            RegisterUserCommand command = new RegisterUserCommand(
                    TestFixtures.TEST_EMAIL,
                    TestFixtures.TEST_FIRST_NAME,
                    TestFixtures.TEST_LAST_NAME,
                    "short"
            );

            // When/Then
            assertThatThrownBy(() -> registerUserService.register(command))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Password must be at least 8 characters");
        }
    }

    @Nested
    @DisplayName("Duplicate Email Tests")
    class DuplicateEmailTests {

        @Test
        @DisplayName("Should throw DuplicateEntityException when email already exists")
        void shouldFailWhenEmailAlreadyExists() {
            // Given
            when(userRepository.existsByEmail(any(Email.class))).thenReturn(true);

            // When/Then
            assertThatThrownBy(() -> registerUserService.register(validCommand))
                    .isInstanceOf(DuplicateEntityException.class)
                    .hasMessageContaining("User")
                    .hasMessageContaining("email");

            verify(userRepository).existsByEmail(any(Email.class));
            verify(userRepository, never()).save(any());
            verify(authenticationPort, never()).createUser(any(), any(), any(), any(), any());
            verify(eventPublisher, never()).publishEvent(any());
        }
    }

    @Nested
    @DisplayName("Keycloak Integration Tests")
    class KeycloakIntegrationTests {

        @Test
        @DisplayName("Should rollback when Keycloak user creation fails")
        void shouldRollbackWhenKeycloakCreationFails() {
            // Given
            User mockUser = TestFixtures.createTestUser();
            when(userRepository.existsByEmail(any(Email.class))).thenReturn(false);
            when(userRepository.save(any(User.class))).thenReturn(mockUser);
            when(authenticationPort.createUser(any(), any(), any(), any(), any()))
                    .thenThrow(new RuntimeException("Keycloak connection failed"));

            // When/Then
            assertThatThrownBy(() -> registerUserService.register(validCommand))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Keycloak connection failed");

            verify(userRepository).save(any(User.class));
            verify(authenticationPort).createUser(any(), any(), any(), any(), any());
            verify(eventPublisher, never()).publishEvent(any());
        }

        @Test
        @DisplayName("Should pass correct parameters to Keycloak")
        void shouldPassCorrectParametersToKeycloak() {
            // Given
            User mockUser = TestFixtures.createTestUser();
            when(userRepository.existsByEmail(any(Email.class))).thenReturn(false);
            when(userRepository.save(any(User.class))).thenReturn(mockUser);
            when(authenticationPort.createUser(any(), any(), anyString(), anyString(), anyString()))
                    .thenReturn("keycloak-user-id");

            // When
            registerUserService.register(validCommand);

            // Then
            verify(authenticationPort).createUser(
                    eq(mockUser.getId()),
                    eq(mockUser.getEmail()),
                    eq(TestFixtures.TEST_FIRST_NAME),
                    eq(TestFixtures.TEST_LAST_NAME),
                    eq(TestFixtures.TEST_PASSWORD)
            );
        }
    }

    @Nested
    @DisplayName("Event Publishing Tests")
    class EventPublishingTests {

        @Test
        @DisplayName("Should publish UserRegisteredEvent with correct data")
        void shouldPublishEventWithCorrectData() {
            // Given
            User mockUser = TestFixtures.createTestUser();
            when(userRepository.existsByEmail(any(Email.class))).thenReturn(false);
            when(userRepository.save(any(User.class))).thenReturn(mockUser);
            when(authenticationPort.createUser(any(), any(), anyString(), anyString(), anyString()))
                    .thenReturn("keycloak-user-id");

            // When
            registerUserService.register(validCommand);

            // Then
            verify(eventPublisher).publishEvent(eventCaptor.capture());
            UserRegisteredEvent event = eventCaptor.getValue();

            assertThat(event.userId()).isEqualTo(mockUser.getId());
            assertThat(event.email()).isEqualTo(mockUser.getEmail());
            assertThat(event.firstName()).isEqualTo(mockUser.getFirstName());
            assertThat(event.lastName()).isEqualTo(mockUser.getLastName());
            assertThat(event.systemRole()).isEqualTo(mockUser.getSystemRole());
        }

        @Test
        @DisplayName("Should not publish event when validation fails")
        void shouldNotPublishEventWhenValidationFails() {
            // Given
            RegisterUserCommand invalidCommand = new RegisterUserCommand(
                    null,
                    TestFixtures.TEST_FIRST_NAME,
                    TestFixtures.TEST_LAST_NAME,
                    TestFixtures.TEST_PASSWORD
            );

            // When/Then
            assertThatThrownBy(() -> registerUserService.register(invalidCommand))
                    .isInstanceOf(ValidationException.class);

            verify(eventPublisher, never()).publishEvent(any());
        }
    }
}
