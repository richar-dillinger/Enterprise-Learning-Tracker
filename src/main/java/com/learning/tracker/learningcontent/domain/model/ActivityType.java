package com.learning.tracker.learningcontent.domain.model;

/**
 * Types of learning activities.
 */
public enum ActivityType {

    /**
     * Video-based learning activity.
     */
    VIDEO("Video", "Watch a video"),

    /**
     * Reading material or article.
     */
    READING("Reading", "Read an article or document"),

    /**
     * Interactive quiz or assessment.
     */
    QUIZ("Quiz", "Complete a quiz"),

    /**
     * Hands-on coding exercise.
     */
    EXERCISE("Exercise", "Complete a coding exercise"),

    /**
     * Project or assignment.
     */
    PROJECT("Project", "Complete a project"),

    /**
     * Live or recorded lecture.
     */
    LECTURE("Lecture", "Attend a lecture"),

    /**
     * Discussion or forum participation.
     */
    DISCUSSION("Discussion", "Participate in discussion");

    private final String displayName;
    private final String description;

    ActivityType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}
