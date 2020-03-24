package com.vdt.veeapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.Instant;

/**
 * A FollowingRelationships.
 */
@Entity
@Table(name = "following_relationships")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FollowingRelationships implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "date_followed", nullable = false)
    private Instant dateFollowed;

    @NotNull
    @Column(name = "action_user_id", nullable = false)
    private Long actionUserId;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("followingRelationships")
    private User user;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("followingRelationships")
    private User userFollowing;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateFollowed() {
        return dateFollowed;
    }

    public FollowingRelationships dateFollowed(Instant dateFollowed) {
        this.dateFollowed = dateFollowed;
        return this;
    }

    public void setDateFollowed(Instant dateFollowed) {
        this.dateFollowed = dateFollowed;
    }

    public Long getActionUserId() {
        return actionUserId;
    }

    public FollowingRelationships actionUserId(Long actionUserId) {
        this.actionUserId = actionUserId;
        return this;
    }

    public void setActionUserId(Long actionUserId) {
        this.actionUserId = actionUserId;
    }

    public User getUser() {
        return user;
    }

    public FollowingRelationships user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUserFollowing() {
        return userFollowing;
    }

    public FollowingRelationships userFollowing(User user) {
        this.userFollowing = user;
        return this;
    }

    public void setUserFollowing(User user) {
        this.userFollowing = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FollowingRelationships)) {
            return false;
        }
        return id != null && id.equals(((FollowingRelationships) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "FollowingRelationships{" +
            "id=" + getId() +
            ", dateFollowed='" + getDateFollowed() + "'" +
            ", actionUserId=" + getActionUserId() +
            "}";
    }
}
