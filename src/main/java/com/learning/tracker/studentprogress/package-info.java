/**
 * Student Progress bounded context.
 * <p>
 * This module is responsible for:
 * <ul>
 *   <li>Tracking student progress through learning paths</li>
 *   <li>Recording activity completions and achievements</li>
 *   <li>Awarding badges and certifications</li>
 *   <li>Maintaining student profiles and learning history</li>
 *   <li>Calculating progress metrics and statistics</li>
 * </ul>
 * <p>
 * This module primarily reacts to events from other modules:
 * <ul>
 *   <li>Listens to enrollment events to create student profiles</li>
 *   <li>Listens to learning content events to track progress</li>
 *   <li>Publishes achievement events for gamification</li>
 * </ul>
 * <p>
 * Dependencies:
 * <ul>
 *   <li>User Management - for student information</li>
 *   <li>Enrollment - for enrollment status</li>
 *   <li>Learning Content - for path structure</li>
 * </ul>
 * <p>
 * Public API:
 * <ul>
 *   <li>{@link com.learning.tracker.studentprogress.api} - REST controllers</li>
 *   <li>{@link com.learning.tracker.studentprogress.application.usecase} - Use case interfaces</li>
 *   <li>{@link com.learning.tracker.studentprogress.domain.event} - Domain events</li>
 * </ul>
 */
@org.springframework.modulith.ApplicationModule(
    displayName = "Student Progress",
    allowedDependencies = {"shared", "usermanagement", "enrollment", "learningcontent"}
)
@org.springframework.lang.NonNullApi
package com.learning.tracker.studentprogress;
