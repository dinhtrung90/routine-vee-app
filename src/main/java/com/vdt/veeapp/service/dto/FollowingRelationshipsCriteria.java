package com.vdt.veeapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link com.vdt.veeapp.domain.FollowingRelationships} entity. This class is used
 * in {@link com.vdt.veeapp.web.rest.FollowingRelationshipsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /following-relationships?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FollowingRelationshipsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter dateFollowed;

    private LongFilter actionUserId;

    private LongFilter userId;

    private LongFilter userFollowingId;

    public FollowingRelationshipsCriteria() {
    }

    public FollowingRelationshipsCriteria(FollowingRelationshipsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.dateFollowed = other.dateFollowed == null ? null : other.dateFollowed.copy();
        this.actionUserId = other.actionUserId == null ? null : other.actionUserId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.userFollowingId = other.userFollowingId == null ? null : other.userFollowingId.copy();
    }

    @Override
    public FollowingRelationshipsCriteria copy() {
        return new FollowingRelationshipsCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public InstantFilter getDateFollowed() {
        return dateFollowed;
    }

    public void setDateFollowed(InstantFilter dateFollowed) {
        this.dateFollowed = dateFollowed;
    }

    public LongFilter getActionUserId() {
        return actionUserId;
    }

    public void setActionUserId(LongFilter actionUserId) {
        this.actionUserId = actionUserId;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getUserFollowingId() {
        return userFollowingId;
    }

    public void setUserFollowingId(LongFilter userFollowingId) {
        this.userFollowingId = userFollowingId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final FollowingRelationshipsCriteria that = (FollowingRelationshipsCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(dateFollowed, that.dateFollowed) &&
            Objects.equals(actionUserId, that.actionUserId) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(userFollowingId, that.userFollowingId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        dateFollowed,
        actionUserId,
        userId,
        userFollowingId
        );
    }

    @Override
    public String toString() {
        return "FollowingRelationshipsCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (dateFollowed != null ? "dateFollowed=" + dateFollowed + ", " : "") +
                (actionUserId != null ? "actionUserId=" + actionUserId + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (userFollowingId != null ? "userFollowingId=" + userFollowingId + ", " : "") +
            "}";
    }

}
