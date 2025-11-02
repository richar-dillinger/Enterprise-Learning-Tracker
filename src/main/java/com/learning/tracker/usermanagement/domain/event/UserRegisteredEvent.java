package com.learning.tracker.usermanagement.domain.event;

import com.learning.tracker.shared.domain.event.DomainEvent;
import com.learning.tracker.shared.domain.vo.Email;
import com.learning.tracker.shared.domain.vo.UserId;
import com.learning.tracker.usermanagement.domain.model.SystemRole;

import java.time.Instant;

/**
 * Domain event published when a new user is registered in the system.
 * <p>
 * Other modules can listen to this event to perform actions such as:
 * <ul>
 *   <li>Creating a student profile</li>
 *   <li>Sending welcome emails</li>
 *   <li>Initializing user preferences</li>
 * </ul>
 */
public record UserRegisteredEvent(
        UserId userId,
        Email email,
        String firstName,
        String lastName,
        SystemRole systemRole,
        Instant occurredOn
) implements DomainEvent {

    /**
     * Creates a new UserRegisteredEvent.
     *
     * @param userId     the new user's identifier
     * @param email      the user's email
     * @param firstName  the user's first name
     * @param lastName   the user's last name
     * @param systemRole the user's system role
     */
    public UserRegisteredEvent(UserId userId, Email email, String firstName,
                               String lastName, SystemRole systemRole) {
        this(userId, email, firstName, lastName, systemRole, Instant.now());
    }
}
