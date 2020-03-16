package com.vdt.veeapp.service.impl;

import com.vdt.veeapp.service.EventTimesService;
import com.vdt.veeapp.domain.EventTimes;
import com.vdt.veeapp.repository.EventTimesRepository;
import com.vdt.veeapp.service.dto.EventTimesDTO;
import com.vdt.veeapp.service.mapper.EventTimesMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link EventTimes}.
 */
@Service
@Transactional
public class EventTimesServiceImpl implements EventTimesService {

    private final Logger log = LoggerFactory.getLogger(EventTimesServiceImpl.class);

    private final EventTimesRepository eventTimesRepository;

    private final EventTimesMapper eventTimesMapper;

    public EventTimesServiceImpl(EventTimesRepository eventTimesRepository, EventTimesMapper eventTimesMapper) {
        this.eventTimesRepository = eventTimesRepository;
        this.eventTimesMapper = eventTimesMapper;
    }

    /**
     * Save a eventTimes.
     *
     * @param eventTimesDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public EventTimesDTO save(EventTimesDTO eventTimesDTO) {
        log.debug("Request to save EventTimes : {}", eventTimesDTO);
        EventTimes eventTimes = eventTimesMapper.toEntity(eventTimesDTO);
        eventTimes = eventTimesRepository.save(eventTimes);
        return eventTimesMapper.toDto(eventTimes);
    }

    /**
     * Get all the eventTimes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<EventTimesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EventTimes");
        return eventTimesRepository.findAll(pageable)
            .map(eventTimesMapper::toDto);
    }

    /**
     * Get one eventTimes by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<EventTimesDTO> findOne(Long id) {
        log.debug("Request to get EventTimes : {}", id);
        return eventTimesRepository.findById(id)
            .map(eventTimesMapper::toDto);
    }

    /**
     * Delete the eventTimes by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete EventTimes : {}", id);
        eventTimesRepository.deleteById(id);
    }
}
