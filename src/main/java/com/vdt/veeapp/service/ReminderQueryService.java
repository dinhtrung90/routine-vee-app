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

import com.vdt.veeapp.domain.Reminder;
import com.vdt.veeapp.domain.*; // for static metamodels
import com.vdt.veeapp.repository.ReminderRepository;
import com.vdt.veeapp.service.dto.ReminderCriteria;
import com.vdt.veeapp.service.dto.ReminderDTO;
import com.vdt.veeapp.service.mapper.ReminderMapper;

/**
 * Service for executing complex queries for {@link Reminder} entities in the database.
 * The main input is a {@link ReminderCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ReminderDTO} or a {@link Page} of {@link ReminderDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReminderQueryService extends QueryService<Reminder> {

    private final Logger log = LoggerFactory.getLogger(ReminderQueryService.class);

    private final ReminderRepository reminderRepository;

    private final ReminderMapper reminderMapper;

    public ReminderQueryService(ReminderRepository reminderRepository, ReminderMapper reminderMapper) {
        this.reminderRepository = reminderRepository;
        this.reminderMapper = reminderMapper;
    }

    /**
     * Return a {@link List} of {@link ReminderDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ReminderDTO> findByCriteria(ReminderCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Reminder> specification = createSpecification(criteria);
        return reminderMapper.toDto(reminderRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ReminderDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ReminderDTO> findByCriteria(ReminderCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Reminder> specification = createSpecification(criteria);
        return reminderRepository.findAll(specification, page)
            .map(reminderMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ReminderCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Reminder> specification = createSpecification(criteria);
        return reminderRepository.count(specification);
    }

    /**
     * Function to convert {@link ReminderCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Reminder> createSpecification(ReminderCriteria criteria) {
        Specification<Reminder> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Reminder_.id));
            }
            if (criteria.getReminderText() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReminderText(), Reminder_.reminderText));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), Reminder_.date));
            }
        }
        return specification;
    }
}
