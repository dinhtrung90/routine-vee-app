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

import com.vdt.veeapp.domain.FollowingRelationships;
import com.vdt.veeapp.domain.*; // for static metamodels
import com.vdt.veeapp.repository.FollowingRelationshipsRepository;
import com.vdt.veeapp.service.dto.FollowingRelationshipsCriteria;
import com.vdt.veeapp.service.dto.FollowingRelationshipsDTO;
import com.vdt.veeapp.service.mapper.FollowingRelationshipsMapper;

/**
 * Service for executing complex queries for {@link FollowingRelationships} entities in the database.
 * The main input is a {@link FollowingRelationshipsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FollowingRelationshipsDTO} or a {@link Page} of {@link FollowingRelationshipsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FollowingRelationshipsQueryService extends QueryService<FollowingRelationships> {

    private final Logger log = LoggerFactory.getLogger(FollowingRelationshipsQueryService.class);

    private final FollowingRelationshipsRepository followingRelationshipsRepository;

    private final FollowingRelationshipsMapper followingRelationshipsMapper;

    public FollowingRelationshipsQueryService(FollowingRelationshipsRepository followingRelationshipsRepository, FollowingRelationshipsMapper followingRelationshipsMapper) {
        this.followingRelationshipsRepository = followingRelationshipsRepository;
        this.followingRelationshipsMapper = followingRelationshipsMapper;
    }

    /**
     * Return a {@link List} of {@link FollowingRelationshipsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FollowingRelationshipsDTO> findByCriteria(FollowingRelationshipsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<FollowingRelationships> specification = createSpecification(criteria);
        return followingRelationshipsMapper.toDto(followingRelationshipsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FollowingRelationshipsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FollowingRelationshipsDTO> findByCriteria(FollowingRelationshipsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FollowingRelationships> specification = createSpecification(criteria);
        return followingRelationshipsRepository.findAll(specification, page)
            .map(followingRelationshipsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FollowingRelationshipsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FollowingRelationships> specification = createSpecification(criteria);
        return followingRelationshipsRepository.count(specification);
    }

    /**
     * Function to convert {@link FollowingRelationshipsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FollowingRelationships> createSpecification(FollowingRelationshipsCriteria criteria) {
        Specification<FollowingRelationships> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), FollowingRelationships_.id));
            }
            if (criteria.getDateFollowed() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateFollowed(), FollowingRelationships_.dateFollowed));
            }
            if (criteria.getActionUserId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getActionUserId(), FollowingRelationships_.actionUserId));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserId(),
                    root -> root.join(FollowingRelationships_.user, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getUserFollowingId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserFollowingId(),
                    root -> root.join(FollowingRelationships_.userFollowing, JoinType.LEFT).get(User_.id)));
            }
        }
        return specification;
    }
}
