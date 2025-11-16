package com.learning.tracker.schoolmanagement.application.service;

import com.learning.tracker.shared.domain.vo.SchoolId;
import com.learning.tracker.shared.domain.vo.UserId;
import com.learning.tracker.shared.infrastructure.exception.DuplicateEntityException;
import com.learning.tracker.shared.infrastructure.exception.ValidationException;
import com.learning.tracker.shared.TestFixtures;
import com.learning.tracker.schoolmanagement.application.usecase.CreateSchoolUseCase.CreateSchoolCommand;
import com.learning.tracker.schoolmanagement.domain.event.SchoolCreatedEvent;
import com.learning.tracker.schoolmanagement.domain.model.School;
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

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateSchoolService Tests")
class CreateSchoolServiceTest {

    @Mock
    private SchoolRepository schoolRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private CreateSchoolService createSchoolService;

    @Captor
    private ArgumentCaptor<School> schoolCaptor;

    @Captor
    private ArgumentCaptor<SchoolCreatedEvent> eventCaptor;

    private CreateSchoolCommand validCommand;
    private UUID creatorId;

    @BeforeEach
    void setUp() {
        creatorId = UUID.randomUUID();
        validCommand = new CreateSchoolCommand(
                TestFixtures.TEST_SCHOOL_NAME,
                TestFixtures.TEST_SCHOOL_DESCRIPTION,
                creatorId
        );
    }

    @Nested
    @DisplayName("Successful Creation Tests")
    class SuccessfulCreationTests {

        @Test
        @DisplayName("Should successfully create a new school")
        void shouldCreateNewSchool() {
            // Given
            School mockSchool = TestFixtures.createTestSchool(UserId.of(creatorId));
            when(schoolRepository.existsByName(anyString())).thenReturn(false);
            when(schoolRepository.save(any(School.class))).thenReturn(mockSchool);

            // When
            SchoolId result = createSchoolService.create(validCommand);

            // Then
            assertThat(result).isNotNull();
            verify(schoolRepository).existsByName(TestFixtures.TEST_SCHOOL_NAME);
            verify(schoolRepository).save(schoolCaptor.capture());
            verify(eventPublisher).publishEvent(eventCaptor.capture());

            School savedSchool = schoolCaptor.getValue();
            assertThat(savedSchool.getName()).isEqualTo(TestFixtures.TEST_SCHOOL_NAME);
            assertThat(savedSchool.getDescription()).isEqualTo(TestFixtures.TEST_SCHOOL_DESCRIPTION);
            assertThat(savedSchool.getCreatedBy()).isEqualTo(UserId.of(creatorId));
        }

