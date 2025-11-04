package com.learning.tracker.usermanagement.api;

import com.learning.tracker.shared.domain.vo.UserId;
import com.learning.tracker.usermanagement.application.dto.CreateUserRequest;
import com.learning.tracker.usermanagement.application.dto.UserDTO;
import com.learning.tracker.usermanagement.application.usecase.AssignRoleUseCase;
import com.learning.tracker.usermanagement.application.usecase.GetUserUseCase;
import com.learning.tracker.usermanagement.application.usecase.RegisterUserUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

/**
 * REST controller for user management operations.
 * <p>
 * Exposes HTTP endpoints for creating, retrieving, and managing users.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final RegisterUserUseCase registerUserUseCase;
    private final GetUserUseCase getUserUseCase;
    private final AssignRoleUseCase assignRoleUseCase;

    public UserController(RegisterUserUseCase registerUserUseCase,
                         GetUserUseCase getUserUseCase,
                         AssignRoleUseCase assignRoleUseCase) {
        this.registerUserUseCase = registerUserUseCase;
        this.getUserUseCase = getUserUseCase;
        this.assignRoleUseCase = assignRoleUseCase;
    }

    /**
     * Registers a new user.
     *
     * @param request the user creation request
     * @return the created user
     */
    @PostMapping
    public ResponseEntity<UserDTO> registerUser(@RequestBody CreateUserRequest request) {
        RegisterUserUseCase.RegisterUserCommand command = new RegisterUserUseCase.RegisterUserCommand(
                request.email(),
                request.firstName(),
                request.lastName(),
                request.password()
        );

        UserId userId = registerUserUseCase.register(command);

        // Fetch the created user
        UserDTO user = getUserUseCase.findById(userId.value())
                .orElseThrow(); // Should always exist since we just created it

        return ResponseEntity
                .created(URI.create("/api/users/" + userId))
                .body(user);
    }

    /**
     * Gets a user by ID.
     *
     * @param id the user identifier
     * @return the user, if found
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable UUID id) {
        return getUserUseCase.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Gets a user by email.
     *
     * @param email the email address
     * @return the user, if found
     */
    @GetMapping("/by-email")
    public ResponseEntity<UserDTO> getUserByEmail(@RequestParam String email) {
        return getUserUseCase.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Gets all active users.
     *
     * @return list of active users
     */
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllActiveUsers() {
        List<UserDTO> users = getUserUseCase.findAllActive();
        return ResponseEntity.ok(users);
    }

    /**
     * Gets all users in a school.
     *
     * @param schoolId the school identifier
     * @return list of users in the school
     */
    @GetMapping("/schools/{schoolId}")
    public ResponseEntity<List<UserDTO>> getUsersBySchool(@PathVariable UUID schoolId) {
        List<UserDTO> users = getUserUseCase.findBySchool(schoolId);
        return ResponseEntity.ok(users);
    }

    /**
     * Assigns a system role to a user.
     *
     * @param userId the user identifier
     * @param command the role assignment command
     * @return no content
     */
    @PutMapping("/{userId}/system-role")
    public ResponseEntity<Void> assignSystemRole(
            @PathVariable UUID userId,
            @RequestBody AssignRoleUseCase.AssignSystemRoleCommand command) {

        // Create command with path variable
        AssignRoleUseCase.AssignSystemRoleCommand fullCommand =
                new AssignRoleUseCase.AssignSystemRoleCommand(userId, command.systemRole());

        assignRoleUseCase.assignSystemRole(fullCommand);
        return ResponseEntity.noContent().build();
    }

    /**
     * Assigns a school role to a user.
     *
     * @param userId the user identifier
     * @param command the school role assignment command
     * @return no content
     */
    @PutMapping("/{userId}/school-role")
    public ResponseEntity<Void> assignSchoolRole(
            @PathVariable UUID userId,
            @RequestBody AssignRoleUseCase.AssignSchoolRoleCommand command) {

        // Create command with path variable
        AssignRoleUseCase.AssignSchoolRoleCommand fullCommand =
                new AssignRoleUseCase.AssignSchoolRoleCommand(
                        userId,
                        command.schoolId(),
                        command.schoolRole()
                );

        assignRoleUseCase.assignSchoolRole(fullCommand);
        return ResponseEntity.noContent().build();
    }

    /**
     * Removes a school role from a user.
     *
     * @param userId the user identifier
     * @param schoolId the school identifier
     * @return no content
     */
    @DeleteMapping("/{userId}/school-role/{schoolId}")
    public ResponseEntity<Void> removeSchoolRole(
            @PathVariable UUID userId,
            @PathVariable UUID schoolId) {

        AssignRoleUseCase.RemoveSchoolRoleCommand command =
                new AssignRoleUseCase.RemoveSchoolRoleCommand(userId, schoolId);

        assignRoleUseCase.removeSchoolRole(command);
        return ResponseEntity.noContent().build();
    }
}
