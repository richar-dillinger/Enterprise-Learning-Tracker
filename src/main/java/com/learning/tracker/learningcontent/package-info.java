/**
 * Learning Content bounded context.
 * <p>
 * This module is responsible for:
 * <ul>
 *   <li>Creating and managing learning paths</li>
 *   <li>Defining activities and resources</li>
 *   <li>Publishing learning content</li>
 *   <li>Managing content structure and dependencies</li>
 * </ul>
 * <p>
 * Dependencies:
 * <ul>
 *   <li>School Management - learning paths are organized by schools</li>
 *   <li>User Management - for content creators and tutors</li>
 * </ul>
 * <p>
 * Public API:
 * <ul>
 *   <li>{@link com.learning.tracker.learningcontent.api} - REST controllers</li>
 *   <li>{@link com.learning.tracker.learningcontent.application.usecase} - Use case interfaces</li>
 *   <li>{@link com.learning.tracker.learningcontent.domain.event} - Domain events</li>
 * </ul>
 */
@org.springframework.modulith.ApplicationModule(
    displayName = "Learning Content",
    allowedDependencies = {"shared", "usermanagement", "schoolmanagement"}
)
@org.springframework.lang.NonNullApi
package com.learning.tracker.learningcontent;
