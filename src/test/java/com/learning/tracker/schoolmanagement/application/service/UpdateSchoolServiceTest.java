package com.learning.tracker.schoolmanagement.application.service;

import com.learning.tracker.shared.domain.vo.SchoolId;
import com.learning.tracker.shared.domain.vo.UserId;
import com.learning.tracker.shared.infrastructure.exception.EntityNotFoundException;
import com.learning.tracker.shared.infrastructure.exception.ValidationException;
import com.learning.tracker.shared.TestFixtures;
import com.learning.tracker.schoolmanagement.application.usecase.UpdateSchoolUseCase.*;
import com.learning.tracker.schoolmanagement.domain.event.SchoolStatusChangedEvent;
import com.learning.tracker.schoolmanagement.domain.model.School;
import com.learning.tracker.schoolmanagement.domain.model.SchoolStatus;
import com.learning.tracker.schoolmanagement.domain.repository.SchoolRepository;
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

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UpdateSchoolService Tests")
class UpdateSchoolServiceTest {

    @Mock
    private SchoolRepository schoolRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private UpdateSchoolService updateSchoolService;

    @Captor
    private ArgumentCaptor<School> schoolCaptor;

    @Captor
    private ArgumentCaptor<SchoolStatusChangedEvent> eventCaptor;

    private UUID schoolId;
    private UUID creatorId;

    @BeforeEach
    void setUp() {
        schoolId = UUID.randomUUID();
        creatorId = UUID.randomUUID();
    }

    @Nested
    @DisplayName("Update Info Tests")
    class UpdateInfoTests {

