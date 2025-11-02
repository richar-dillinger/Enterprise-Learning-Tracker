package com.learning.tracker.learningcontent.application.service;

import com.learning.tracker.shared.domain.vo.PathId;
import com.learning.tracker.shared.domain.vo.SchoolId;
import com.learning.tracker.shared.domain.vo.UserId;
import com.learning.tracker.learningcontent.application.dto.LearningPathDTO;
import com.learning.tracker.learningcontent.application.usecase.GetPathUseCase;
import com.learning.tracker.learningcontent.domain.repository.LearningPathRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class GetPathService implements GetPathUseCase {

    private final LearningPathRepository repository;

    public GetPathService(LearningPathRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<LearningPathDTO> findById(UUID pathId) {
        return repository.findById(PathId.of(pathId))
                .map(LearningPathDTO::from);
    }

    @Override
    public List<LearningPathDTO> findBySchool(UUID schoolId) {
        return repository.findBySchool(SchoolId.of(schoolId)).stream()
                .map(LearningPathDTO::from)
                .collect(Collectors.toList());
    }

    @Override
    public List<LearningPathDTO> findPublishedBySchool(UUID schoolId) {
        return repository.findPublishedBySchool(SchoolId.of(schoolId)).stream()
                .map(LearningPathDTO::from)
                .collect(Collectors.toList());
    }

    @Override
    public List<LearningPathDTO> findByCreatedBy(UUID userId) {
        return repository.findByCreatedBy(UserId.of(userId)).stream()
                .map(LearningPathDTO::from)
                .collect(Collectors.toList());
    }
}
