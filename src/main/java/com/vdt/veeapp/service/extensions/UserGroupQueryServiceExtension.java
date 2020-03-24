package com.vdt.veeapp.service.extensions;

import com.netflix.discovery.converters.Auto;
import com.vdt.veeapp.domain.UserGroups;
import com.vdt.veeapp.repository.UserGroupsRepository;
import com.vdt.veeapp.repository.extensions.UserGroupsRepositoryExtension;
import com.vdt.veeapp.service.UserGroupsQueryService;
import com.vdt.veeapp.service.dto.UserGroupsCriteria;
import com.vdt.veeapp.service.dto.UserGroupsDTO;
import com.vdt.veeapp.service.mapper.UserGroupsMapper;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserGroupQueryServiceExtension extends UserGroupsQueryService {

    private final Logger log = LoggerFactory.getLogger(UserGroupQueryServiceExtension.class);

    @Autowired
    private UserGroupsRepositoryExtension userGroupsRepositoryExtension;

    @Autowired
    private UserGroupsMapper userGroupsMapper;

    public UserGroupQueryServiceExtension(UserGroupsRepository userGroupsRepository, UserGroupsMapper userGroupsMapper) {
        super(userGroupsRepository, userGroupsMapper);
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



        return userGroupsMapper.toDto(userGroupsRepositoryExtension.findAll(specification));
    }
}
