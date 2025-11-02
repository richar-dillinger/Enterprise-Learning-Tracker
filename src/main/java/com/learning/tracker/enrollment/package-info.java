/**
 * Enrollment bounded context.
 * <p>
 * This module is responsible for:
 * <ul>
 *   <li>Enrolling users in learning paths</li>
 *   <li>Managing enrollment lifecycle (active, completed, cancelled)</li>
 *   <li>Assignment of users to paths by managers/tutors</li>
 *   <li>Self-enrollment by students</li>
 * </ul>
 * <p>
 * Dependencies:
 * <ul>
 *   <li>User Management - for student and tutor information</li>
 *   <li>Learning Content - for available learning paths</li>
 *   <li>School Management - for school context</li>
 * </ul>
 * <p>
 * Public API:
 * <ul>
 *   <li>{@link com.learning.tracker.enrollment.api} - REST controllers</li>
 *   <li>{@link com.learning.tracker.enrollment.application.usecase} - Use case interfaces</li>
 *   <li>{@link com.learning.tracker.enrollment.domain.event} - Domain events</li>
 * </ul>
 */
@org.springframework.modulith.ApplicationModule(
    displayName = "Enrollment",
    allowedDependencies = {"shared", "usermanagement", "schoolmanagement", "learningcontent"}
)
@org.springframework.lang.NonNullApi
package com.learning.tracker.enrollment;
