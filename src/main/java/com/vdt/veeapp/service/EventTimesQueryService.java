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

import com.vdt.veeapp.domain.EventTimes;
import com.vdt.veeapp.domain.*; // for static metamodels
import com.vdt.veeapp.repository.EventTimesRepository;
import com.vdt.veeapp.service.dto.EventTimesCriteria;
import com.vdt.veeapp.service.dto.EventTimesDTO;
import com.vdt.veeapp.service.mapper.EventTimesMapper;

/**
 * Service for executing complex queries for {@link EventTimes} entities in the database.
 * The main input is a {@link EventTimesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EventTimesDTO} or a {@link Page} of {@link EventTimesDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EventTimesQueryService extends QueryService<EventTimes> {

    private final Logger log = LoggerFactory.getLogger(EventTimesQueryService.class);

    private final EventTimesRepository eventTimesRepository;

    private final EventTimesMapper eventTimesMapper;

    public EventTimesQueryService(EventTimesRepository eventTimesRepository, EventTimesMapper eventTimesMapper) {
        this.eventTimesRepository = eventTimesRepository;
        this.eventTimesMapper = eventTimesMapper;
    }

    /**
     * Return a {@link List} of {@link EventTimesDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EventTimesDTO> findByCriteria(EventTimesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EventTimes> specification = createSpecification(criteria);
        return eventTimesMapper.toDto(eventTimesRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EventTimesDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EventTimesDTO> findByCriteria(EventTimesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EventTimes> specification = createSpecification(criteria);
        return eventTimesRepository.findAll(specification, page)
            .map(eventTimesMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EventTimesCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EventTimes> specification = createSpecification(criteria);
        return eventTimesRepository.count(specification);
    }

    /**
     * Function to convert {@link EventTimesCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EventTimes> createSpecification(EventTimesCriteria criteria) {
        Specification<EventTimes> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EventTimes_.id));
            }
            if (criteria.getDayOfWeek() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDayOfWeek(), EventTimes_.dayOfWeek));
            }
            if (criteria.getStartTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartTime(), EventTimes_.startTime));
            }
            if (criteria.getEndTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndTime(), EventTimes_.endTime));
            }
            if (criteria.getHabitId() != null) {
                specification = specification.and(buildSpecification(criteria.getHabitId(),
                    root -> root.join(EventTimes_.habit, JoinType.LEFT).get(Habit_.id)));
            }
        }
        return specification;
    }
}
