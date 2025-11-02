package com.learning.tracker.usermanagement.infrastructure.persistence;

import com.learning.tracker.shared.domain.vo.Email;
import com.learning.tracker.shared.domain.vo.SchoolId;
import com.learning.tracker.shared.domain.vo.UserId;
import com.learning.tracker.usermanagement.domain.model.SchoolRole;
import com.learning.tracker.usermanagement.domain.model.User;
import com.learning.tracker.usermanagement.domain.repository.UserRepository;
import com.learning.tracker.usermanagement.infrastructure.mapper.UserMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of UserRepository using Spring Data JPA.
 * <p>
 * This adapter implements the domain repository interface
 * and delegates to the Spring Data JPA repository.
 */
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository jpaRepository;
    private final UserMapper mapper;

    public UserRepositoryImpl(UserJpaRepository jpaRepository, UserMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public User save(User user) {
        UserEntity entity = mapper.toEntity(user);
        UserEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<User> findById(UserId id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        return jpaRepository.findByEmail(email)
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsByEmail(Email email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public List<User> findBySchoolRole(SchoolId schoolId, SchoolRole role) {
        return jpaRepository.findBySchool(schoolId.value()).stream()
                .map(mapper::toDomain)
                .filter(user -> user.getSchoolRole(schoolId)
                        .map(r -> r == role)
                        .orElse(false))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findBySchool(SchoolId schoolId) {
        return jpaRepository.findBySchool(schoolId.value()).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findAllActive() {
        return jpaRepository.findByActiveTrue().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UserId id) {
        jpaRepository.deleteById(id);
    }
}
