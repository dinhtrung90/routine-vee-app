package com.vdt.veeapp.service.dto;

import java.time.Instant;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.vdt.veeapp.domain.UserGroups} entity.
 */
public class UserGroupsDTO implements Serializable {
    
    private Long id;

    private String name;

    private String avataGroupUrl;

    private Instant createAt;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvataGroupUrl() {
        return avataGroupUrl;
    }

    public void setAvataGroupUrl(String avataGroupUrl) {
        this.avataGroupUrl = avataGroupUrl;
    }

    public Instant getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Instant createAt) {
        this.createAt = createAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserGroupsDTO userGroupsDTO = (UserGroupsDTO) o;
        if (userGroupsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userGroupsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserGroupsDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", avataGroupUrl='" + getAvataGroupUrl() + "'" +
            ", createAt='" + getCreateAt() + "'" +
            "}";
    }
}
