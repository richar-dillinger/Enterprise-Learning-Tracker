/**
 * User Management bounded context.
 * <p>
 * This module is responsible for:
 * <ul>
 *   <li>User registration and profile management</li>
 *   <li>System and school role assignments</li>
 *   <li>User authentication and authorization</li>
 *   <li>Integration with Keycloak for identity management</li>
 * </ul>
 * <p>
 * This is a CORE module that other modules depend on for user identity.
 * <p>
 * Public API:
 * <ul>
 *   <li>{@link com.learning.tracker.usermanagement.api} - REST controllers</li>
 *   <li>{@link com.learning.tracker.usermanagement.application.usecase} - Use case interfaces</li>
 *   <li>{@link com.learning.tracker.usermanagement.domain.event} - Domain events</li>
 * </ul>
 */
@org.springframework.modulith.ApplicationModule(
    displayName = "User Management",
    allowedDependencies = {"shared"}
)
@org.springframework.lang.NonNullApi
package com.learning.tracker.usermanagement;
