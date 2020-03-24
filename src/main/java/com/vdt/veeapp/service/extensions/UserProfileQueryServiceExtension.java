package com.vdt.veeapp.service.extensions;

import com.vdt.veeapp.domain.UserProfile;
import com.vdt.veeapp.repository.UserProfileRepository;
import com.vdt.veeapp.repository.extensions.UserProfileRepositoryExtension;
import com.vdt.veeapp.service.UserProfileQueryService;
import com.vdt.veeapp.service.dto.UserProfileCriteria;
import com.vdt.veeapp.service.dto.UserProfileDTO;
import com.vdt.veeapp.service.mapper.UserProfileMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserProfileQueryServiceExtension extends UserProfileQueryService {

    private final Logger log = LoggerFactory.getLogger(UserProfileQueryServiceExtension.class);

    @Autowired
    private UserProfileRepositoryExtension userProfileRepositoryExtension;

    public UserProfileQueryServiceExtension(UserProfileRepository userProfileRepository, UserProfileMapper userProfileMapper) {
        super(userProfileRepository, userProfileMapper);
    }



}
