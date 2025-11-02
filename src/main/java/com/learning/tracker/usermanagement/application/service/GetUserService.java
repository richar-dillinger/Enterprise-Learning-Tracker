package com.learning.tracker.usermanagement.application.service;

import com.learning.tracker.shared.domain.vo.Email;
import com.learning.tracker.shared.domain.vo.SchoolId;
import com.learning.tracker.shared.domain.vo.UserId;
import com.learning.tracker.usermanagement.application.dto.UserDTO;
import com.learning.tracker.usermanagement.application.usecase.GetUserUseCase;
import com.learning.tracker.usermanagement.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Application service for retrieving user information.
 * <p>
 * This service implements the GetUserUseCase and handles
 * querying user data from the repository.
 */
@Service
@Transactional(readOnly = true)
public class GetUserService implements GetUserUseCase {

    private final UserRepository userRepository;

    public GetUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<UserDTO> findById(UUID userId) {
        return userRepository.findById(UserId.of(userId))
                .map(UserDTO::from);
    }

    @Override
    public Optional<UserDTO> findByEmail(String email) {
        return userRepository.findByEmail(Email.of(email))
                .map(UserDTO::from);
    }

    @Override
    public List<UserDTO> findBySchool(UUID schoolId) {
        return userRepository.findBySchool(SchoolId.of(schoolId)).stream()
                .map(UserDTO::from)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> findAllActive() {
        return userRepository.findAllActive().stream()
                .map(UserDTO::from)
                .collect(Collectors.toList());
    }
}
