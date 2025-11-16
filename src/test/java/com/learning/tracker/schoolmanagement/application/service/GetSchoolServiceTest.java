package com.learning.tracker.schoolmanagement.application.service;

import com.learning.tracker.shared.domain.vo.SchoolId;
import com.learning.tracker.shared.domain.vo.UserId;
import com.learning.tracker.shared.TestFixtures;
import com.learning.tracker.schoolmanagement.application.dto.SchoolDTO;
import com.learning.tracker.schoolmanagement.domain.model.School;
import com.learning.tracker.schoolmanagement.domain.model.SchoolStatus;
import com.learning.tracker.schoolmanagement.domain.repository.SchoolRepository;
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
@DisplayName("GetSchoolService Tests")
class GetSchoolServiceTest {

    @Mock
    private SchoolRepository schoolRepository;

    @InjectMocks
    private GetSchoolService getSchoolService;

    @Nested
    @DisplayName("Find By Id Tests")
    class FindByIdTests {

        @Test
        @DisplayName("Should return SchoolDTO when school exists")
        void shouldReturnSchoolWhenExists() {
            // Given
            UUID schoolId = UUID.randomUUID();
            UUID creatorId = UUID.randomUUID();
            School mockSchool = TestFixtures.reconstituteTestSchool(
                    SchoolId.of(schoolId),
                    UserId.of(creatorId)
            );
            when(schoolRepository.findById(any(SchoolId.class)))
                    .thenReturn(Optional.of(mockSchool));

            // When
            Optional<SchoolDTO> result = getSchoolService.findById(schoolId);

            // Then
            assertThat(result).isPresent();
            assertThat(result.get().id()).isEqualTo(schoolId);
            assertThat(result.get().name()).isEqualTo(TestFixtures.TEST_SCHOOL_NAME);
            assertThat(result.get().createdBy()).isEqualTo(creatorId);

            verify(schoolRepository).findById(SchoolId.of(schoolId));
        }

        @Test
        @DisplayName("Should return empty Optional when school does not exist")
        void shouldReturnEmptyWhenSchoolDoesNotExist() {
            // Given
            UUID schoolId = UUID.randomUUID();
            when(schoolRepository.findById(any(SchoolId.class)))
                    .thenReturn(Optional.empty());

            // When
            Optional<SchoolDTO> result = getSchoolService.findById(schoolId);

            // Then
            assertThat(result).isEmpty();
            verify(schoolRepository).findById(SchoolId.of(schoolId));
        }

        @Test
        @DisplayName("Should convert School to SchoolDTO correctly")
        void shouldConvertSchoolToDTOCorrectly() {
            // Given
            UUID schoolId = UUID.randomUUID();
            UUID creatorId = UUID.randomUUID();
            School mockSchool = TestFixtures.reconstituteTestSchool(
                    SchoolId.of(schoolId),
                    "Tech Academy",
                    SchoolStatus.ACTIVE,
                    UserId.of(creatorId)
            );
            when(schoolRepository.findById(any(SchoolId.class)))
                    .thenReturn(Optional.of(mockSchool));

            // When
            Optional<SchoolDTO> result = getSchoolService.findById(schoolId);

            // Then
            assertThat(result).isPresent();
            SchoolDTO dto = result.get();
            assertThat(dto.id()).isEqualTo(schoolId);
            assertThat(dto.name()).isEqualTo("Tech Academy");
            assertThat(dto.status()).isEqualTo(SchoolStatus.ACTIVE);
            assertThat(dto.createdBy()).isEqualTo(creatorId);
            assertThat(dto.createdAt()).isNotNull();
            assertThat(dto.updatedAt()).isNotNull();
        }
    }

    @Nested
    @DisplayName("Find By Name Tests")
    class FindByNameTests {

