package com.vdt.veeapp.service.impl;

import com.vdt.veeapp.service.ReminderService;
import com.vdt.veeapp.domain.Reminder;
import com.vdt.veeapp.repository.ReminderRepository;
import com.vdt.veeapp.service.dto.ReminderDTO;
import com.vdt.veeapp.service.mapper.ReminderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Reminder}.
 */
@Service
@Transactional
public class ReminderServiceImpl implements ReminderService {

    private final Logger log = LoggerFactory.getLogger(ReminderServiceImpl.class);

    private final ReminderRepository reminderRepository;

    private final ReminderMapper reminderMapper;

    public ReminderServiceImpl(ReminderRepository reminderRepository, ReminderMapper reminderMapper) {
        this.reminderRepository = reminderRepository;
        this.reminderMapper = reminderMapper;
    }

    /**
     * Save a reminder.
     *
     * @param reminderDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ReminderDTO save(ReminderDTO reminderDTO) {
        log.debug("Request to save Reminder : {}", reminderDTO);
        Reminder reminder = reminderMapper.toEntity(reminderDTO);
        reminder = reminderRepository.save(reminder);
        return reminderMapper.toDto(reminder);
    }

    /**
     * Get all the reminders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ReminderDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Reminders");
        return reminderRepository.findAll(pageable)
            .map(reminderMapper::toDto);
    }

    /**
     * Get one reminder by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ReminderDTO> findOne(Long id) {
        log.debug("Request to get Reminder : {}", id);
        return reminderRepository.findById(id)
            .map(reminderMapper::toDto);
    }

    /**
     * Delete the reminder by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Reminder : {}", id);
        reminderRepository.deleteById(id);
    }
}
