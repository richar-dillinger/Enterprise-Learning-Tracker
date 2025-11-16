package com.learning.tracker.usermanagement.application.service;

import com.learning.tracker.shared.domain.vo.Email;
import com.learning.tracker.shared.domain.vo.SchoolId;
import com.learning.tracker.shared.domain.vo.UserId;
import com.learning.tracker.shared.TestFixtures;
import com.learning.tracker.usermanagement.application.dto.UserDTO;
import com.learning.tracker.usermanagement.domain.model.SchoolRole;
import com.learning.tracker.usermanagement.domain.model.SystemRole;
import com.learning.tracker.usermanagement.domain.model.User;
import com.learning.tracker.usermanagement.domain.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("GetUserService Tests")
class GetUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GetUserService getUserService;

    @Nested
    @DisplayName("Find By Id Tests")
    class FindByIdTests {

        @Test
        @DisplayName("Should return UserDTO when user exists")
        void shouldReturnUserWhenExists() {
            // Given
            UUID userId = UUID.randomUUID();
            User mockUser = TestFixtures.reconstituteTestUser(
                    UserId.of(userId),
                    TestFixtures.testEmail()
            );
            when(userRepository.findById(any(UserId.class)))
                    .thenReturn(Optional.of(mockUser));

            // When
            Optional<UserDTO> result = getUserService.findById(userId);

            // Then
            assertThat(result).isPresent();
            assertThat(result.get().id()).isEqualTo(userId);
            assertThat(result.get().email()).isEqualTo(TestFixtures.TEST_EMAIL);
            assertThat(result.get().firstName()).isEqualTo(TestFixtures.TEST_FIRST_NAME);
            assertThat(result.get().lastName()).isEqualTo(TestFixtures.TEST_LAST_NAME);

            verify(userRepository).findById(UserId.of(userId));
        }

        @Test
        @DisplayName("Should return empty Optional when user does not exist")
        void shouldReturnEmptyWhenUserDoesNotExist() {
            // Given
            UUID userId = UUID.randomUUID();
            when(userRepository.findById(any(UserId.class)))
                    .thenReturn(Optional.empty());

            // When
            Optional<UserDTO> result = getUserService.findById(userId);

            // Then
            assertThat(result).isEmpty();
            verify(userRepository).findById(UserId.of(userId));
        }

        @Test
        @DisplayName("Should convert User to UserDTO correctly")
        void shouldConvertUserToDTOCorrectly() {
            // Given
            UUID userId = UUID.randomUUID();
            User mockUser = TestFixtures.reconstituteTestUser(
                    UserId.of(userId),
                    TestFixtures.testEmail(),
                    SystemRole.ADMIN
            );
            when(userRepository.findById(any(UserId.class)))
                    .thenReturn(Optional.of(mockUser));

            // When
            Optional<UserDTO> result = getUserService.findById(userId);

            // Then
            assertThat(result).isPresent();
            UserDTO dto = result.get();
            assertThat(dto.systemRole()).isEqualTo(SystemRole.ADMIN);
            assertThat(dto.active()).isTrue();
            assertThat(dto.fullName()).isEqualTo(mockUser.getFullName());
        }
    }

    @Nested
    @DisplayName("Find By Email Tests")
    class FindByEmailTests {

        @Test
        @DisplayName("Should return UserDTO when user with email exists")
        void shouldReturnUserWhenEmailExists() {
            // Given
            String email = "test@example.com";
            User mockUser = TestFixtures.createTestUser(email, "John", "Doe");
            when(userRepository.findByEmail(any(Email.class)))
                    .thenReturn(Optional.of(mockUser));

            // When
            Optional<UserDTO> result = getUserService.findByEmail(email);

            // Then
            assertThat(result).isPresent();
            assertThat(result.get().email()).isEqualTo(email);
            assertThat(result.get().firstName()).isEqualTo("John");
            assertThat(result.get().lastName()).isEqualTo("Doe");

            verify(userRepository).findByEmail(Email.of(email));
        }

        @Test
        @DisplayName("Should return empty Optional when email does not exist")
        void shouldReturnEmptyWhenEmailDoesNotExist() {
            // Given
            String email = "nonexistent@example.com";
            when(userRepository.findByEmail(any(Email.class)))
                    .thenReturn(Optional.empty());

            // When
            Optional<UserDTO> result = getUserService.findByEmail(email);

            // Then
            assertThat(result).isEmpty();
            verify(userRepository).findByEmail(Email.of(email));
        }
    }

    @Nested
    @DisplayName("Find By School Tests")
    class FindBySchoolTests {

        @Test
        @DisplayName("Should return list of users in school")
        void shouldReturnUsersInSchool() {
            // Given
            UUID schoolId = UUID.randomUUID();
            User user1 = TestFixtures.createTestUser("user1@example.com", "User", "One");
            User user2 = TestFixtures.createTestUser("user2@example.com", "User", "Two");
            when(userRepository.findBySchool(any(SchoolId.class)))
                    .thenReturn(Arrays.asList(user1, user2));

            // When
            List<UserDTO> result = getUserService.findBySchool(schoolId);

            // Then
            assertThat(result).hasSize(2);
            assertThat(result.get(0).email()).isEqualTo("user1@example.com");
            assertThat(result.get(1).email()).isEqualTo("user2@example.com");

            verify(userRepository).findBySchool(SchoolId.of(schoolId));
        }

        @Test
        @DisplayName("Should return empty list when no users in school")
        void shouldReturnEmptyListWhenNoUsersInSchool() {
            // Given
            UUID schoolId = UUID.randomUUID();
            when(userRepository.findBySchool(any(SchoolId.class)))
                    .thenReturn(List.of());

            // When
            List<UserDTO> result = getUserService.findBySchool(schoolId);

            // Then
            assertThat(result).isEmpty();
            verify(userRepository).findBySchool(SchoolId.of(schoolId));
        }

        @Test
        @DisplayName("Should convert all users to DTOs correctly")
        void shouldConvertAllUsersToDTOs() {
            // Given
            UUID schoolId = UUID.randomUUID();
            SchoolId schoolIdVO = SchoolId.of(schoolId);

            User user1 = TestFixtures.createTestUserWithSchoolRole(schoolIdVO, SchoolRole.STUDENT);
            User user2 = TestFixtures.createTestUserWithSchoolRole(schoolIdVO, SchoolRole.TUTOR);
            when(userRepository.findBySchool(any(SchoolId.class)))
                    .thenReturn(Arrays.asList(user1, user2));

            // When
            List<UserDTO> result = getUserService.findBySchool(schoolId);

            // Then
            assertThat(result).hasSize(2);
            assertThat(result).allMatch(dto -> dto.schoolRoles().containsKey(schoolId));
        }
    }

    @Nested
    @DisplayName("Find All Active Tests")
    class FindAllActiveTests {

        @Test
        @DisplayName("Should return all active users")
        void shouldReturnAllActiveUsers() {
            // Given
            User user1 = TestFixtures.createTestUser("active1@example.com", "Active", "One");
            User user2 = TestFixtures.createTestUser("active2@example.com", "Active", "Two");
            User user3 = TestFixtures.createTestUser("active3@example.com", "Active", "Three");
            when(userRepository.findAllActive())
                    .thenReturn(Arrays.asList(user1, user2, user3));

            // When
            List<UserDTO> result = getUserService.findAllActive();

            // Then
            assertThat(result).hasSize(3);
            assertThat(result).allMatch(UserDTO::active);

            verify(userRepository).findAllActive();
        }

        @Test
        @DisplayName("Should return empty list when no active users")
        void shouldReturnEmptyListWhenNoActiveUsers() {
            // Given
            when(userRepository.findAllActive()).thenReturn(List.of());

            // When
            List<UserDTO> result = getUserService.findAllActive();

            // Then
            assertThat(result).isEmpty();
            verify(userRepository).findAllActive();
        }

        @Test
        @DisplayName("Should convert all active users to DTOs")
        void shouldConvertAllActiveUsersToDTOs() {
            // Given
            User admin = TestFixtures.createTestUserWithRole(SystemRole.ADMIN);
            User platformManager = TestFixtures.createTestUserWithRole(SystemRole.PLATFORM_MANAGER);
            User regularUser = TestFixtures.createTestUserWithRole(SystemRole.USER);

            when(userRepository.findAllActive())
                    .thenReturn(Arrays.asList(admin, platformManager, regularUser));

            // When
            List<UserDTO> result = getUserService.findAllActive();

            // Then
            assertThat(result).hasSize(3);
            assertThat(result).extracting(UserDTO::systemRole)
                    .containsExactlyInAnyOrder(SystemRole.ADMIN, SystemRole.PLATFORM_MANAGER, SystemRole.USER);
        }
    }
}
