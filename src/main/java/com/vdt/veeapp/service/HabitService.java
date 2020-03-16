package com.vdt.veeapp.service;

import com.vdt.veeapp.service.dto.HabitDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.vdt.veeapp.domain.Habit}.
 */
public interface HabitService {

    /**
     * Save a habit.
     *
     * @param habitDTO the entity to save.
     * @return the persisted entity.
     */
    HabitDTO save(HabitDTO habitDTO);

    /**
     * Get all the habits.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<HabitDTO> findAll(Pageable pageable);

    /**
     * Get the "id" habit.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<HabitDTO> findOne(Long id);

    /**
     * Delete the "id" habit.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
