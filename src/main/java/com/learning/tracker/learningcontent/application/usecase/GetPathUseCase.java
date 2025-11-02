package com.learning.tracker.learningcontent.application.usecase;

import com.learning.tracker.learningcontent.application.dto.LearningPathDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GetPathUseCase {

    Optional<LearningPathDTO> findById(UUID pathId);

    List<LearningPathDTO> findBySchool(UUID schoolId);

    List<LearningPathDTO> findPublishedBySchool(UUID schoolId);

    List<LearningPathDTO> findByCreatedBy(UUID userId);
}
