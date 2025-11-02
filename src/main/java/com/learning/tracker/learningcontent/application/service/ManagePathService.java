package com.learning.tracker.learningcontent.application.service;

import com.learning.tracker.shared.domain.vo.PathId;
import com.learning.tracker.shared.infrastructure.exception.EntityNotFoundException;
import com.learning.tracker.learningcontent.application.usecase.ManagePathUseCase;
import com.learning.tracker.learningcontent.domain.event.PathPublishedEvent;
import com.learning.tracker.learningcontent.domain.model.Activity;
import com.learning.tracker.learningcontent.domain.model.LearningPath;
import com.learning.tracker.learningcontent.domain.repository.LearningPathRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ManagePathService implements ManagePathUseCase {

    private final LearningPathRepository repository;
    private final ApplicationEventPublisher eventPublisher;

    public ManagePathService(LearningPathRepository repository,
                            ApplicationEventPublisher eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void updateInfo(UpdatePathInfoCommand command) {
        PathId pathId = PathId.of(command.pathId());
        LearningPath path = repository.findById(pathId)
                .orElseThrow(() -> new EntityNotFoundException("LearningPath", pathId));

        path.updateInfo(command.title(), command.description());
        repository.save(path);
    }

    @Override
    public void publish(PublishPathCommand command) {
        PathId pathId = PathId.of(command.pathId());
        LearningPath path = repository.findById(pathId)
                .orElseThrow(() -> new EntityNotFoundException("LearningPath", pathId));

        path.publish();
        repository.save(path);

        eventPublisher.publishEvent(new PathPublishedEvent(
                path.getId(),
                path.getSchoolId(),
                path.getTitle()
        ));
    }

    @Override
    public void archive(ArchivePathCommand command) {
        PathId pathId = PathId.of(command.pathId());
        LearningPath path = repository.findById(pathId)
                .orElseThrow(() -> new EntityNotFoundException("LearningPath", pathId));

        path.archive();
        repository.save(path);
    }

    @Override
    public void addActivity(AddActivityCommand command) {
        PathId pathId = PathId.of(command.pathId());
        LearningPath path = repository.findById(pathId)
                .orElseThrow(() -> new EntityNotFoundException("LearningPath", pathId));

        Activity activity = Activity.create(
                command.title(),
                command.description(),
                command.type(),
                command.displayOrder(),
                command.estimatedMinutes()
        );

        path.addActivity(activity);
        repository.save(path);
    }
}
