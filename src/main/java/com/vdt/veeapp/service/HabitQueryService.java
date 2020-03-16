package com.vdt.veeapp.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.vdt.veeapp.domain.Habit;
import com.vdt.veeapp.domain.*; // for static metamodels
import com.vdt.veeapp.repository.HabitRepository;
import com.vdt.veeapp.service.dto.HabitCriteria;
import com.vdt.veeapp.service.dto.HabitDTO;
import com.vdt.veeapp.service.mapper.HabitMapper;

/**
 * Service for executing complex queries for {@link Habit} entities in the database.
 * The main input is a {@link HabitCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link HabitDTO} or a {@link Page} of {@link HabitDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HabitQueryService extends QueryService<Habit> {

    private final Logger log = LoggerFactory.getLogger(HabitQueryService.class);

    private final HabitRepository habitRepository;

    private final HabitMapper habitMapper;

    public HabitQueryService(HabitRepository habitRepository, HabitMapper habitMapper) {
        this.habitRepository = habitRepository;
        this.habitMapper = habitMapper;
    }

    /**
     * Return a {@link List} of {@link HabitDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<HabitDTO> findByCriteria(HabitCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Habit> specification = createSpecification(criteria);
        return habitMapper.toDto(habitRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link HabitDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HabitDTO> findByCriteria(HabitCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Habit> specification = createSpecification(criteria);
        return habitRepository.findAll(specification, page)
            .map(habitMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HabitCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Habit> specification = createSpecification(criteria);
        return habitRepository.count(specification);
    }

    /**
     * Function to convert {@link HabitCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Habit> createSpecification(HabitCriteria criteria) {
        Specification<Habit> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Habit_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Habit_.name));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), Habit_.type));
            }
            if (criteria.getGoalPeriod() != null) {
                specification = specification.and(buildSpecification(criteria.getGoalPeriod(), Habit_.goalPeriod));
            }
            if (criteria.getCompletionGoal() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCompletionGoal(), Habit_.completionGoal));
            }
            if (criteria.getIsGroupTracking() != null) {
                specification = specification.and(buildSpecification(criteria.getIsGroupTracking(), Habit_.isGroupTracking));
            }
            if (criteria.getNoteText() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNoteText(), Habit_.noteText));
            }
            if (criteria.getMotivateText() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMotivateText(), Habit_.motivateText));
            }
            if (criteria.getIsReminder() != null) {
                specification = specification.and(buildSpecification(criteria.getIsReminder(), Habit_.isReminder));
            }
            if (criteria.getReminderId() != null) {
                specification = specification.and(buildSpecification(criteria.getReminderId(),
                    root -> root.join(Habit_.reminder, JoinType.LEFT).get(Reminder_.id)));
            }
            if (criteria.getEventTimesId() != null) {
                specification = specification.and(buildSpecification(criteria.getEventTimesId(),
                    root -> root.join(Habit_.eventTimes, JoinType.LEFT).get(EventTimes_.id)));
            }
        }
        return specification;
    }
}
