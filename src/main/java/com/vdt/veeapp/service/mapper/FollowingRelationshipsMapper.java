package com.vdt.veeapp.service.mapper;


import com.vdt.veeapp.domain.*;
import com.vdt.veeapp.service.dto.FollowingRelationshipsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link FollowingRelationships} and its DTO {@link FollowingRelationshipsDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface FollowingRelationshipsMapper extends EntityMapper<FollowingRelationshipsDTO, FollowingRelationships> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    @Mapping(source = "userFollowing.id", target = "userFollowingId")
    @Mapping(source = "userFollowing.login", target = "userFollowingLogin")
    FollowingRelationshipsDTO toDto(FollowingRelationships followingRelationships);

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "userFollowingId", target = "userFollowing")
    FollowingRelationships toEntity(FollowingRelationshipsDTO followingRelationshipsDTO);

    default FollowingRelationships fromId(Long id) {
        if (id == null) {
            return null;
        }
        FollowingRelationships followingRelationships = new FollowingRelationships();
        followingRelationships.setId(id);
        return followingRelationships;
    }
}
