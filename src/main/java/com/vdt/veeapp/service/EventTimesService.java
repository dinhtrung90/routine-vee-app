package com.vdt.veeapp.service;

import com.vdt.veeapp.service.dto.EventTimesDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.vdt.veeapp.domain.EventTimes}.
 */
public interface EventTimesService {

    /**
     * Save a eventTimes.
     *
     * @param eventTimesDTO the entity to save.
     * @return the persisted entity.
     */
    EventTimesDTO save(EventTimesDTO eventTimesDTO);

    /**
     * Get all the eventTimes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EventTimesDTO> findAll(Pageable pageable);

    /**
     * Get the "id" eventTimes.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EventTimesDTO> findOne(Long id);

    /**
     * Delete the "id" eventTimes.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
