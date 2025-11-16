package com.learning.tracker.shared;

import com.learning.tracker.shared.domain.vo.Email;
import com.learning.tracker.shared.domain.vo.SchoolId;
import com.learning.tracker.shared.domain.vo.UserId;
import com.learning.tracker.usermanagement.domain.model.SchoolRole;
import com.learning.tracker.usermanagement.domain.model.SystemRole;
import com.learning.tracker.usermanagement.domain.model.User;
import com.learning.tracker.schoolmanagement.domain.model.School;
import com.learning.tracker.schoolmanagement.domain.model.SchoolStatus;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Test fixtures for creating test data.
 * Provides factory methods for creating domain objects with sensible defaults.
 */
public class TestFixtures {

    // User Fixtures
    public static final String TEST_EMAIL = "test@example.com";
    public static final String TEST_FIRST_NAME = "John";
    public static final String TEST_LAST_NAME = "Doe";
    public static final String TEST_PASSWORD = "Password123!";

    public static UserId randomUserId() {
        return UserId.generate();
    }

    public static SchoolId randomSchoolId() {
        return SchoolId.generate();
    }

    public static Email testEmail() {
        return Email.of(TEST_EMAIL);
    }

    public static Email testEmail(String email) {
        return Email.of(email);
    }

    public static User createTestUser() {
        return User.create(
                testEmail(),
                TEST_FIRST_NAME,
                TEST_LAST_NAME
        );
    }

    public static User createTestUser(String email, String firstName, String lastName) {
        return User.create(
                Email.of(email),
                firstName,
                lastName
        );
    }

    public static User createTestUserWithRole(SystemRole systemRole) {
        User user = createTestUser();
        user.assignSystemRole(systemRole);
        return user;
    }

    public static User createTestUserWithSchoolRole(SchoolId schoolId, SchoolRole schoolRole) {
        User user = createTestUser();
        user.assignSchoolRole(schoolId, schoolRole);
        return user;
    }

    public static User reconstituteTestUser(UserId userId, Email email) {
        return User.reconstitute(
                userId,
                email,
                TEST_FIRST_NAME,
                TEST_LAST_NAME,
                SystemRole.USER,
                new HashMap<>(),
                true,
                Instant.now(),
                Instant.now()
        );
    }

    public static User reconstituteTestUser(UserId userId, Email email, SystemRole systemRole) {
        return User.reconstitute(
                userId,
                email,
                TEST_FIRST_NAME,
                TEST_LAST_NAME,
                systemRole,
                new HashMap<>(),
                true,
                Instant.now(),
                Instant.now()
        );
    }

    public static User reconstituteTestUser(
            UserId userId,
            Email email,
            String firstName,
            String lastName,
            SystemRole systemRole,
            Map<SchoolId, SchoolRole> schoolRoles
    ) {
        return User.reconstitute(
                userId,
                email,
                firstName,
                lastName,
                systemRole,
                schoolRoles,
                true,
                Instant.now(),
                Instant.now()
        );
    }

    // School Fixtures
    public static final String TEST_SCHOOL_NAME = "Test School";
    public static final String TEST_SCHOOL_DESCRIPTION = "Test school description";

    public static School createTestSchool(UserId createdBy) {
        return School.create(
                TEST_SCHOOL_NAME,
                TEST_SCHOOL_DESCRIPTION,
                createdBy
        );
    }

    public static School createTestSchool(String name, UserId createdBy) {
        return School.create(
                name,
                TEST_SCHOOL_DESCRIPTION,
                createdBy
        );
    }

    public static School reconstituteTestSchool(SchoolId schoolId, UserId createdBy) {
        return School.reconstitute(
                schoolId,
                TEST_SCHOOL_NAME,
                TEST_SCHOOL_DESCRIPTION,
                SchoolStatus.DRAFT,
                createdBy,
                Instant.now(),
                Instant.now()
        );
    }

    public static School reconstituteTestSchool(
            SchoolId schoolId,
            String name,
            SchoolStatus status,
            UserId createdBy
    ) {
        return School.reconstitute(
                schoolId,
                name,
                TEST_SCHOOL_DESCRIPTION,
                status,
                createdBy,
                Instant.now(),
                Instant.now()
        );
    }
}
