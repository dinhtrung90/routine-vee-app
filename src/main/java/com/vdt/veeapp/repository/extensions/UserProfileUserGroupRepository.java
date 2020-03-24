package com.vdt.veeapp.repository.extensions;

import com.vdt.veeapp.domain.UserGroups;
import com.vdt.veeapp.domain.UserProfileGroups;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProfileUserGroupRepository extends JpaRepository<UserProfileGroups, Long>, JpaSpecificationExecutor<UserProfileGroups> {
    
}
