package com.vdt.veeapp.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import java.io.Serializable;

@Embeddable
public class UserProfileUserGroupsKey implements Serializable {

    @Column(name = "user_profile_id")
    Long userProfileId;

    @Column(name = "user_groups_id")
    Long userGroupsId;


    public Long getUserProfileId() {
        return userProfileId;
    }

    public void setUserProfileId(Long userProfileId) {
        this.userProfileId = userProfileId;
    }

    public Long getUserGroupsId() {
        return userGroupsId;
    }

    public void setUserGroupsId(Long userGroupsId) {
        this.userGroupsId = userGroupsId;
    }
}
