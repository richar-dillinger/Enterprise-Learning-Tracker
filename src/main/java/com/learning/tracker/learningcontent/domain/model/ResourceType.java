package com.learning.tracker.learningcontent.domain.model;

/**
 * Types of learning resources.
 */
public enum ResourceType {

    /**
     * External URL or link.
     */
    URL("URL", "External web link"),

    /**
     * PDF document.
     */
    PDF("PDF", "PDF document"),

    /**
     * Video file or embedded video.
     */
    VIDEO("Video", "Video content"),

    /**
     * Text or markdown content.
     */
    TEXT("Text", "Text content"),

    /**
     * Code repository or snippet.
     */
    CODE("Code", "Code repository"),

    /**
     * Downloadable file.
     */
    FILE("File", "Downloadable file");

    private final String displayName;
    private final String description;

    ResourceType(String displayName, String description) {
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
