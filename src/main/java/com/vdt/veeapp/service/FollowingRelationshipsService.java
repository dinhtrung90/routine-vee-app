package com.vdt.veeapp.service;

import com.vdt.veeapp.service.dto.FollowingRelationshipsDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.vdt.veeapp.domain.FollowingRelationships}.
 */
public interface FollowingRelationshipsService {

    /**
     * Save a followingRelationships.
     *
     * @param followingRelationshipsDTO the entity to save.
     * @return the persisted entity.
     */
    FollowingRelationshipsDTO save(FollowingRelationshipsDTO followingRelationshipsDTO);

    /**
     * Get all the followingRelationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FollowingRelationshipsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" followingRelationships.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FollowingRelationshipsDTO> findOne(Long id);

    /**
     * Delete the "id" followingRelationships.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
