package com.learning.tracker.usermanagement.domain.model;

import com.learning.tracker.shared.domain.vo.Email;
import com.learning.tracker.shared.domain.vo.SchoolId;
import com.learning.tracker.shared.domain.vo.UserId;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * User aggregate root representing a system user.
 * <p>
 * A user can have:
 * <ul>
 *   <li>One system-wide role (e.g., ADMIN, PLATFORM_MANAGER, USER)</li>
 *   <li>Multiple school-specific roles (e.g., TUTOR in School A, STUDENT in School B)</li>
 * </ul>
 * <p>
 * This is an aggregate root that maintains consistency of user data and roles.
 */
public class User {

    private final UserId id;
    private Email email;
    private String firstName;
    private String lastName;
    private SystemRole systemRole;
    private final Map<SchoolId, SchoolRole> schoolRoles;
    private boolean active;
    private final Instant createdAt;
    private Instant updatedAt;

    /**
     * Private constructor for reconstruction from persistence.
     */
    private User(UserId id, Email email, String firstName, String lastName,
                 SystemRole systemRole, Map<SchoolId, SchoolRole> schoolRoles,
                 boolean active, Instant createdAt, Instant updatedAt) {
        this.id = Objects.requireNonNull(id, "User id cannot be null");
        this.email = Objects.requireNonNull(email, "Email cannot be null");
        this.firstName = validateAndTrimName(firstName, "First name");
        this.lastName = validateAndTrimName(lastName, "Last name");
        this.systemRole = Objects.requireNonNull(systemRole, "System role cannot be null");
        this.schoolRoles = new HashMap<>(schoolRoles);
        this.active = active;
        this.createdAt = Objects.requireNonNull(createdAt, "Created at cannot be null");
        this.updatedAt = Objects.requireNonNull(updatedAt, "Updated at cannot be null");
    }

    /**
     * Creates a new User with basic information.
     * New users are created with USER system role and active status.
     *
     * @param email     the user's email
     * @param firstName the first name
     * @param lastName  the last name
     * @return a new User instance
     */
    public static User create(Email email, String firstName, String lastName) {
        Instant now = Instant.now();
        return new User(
                UserId.generate(),
                email,
                firstName,
                lastName,
                SystemRole.USER,
                new HashMap<>(),
                true,
                now,
                now
        );
    }

    /**
     * Reconstructs a User from persistence.
     *
     * @param id          the user id
     * @param email       the email
     * @param firstName   the first name
     * @param lastName    the last name
     * @param systemRole  the system role
     * @param schoolRoles the school roles map
     * @param active      the active status
     * @param createdAt   the creation timestamp
     * @param updatedAt   the last update timestamp
     * @return a User instance
     */
    public static User reconstitute(UserId id, Email email, String firstName, String lastName,
                                    SystemRole systemRole, Map<SchoolId, SchoolRole> schoolRoles,
                                    boolean active, Instant createdAt, Instant updatedAt) {
        return new User(id, email, firstName, lastName, systemRole, schoolRoles,
                active, createdAt, updatedAt);
    }

    /**
     * Assigns a system role to this user.
     *
     * @param role the system role to assign
     */
    public void assignSystemRole(SystemRole role) {
        Objects.requireNonNull(role, "System role cannot be null");
        this.systemRole = role;
        this.updatedAt = Instant.now();
    }

    /**
     * Assigns a school role to this user for a specific school.
     *
     * @param schoolId the school identifier
     * @param role     the school role to assign
     */
    public void assignSchoolRole(SchoolId schoolId, SchoolRole role) {
        Objects.requireNonNull(schoolId, "School id cannot be null");
        Objects.requireNonNull(role, "School role cannot be null");
        this.schoolRoles.put(schoolId, role);
        this.updatedAt = Instant.now();
    }

    /**
     * Removes a school role from this user.
     *
     * @param schoolId the school identifier
     * @return true if the role was removed, false if it didn't exist
     */
    public boolean removeSchoolRole(SchoolId schoolId) {
        Objects.requireNonNull(schoolId, "School id cannot be null");
        boolean removed = this.schoolRoles.remove(schoolId) != null;
        if (removed) {
            this.updatedAt = Instant.now();
        }
        return removed;
    }

    /**
     * Gets the school role for a specific school.
     *
     * @param schoolId the school identifier
     * @return the school role, if present
     */
    public Optional<SchoolRole> getSchoolRole(SchoolId schoolId) {
        Objects.requireNonNull(schoolId, "School id cannot be null");
        return Optional.ofNullable(schoolRoles.get(schoolId));
    }

    /**
     * Checks if the user has a role in the specified school.
     *
     * @param schoolId the school identifier
     * @return true if the user has a role in the school
     */
    public boolean hasRoleInSchool(SchoolId schoolId) {
        return schoolRoles.containsKey(schoolId);
    }

    /**
     * Checks if the user is a student in the specified school.
     *
     * @param schoolId the school identifier
     * @return true if the user is a student in the school
     */
    public boolean isStudentInSchool(SchoolId schoolId) {
        return getSchoolRole(schoolId)
                .map(SchoolRole::isStudent)
                .orElse(false);
    }

    /**
     * Updates the user's email.
     *
     * @param email the new email
     */
    public void updateEmail(Email email) {
        this.email = Objects.requireNonNull(email, "Email cannot be null");
        this.updatedAt = Instant.now();
    }

    /**
     * Updates the user's name.
     *
     * @param firstName the first name
     * @param lastName  the last name
     */
    public void updateName(String firstName, String lastName) {
        this.firstName = validateAndTrimName(firstName, "First name");
        this.lastName = validateAndTrimName(lastName, "Last name");
        this.updatedAt = Instant.now();
    }

    /**
     * Deactivates this user.
     */
    public void deactivate() {
        this.active = false;
        this.updatedAt = Instant.now();
    }

    /**
     * Activates this user.
     */
    public void activate() {
        this.active = true;
        this.updatedAt = Instant.now();
    }

    /**
     * Returns the full name of the user.
     *
     * @return the full name
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    // Getters
    public UserId getId() {
        return id;
    }

    public Email getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public SystemRole getSystemRole() {
        return systemRole;
    }

    public Map<SchoolId, SchoolRole> getSchoolRoles() {
        return Collections.unmodifiableMap(schoolRoles);
    }

    public boolean isActive() {
        return active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    // Helper methods
    private String validateAndTrimName(String name, String fieldName) {
        Objects.requireNonNull(name, fieldName + " cannot be null");
        String trimmed = name.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be blank");
        }
        if (trimmed.length() > 100) {
            throw new IllegalArgumentException(fieldName + " cannot exceed 100 characters");
        }
        return trimmed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email=" + email +
                ", fullName='" + getFullName() + '\'' +
                ", systemRole=" + systemRole +
                ", active=" + active +
                '}';
    }
}
