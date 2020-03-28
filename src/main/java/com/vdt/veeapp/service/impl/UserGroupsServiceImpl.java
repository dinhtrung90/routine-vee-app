package com.vdt.veeapp.service.impl;

import com.vdt.veeapp.service.UserGroupsService;
import com.vdt.veeapp.domain.UserGroups;
import com.vdt.veeapp.repository.UserGroupsRepository;
import com.vdt.veeapp.service.dto.UserGroupsDTO;
import com.vdt.veeapp.service.mapper.UserGroupsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link UserGroups}.
 */
@Service
@Transactional
public class UserGroupsServiceImpl implements UserGroupsService {

    private final Logger log = LoggerFactory.getLogger(UserGroupsServiceImpl.class);

    private final UserGroupsRepository userGroupsRepository;

    private final UserGroupsMapper userGroupsMapper;

    public UserGroupsServiceImpl(UserGroupsRepository userGroupsRepository, UserGroupsMapper userGroupsMapper) {
        this.userGroupsRepository = userGroupsRepository;
        this.userGroupsMapper = userGroupsMapper;
    }

    /**
     * Save a userGroups.
     *
     * @param userGroupsDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public UserGroupsDTO save(UserGroupsDTO userGroupsDTO) {
        log.debug("Request to save UserGroups : {}", userGroupsDTO);
        UserGroups userGroups = userGroupsMapper.toEntity(userGroupsDTO);
        userGroups = userGroupsRepository.save(userGroups);
        return userGroupsMapper.toDto(userGroups);
    }

    /**
     * Get all the userGroups.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserGroupsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserGroups");
        return userGroupsRepository.findAll(pageable)
            .map(userGroupsMapper::toDto);
    }

    /**
     * Get all the userGroups with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<UserGroupsDTO> findAllWithEagerRelationships(Pageable pageable) {
        return userGroupsRepository.findAllWithEagerRelationships(pageable).map(userGroupsMapper::toDto);
    }

    /**
     * Get one userGroups by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<UserGroupsDTO> findOne(Long id) {
        log.debug("Request to get UserGroups : {}", id);
        return userGroupsRepository.findOneWithEagerRelationships(id)
            .map(userGroupsMapper::toDto);
    }

    /**
     * Delete the userGroups by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserGroups : {}", id);
        userGroupsRepository.deleteById(id);
    }
}
