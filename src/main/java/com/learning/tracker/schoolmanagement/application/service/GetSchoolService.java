package com.learning.tracker.schoolmanagement.application.service;

import com.learning.tracker.shared.domain.vo.SchoolId;
import com.learning.tracker.shared.domain.vo.UserId;
import com.learning.tracker.schoolmanagement.application.dto.SchoolDTO;
import com.learning.tracker.schoolmanagement.application.usecase.GetSchoolUseCase;
import com.learning.tracker.schoolmanagement.domain.model.SchoolStatus;
import com.learning.tracker.schoolmanagement.domain.repository.SchoolRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Application service for retrieving school information.
 * <p>
 * Implements the GetSchoolUseCase and handles querying school data.
 */
@Service
@Transactional(readOnly = true)
public class GetSchoolService implements GetSchoolUseCase {

    private final SchoolRepository schoolRepository;

    public GetSchoolService(SchoolRepository schoolRepository) {
        this.schoolRepository = schoolRepository;
    }

    @Override
    public Optional<SchoolDTO> findById(UUID schoolId) {
        return schoolRepository.findById(SchoolId.of(schoolId))
                .map(SchoolDTO::from);
    }

    @Override
    public Optional<SchoolDTO> findByName(String name) {
        return schoolRepository.findByName(name)
                .map(SchoolDTO::from);
    }

    @Override
    public List<SchoolDTO> findByStatus(SchoolStatus status) {
        return schoolRepository.findByStatus(status).stream()
                .map(SchoolDTO::from)
                .collect(Collectors.toList());
    }

    @Override
    public List<SchoolDTO> findAllActive() {
        return schoolRepository.findAllActive().stream()
                .map(SchoolDTO::from)
                .collect(Collectors.toList());
    }

    @Override
    public List<SchoolDTO> findByCreatedBy(UUID userId) {
        return schoolRepository.findByCreatedBy(UserId.of(userId)).stream()
                .map(SchoolDTO::from)
                .collect(Collectors.toList());
    }

    @Override
    public List<SchoolDTO> findAll() {
        return schoolRepository.findAll().stream()
                .map(SchoolDTO::from)
                .collect(Collectors.toList());
    }
}
