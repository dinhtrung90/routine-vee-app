package com.vdt.veeapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A UserGroups.
 */
@Entity
@Table(name = "user_groups")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UserGroups implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "avata_group_url")
    private String avataGroupUrl;

    @Column(name = "create_at")
    private Instant createAt;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "user_groups_user_profile",
               joinColumns = @JoinColumn(name = "user_groups_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "user_profile_id", referencedColumnName = "id"))
    private Set<UserProfile> userProfiles = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public UserGroups name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvataGroupUrl() {
        return avataGroupUrl;
    }

    public UserGroups avataGroupUrl(String avataGroupUrl) {
        this.avataGroupUrl = avataGroupUrl;
        return this;
    }

    public void setAvataGroupUrl(String avataGroupUrl) {
        this.avataGroupUrl = avataGroupUrl;
    }

    public Instant getCreateAt() {
        return createAt;
    }

    public UserGroups createAt(Instant createAt) {
        this.createAt = createAt;
        return this;
    }

    public void setCreateAt(Instant createAt) {
        this.createAt = createAt;
    }

    public Set<UserProfile> getUserProfiles() {
        return userProfiles;
    }

    public UserGroups userProfiles(Set<UserProfile> userProfiles) {
        this.userProfiles = userProfiles;
        return this;
    }

    public UserGroups addUserProfile(UserProfile userProfile) {
        this.userProfiles.add(userProfile);
        userProfile.getUserGroups().add(this);
        return this;
    }

    public UserGroups removeUserProfile(UserProfile userProfile) {
        this.userProfiles.remove(userProfile);
        userProfile.getUserGroups().remove(this);
        return this;
    }

    public void setUserProfiles(Set<UserProfile> userProfiles) {
        this.userProfiles = userProfiles;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserGroups)) {
            return false;
        }
        return id != null && id.equals(((UserGroups) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "UserGroups{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", avataGroupUrl='" + getAvataGroupUrl() + "'" +
            ", createAt='" + getCreateAt() + "'" +
            "}";
    }
}