        @Test
        @DisplayName("Should create school in DRAFT status")
        void shouldCreateSchoolInDraftStatus() {
            // Given
            when(schoolRepository.existsByName(anyString())).thenReturn(false);
            when(schoolRepository.save(any(School.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // When
            createSchoolService.create(validCommand);

            // Then
            verify(schoolRepository).save(schoolCaptor.capture());
            School savedSchool = schoolCaptor.getValue();
            assertThat(savedSchool.getStatus().name()).isEqualTo("DRAFT");
        }

        @Test
        @DisplayName("Should publish SchoolCreatedEvent after creation")
        void shouldPublishEvent() {
            // Given
            School mockSchool = TestFixtures.createTestSchool(UserId.of(creatorId));
            when(schoolRepository.existsByName(anyString())).thenReturn(false);
            when(schoolRepository.save(any(School.class))).thenReturn(mockSchool);

            // When
            createSchoolService.create(validCommand);

            // Then
            verify(eventPublisher).publishEvent(eventCaptor.capture());
            SchoolCreatedEvent event = eventCaptor.getValue();
            assertThat(event.schoolId()).isEqualTo(mockSchool.getId());
            assertThat(event.name()).isEqualTo(mockSchool.getName());
            assertThat(event.createdBy()).isEqualTo(mockSchool.getCreatedBy());
        }

        @Test
        @DisplayName("Should handle school without description")
        void shouldHandleSchoolWithoutDescription() {
            // Given
            CreateSchoolCommand commandWithoutDescription = new CreateSchoolCommand(
                    TestFixtures.TEST_SCHOOL_NAME,
                    null,
                    creatorId
            );
            when(schoolRepository.existsByName(anyString())).thenReturn(false);
            when(schoolRepository.save(any(School.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // When
            SchoolId result = createSchoolService.create(commandWithoutDescription);

            // Then
            assertThat(result).isNotNull();
            verify(schoolRepository).save(schoolCaptor.capture());
            School savedSchool = schoolCaptor.getValue();
            assertThat(savedSchool.getDescription()).isNotNull();
        }
    }

    @Nested
    @DisplayName("Validation Tests")
    class ValidationTests {

        @Test
        @DisplayName("Should throw ValidationException when command is null")
        void shouldFailWhenCommandIsNull() {
            // When/Then
            assertThatThrownBy(() -> createSchoolService.create(null))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Create school command cannot be null");

            verify(schoolRepository, never()).save(any());
            verify(eventPublisher, never()).publishEvent(any());
        }

        @Test
        @DisplayName("Should throw ValidationException when name is null")
        void shouldFailWhenNameIsNull() {
            // Given
            CreateSchoolCommand command = new CreateSchoolCommand(
                    null,
                    TestFixtures.TEST_SCHOOL_DESCRIPTION,
                    creatorId
            );

            // When/Then
            assertThatThrownBy(() -> createSchoolService.create(command))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("School name is required");

            verify(schoolRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw ValidationException when name is blank")
        void shouldFailWhenNameIsBlank() {
            // Given
            CreateSchoolCommand command = new CreateSchoolCommand(
                    "   ",
                    TestFixtures.TEST_SCHOOL_DESCRIPTION,
                    creatorId
            );

            // When/Then
            assertThatThrownBy(() -> createSchoolService.create(command))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("School name is required");

            verify(schoolRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw ValidationException when name is empty string")
        void shouldFailWhenNameIsEmpty() {
            // Given
            CreateSchoolCommand command = new CreateSchoolCommand(
                    "",
                    TestFixtures.TEST_SCHOOL_DESCRIPTION,
                    creatorId
            );

            // When/Then
            assertThatThrownBy(() -> createSchoolService.create(command))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("School name is required");

            verify(schoolRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw ValidationException when createdBy is null")
        void shouldFailWhenCreatedByIsNull() {
            // Given
            CreateSchoolCommand command = new CreateSchoolCommand(
                    TestFixtures.TEST_SCHOOL_NAME,
                    TestFixtures.TEST_SCHOOL_DESCRIPTION,
                    null
            );

            // When/Then
            assertThatThrownBy(() -> createSchoolService.create(command))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Creator is required");

            verify(schoolRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Duplicate School Tests")
    class DuplicateSchoolTests {

        @Test
        @DisplayName("Should throw DuplicateEntityException when school name already exists")
        void shouldFailWhenSchoolNameExists() {
            // Given
            when(schoolRepository.existsByName(anyString())).thenReturn(true);

            // When/Then
            assertThatThrownBy(() -> createSchoolService.create(validCommand))
                    .isInstanceOf(DuplicateEntityException.class)
                    .hasMessageContaining("School")
                    .hasMessageContaining("name");

            verify(schoolRepository).existsByName(TestFixtures.TEST_SCHOOL_NAME);
            verify(schoolRepository, never()).save(any());
            verify(eventPublisher, never()).publishEvent(any());
        }

        @Test
        @DisplayName("Should check for duplicate name before saving")
        void shouldCheckDuplicateBeforeSaving() {
            // Given
            when(schoolRepository.existsByName(anyString())).thenReturn(false);
            when(schoolRepository.save(any(School.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            // When
            createSchoolService.create(validCommand);

            // Then - verify order of operations
            var inOrder = inOrder(schoolRepository);
            inOrder.verify(schoolRepository).existsByName(TestFixtures.TEST_SCHOOL_NAME);
            inOrder.verify(schoolRepository).save(any(School.class));
        }
    }

    @Nested
    @DisplayName("Creator User Tests")
    class CreatorUserTests {

        @Test
        @DisplayName("Should associate school with creator user")
        void shouldAssociateSchoolWithCreator() {
            // Given
            UUID creatorUserId = UUID.randomUUID();
            CreateSchoolCommand command = new CreateSchoolCommand(
                    "New School",
                    "Description",
                    creatorUserId
            );

            when(schoolRepository.existsByName(anyString())).thenReturn(false);
            when(schoolRepository.save(any(School.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            // When
            createSchoolService.create(command);

            // Then
            verify(schoolRepository).save(schoolCaptor.capture());
            School savedSchool = schoolCaptor.getValue();
            assertThat(savedSchool.getCreatedBy().value()).isEqualTo(creatorUserId);
        }

        @Test
        @DisplayName("Should include creator in published event")
        void shouldIncludeCreatorInEvent() {
            // Given
            School mockSchool = TestFixtures.createTestSchool(UserId.of(creatorId));
            when(schoolRepository.existsByName(anyString())).thenReturn(false);
            when(schoolRepository.save(any(School.class))).thenReturn(mockSchool);

            // When
            createSchoolService.create(validCommand);

            // Then
            verify(eventPublisher).publishEvent(eventCaptor.capture());
            SchoolCreatedEvent event = eventCaptor.getValue();
            assertThat(event.createdBy()).isEqualTo(UserId.of(creatorId));
        }
    }

    @Nested
    @DisplayName("Return Value Tests")
    class ReturnValueTests {

        @Test
        @DisplayName("Should return SchoolId of created school")
        void shouldReturnSchoolId() {
            // Given
            School mockSchool = TestFixtures.createTestSchool(UserId.of(creatorId));
            when(schoolRepository.existsByName(anyString())).thenReturn(false);
            when(schoolRepository.save(any(School.class))).thenReturn(mockSchool);

            // When
            SchoolId result = createSchoolService.create(validCommand);

            // Then
            assertThat(result).isNotNull();
            assertThat(result).isEqualTo(mockSchool.getId());
        }

        @Test
        @DisplayName("Should return valid SchoolId that can be used for queries")
        void shouldReturnValidSchoolId() {
            // Given
            School mockSchool = TestFixtures.createTestSchool(UserId.of(creatorId));
            when(schoolRepository.existsByName(anyString())).thenReturn(false);
            when(schoolRepository.save(any(School.class))).thenReturn(mockSchool);

            // When
            SchoolId result = createSchoolService.create(validCommand);

            // Then
            assertThat(result.value()).isNotNull();
            assertThatCode(() -> UUID.fromString(result.value().toString()))
                    .doesNotThrowAnyException();
        }
    }
}
