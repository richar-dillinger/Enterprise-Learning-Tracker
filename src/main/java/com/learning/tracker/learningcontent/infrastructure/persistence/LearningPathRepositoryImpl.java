package com.learning.tracker.learningcontent.infrastructure.persistence;

import com.learning.tracker.shared.domain.vo.PathId;
import com.learning.tracker.shared.domain.vo.SchoolId;
import com.learning.tracker.shared.domain.vo.UserId;
import com.learning.tracker.learningcontent.domain.model.LearningPath;
import com.learning.tracker.learningcontent.domain.model.PathStatus;
import com.learning.tracker.learningcontent.domain.repository.LearningPathRepository;
import com.learning.tracker.learningcontent.infrastructure.mapper.LearningPathMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class LearningPathRepositoryImpl implements LearningPathRepository {

    private final LearningPathJpaRepository jpaRepository;
    private final LearningPathMapper mapper;

    public LearningPathRepositoryImpl(LearningPathJpaRepository jpaRepository,
                                      LearningPathMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public LearningPath save(LearningPath path) {
        LearningPathEntity entity = mapper.toEntity(path);
        LearningPathEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<LearningPath> findById(PathId id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<LearningPath> findBySchool(SchoolId schoolId) {
        return jpaRepository.findBySchoolId(schoolId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<LearningPath> findBySchoolAndStatus(SchoolId schoolId, PathStatus status) {
        return jpaRepository.findBySchoolIdAndStatus(schoolId, status).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<LearningPath> findPublishedBySchool(SchoolId schoolId) {
        return findBySchoolAndStatus(schoolId, PathStatus.PUBLISHED);
    }

    @Override
    public List<LearningPath> findByCreatedBy(UserId userId) {
        return jpaRepository.findByCreatedBy(userId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(PathId id) {
        jpaRepository.deleteById(id);
    }
}
