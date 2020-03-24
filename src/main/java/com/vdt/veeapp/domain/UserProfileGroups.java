package com.vdt.veeapp.domain;

import javax.persistence.*;

@Entity
@Table(name = "user_profile_user_groups")
public class UserProfileGroups {

    @EmbeddedId
    UserProfileUserGroupsKey id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("user_profile_id")
    @JoinColumn(name = "user_profile_id")
    UserProfile userProfile;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("user_groups_id")
    @JoinColumn(name = "user_groups_id")
    UserGroups userGroup;

    public UserProfileUserGroupsKey getId() {
        return id;
    }

    public void setId(UserProfileUserGroupsKey id) {
        this.id = id;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public UserGroups getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(UserGroups userGroup) {
        this.userGroup = userGroup;
    }
}
