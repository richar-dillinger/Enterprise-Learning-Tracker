package com.learning.tracker.usermanagement.infrastructure.persistence;

import com.learning.tracker.shared.domain.vo.Email;
import com.learning.tracker.shared.domain.vo.SchoolId;
import com.learning.tracker.shared.domain.vo.UserId;
import com.learning.tracker.usermanagement.domain.model.SchoolRole;
import com.learning.tracker.usermanagement.domain.model.SystemRole;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * JPA entity for User persistence.
 * <p>
 * This is the infrastructure representation of the User domain model.
 * It uses JPA annotations for mapping to the database.
 */
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    private UserId id;

    @Column(nullable = false, unique = true, length = 254)
    private Email email;

    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(nullable = false, length = 100)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private SystemRole systemRole;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "user_school_roles",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @MapKeyColumn(name = "school_id")
    @Column(name = "school_role")
    @Enumerated(EnumType.STRING)
    private Map<SchoolId, SchoolRole> schoolRoles = new HashMap<>();

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    /**
     * Default constructor for JPA.
     */
    protected UserEntity() {
    }

    /**
     * Constructor for creating a new entity.
     */
    public UserEntity(UserId id, Email email, String firstName, String lastName,
                      SystemRole systemRole, Map<SchoolId, SchoolRole> schoolRoles,
                      boolean active, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.systemRole = systemRole;
        this.schoolRoles = new HashMap<>(schoolRoles);
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and setters
    public UserId getId() {
        return id;
    }

    public void setId(UserId id) {
        this.id = id;
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public SystemRole getSystemRole() {
        return systemRole;
    }

    public void setSystemRole(SystemRole systemRole) {
        this.systemRole = systemRole;
    }

    public Map<SchoolId, SchoolRole> getSchoolRoles() {
        return schoolRoles;
    }

    public void setSchoolRoles(Map<SchoolId, SchoolRole> schoolRoles) {
        this.schoolRoles = schoolRoles;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
