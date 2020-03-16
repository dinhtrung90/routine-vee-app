package com.vdt.veeapp.service;

import com.vdt.veeapp.service.dto.ReminderDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.vdt.veeapp.domain.Reminder}.
 */
public interface ReminderService {

    /**
     * Save a reminder.
     *
     * @param reminderDTO the entity to save.
     * @return the persisted entity.
     */
    ReminderDTO save(ReminderDTO reminderDTO);

    /**
     * Get all the reminders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ReminderDTO> findAll(Pageable pageable);

    /**
     * Get the "id" reminder.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ReminderDTO> findOne(Long id);

    /**
     * Delete the "id" reminder.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
