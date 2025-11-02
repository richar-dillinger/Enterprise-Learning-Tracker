package com.learning.tracker.learningcontent.application.usecase;

import com.learning.tracker.shared.domain.vo.PathId;

import java.util.UUID;

public interface CreatePathUseCase {

    PathId create(CreatePathCommand command);

    record CreatePathCommand(
            UUID schoolId,
            String title,
            String description,
            UUID createdBy
    ) {
    }
}
