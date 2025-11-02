package com.learning.tracker.learningcontent.infrastructure.persistence;

import com.learning.tracker.shared.domain.vo.PathId;
import com.learning.tracker.shared.domain.vo.SchoolId;
import com.learning.tracker.shared.domain.vo.UserId;
import com.learning.tracker.learningcontent.domain.model.PathStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LearningPathJpaRepository extends JpaRepository<LearningPathEntity, PathId> {

    List<LearningPathEntity> findBySchoolId(SchoolId schoolId);

    List<LearningPathEntity> findBySchoolIdAndStatus(SchoolId schoolId, PathStatus status);

    List<LearningPathEntity> findByCreatedBy(UserId userId);
}
