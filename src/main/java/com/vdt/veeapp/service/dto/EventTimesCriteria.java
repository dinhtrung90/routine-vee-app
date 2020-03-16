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
 * Criteria class for the {@link com.vdt.veeapp.domain.EventTimes} entity. This class is used
 * in {@link com.vdt.veeapp.web.rest.EventTimesResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /event-times?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EventTimesCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter dayOfWeek;

    private InstantFilter startTime;

    private InstantFilter endTime;

    private LongFilter habitId;

    public EventTimesCriteria() {
    }

    public EventTimesCriteria(EventTimesCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.dayOfWeek = other.dayOfWeek == null ? null : other.dayOfWeek.copy();
        this.startTime = other.startTime == null ? null : other.startTime.copy();
        this.endTime = other.endTime == null ? null : other.endTime.copy();
        this.habitId = other.habitId == null ? null : other.habitId.copy();
    }

    @Override
    public EventTimesCriteria copy() {
        return new EventTimesCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(IntegerFilter dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public InstantFilter getStartTime() {
        return startTime;
    }

    public void setStartTime(InstantFilter startTime) {
        this.startTime = startTime;
    }

    public InstantFilter getEndTime() {
        return endTime;
    }

    public void setEndTime(InstantFilter endTime) {
        this.endTime = endTime;
    }

    public LongFilter getHabitId() {
        return habitId;
    }

    public void setHabitId(LongFilter habitId) {
        this.habitId = habitId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EventTimesCriteria that = (EventTimesCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(dayOfWeek, that.dayOfWeek) &&
            Objects.equals(startTime, that.startTime) &&
            Objects.equals(endTime, that.endTime) &&
            Objects.equals(habitId, that.habitId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        dayOfWeek,
        startTime,
        endTime,
        habitId
        );
    }

    @Override
    public String toString() {
        return "EventTimesCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (dayOfWeek != null ? "dayOfWeek=" + dayOfWeek + ", " : "") +
                (startTime != null ? "startTime=" + startTime + ", " : "") +
                (endTime != null ? "endTime=" + endTime + ", " : "") +
                (habitId != null ? "habitId=" + habitId + ", " : "") +
            "}";
    }

}