        @Test
        @DisplayName("Should successfully update school information")
        void shouldUpdateSchoolInfo() {
            // Given
            School mockSchool = TestFixtures.reconstituteTestSchool(
                    SchoolId.of(schoolId),
                    UserId.of(creatorId)
            );
            UpdateSchoolInfoCommand command = new UpdateSchoolInfoCommand(
                    schoolId,
                    "Updated School Name",
                    "Updated description"
            );

            when(schoolRepository.findById(any(SchoolId.class)))
                    .thenReturn(Optional.of(mockSchool));
            when(schoolRepository.save(any(School.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            // When
            updateSchoolService.updateInfo(command);

            // Then
            verify(schoolRepository).findById(SchoolId.of(schoolId));
            verify(schoolRepository).save(schoolCaptor.capture());

            School savedSchool = schoolCaptor.getValue();
            assertThat(savedSchool.getName()).isEqualTo("Updated School Name");
            assertThat(savedSchool.getDescription()).isEqualTo("Updated description");
        }

        @Test
        @DisplayName("Should throw EntityNotFoundException when school does not exist")
        void shouldThrowExceptionWhenSchoolNotFound() {
            // Given
            UpdateSchoolInfoCommand command = new UpdateSchoolInfoCommand(
                    schoolId,
                    "New Name",
                    "New Description"
            );

            when(schoolRepository.findById(any(SchoolId.class)))
                    .thenReturn(Optional.empty());

            // When/Then
            assertThatThrownBy(() -> updateSchoolService.updateInfo(command))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("School");

            verify(schoolRepository).findById(SchoolId.of(schoolId));
            verify(schoolRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw ValidationException when command is null")
        void shouldFailWhenCommandIsNull() {
            // When/Then
            assertThatThrownBy(() -> updateSchoolService.updateInfo(null))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Update command cannot be null");

            verify(schoolRepository, never()).findById(any());
            verify(schoolRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw ValidationException when schoolId is null")
        void shouldFailWhenSchoolIdIsNull() {
            // Given
            UpdateSchoolInfoCommand command = new UpdateSchoolInfoCommand(
                    null,
                    "School Name",
                    "Description"
            );

            // When/Then
            assertThatThrownBy(() -> updateSchoolService.updateInfo(command))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("School ID is required");

            verify(schoolRepository, never()).findById(any());
        }

        @Test
        @DisplayName("Should throw ValidationException when name is null")
        void shouldFailWhenNameIsNull() {
            // Given
            UpdateSchoolInfoCommand command = new UpdateSchoolInfoCommand(
                    schoolId,
                    null,
                    "Description"
            );

            // When/Then
            assertThatThrownBy(() -> updateSchoolService.updateInfo(command))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("School name is required");

            verify(schoolRepository, never()).findById(any());
        }

        @Test
        @DisplayName("Should throw ValidationException when name is blank")
        void shouldFailWhenNameIsBlank() {
            // Given
            UpdateSchoolInfoCommand command = new UpdateSchoolInfoCommand(
                    schoolId,
                    "   ",
                    "Description"
            );

            // When/Then
            assertThatThrownBy(() -> updateSchoolService.updateInfo(command))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("School name is required");

            verify(schoolRepository, never()).findById(any());
        }

        @Test
        @DisplayName("Should update school with null description")
        void shouldUpdateSchoolWithNullDescription() {
            // Given
            School mockSchool = TestFixtures.reconstituteTestSchool(
                    SchoolId.of(schoolId),
                    UserId.of(creatorId)
            );
            UpdateSchoolInfoCommand command = new UpdateSchoolInfoCommand(
                    schoolId,
                    "Updated Name",
                    null
            );

            when(schoolRepository.findById(any(SchoolId.class)))
                    .thenReturn(Optional.of(mockSchool));
            when(schoolRepository.save(any(School.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            // When
            updateSchoolService.updateInfo(command);

            // Then
            verify(schoolRepository).save(schoolCaptor.capture());
            School savedSchool = schoolCaptor.getValue();
            assertThat(savedSchool.getName()).isEqualTo("Updated Name");
            assertThat(savedSchool.getDescription()).isNotNull();
        }
    }

    @Nested
    @DisplayName("Change Status Tests")
    class ChangeStatusTests {

        @Test
        @DisplayName("Should activate a draft school")
        void shouldActivateDraftSchool() {
            // Given
            School mockSchool = TestFixtures.reconstituteTestSchool(
                    SchoolId.of(schoolId),
                    "School Name",
                    SchoolStatus.DRAFT,
                    UserId.of(creatorId)
            );
            ChangeSchoolStatusCommand command = new ChangeSchoolStatusCommand(
                    schoolId,
                    SchoolStatus.ACTIVE
            );

            when(schoolRepository.findById(any(SchoolId.class)))
                    .thenReturn(Optional.of(mockSchool));
            when(schoolRepository.save(any(School.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            // When
            updateSchoolService.changeStatus(command);

            // Then
            verify(schoolRepository).save(schoolCaptor.capture());
            School savedSchool = schoolCaptor.getValue();
            assertThat(savedSchool.getStatus()).isEqualTo(SchoolStatus.ACTIVE);

            verify(eventPublisher).publishEvent(eventCaptor.capture());
            SchoolStatusChangedEvent event = eventCaptor.getValue();
            assertThat(event.previousStatus()).isEqualTo(SchoolStatus.DRAFT);
            assertThat(event.newStatus()).isEqualTo(SchoolStatus.ACTIVE);
        }

        @Test
        @DisplayName("Should suspend an active school")
        void shouldSuspendActiveSchool() {
            // Given
            School mockSchool = TestFixtures.reconstituteTestSchool(
                    SchoolId.of(schoolId),
                    "School Name",
                    SchoolStatus.ACTIVE,
                    UserId.of(creatorId)
            );
            ChangeSchoolStatusCommand command = new ChangeSchoolStatusCommand(
                    schoolId,
                    SchoolStatus.SUSPENDED
            );

            when(schoolRepository.findById(any(SchoolId.class)))
                    .thenReturn(Optional.of(mockSchool));
            when(schoolRepository.save(any(School.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            // When
            updateSchoolService.changeStatus(command);

            // Then
            verify(schoolRepository).save(schoolCaptor.capture());
            School savedSchool = schoolCaptor.getValue();
            assertThat(savedSchool.getStatus()).isEqualTo(SchoolStatus.SUSPENDED);

            verify(eventPublisher).publishEvent(eventCaptor.capture());
            SchoolStatusChangedEvent event = eventCaptor.getValue();
            assertThat(event.previousStatus()).isEqualTo(SchoolStatus.ACTIVE);
            assertThat(event.newStatus()).isEqualTo(SchoolStatus.SUSPENDED);
        }

        @Test
        @DisplayName("Should archive a school")
        void shouldArchiveSchool() {
            // Given
            School mockSchool = TestFixtures.reconstituteTestSchool(
                    SchoolId.of(schoolId),
                    "School Name",
                    SchoolStatus.ACTIVE,
                    UserId.of(creatorId)
            );
            ChangeSchoolStatusCommand command = new ChangeSchoolStatusCommand(
                    schoolId,
                    SchoolStatus.ARCHIVED
            );

            when(schoolRepository.findById(any(SchoolId.class)))
                    .thenReturn(Optional.of(mockSchool));
            when(schoolRepository.save(any(School.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            // When
            updateSchoolService.changeStatus(command);

            // Then
            verify(schoolRepository).save(schoolCaptor.capture());
            School savedSchool = schoolCaptor.getValue();
            assertThat(savedSchool.getStatus()).isEqualTo(SchoolStatus.ARCHIVED);

            verify(eventPublisher).publishEvent(eventCaptor.capture());
            SchoolStatusChangedEvent event = eventCaptor.getValue();
            assertThat(event.newStatus()).isEqualTo(SchoolStatus.ARCHIVED);
        }

        @Test
        @DisplayName("Should throw ValidationException when trying to revert to DRAFT")
        void shouldFailWhenRevertingToDraft() {
            // Given
            School mockSchool = TestFixtures.reconstituteTestSchool(
                    SchoolId.of(schoolId),
                    "School Name",
                    SchoolStatus.ACTIVE,
                    UserId.of(creatorId)
            );
            ChangeSchoolStatusCommand command = new ChangeSchoolStatusCommand(
                    schoolId,
                    SchoolStatus.DRAFT
            );

            when(schoolRepository.findById(any(SchoolId.class)))
                    .thenReturn(Optional.of(mockSchool));

            // When/Then
            assertThatThrownBy(() -> updateSchoolService.changeStatus(command))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Cannot revert school to DRAFT status");

            verify(schoolRepository, never()).save(any());
            verify(eventPublisher, never()).publishEvent(any());
        }

        @Test
        @DisplayName("Should throw EntityNotFoundException when school does not exist")
        void shouldThrowExceptionWhenSchoolNotFound() {
            // Given
            ChangeSchoolStatusCommand command = new ChangeSchoolStatusCommand(
                    schoolId,
                    SchoolStatus.ACTIVE
            );

            when(schoolRepository.findById(any(SchoolId.class)))
                    .thenReturn(Optional.empty());

            // When/Then
            assertThatThrownBy(() -> updateSchoolService.changeStatus(command))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("School");

            verify(schoolRepository).findById(SchoolId.of(schoolId));
            verify(schoolRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw ValidationException when command is null")
        void shouldFailWhenCommandIsNull() {
            // When/Then
            assertThatThrownBy(() -> updateSchoolService.changeStatus(null))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Status change command cannot be null");

            verify(schoolRepository, never()).findById(any());
        }

        @Test
        @DisplayName("Should throw ValidationException when schoolId is null")
        void shouldFailWhenSchoolIdIsNull() {
            // Given
            ChangeSchoolStatusCommand command = new ChangeSchoolStatusCommand(
                    null,
                    SchoolStatus.ACTIVE
            );

            // When/Then
            assertThatThrownBy(() -> updateSchoolService.changeStatus(command))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("School ID is required");

            verify(schoolRepository, never()).findById(any());
        }

        @Test
        @DisplayName("Should throw ValidationException when newStatus is null")
        void shouldFailWhenNewStatusIsNull() {
            // Given
            ChangeSchoolStatusCommand command = new ChangeSchoolStatusCommand(
                    schoolId,
                    null
            );

            // When/Then
            assertThatThrownBy(() -> updateSchoolService.changeStatus(command))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("New status is required");

            verify(schoolRepository, never()).findById(any());
        }

        @Test
        @DisplayName("Should not publish event when status does not change")
        void shouldNotPublishEventWhenStatusUnchanged() {
            // Given
            School mockSchool = TestFixtures.reconstituteTestSchool(
                    SchoolId.of(schoolId),
                    "School Name",
                    SchoolStatus.ACTIVE,
                    UserId.of(creatorId)
            );
            // Manually set the status to ACTIVE after creation
            mockSchool.activate();

            ChangeSchoolStatusCommand command = new ChangeSchoolStatusCommand(
                    schoolId,
                    SchoolStatus.ACTIVE
            );

            when(schoolRepository.findById(any(SchoolId.class)))
                    .thenReturn(Optional.of(mockSchool));
            when(schoolRepository.save(any(School.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            // When
            updateSchoolService.changeStatus(command);

            // Then
            verify(schoolRepository).save(any(School.class));
            verify(eventPublisher, never()).publishEvent(any());
        }

        @Test
        @DisplayName("Should publish event with correct school ID")
        void shouldPublishEventWithCorrectSchoolId() {
            // Given
            School mockSchool = TestFixtures.reconstituteTestSchool(
                    SchoolId.of(schoolId),
                    "School Name",
                    SchoolStatus.DRAFT,
                    UserId.of(creatorId)
            );
            ChangeSchoolStatusCommand command = new ChangeSchoolStatusCommand(
                    schoolId,
                    SchoolStatus.ACTIVE
            );

            when(schoolRepository.findById(any(SchoolId.class)))
                    .thenReturn(Optional.of(mockSchool));
            when(schoolRepository.save(any(School.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            // When
            updateSchoolService.changeStatus(command);

            // Then
            verify(eventPublisher).publishEvent(eventCaptor.capture());
            SchoolStatusChangedEvent event = eventCaptor.getValue();
            assertThat(event.schoolId()).isEqualTo(SchoolId.of(schoolId));
        }
    }

    @Nested
    @DisplayName("Domain Rules Tests")
    class DomainRulesTests {

        @Test
        @DisplayName("Should throw exception when suspending non-active school")
        void shouldFailWhenSuspendingNonActiveSchool() {
            // Given
            School mockSchool = TestFixtures.reconstituteTestSchool(
                    SchoolId.of(schoolId),
                    "School Name",
                    SchoolStatus.DRAFT,
                    UserId.of(creatorId)
            );
            ChangeSchoolStatusCommand command = new ChangeSchoolStatusCommand(
                    schoolId,
                    SchoolStatus.SUSPENDED
            );

            when(schoolRepository.findById(any(SchoolId.class)))
                    .thenReturn(Optional.of(mockSchool));

            // When/Then
            assertThatThrownBy(() -> updateSchoolService.changeStatus(command))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("Can only suspend active schools");

            verify(schoolRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when activating archived school")
        void shouldFailWhenActivatingArchivedSchool() {
            // Given
            School mockSchool = TestFixtures.reconstituteTestSchool(
                    SchoolId.of(schoolId),
                    "School Name",
                    SchoolStatus.ARCHIVED,
                    UserId.of(creatorId)
            );
            ChangeSchoolStatusCommand command = new ChangeSchoolStatusCommand(
                    schoolId,
                    SchoolStatus.ACTIVE
            );

            when(schoolRepository.findById(any(SchoolId.class)))
                    .thenReturn(Optional.of(mockSchool));

            // When/Then
            assertThatThrownBy(() -> updateSchoolService.changeStatus(command))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("Cannot activate an archived school");

            verify(schoolRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should allow archiving from any status")
        void shouldAllowArchivingFromAnyStatus() {
            // Given
            School draftSchool = TestFixtures.reconstituteTestSchool(
                    SchoolId.of(schoolId),
                    "School Name",
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

            when(schoolRepository.save(any(School.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            // When/Then - All should succeed
            when(schoolRepository.findById(any(SchoolId.class)))
                    .thenReturn(Optional.of(draftSchool));
            assertThatCode(() -> updateSchoolService.changeStatus(
                    new ChangeSchoolStatusCommand(schoolId, SchoolStatus.ARCHIVED)
            )).doesNotThrowAnyException();

            when(schoolRepository.findById(any(SchoolId.class)))
                    .thenReturn(Optional.of(activeSchool));
            assertThatCode(() -> updateSchoolService.changeStatus(
                    new ChangeSchoolStatusCommand(schoolId, SchoolStatus.ARCHIVED)
            )).doesNotThrowAnyException();

            when(schoolRepository.findById(any(SchoolId.class)))
                    .thenReturn(Optional.of(suspendedSchool));
            assertThatCode(() -> updateSchoolService.changeStatus(
                    new ChangeSchoolStatusCommand(schoolId, SchoolStatus.ARCHIVED)
            )).doesNotThrowAnyException();
        }
    }
}
