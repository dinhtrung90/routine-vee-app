package com.vdt.veeapp.repository;

import com.vdt.veeapp.domain.UserGroups;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the UserGroups entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserGroupsRepository extends JpaRepository<UserGroups, Long>, JpaSpecificationExecutor<UserGroups> {
}
