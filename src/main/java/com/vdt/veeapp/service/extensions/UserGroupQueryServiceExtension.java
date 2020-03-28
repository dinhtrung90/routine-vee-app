package com.vdt.veeapp.service.extensions;

import com.netflix.discovery.converters.Auto;
import com.vdt.veeapp.domain.User;
import com.vdt.veeapp.domain.UserGroups;
import com.vdt.veeapp.domain.UserProfile;
import com.vdt.veeapp.repository.UserGroupsRepository;
import com.vdt.veeapp.repository.extensions.UserGroupsRepositoryExtension;
import com.vdt.veeapp.repository.extensions.UserProfileRepositoryExtension;
import com.vdt.veeapp.service.UserGroupsQueryService;
import com.vdt.veeapp.service.dto.UserGroupsCriteria;
import com.vdt.veeapp.service.dto.UserGroupsDTO;
import com.vdt.veeapp.service.mapper.UserGroupsMapper;
import com.vdt.veeapp.web.rest.errors.BadRequestAlertException;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserGroupQueryServiceExtension extends UserGroupsQueryService {

    private final Logger log = LoggerFactory.getLogger(UserGroupQueryServiceExtension.class);

    @Autowired
    private UserGroupsRepositoryExtension userGroupsRepositoryExtension;

    @Autowired
    private UserProfileRepositoryExtension userProfileRepositoryExtension;

    @Autowired
    private UserGroupsMapper userGroupsMapper;

    public UserGroupQueryServiceExtension(UserGroupsRepository userGroupsRepository, UserGroupsMapper userGroupsMapper) {
        super(userGroupsRepository, userGroupsMapper);
    }

    @Transactional(readOnly = true)
    public Page<UserGroupsDTO> findByProfileId(Long profileId, Pageable page) {
        log.debug("find by profile Id : {}, page: {}", profileId, page);
        Optional<UserProfile> userProfile = userProfileRepositoryExtension.findById(profileId);
        if (!userProfile.isPresent()) {
            throw  new BadRequestAlertException("User is not existed.", "UserProfile", profileId.toString());
        }
        UserProfile profile = userProfile.get();
        return userGroupsRepositoryExtension.findAllByUserProfilesEquals(page, profile)
            .map(userGroupsMapper::toDto);
    }
}
