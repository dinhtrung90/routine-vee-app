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
 * Criteria class for the {@link com.vdt.veeapp.domain.UserGroups} entity. This class is used
 * in {@link com.vdt.veeapp.web.rest.UserGroupsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /user-groups?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class UserGroupsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter avataGroupUrl;

    private InstantFilter createAt;

    private LongFilter userProfileId;

    public UserGroupsCriteria() {
    }

    public UserGroupsCriteria(UserGroupsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.avataGroupUrl = other.avataGroupUrl == null ? null : other.avataGroupUrl.copy();
        this.createAt = other.createAt == null ? null : other.createAt.copy();
        this.userProfileId = other.userProfileId == null ? null : other.userProfileId.copy();
    }

    @Override
    public UserGroupsCriteria copy() {
        return new UserGroupsCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getAvataGroupUrl() {
        return avataGroupUrl;
    }

    public void setAvataGroupUrl(StringFilter avataGroupUrl) {
        this.avataGroupUrl = avataGroupUrl;
    }

    public InstantFilter getCreateAt() {
        return createAt;
    }

    public void setCreateAt(InstantFilter createAt) {
        this.createAt = createAt;
    }

    public LongFilter getUserProfileId() {
        return userProfileId;
    }

    public void setUserProfileId(LongFilter userProfileId) {
        this.userProfileId = userProfileId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final UserGroupsCriteria that = (UserGroupsCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(avataGroupUrl, that.avataGroupUrl) &&
            Objects.equals(createAt, that.createAt) &&
            Objects.equals(userProfileId, that.userProfileId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        avataGroupUrl,
        createAt,
        userProfileId
        );
    }

    @Override
    public String toString() {
        return "UserGroupsCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (avataGroupUrl != null ? "avataGroupUrl=" + avataGroupUrl + ", " : "") +
                (createAt != null ? "createAt=" + createAt + ", " : "") +
                (userProfileId != null ? "userProfileId=" + userProfileId + ", " : "") +
            "}";
    }

}
