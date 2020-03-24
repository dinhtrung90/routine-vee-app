package com.vdt.veeapp.service.mapper;


import com.vdt.veeapp.domain.*;
import com.vdt.veeapp.service.dto.UserGroupsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserGroups} and its DTO {@link UserGroupsDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface UserGroupsMapper extends EntityMapper<UserGroupsDTO, UserGroups> {



    default UserGroups fromId(Long id) {
        if (id == null) {
            return null;
        }
        UserGroups userGroups = new UserGroups();
        userGroups.setId(id);
        return userGroups;
    }
}
