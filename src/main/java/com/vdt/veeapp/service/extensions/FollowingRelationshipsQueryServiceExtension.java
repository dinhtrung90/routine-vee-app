package com.vdt.veeapp.service.extensions;

import com.vdt.veeapp.domain.FollowingRelationships;
import com.vdt.veeapp.domain.FollowingRelationships_;
import com.vdt.veeapp.domain.User_;
import com.vdt.veeapp.repository.FollowingRelationshipsRepository;
import com.vdt.veeapp.repository.extensions.FollowingRelationshipsRepositoryExtension;
import com.vdt.veeapp.service.dto.FollowingRelationshipsCriteria;
import com.vdt.veeapp.service.dto.FollowingRelationshipsDTO;
import com.vdt.veeapp.service.impl.FollowingRelationshipsServiceImpl;
import com.vdt.veeapp.service.mapper.FollowingRelationshipsMapper;
import io.github.jhipster.service.QueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.JoinType;
import java.util.List;

/**
 * Service for executing complex queries for {@link FollowingRelationships} entities in the database.
 * The main input is a {@link FollowingRelationshipsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FollowingRelationshipsDTO} or a {@link Page} of {@link FollowingRelationshipsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
@Primary
public class FollowingRelationshipsQueryServiceExtension extends FollowingRelationshipsServiceImpl {

    private final Logger log = LoggerFactory.getLogger(FollowingRelationshipsQueryServiceExtension.class);

    @Autowired
    private FollowingRelationshipsRepositoryExtension followingRelationshipsRepositoryExtension;

    @Autowired
    private FollowingRelationshipsMapper followingRelationshipsMapper;

    public FollowingRelationshipsQueryServiceExtension(FollowingRelationshipsRepository followingRelationshipsRepository, FollowingRelationshipsMapper followingRelationshipsMapper) {
        super(followingRelationshipsRepository, followingRelationshipsMapper );

    }

    @Transactional(readOnly = true)
    public Page<FollowingRelationshipsDTO> findYourUserFollowed(Long userKey, Pageable pageable) {
        log.debug("find all followed user");
        return followingRelationshipsRepositoryExtension.findAllByActionUserId(userKey, pageable)
            .map(followingRelationshipsMapper::toDto);
    }
}
