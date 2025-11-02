package com.learning.tracker.learningcontent.domain.repository;

import com.learning.tracker.shared.domain.vo.PathId;
import com.learning.tracker.shared.domain.vo.SchoolId;
import com.learning.tracker.shared.domain.vo.UserId;
import com.learning.tracker.learningcontent.domain.model.LearningPath;
import com.learning.tracker.learningcontent.domain.model.PathStatus;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for LearningPath aggregate.
 * <p>
 * Defines data access operations for learning paths.
 */
public interface LearningPathRepository {

    /**
     * Saves a learning path.
     *
     * @param path the learning path to save
     * @return the saved learning path
     */
    LearningPath save(LearningPath path);

    /**
     * Finds a learning path by its identifier.
     *
     * @param id the path id
     * @return the learning path, if found
     */
    Optional<LearningPath> findById(PathId id);

    /**
     * Finds all learning paths for a school.
     *
     * @param schoolId the school identifier
     * @return list of learning paths
     */
    List<LearningPath> findBySchool(SchoolId schoolId);

    /**
     * Finds learning paths by school and status.
     *
     * @param schoolId the school identifier
     * @param status   the path status
     * @return list of learning paths
     */
    List<LearningPath> findBySchoolAndStatus(SchoolId schoolId, PathStatus status);

    /**
     * Finds all published learning paths for a school.
     *
     * @param schoolId the school identifier
     * @return list of published paths
     */
    List<LearningPath> findPublishedBySchool(SchoolId schoolId);

    /**
     * Finds learning paths created by a specific user.
     *
     * @param userId the user identifier
     * @return list of learning paths
     */
    List<LearningPath> findByCreatedBy(UserId userId);

    /**
     * Deletes a learning path by its identifier.
     *
     * @param id the path id
     */
    void deleteById(PathId id);
}
