package com.vdt.veeapp.repository.extensions;

import com.vdt.veeapp.domain.FollowingRelationships;
import com.vdt.veeapp.domain.UserProfileGroups;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FollowingRelationshipsRepositoryExtension extends JpaRepository<FollowingRelationships, Long>, JpaSpecificationExecutor<FollowingRelationships>  {

    Page<FollowingRelationships> findAllByActionUserId(Long userId, Pageable pageable);

}
