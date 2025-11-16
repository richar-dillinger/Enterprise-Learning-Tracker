package com.learning.tracker.usermanagement.application.service;

import com.learning.tracker.shared.domain.vo.SchoolId;
import com.learning.tracker.shared.domain.vo.UserId;
import com.learning.tracker.shared.infrastructure.exception.EntityNotFoundException;
import com.learning.tracker.shared.TestFixtures;
import com.learning.tracker.usermanagement.application.usecase.AssignRoleUseCase.*;
import com.learning.tracker.usermanagement.domain.event.UserRoleChangedEvent;
import com.learning.tracker.usermanagement.domain.model.SchoolRole;
import com.learning.tracker.usermanagement.domain.model.SystemRole;
import com.learning.tracker.usermanagement.domain.model.User;
import com.learning.tracker.usermanagement.domain.repository.UserRepository;
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

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AssignRoleService Tests")
class AssignRoleServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private AssignRoleService assignRoleService;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Captor
    private ArgumentCaptor<UserRoleChangedEvent> eventCaptor;

    @Nested
    @DisplayName("Assign System Role Tests")
    class AssignSystemRoleTests {

        @Test
        @DisplayName("Should successfully assign system role to user")
        void shouldAssignSystemRole() {
            // Given
            UUID userId = UUID.randomUUID();
            User mockUser = TestFixtures.reconstituteTestUser(
                    UserId.of(userId),
                    TestFixtures.testEmail()
            );
            AssignSystemRoleCommand command = new AssignSystemRoleCommand(
                    userId,
                    SystemRole.ADMIN
            );

            when(userRepository.findById(any(UserId.class)))
                    .thenReturn(Optional.of(mockUser));
            when(userRepository.save(any(User.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            // When
            assignRoleService.assignSystemRole(command);

            // Then
            verify(userRepository).findById(UserId.of(userId));
            verify(userRepository).save(userCaptor.capture());
            verify(eventPublisher).publishEvent(eventCaptor.capture());

            User savedUser = userCaptor.getValue();
            assertThat(savedUser.getSystemRole()).isEqualTo(SystemRole.ADMIN);

            UserRoleChangedEvent event = eventCaptor.getValue();
            assertThat(event.userId()).isEqualTo(UserId.of(userId));
            assertThat(event.systemRole()).isEqualTo(SystemRole.ADMIN);
            assertThat(event.isSystemRoleChange()).isTrue();
        }

        @Test
        @DisplayName("Should throw EntityNotFoundException when user does not exist")
        void shouldThrowExceptionWhenUserNotFound() {
            // Given
            UUID userId = UUID.randomUUID();
            AssignSystemRoleCommand command = new AssignSystemRoleCommand(
                    userId,
                    SystemRole.ADMIN
            );

            when(userRepository.findById(any(UserId.class)))
                    .thenReturn(Optional.empty());

            // When/Then
            assertThatThrownBy(() -> assignRoleService.assignSystemRole(command))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("User");

            verify(userRepository).findById(UserId.of(userId));
            verify(userRepository, never()).save(any());
            verify(eventPublisher, never()).publishEvent(any());
        }

        @Test
        @DisplayName("Should change existing system role")
        void shouldChangeExistingSystemRole() {
            // Given
            UUID userId = UUID.randomUUID();
            User mockUser = TestFixtures.reconstituteTestUser(
                    UserId.of(userId),
                    TestFixtures.testEmail(),
                    SystemRole.USER
            );
            AssignSystemRoleCommand command = new AssignSystemRoleCommand(
                    userId,
                    SystemRole.PLATFORM_MANAGER
            );

            when(userRepository.findById(any(UserId.class)))
                    .thenReturn(Optional.of(mockUser));
            when(userRepository.save(any(User.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            // When
            assignRoleService.assignSystemRole(command);

            // Then
            verify(userRepository).save(userCaptor.capture());
            User savedUser = userCaptor.getValue();
            assertThat(savedUser.getSystemRole()).isEqualTo(SystemRole.PLATFORM_MANAGER);
        }

        @Test
        @DisplayName("Should publish UserRoleChangedEvent after assigning system role")
        void shouldPublishEvent() {
            // Given
            UUID userId = UUID.randomUUID();
            User mockUser = TestFixtures.reconstituteTestUser(
                    UserId.of(userId),
                    TestFixtures.testEmail()
            );
            AssignSystemRoleCommand command = new AssignSystemRoleCommand(
                    userId,
                    SystemRole.ADMIN
            );

            when(userRepository.findById(any(UserId.class)))
                    .thenReturn(Optional.of(mockUser));

            // When
            assignRoleService.assignSystemRole(command);

            // Then
            verify(eventPublisher).publishEvent(eventCaptor.capture());
            UserRoleChangedEvent event = eventCaptor.getValue();
            assertThat(event.userId()).isEqualTo(UserId.of(userId));
            assertThat(event.systemRole()).isEqualTo(SystemRole.ADMIN);
            assertThat(event.isSystemRoleChange()).isTrue();
        }
    }

    @Nested
    @DisplayName("Assign School Role Tests")
    class AssignSchoolRoleTests {

        @Test
        @DisplayName("Should successfully assign school role to user")
        void shouldAssignSchoolRole() {
            // Given
            UUID userId = UUID.randomUUID();
            UUID schoolId = UUID.randomUUID();
            User mockUser = TestFixtures.reconstituteTestUser(
                    UserId.of(userId),
                    TestFixtures.testEmail()
            );
            AssignSchoolRoleCommand command = new AssignSchoolRoleCommand(
                    userId,
                    schoolId,
                    SchoolRole.TUTOR
            );

            when(userRepository.findById(any(UserId.class)))
                    .thenReturn(Optional.of(mockUser));
            when(userRepository.save(any(User.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            // When
            assignRoleService.assignSchoolRole(command);

            // Then
            verify(userRepository).findById(UserId.of(userId));
            verify(userRepository).save(userCaptor.capture());
            verify(eventPublisher).publishEvent(eventCaptor.capture());

            User savedUser = userCaptor.getValue();
            assertThat(savedUser.getSchoolRole(SchoolId.of(schoolId)))
                    .isPresent()
                    .contains(SchoolRole.TUTOR);

            UserRoleChangedEvent event = eventCaptor.getValue();
            assertThat(event.userId()).isEqualTo(UserId.of(userId));
            assertThat(event.getSchoolId()).isPresent();
            assertThat(event.getSchoolId().get()).isEqualTo(SchoolId.of(schoolId));
            assertThat(event.getSchoolRole()).isPresent();
            assertThat(event.getSchoolRole().get()).isEqualTo(SchoolRole.TUTOR);
            assertThat(event.isSchoolRoleAssignment()).isTrue();
        }

        @Test
        @DisplayName("Should throw EntityNotFoundException when user does not exist")
        void shouldThrowExceptionWhenUserNotFound() {
            // Given
            UUID userId = UUID.randomUUID();
            UUID schoolId = UUID.randomUUID();
            AssignSchoolRoleCommand command = new AssignSchoolRoleCommand(
                    userId,
                    schoolId,
                    SchoolRole.STUDENT
            );

            when(userRepository.findById(any(UserId.class)))
                    .thenReturn(Optional.empty());

            // When/Then
            assertThatThrownBy(() -> assignRoleService.assignSchoolRole(command))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("User");

            verify(userRepository, never()).save(any());
            verify(eventPublisher, never()).publishEvent(any());
        }

        @Test
        @DisplayName("Should assign different roles to different schools")
        void shouldAssignDifferentRolesToDifferentSchools() {
            // Given
            UUID userId = UUID.randomUUID();
            UUID school1Id = UUID.randomUUID();
            UUID school2Id = UUID.randomUUID();
            User mockUser = TestFixtures.reconstituteTestUser(
                    UserId.of(userId),
                    TestFixtures.testEmail()
            );

            when(userRepository.findById(any(UserId.class)))
                    .thenReturn(Optional.of(mockUser));
            when(userRepository.save(any(User.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            // When
            assignRoleService.assignSchoolRole(new AssignSchoolRoleCommand(
                    userId, school1Id, SchoolRole.TUTOR
            ));
            assignRoleService.assignSchoolRole(new AssignSchoolRoleCommand(
                    userId, school2Id, SchoolRole.STUDENT
            ));

            // Then
            verify(userRepository, times(2)).save(userCaptor.capture());
            User lastSavedUser = userCaptor.getValue();

            assertThat(lastSavedUser.getSchoolRole(SchoolId.of(school1Id)))
                    .isPresent()
                    .contains(SchoolRole.TUTOR);
            assertThat(lastSavedUser.getSchoolRole(SchoolId.of(school2Id)))
                    .isPresent()
                    .contains(SchoolRole.STUDENT);
        }
    }

    @Nested
    @DisplayName("Remove School Role Tests")
    class RemoveSchoolRoleTests {

        @Test
        @DisplayName("Should successfully remove school role from user")
        void shouldRemoveSchoolRole() {
            // Given
            UUID userId = UUID.randomUUID();
            UUID schoolId = UUID.randomUUID();
            SchoolId schoolIdVO = SchoolId.of(schoolId);
            User mockUser = TestFixtures.createTestUserWithSchoolRole(schoolIdVO, SchoolRole.STUDENT);

            RemoveSchoolRoleCommand command = new RemoveSchoolRoleCommand(userId, schoolId);

            when(userRepository.findById(any(UserId.class)))
                    .thenReturn(Optional.of(mockUser));
            when(userRepository.save(any(User.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            // When
            assignRoleService.removeSchoolRole(command);

            // Then
            verify(userRepository).findById(UserId.of(userId));
            verify(userRepository).save(userCaptor.capture());
            verify(eventPublisher).publishEvent(eventCaptor.capture());

            User savedUser = userCaptor.getValue();
            assertThat(savedUser.getSchoolRole(schoolIdVO)).isEmpty();

            UserRoleChangedEvent event = eventCaptor.getValue();
            assertThat(event.userId()).isEqualTo(UserId.of(userId));
            assertThat(event.getSchoolId()).isPresent();
            assertThat(event.getSchoolId().get()).isEqualTo(schoolIdVO);
            assertThat(event.getSchoolRole()).isEmpty();
            assertThat(event.isSchoolRoleRemoval()).isTrue();
        }

        @Test
        @DisplayName("Should not save or publish event when role doesn't exist")
        void shouldNotSaveWhenRoleDoesNotExist() {
            // Given
            UUID userId = UUID.randomUUID();
            UUID schoolId = UUID.randomUUID();
            User mockUser = TestFixtures.reconstituteTestUser(
                    UserId.of(userId),
                    TestFixtures.testEmail()
            );

            RemoveSchoolRoleCommand command = new RemoveSchoolRoleCommand(userId, schoolId);

            when(userRepository.findById(any(UserId.class)))
                    .thenReturn(Optional.of(mockUser));

            // When
            assignRoleService.removeSchoolRole(command);

            // Then
            verify(userRepository).findById(UserId.of(userId));
            verify(userRepository, never()).save(any());
            verify(eventPublisher, never()).publishEvent(any());
        }

        @Test
        @DisplayName("Should throw EntityNotFoundException when user does not exist")
        void shouldThrowExceptionWhenUserNotFound() {
            // Given
            UUID userId = UUID.randomUUID();
            UUID schoolId = UUID.randomUUID();
            RemoveSchoolRoleCommand command = new RemoveSchoolRoleCommand(userId, schoolId);

            when(userRepository.findById(any(UserId.class)))
                    .thenReturn(Optional.empty());

            // When/Then
            assertThatThrownBy(() -> assignRoleService.removeSchoolRole(command))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("User");

            verify(userRepository, never()).save(any());
            verify(eventPublisher, never()).publishEvent(any());
        }
    }
}
