/**
 * School Management bounded context.
 * <p>
 * This module is responsible for:
 * <ul>
 *   <li>School creation and management</li>
 *   <li>School configuration and settings</li>
 *   <li>School lifecycle management</li>
 * </ul>
 * <p>
 * Dependencies:
 * <ul>
 *   <li>User Management - for school administrators and staff</li>
 * </ul>
 * <p>
 * Public API:
 * <ul>
 *   <li>{@link com.learning.tracker.schoolmanagement.api} - REST controllers</li>
 *   <li>{@link com.learning.tracker.schoolmanagement.application.usecase} - Use case interfaces</li>
 *   <li>{@link com.learning.tracker.schoolmanagement.domain.event} - Domain events</li>
 * </ul>
 */
@org.springframework.modulith.ApplicationModule(
    displayName = "School Management",
    allowedDependencies = {"shared", "usermanagement"}
)
@org.springframework.lang.NonNullApi
package com.learning.tracker.schoolmanagement;
