package com.vdt.veeapp.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.vdt.veeapp.domain.UserProfile} entity.
 */
public class UserProfileDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String userKey;

    private String fullName;

    private String avartarUrl;

    private String coverURl;

    private Double longitude;

    private Double latitude;


    private Long userId;

    private String userLogin;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAvartarUrl() {
        return avartarUrl;
    }

    public void setAvartarUrl(String avartarUrl) {
        this.avartarUrl = avartarUrl;
    }

    public String getCoverURl() {
        return coverURl;
    }

    public void setCoverURl(String coverURl) {
        this.coverURl = coverURl;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserProfileDTO userProfileDTO = (UserProfileDTO) o;
        if (userProfileDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userProfileDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserProfileDTO{" +
            "id=" + getId() +
            ", userKey='" + getUserKey() + "'" +
            ", fullName='" + getFullName() + "'" +
            ", avartarUrl='" + getAvartarUrl() + "'" +
            ", coverURl='" + getCoverURl() + "'" +
            ", longitude=" + getLongitude() +
            ", latitude=" + getLatitude() +
            ", userId=" + getUserId() +
            ", userLogin='" + getUserLogin() + "'" +
            "}";
    }
}
