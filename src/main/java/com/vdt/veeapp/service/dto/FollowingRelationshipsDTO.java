package com.vdt.veeapp.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.vdt.veeapp.domain.FollowingRelationships} entity.
 */
public class FollowingRelationshipsDTO implements Serializable {
    
    private Long id;

    @NotNull
    private Instant dateFollowed;

    @NotNull
    private Long actionUserId;


    private Long userId;

    private String userLogin;

    private Long userFollowingId;

    private String userFollowingLogin;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateFollowed() {
        return dateFollowed;
    }

    public void setDateFollowed(Instant dateFollowed) {
        this.dateFollowed = dateFollowed;
    }

    public Long getActionUserId() {
        return actionUserId;
    }

    public void setActionUserId(Long actionUserId) {
        this.actionUserId = actionUserId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public Long getUserFollowingId() {
        return userFollowingId;
    }

    public void setUserFollowingId(Long userId) {
        this.userFollowingId = userId;
    }

    public String getUserFollowingLogin() {
        return userFollowingLogin;
    }

    public void setUserFollowingLogin(String userLogin) {
        this.userFollowingLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FollowingRelationshipsDTO followingRelationshipsDTO = (FollowingRelationshipsDTO) o;
        if (followingRelationshipsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), followingRelationshipsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FollowingRelationshipsDTO{" +
            "id=" + getId() +
            ", dateFollowed='" + getDateFollowed() + "'" +
            ", actionUserId=" + getActionUserId() +
            ", userId=" + getUserId() +
            ", userLogin='" + getUserLogin() + "'" +
            ", userFollowingId=" + getUserFollowingId() +
            ", userFollowingLogin='" + getUserFollowingLogin() + "'" +
            "}";
    }
}
