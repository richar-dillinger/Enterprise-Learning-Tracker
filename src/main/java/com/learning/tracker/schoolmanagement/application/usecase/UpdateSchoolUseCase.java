package com.learning.tracker.schoolmanagement.application.usecase;

import com.learning.tracker.schoolmanagement.domain.model.SchoolStatus;

import java.util.UUID;

/**
 * Use case for updating school information and status.
 * <p>
 * This is an inbound port (interface) for school update operations.
 */
public interface UpdateSchoolUseCase {

    /**
     * Updates school information.
     *
     * @param command the update command
     */
    void updateInfo(UpdateSchoolInfoCommand command);

    /**
     * Changes the school's status.
     *
     * @param command the status change command
     */
    void changeStatus(ChangeSchoolStatusCommand command);

    /**
     * Command for updating school information.
     *
     * @param schoolId    the school identifier
     * @param name        the new name
     * @param description the new description
     */
    record UpdateSchoolInfoCommand(
            UUID schoolId,
            String name,
            String description
    ) {
    }

    /**
     * Command for changing school status.
     *
     * @param schoolId  the school identifier
     * @param newStatus the new status
     */
    record ChangeSchoolStatusCommand(
            UUID schoolId,
            SchoolStatus newStatus
    ) {
    }
}
