/**
 * Shared Kernel module containing common domain concepts, value objects, and infrastructure utilities
 * shared across all bounded contexts.
 * <p>
 * This module provides:
 * <ul>
 *   <li>Common value objects (UserId, SchoolId, PathId, Email, etc.)</li>
 *   <li>Base domain events and event publishing infrastructure</li>
 *   <li>Shared exception types</li>
 *   <li>Common utility classes</li>
 * </ul>
 * <p>
 * All classes in this module are PUBLIC and can be used by any other module.
 */
@org.springframework.modulith.ApplicationModule(
    displayName = "Shared Kernel",
    allowedDependencies = {}
)
@org.springframework.lang.NonNullApi
package com.learning.tracker.shared;
