package com.vdt.veeapp.service.impl;

import com.vdt.veeapp.service.FollowingRelationshipsService;
import com.vdt.veeapp.domain.FollowingRelationships;
import com.vdt.veeapp.repository.FollowingRelationshipsRepository;
import com.vdt.veeapp.service.dto.FollowingRelationshipsDTO;
import com.vdt.veeapp.service.mapper.FollowingRelationshipsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link FollowingRelationships}.
 */
@Service
@Transactional
public class FollowingRelationshipsServiceImpl implements FollowingRelationshipsService {

    private final Logger log = LoggerFactory.getLogger(FollowingRelationshipsServiceImpl.class);

    private final FollowingRelationshipsRepository followingRelationshipsRepository;

    private final FollowingRelationshipsMapper followingRelationshipsMapper;

    public FollowingRelationshipsServiceImpl(FollowingRelationshipsRepository followingRelationshipsRepository, FollowingRelationshipsMapper followingRelationshipsMapper) {
        this.followingRelationshipsRepository = followingRelationshipsRepository;
        this.followingRelationshipsMapper = followingRelationshipsMapper;
    }

    /**
     * Save a followingRelationships.
     *
     * @param followingRelationshipsDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public FollowingRelationshipsDTO save(FollowingRelationshipsDTO followingRelationshipsDTO) {
        log.debug("Request to save FollowingRelationships : {}", followingRelationshipsDTO);
        FollowingRelationships followingRelationships = followingRelationshipsMapper.toEntity(followingRelationshipsDTO);
        followingRelationships = followingRelationshipsRepository.save(followingRelationships);
        return followingRelationshipsMapper.toDto(followingRelationships);
    }

    /**
     * Get all the followingRelationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FollowingRelationshipsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FollowingRelationships");
        return followingRelationshipsRepository.findAll(pageable)
            .map(followingRelationshipsMapper::toDto);
    }

    /**
     * Get one followingRelationships by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<FollowingRelationshipsDTO> findOne(Long id) {
        log.debug("Request to get FollowingRelationships : {}", id);
        return followingRelationshipsRepository.findById(id)
            .map(followingRelationshipsMapper::toDto);
    }

    /**
     * Delete the followingRelationships by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete FollowingRelationships : {}", id);
        followingRelationshipsRepository.deleteById(id);
    }
}
