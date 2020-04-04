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

/**
 * Criteria class for the {@link com.vdt.veeapp.domain.UserProfile} entity. This class is used
 * in {@link com.vdt.veeapp.web.rest.UserProfileResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /user-profiles?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class UserProfileCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter userKey;

    private StringFilter fullName;

    private StringFilter avartarUrl;

    private StringFilter coverURl;

    private DoubleFilter longitude;

    private DoubleFilter latitude;

    private LongFilter userId;

    private LongFilter userGroupsId;

    public UserProfileCriteria() {
    }

    public UserProfileCriteria(UserProfileCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.userKey = other.userKey == null ? null : other.userKey.copy();
        this.fullName = other.fullName == null ? null : other.fullName.copy();
        this.avartarUrl = other.avartarUrl == null ? null : other.avartarUrl.copy();
        this.coverURl = other.coverURl == null ? null : other.coverURl.copy();
        this.longitude = other.longitude == null ? null : other.longitude.copy();
        this.latitude = other.latitude == null ? null : other.latitude.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.userGroupsId = other.userGroupsId == null ? null : other.userGroupsId.copy();
    }

    @Override
    public UserProfileCriteria copy() {
        return new UserProfileCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getUserKey() {
        return userKey;
    }

    public void setUserKey(StringFilter userKey) {
        this.userKey = userKey;
    }

    public StringFilter getFullName() {
        return fullName;
    }

    public void setFullName(StringFilter fullName) {
        this.fullName = fullName;
    }

    public StringFilter getAvartarUrl() {
        return avartarUrl;
    }

    public void setAvartarUrl(StringFilter avartarUrl) {
        this.avartarUrl = avartarUrl;
    }

    public StringFilter getCoverURl() {
        return coverURl;
    }

    public void setCoverURl(StringFilter coverURl) {
        this.coverURl = coverURl;
    }

    public DoubleFilter getLongitude() {
        return longitude;
    }

    public void setLongitude(DoubleFilter longitude) {
        this.longitude = longitude;
    }

    public DoubleFilter getLatitude() {
        return latitude;
    }

    public void setLatitude(DoubleFilter latitude) {
        this.latitude = latitude;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getUserGroupsId() {
        return userGroupsId;
    }

    public void setUserGroupsId(LongFilter userGroupsId) {
        this.userGroupsId = userGroupsId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final UserProfileCriteria that = (UserProfileCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(userKey, that.userKey) &&
            Objects.equals(fullName, that.fullName) &&
            Objects.equals(avartarUrl, that.avartarUrl) &&
            Objects.equals(coverURl, that.coverURl) &&
            Objects.equals(longitude, that.longitude) &&
            Objects.equals(latitude, that.latitude) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(userGroupsId, that.userGroupsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        userKey,
        fullName,
        avartarUrl,
        coverURl,
        longitude,
        latitude,
        userId,
        userGroupsId
        );
    }

    @Override
    public String toString() {
        return "UserProfileCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (userKey != null ? "userKey=" + userKey + ", " : "") +
                (fullName != null ? "fullName=" + fullName + ", " : "") +
                (avartarUrl != null ? "avartarUrl=" + avartarUrl + ", " : "") +
                (coverURl != null ? "coverURl=" + coverURl + ", " : "") +
                (longitude != null ? "longitude=" + longitude + ", " : "") +
                (latitude != null ? "latitude=" + latitude + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (userGroupsId != null ? "userGroupsId=" + userGroupsId + ", " : "") +
            "}";
    }

}
