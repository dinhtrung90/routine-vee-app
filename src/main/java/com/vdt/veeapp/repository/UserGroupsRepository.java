package com.vdt.veeapp.repository;

import com.vdt.veeapp.domain.UserGroups;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the UserGroups entity.
 */
@Repository
public interface UserGroupsRepository extends JpaRepository<UserGroups, Long>, JpaSpecificationExecutor<UserGroups> {

    @Query(value = "select distinct userGroups from UserGroups userGroups left join fetch userGroups.userProfiles",
        countQuery = "select count(distinct userGroups) from UserGroups userGroups")
    Page<UserGroups> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct userGroups from UserGroups userGroups left join fetch userGroups.userProfiles")
    List<UserGroups> findAllWithEagerRelationships();

    @Query("select userGroups from UserGroups userGroups left join fetch userGroups.userProfiles where userGroups.id =:id")
    Optional<UserGroups> findOneWithEagerRelationships(@Param("id") Long id);
}