        @Test
        @DisplayName("Should return SchoolDTO when school with name exists")
        void shouldReturnSchoolWhenNameExists() {
            // Given
            String schoolName = "Tech Academy";
            UUID creatorId = UUID.randomUUID();
            School mockSchool = TestFixtures.createTestSchool(schoolName, UserId.of(creatorId));
            when(schoolRepository.findByName(schoolName))
                    .thenReturn(Optional.of(mockSchool));

            // When
            Optional<SchoolDTO> result = getSchoolService.findByName(schoolName);

            // Then
            assertThat(result).isPresent();
            assertThat(result.get().name()).isEqualTo(schoolName);

            verify(schoolRepository).findByName(schoolName);
        }

        @Test
        @DisplayName("Should return empty Optional when name does not exist")
        void shouldReturnEmptyWhenNameDoesNotExist() {
            // Given
            String schoolName = "Nonexistent School";
            when(schoolRepository.findByName(schoolName))
                    .thenReturn(Optional.empty());

            // When
            Optional<SchoolDTO> result = getSchoolService.findByName(schoolName);

            // Then
            assertThat(result).isEmpty();
            verify(schoolRepository).findByName(schoolName);
        }
    }

    @Nested
    @DisplayName("Find By Status Tests")
    class FindByStatusTests {

        @Test
        @DisplayName("Should return list of schools with given status")
        void shouldReturnSchoolsByStatus() {
            // Given
            UUID creatorId = UUID.randomUUID();
            School school1 = TestFixtures.reconstituteTestSchool(
                    SchoolId.generate(),
                    "School 1",
                    SchoolStatus.ACTIVE,
                    UserId.of(creatorId)
            );
            School school2 = TestFixtures.reconstituteTestSchool(
                    SchoolId.generate(),
                    "School 2",
                    SchoolStatus.ACTIVE,
                    UserId.of(creatorId)
            );
            when(schoolRepository.findByStatus(SchoolStatus.ACTIVE))
                    .thenReturn(Arrays.asList(school1, school2));

            // When
            List<SchoolDTO> result = getSchoolService.findByStatus(SchoolStatus.ACTIVE);

            // Then
            assertThat(result).hasSize(2);
            assertThat(result).allMatch(dto -> dto.status() == SchoolStatus.ACTIVE);
            assertThat(result.get(0).name()).isEqualTo("School 1");
            assertThat(result.get(1).name()).isEqualTo("School 2");

            verify(schoolRepository).findByStatus(SchoolStatus.ACTIVE);
        }

        @Test
        @DisplayName("Should return empty list when no schools have the status")
        void shouldReturnEmptyListWhenNoSchoolsWithStatus() {
            // Given
            when(schoolRepository.findByStatus(SchoolStatus.SUSPENDED))
                    .thenReturn(List.of());

            // When
            List<SchoolDTO> result = getSchoolService.findByStatus(SchoolStatus.SUSPENDED);

            // Then
            assertThat(result).isEmpty();
            verify(schoolRepository).findByStatus(SchoolStatus.SUSPENDED);
        }

        @Test
        @DisplayName("Should handle different statuses correctly")
        void shouldHandleDifferentStatuses() {
            // Given
            UUID creatorId = UUID.randomUUID();
            School draftSchool = TestFixtures.reconstituteTestSchool(
                    SchoolId.generate(),
                    "Draft School",
                    SchoolStatus.DRAFT,
                    UserId.of(creatorId)
            );
            School archivedSchool = TestFixtures.reconstituteTestSchool(
                    SchoolId.generate(),
                    "Archived School",
                    SchoolStatus.ARCHIVED,
                    UserId.of(creatorId)
            );

            when(schoolRepository.findByStatus(SchoolStatus.DRAFT))
                    .thenReturn(List.of(draftSchool));
            when(schoolRepository.findByStatus(SchoolStatus.ARCHIVED))
                    .thenReturn(List.of(archivedSchool));

            // When
            List<SchoolDTO> draftResults = getSchoolService.findByStatus(SchoolStatus.DRAFT);
            List<SchoolDTO> archivedResults = getSchoolService.findByStatus(SchoolStatus.ARCHIVED);

            // Then
            assertThat(draftResults).hasSize(1);
            assertThat(draftResults.get(0).status()).isEqualTo(SchoolStatus.DRAFT);

            assertThat(archivedResults).hasSize(1);
            assertThat(archivedResults.get(0).status()).isEqualTo(SchoolStatus.ARCHIVED);
        }
    }

