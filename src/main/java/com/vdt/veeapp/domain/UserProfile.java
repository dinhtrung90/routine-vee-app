package com.vdt.veeapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

/**
 * A UserProfile.
 */
@Entity
@Table(name = "user_profile")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UserProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "user_key", nullable = false, unique = true)
    private String userKey;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "avartar_url")
    private String avartarUrl;

    @Column(name = "cover_u_rl")
    private String coverURl;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "latitude")
    private Double latitude;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @ManyToMany(mappedBy = "userProfiles")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<UserGroups> userGroups = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserKey() {
        return userKey;
    }

    public UserProfile userKey(String userKey) {
        this.userKey = userKey;
        return this;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getFullName() {
        return fullName;
    }

    public UserProfile fullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAvartarUrl() {
        return avartarUrl;
    }

    public UserProfile avartarUrl(String avartarUrl) {
        this.avartarUrl = avartarUrl;
        return this;
    }

    public void setAvartarUrl(String avartarUrl) {
        this.avartarUrl = avartarUrl;
    }

    public String getCoverURl() {
        return coverURl;
    }

    public UserProfile coverURl(String coverURl) {
        this.coverURl = coverURl;
        return this;
    }

    public void setCoverURl(String coverURl) {
        this.coverURl = coverURl;
    }

    public Double getLongitude() {
        return longitude;
    }

    public UserProfile longitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public UserProfile latitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public User getUser() {
        return user;
    }

    public UserProfile user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<UserGroups> getUserGroups() {
        return userGroups;
    }

    public UserProfile userGroups(Set<UserGroups> userGroups) {
        this.userGroups = userGroups;
        return this;
    }

    public UserProfile addUserGroups(UserGroups userGroups) {
        this.userGroups.add(userGroups);
        userGroups.getUserProfiles().add(this);
        return this;
    }

    public UserProfile removeUserGroups(UserGroups userGroups) {
        this.userGroups.remove(userGroups);
        userGroups.getUserProfiles().remove(this);
        return this;
    }

    public void setUserGroups(Set<UserGroups> userGroups) {
        this.userGroups = userGroups;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserProfile)) {
            return false;
        }
        return id != null && id.equals(((UserProfile) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "UserProfile{" +
            "id=" + getId() +
            ", userKey='" + getUserKey() + "'" +
            ", fullName='" + getFullName() + "'" +
            ", avartarUrl='" + getAvartarUrl() + "'" +
            ", coverURl='" + getCoverURl() + "'" +
            ", longitude=" + getLongitude() +
            ", latitude=" + getLatitude() +
            "}";
    }
}
