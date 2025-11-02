package com.learning.tracker.schoolmanagement.api;

import com.learning.tracker.shared.domain.vo.SchoolId;
import com.learning.tracker.schoolmanagement.application.dto.CreateSchoolRequest;
import com.learning.tracker.schoolmanagement.application.dto.SchoolDTO;
import com.learning.tracker.schoolmanagement.application.dto.UpdateSchoolRequest;
import com.learning.tracker.schoolmanagement.application.usecase.CreateSchoolUseCase;
import com.learning.tracker.schoolmanagement.application.usecase.GetSchoolUseCase;
import com.learning.tracker.schoolmanagement.application.usecase.UpdateSchoolUseCase;
import com.learning.tracker.schoolmanagement.domain.model.SchoolStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

/**
 * REST controller for school management operations.
 * <p>
 * Exposes HTTP endpoints for creating, retrieving, and managing schools.
 */
@RestController
@RequestMapping("/api/schools")
public class SchoolController {

    private final CreateSchoolUseCase createSchoolUseCase;
    private final GetSchoolUseCase getSchoolUseCase;
    private final UpdateSchoolUseCase updateSchoolUseCase;

    public SchoolController(CreateSchoolUseCase createSchoolUseCase,
                           GetSchoolUseCase getSchoolUseCase,
                           UpdateSchoolUseCase updateSchoolUseCase) {
        this.createSchoolUseCase = createSchoolUseCase;
        this.getSchoolUseCase = getSchoolUseCase;
        this.updateSchoolUseCase = updateSchoolUseCase;
    }

    /**
     * Creates a new school.
     *
     * @param request the school creation request
     * @return the created school
     */
    @PostMapping
    public ResponseEntity<SchoolDTO> createSchool(@RequestBody CreateSchoolRequest request) {
        CreateSchoolUseCase.CreateSchoolCommand command = new CreateSchoolUseCase.CreateSchoolCommand(
                request.name(),
                request.description(),
                request.createdBy()
        );

        SchoolId schoolId = createSchoolUseCase.create(command);

        // Fetch the created school
        SchoolDTO school = getSchoolUseCase.findById(schoolId.value())
                .orElseThrow(); // Should always exist since we just created it

        return ResponseEntity
                .created(URI.create("/api/schools/" + schoolId))
                .body(school);
    }

    /**
     * Gets a school by ID.
     *
     * @param id the school identifier
     * @return the school, if found
     */
    @GetMapping("/{id}")
    public ResponseEntity<SchoolDTO> getSchool(@PathVariable UUID id) {
        return getSchoolUseCase.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Gets a school by name.
     *
     * @param name the school name
     * @return the school, if found
     */
    @GetMapping("/by-name")
    public ResponseEntity<SchoolDTO> getSchoolByName(@RequestParam String name) {
        return getSchoolUseCase.findByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Gets all schools.
     *
     * @return list of all schools
     */
    @GetMapping
    public ResponseEntity<List<SchoolDTO>> getAllSchools() {
        List<SchoolDTO> schools = getSchoolUseCase.findAll();
        return ResponseEntity.ok(schools);
    }

    /**
     * Gets all active schools.
     *
     * @return list of active schools
     */
    @GetMapping("/active")
    public ResponseEntity<List<SchoolDTO>> getActiveSchools() {
        List<SchoolDTO> schools = getSchoolUseCase.findAllActive();
        return ResponseEntity.ok(schools);
    }

    /**
     * Gets schools by status.
     *
     * @param status the school status
     * @return list of schools with the specified status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<SchoolDTO>> getSchoolsByStatus(@PathVariable SchoolStatus status) {
        List<SchoolDTO> schools = getSchoolUseCase.findByStatus(status);
        return ResponseEntity.ok(schools);
    }

    /**
     * Gets schools created by a specific user.
     *
     * @param userId the user identifier
     * @return list of schools
     */
    @GetMapping("/created-by/{userId}")
    public ResponseEntity<List<SchoolDTO>> getSchoolsByCreator(@PathVariable UUID userId) {
        List<SchoolDTO> schools = getSchoolUseCase.findByCreatedBy(userId);
        return ResponseEntity.ok(schools);
    }

    /**
     * Updates school information.
     *
     * @param id      the school identifier
     * @param request the update request
     * @return no content
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateSchool(
            @PathVariable UUID id,
            @RequestBody UpdateSchoolRequest request) {

        UpdateSchoolUseCase.UpdateSchoolInfoCommand command =
                new UpdateSchoolUseCase.UpdateSchoolInfoCommand(
                        id,
                        request.name(),
                        request.description()
                );

        updateSchoolUseCase.updateInfo(command);
        return ResponseEntity.noContent().build();
    }

    /**
     * Changes school status.
     *
     * @param id        the school identifier
     * @param newStatus the new status
     * @return no content
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<Void> changeSchoolStatus(
            @PathVariable UUID id,
            @RequestParam SchoolStatus newStatus) {

        UpdateSchoolUseCase.ChangeSchoolStatusCommand command =
                new UpdateSchoolUseCase.ChangeSchoolStatusCommand(id, newStatus);

        updateSchoolUseCase.changeStatus(command);
        return ResponseEntity.noContent().build();
    }
}