    @Nested
    @DisplayName("Find All Active Tests")
    class FindAllActiveTests {

        @Test
        @DisplayName("Should return all active schools")
        void shouldReturnAllActiveSchools() {
            // Given
            UUID creatorId = UUID.randomUUID();
            School school1 = TestFixtures.reconstituteTestSchool(
                    SchoolId.generate(),
                    "Active School 1",
                    SchoolStatus.ACTIVE,
                    UserId.of(creatorId)
            );
            School school2 = TestFixtures.reconstituteTestSchool(
                    SchoolId.generate(),
                    "Active School 2",
                    SchoolStatus.ACTIVE,
                    UserId.of(creatorId)
            );
            School school3 = TestFixtures.reconstituteTestSchool(
                    SchoolId.generate(),
                    "Active School 3",
                    SchoolStatus.ACTIVE,
                    UserId.of(creatorId)
            );
            when(schoolRepository.findAllActive())
                    .thenReturn(Arrays.asList(school1, school2, school3));

            // When
            List<SchoolDTO> result = getSchoolService.findAllActive();

            // Then
            assertThat(result).hasSize(3);
            assertThat(result).allMatch(dto -> dto.status() == SchoolStatus.ACTIVE);

            verify(schoolRepository).findAllActive();
        }

        @Test
        @DisplayName("Should return empty list when no active schools")
        void shouldReturnEmptyListWhenNoActiveSchools() {
            // Given
            when(schoolRepository.findAllActive()).thenReturn(List.of());

            // When
            List<SchoolDTO> result = getSchoolService.findAllActive();

            // Then
            assertThat(result).isEmpty();
            verify(schoolRepository).findAllActive();
        }

        @Test
        @DisplayName("Should convert all active schools to DTOs")
        void shouldConvertAllActiveSchoolsToDTOs() {
            // Given
            UUID creatorId = UUID.randomUUID();
            School school1 = TestFixtures.createTestSchool("School 1", UserId.of(creatorId));
            School school2 = TestFixtures.createTestSchool("School 2", UserId.of(creatorId));

            when(schoolRepository.findAllActive())
                    .thenReturn(Arrays.asList(school1, school2));

            // When
            List<SchoolDTO> result = getSchoolService.findAllActive();

            // Then
            assertThat(result).hasSize(2);
            assertThat(result.get(0).name()).isEqualTo("School 1");
            assertThat(result.get(1).name()).isEqualTo("School 2");
        }
    }

    @Nested
    @DisplayName("Find By Created By Tests")
    class FindByCreatedByTests {

        @Test
        @DisplayName("Should return schools created by specific user")
        void shouldReturnSchoolsCreatedByUser() {
            // Given
            UUID creatorId = UUID.randomUUID();
            School school1 = TestFixtures.createTestSchool("School 1", UserId.of(creatorId));
            School school2 = TestFixtures.createTestSchool("School 2", UserId.of(creatorId));
            when(schoolRepository.findByCreatedBy(any(UserId.class)))
                    .thenReturn(Arrays.asList(school1, school2));

            // When
            List<SchoolDTO> result = getSchoolService.findByCreatedBy(creatorId);

            // Then
            assertThat(result).hasSize(2);
            assertThat(result).allMatch(dto -> dto.createdBy().equals(creatorId));

            verify(schoolRepository).findByCreatedBy(UserId.of(creatorId));
        }

        @Test
        @DisplayName("Should return empty list when user has not created any schools")
        void shouldReturnEmptyListWhenUserHasNoSchools() {
            // Given
            UUID creatorId = UUID.randomUUID();
            when(schoolRepository.findByCreatedBy(any(UserId.class)))
                    .thenReturn(List.of());

            // When
            List<SchoolDTO> result = getSchoolService.findByCreatedBy(creatorId);

            // Then
            assertThat(result).isEmpty();
            verify(schoolRepository).findByCreatedBy(UserId.of(creatorId));
        }

