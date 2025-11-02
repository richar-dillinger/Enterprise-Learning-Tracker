package com.learning.tracker.schoolmanagement.infrastructure.persistence;

import com.learning.tracker.shared.domain.vo.SchoolId;
import com.learning.tracker.shared.domain.vo.UserId;
import com.learning.tracker.schoolmanagement.domain.model.SchoolStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for SchoolEntity.
 * <p>
 * Provides database access methods for school entities.
 */
@Repository
public interface SchoolJpaRepository extends JpaRepository<SchoolEntity, SchoolId> {

    /**
     * Finds a school by name.
     *
     * @param name the school name
     * @return the school entity, if found
     */
    Optional<SchoolEntity> findByName(String name);

    /**
     * Checks if a school with the given name exists.
     *
     * @param name the name
     * @return true if exists
     */
    boolean existsByName(String name);

    /**
     * Finds all schools with a specific status.
     *
     * @param status the school status
     * @return list of school entities
     */
    List<SchoolEntity> findByStatus(SchoolStatus status);

    /**
     * Finds all schools created by a specific user.
     *
     * @param createdBy the user identifier
     * @return list of school entities
     */
    List<SchoolEntity> findByCreatedBy(UserId createdBy);

    /**
     * Finds all active schools.
     *
     * @return list of active school entities
     */
    default List<SchoolEntity> findAllActive() {
        return findByStatus(SchoolStatus.ACTIVE);
    }
}
