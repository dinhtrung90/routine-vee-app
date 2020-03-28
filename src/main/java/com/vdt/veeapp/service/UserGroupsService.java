package com.vdt.veeapp.service;

import com.vdt.veeapp.service.dto.UserGroupsDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.vdt.veeapp.domain.UserGroups}.
 */
public interface UserGroupsService {

    /**
     * Save a userGroups.
     *
     * @param userGroupsDTO the entity to save.
     * @return the persisted entity.
     */
    UserGroupsDTO save(UserGroupsDTO userGroupsDTO);

    /**
     * Get all the userGroups.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UserGroupsDTO> findAll(Pageable pageable);

    /**
     * Get all the userGroups with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    Page<UserGroupsDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" userGroups.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserGroupsDTO> findOne(Long id);

    /**
     * Delete the "id" userGroups.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
