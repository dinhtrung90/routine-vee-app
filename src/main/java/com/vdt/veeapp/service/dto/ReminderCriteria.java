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
 * Criteria class for the {@link com.vdt.veeapp.domain.Reminder} entity. This class is used
 * in {@link com.vdt.veeapp.web.rest.ReminderResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /reminders?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ReminderCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter reminderText;

    private InstantFilter date;

    public ReminderCriteria() {
    }

    public ReminderCriteria(ReminderCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.reminderText = other.reminderText == null ? null : other.reminderText.copy();
        this.date = other.date == null ? null : other.date.copy();
    }

    @Override
    public ReminderCriteria copy() {
        return new ReminderCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getReminderText() {
        return reminderText;
    }

    public void setReminderText(StringFilter reminderText) {
        this.reminderText = reminderText;
    }

    public InstantFilter getDate() {
        return date;
    }

    public void setDate(InstantFilter date) {
        this.date = date;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ReminderCriteria that = (ReminderCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(reminderText, that.reminderText) &&
            Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        reminderText,
        date
        );
    }

    @Override
    public String toString() {
        return "ReminderCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (reminderText != null ? "reminderText=" + reminderText + ", " : "") +
                (date != null ? "date=" + date + ", " : "") +
            "}";
    }

}