        @Test
        @DisplayName("Should only return schools for the specified creator")
        void shouldReturnOnlySchoolsForSpecifiedCreator() {
            // Given
            UUID creator1 = UUID.randomUUID();
            UUID creator2 = UUID.randomUUID();

            School school1 = TestFixtures.createTestSchool("School 1", UserId.of(creator1));
            School school2 = TestFixtures.createTestSchool("School 2", UserId.of(creator1));
            School school3 = TestFixtures.createTestSchool("School 3", UserId.of(creator2));

            when(schoolRepository.findByCreatedBy(UserId.of(creator1)))
                    .thenReturn(Arrays.asList(school1, school2));
            when(schoolRepository.findByCreatedBy(UserId.of(creator2)))
                    .thenReturn(List.of(school3));

            // When
            List<SchoolDTO> result1 = getSchoolService.findByCreatedBy(creator1);
            List<SchoolDTO> result2 = getSchoolService.findByCreatedBy(creator2);

            // Then
            assertThat(result1).hasSize(2);
            assertThat(result1).allMatch(dto -> dto.createdBy().equals(creator1));

            assertThat(result2).hasSize(1);
            assertThat(result2.get(0).createdBy()).isEqualTo(creator2);
        }
    }

    @Nested
    @DisplayName("Find All Tests")
    class FindAllTests {

        @Test
        @DisplayName("Should return all schools regardless of status")
        void shouldReturnAllSchools() {
            // Given
            UUID creatorId = UUID.randomUUID();
            School draftSchool = TestFixtures.reconstituteTestSchool(
                    SchoolId.generate(),
                    "Draft School",
                    SchoolStatus.DRAFT,
                    UserId.of(creatorId)
            );
            School activeSchool = TestFixtures.reconstituteTestSchool(
                    SchoolId.generate(),
                    "Active School",
                    SchoolStatus.ACTIVE,
                    UserId.of(creatorId)
            );
            School suspendedSchool = TestFixtures.reconstituteTestSchool(
                    SchoolId.generate(),
                    "Suspended School",
                    SchoolStatus.SUSPENDED,
                    UserId.of(creatorId)
            );
            School archivedSchool = TestFixtures.reconstituteTestSchool(
                    SchoolId.generate(),
                    "Archived School",
                    SchoolStatus.ARCHIVED,
                    UserId.of(creatorId)
            );

            when(schoolRepository.findAll())
                    .thenReturn(Arrays.asList(draftSchool, activeSchool, suspendedSchool, archivedSchool));

            // When
            List<SchoolDTO> result = getSchoolService.findAll();

            // Then
            assertThat(result).hasSize(4);
            assertThat(result).extracting(SchoolDTO::status)
                    .containsExactlyInAnyOrder(
                            SchoolStatus.DRAFT,
                            SchoolStatus.ACTIVE,
                            SchoolStatus.SUSPENDED,
                            SchoolStatus.ARCHIVED
                    );

            verify(schoolRepository).findAll();
        }

        @Test
        @DisplayName("Should return empty list when no schools exist")
        void shouldReturnEmptyListWhenNoSchools() {
            // Given
            when(schoolRepository.findAll()).thenReturn(List.of());

            // When
            List<SchoolDTO> result = getSchoolService.findAll();

            // Then
            assertThat(result).isEmpty();
            verify(schoolRepository).findAll();
        }

        @Test
        @DisplayName("Should convert all schools to DTOs correctly")
        void shouldConvertAllSchoolsToDTOs() {
            // Given
            UUID creatorId = UUID.randomUUID();
            School school1 = TestFixtures.createTestSchool("School 1", UserId.of(creatorId));
            School school2 = TestFixtures.createTestSchool("School 2", UserId.of(creatorId));
            School school3 = TestFixtures.createTestSchool("School 3", UserId.of(creatorId));

            when(schoolRepository.findAll())
                    .thenReturn(Arrays.asList(school1, school2, school3));

            // When
            List<SchoolDTO> result = getSchoolService.findAll();

            // Then
            assertThat(result).hasSize(3);
            assertThat(result).extracting(SchoolDTO::name)
                    .containsExactlyInAnyOrder("School 1", "School 2", "School 3");
        }
    }
}
