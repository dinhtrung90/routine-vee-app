package com.vdt.veeapp.service.impl;

import com.vdt.veeapp.service.HabitService;
import com.vdt.veeapp.domain.Habit;
import com.vdt.veeapp.repository.HabitRepository;
import com.vdt.veeapp.repository.ReminderRepository;
import com.vdt.veeapp.service.dto.HabitDTO;
import com.vdt.veeapp.service.mapper.HabitMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Habit}.
 */
@Service
@Transactional
public class HabitServiceImpl implements HabitService {

    private final Logger log = LoggerFactory.getLogger(HabitServiceImpl.class);

    private final HabitRepository habitRepository;

    private final HabitMapper habitMapper;

    private final ReminderRepository reminderRepository;

    public HabitServiceImpl(HabitRepository habitRepository, HabitMapper habitMapper, ReminderRepository reminderRepository) {
        this.habitRepository = habitRepository;
        this.habitMapper = habitMapper;
        this.reminderRepository = reminderRepository;
    }

    /**
     * Save a habit.
     *
     * @param habitDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public HabitDTO save(HabitDTO habitDTO) {
        log.debug("Request to save Habit : {}", habitDTO);
        Habit habit = habitMapper.toEntity(habitDTO);
        long reminderId = habitDTO.getReminderId();
        reminderRepository.findById(reminderId).ifPresent(habit::reminder);
        habit = habitRepository.save(habit);
        return habitMapper.toDto(habit);
    }

    /**
     * Get all the habits.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<HabitDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Habits");
        return habitRepository.findAll(pageable)
            .map(habitMapper::toDto);
    }

    /**
     * Get one habit by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<HabitDTO> findOne(Long id) {
        log.debug("Request to get Habit : {}", id);
        return habitRepository.findById(id)
            .map(habitMapper::toDto);
    }

    /**
     * Delete the habit by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Habit : {}", id);
        habitRepository.deleteById(id);
    }
}
