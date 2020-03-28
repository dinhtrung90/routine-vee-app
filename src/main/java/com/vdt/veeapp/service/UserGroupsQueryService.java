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

import com.vdt.veeapp.domain.UserGroups;
import com.vdt.veeapp.domain.*; // for static metamodels
import com.vdt.veeapp.repository.UserGroupsRepository;
import com.vdt.veeapp.service.dto.UserGroupsCriteria;
import com.vdt.veeapp.service.dto.UserGroupsDTO;
import com.vdt.veeapp.service.mapper.UserGroupsMapper;

/**
 * Service for executing complex queries for {@link UserGroups} entities in the database.
 * The main input is a {@link UserGroupsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UserGroupsDTO} or a {@link Page} of {@link UserGroupsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UserGroupsQueryService extends QueryService<UserGroups> {

    private final Logger log = LoggerFactory.getLogger(UserGroupsQueryService.class);

    private final UserGroupsRepository userGroupsRepository;

    private final UserGroupsMapper userGroupsMapper;

    public UserGroupsQueryService(UserGroupsRepository userGroupsRepository, UserGroupsMapper userGroupsMapper) {
        this.userGroupsRepository = userGroupsRepository;
        this.userGroupsMapper = userGroupsMapper;
    }

    /**
     * Return a {@link List} of {@link UserGroupsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UserGroupsDTO> findByCriteria(UserGroupsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<UserGroups> specification = createSpecification(criteria);
        return userGroupsMapper.toDto(userGroupsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link UserGroupsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UserGroupsDTO> findByCriteria(UserGroupsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<UserGroups> specification = createSpecification(criteria);
        return userGroupsRepository.findAll(specification, page)
            .map(userGroupsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UserGroupsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<UserGroups> specification = createSpecification(criteria);
        return userGroupsRepository.count(specification);
    }

    /**
     * Function to convert {@link UserGroupsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<UserGroups> createSpecification(UserGroupsCriteria criteria) {
        Specification<UserGroups> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), UserGroups_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), UserGroups_.name));
            }
            if (criteria.getAvataGroupUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAvataGroupUrl(), UserGroups_.avataGroupUrl));
            }
            if (criteria.getCreateAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreateAt(), UserGroups_.createAt));
            }
            if (criteria.getUserProfileId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserProfileId(),
                    root -> root.join(UserGroups_.userProfiles, JoinType.LEFT).get(UserProfile_.id)));
            }
        }
        return specification;
    }
}
