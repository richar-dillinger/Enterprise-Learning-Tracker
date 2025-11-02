package com.learning.tracker.learningcontent.api;

import com.learning.tracker.shared.domain.vo.PathId;
import com.learning.tracker.learningcontent.application.dto.LearningPathDTO;
import com.learning.tracker.learningcontent.application.usecase.CreatePathUseCase;
import com.learning.tracker.learningcontent.application.usecase.GetPathUseCase;
import com.learning.tracker.learningcontent.application.usecase.ManagePathUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

/**
 * REST controller for learning path management.
 */
@RestController
@RequestMapping("/api/learning-paths")
public class LearningPathController {

    private final CreatePathUseCase createPathUseCase;
    private final GetPathUseCase getPathUseCase;
    private final ManagePathUseCase managePathUseCase;

    public LearningPathController(CreatePathUseCase createPathUseCase,
                                  GetPathUseCase getPathUseCase,
                                  ManagePathUseCase managePathUseCase) {
        this.createPathUseCase = createPathUseCase;
        this.getPathUseCase = getPathUseCase;
        this.managePathUseCase = managePathUseCase;
    }

    @PostMapping
    public ResponseEntity<LearningPathDTO> createPath(@RequestBody CreatePathUseCase.CreatePathCommand command) {
        PathId pathId = createPathUseCase.create(command);

        LearningPathDTO path = getPathUseCase.findById(pathId.value())
                .orElseThrow();

        return ResponseEntity
                .created(URI.create("/api/learning-paths/" + pathId))
                .body(path);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LearningPathDTO> getPath(@PathVariable UUID id) {
        return getPathUseCase.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/schools/{schoolId}")
    public ResponseEntity<List<LearningPathDTO>> getPathsBySchool(@PathVariable UUID schoolId) {
        List<LearningPathDTO> paths = getPathUseCase.findBySchool(schoolId);
        return ResponseEntity.ok(paths);
    }

    @GetMapping("/schools/{schoolId}/published")
    public ResponseEntity<List<LearningPathDTO>> getPublishedPathsBySchool(@PathVariable UUID schoolId) {
        List<LearningPathDTO> paths = getPathUseCase.findPublishedBySchool(schoolId);
        return ResponseEntity.ok(paths);
    }

    @GetMapping("/created-by/{userId}")
    public ResponseEntity<List<LearningPathDTO>> getPathsByCreator(@PathVariable UUID userId) {
        List<LearningPathDTO> paths = getPathUseCase.findByCreatedBy(userId);
        return ResponseEntity.ok(paths);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePath(
            @PathVariable UUID id,
            @RequestBody ManagePathUseCase.UpdatePathInfoCommand command) {

        ManagePathUseCase.UpdatePathInfoCommand fullCommand =
                new ManagePathUseCase.UpdatePathInfoCommand(id, command.title(), command.description());

        managePathUseCase.updateInfo(fullCommand);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/publish")
    public ResponseEntity<Void> publishPath(@PathVariable UUID id) {
        managePathUseCase.publish(new ManagePathUseCase.PublishPathCommand(id));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/archive")
    public ResponseEntity<Void> archivePath(@PathVariable UUID id) {
        managePathUseCase.archive(new ManagePathUseCase.ArchivePathCommand(id));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/activities")
    public ResponseEntity<Void> addActivity(
            @PathVariable UUID id,
            @RequestBody ManagePathUseCase.AddActivityCommand command) {

        ManagePathUseCase.AddActivityCommand fullCommand =
                new ManagePathUseCase.AddActivityCommand(
                        id,
                        command.title(),
                        command.description(),
                        command.type(),
                        command.displayOrder(),
                        command.estimatedMinutes()
                );

        managePathUseCase.addActivity(fullCommand);
        return ResponseEntity.noContent().build();
    }
}
