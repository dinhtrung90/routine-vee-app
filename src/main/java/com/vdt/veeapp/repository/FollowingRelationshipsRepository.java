package com.vdt.veeapp.repository;

import com.vdt.veeapp.domain.FollowingRelationships;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the FollowingRelationships entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FollowingRelationshipsRepository extends JpaRepository<FollowingRelationships, Long>, JpaSpecificationExecutor<FollowingRelationships> {

    @Query("select followingRelationships from FollowingRelationships followingRelationships where followingRelationships.user.login = ?#{principal.username}")
    List<FollowingRelationships> findByUserIsCurrentUser();

    @Query("select followingRelationships from FollowingRelationships followingRelationships where followingRelationships.userFollowing.login = ?#{principal.username}")
    List<FollowingRelationships> findByUserFollowingIsCurrentUser();
}
