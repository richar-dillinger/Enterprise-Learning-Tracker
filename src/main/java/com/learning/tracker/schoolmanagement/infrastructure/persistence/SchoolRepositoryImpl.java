package com.learning.tracker.schoolmanagement.infrastructure.persistence;

import com.learning.tracker.shared.domain.vo.SchoolId;
import com.learning.tracker.shared.domain.vo.UserId;
import com.learning.tracker.schoolmanagement.domain.model.School;
import com.learning.tracker.schoolmanagement.domain.model.SchoolStatus;
import com.learning.tracker.schoolmanagement.domain.repository.SchoolRepository;
import com.learning.tracker.schoolmanagement.infrastructure.mapper.SchoolMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of SchoolRepository using Spring Data JPA.
 * <p>
 * This adapter implements the domain repository interface
 * and delegates to the Spring Data JPA repository.
 */
@Repository
public class SchoolRepositoryImpl implements SchoolRepository {

    private final SchoolJpaRepository jpaRepository;
    private final SchoolMapper mapper;

    public SchoolRepositoryImpl(SchoolJpaRepository jpaRepository, SchoolMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public School save(School school) {
        SchoolEntity entity = mapper.toEntity(school);
        SchoolEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<School> findById(SchoolId id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<School> findByName(String name) {
        return jpaRepository.findByName(name)
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsByName(String name) {
        return jpaRepository.existsByName(name);
    }

    @Override
    public List<School> findByStatus(SchoolStatus status) {
        return jpaRepository.findByStatus(status).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<School> findByCreatedBy(UserId userId) {
        return jpaRepository.findByCreatedBy(userId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<School> findAllActive() {
        return jpaRepository.findAllActive().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<School> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(SchoolId id) {
        jpaRepository.deleteById(id);
    }
}
