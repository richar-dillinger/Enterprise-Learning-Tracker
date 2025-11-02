package com.learning.tracker.shared.infrastructure.exception;

/**
 * Exception thrown when a business rule is violated.
 * <p>
 * This exception represents violations of domain-specific business rules
 * that go beyond simple validation. Examples include:
 * <ul>
 *   <li>Cannot enroll in a path that hasn't been published</li>
 *   <li>Cannot complete an activity without completing prerequisites</li>
 *   <li>Cannot assign a role without proper permissions</li>
 * </ul>
 */
public class BusinessRuleViolationException extends DomainException {

    private final String ruleCode;

    /**
     * Constructs a new business rule violation exception.
     *
     * @param message the detail message explaining the rule violation
     */
    public BusinessRuleViolationException(String message) {
        super(message);
        this.ruleCode = null;
    }

    /**
     * Constructs a new business rule violation exception with a rule code.
     *
     * @param ruleCode a code identifying the specific business rule
     * @param message  the detail message explaining the rule violation
     */
    public BusinessRuleViolationException(String ruleCode, String message) {
        super(message);
        this.ruleCode = ruleCode;
    }

    /**
     * Constructs a new business rule violation exception with a cause.
     *
     * @param message the detail message
     * @param cause   the underlying cause
     */
    public BusinessRuleViolationException(String message, Throwable cause) {
        super(message, cause);
        this.ruleCode = null;
    }

    /**
     * Returns the rule code if specified.
     *
     * @return the rule code, or null if not specified
     */
    public String getRuleCode() {
        return ruleCode;
    }
}
