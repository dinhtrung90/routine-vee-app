package com.vdt.veeapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.vdt.veeapp.domain.enumeration.HabitType;
import com.vdt.veeapp.domain.enumeration.Period;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.vdt.veeapp.domain.Habit} entity. This class is used
 * in {@link com.vdt.veeapp.web.rest.HabitResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /habits?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class HabitCriteria implements Serializable, Criteria {
    /**
     * Class for filtering HabitType
     */
    public static class HabitTypeFilter extends Filter<HabitType> {

        public HabitTypeFilter() {
        }

        public HabitTypeFilter(HabitTypeFilter filter) {
            super(filter);
        }

        @Override
        public HabitTypeFilter copy() {
            return new HabitTypeFilter(this);
        }

    }
    /**
     * Class for filtering Period
     */
    public static class PeriodFilter extends Filter<Period> {

        public PeriodFilter() {
        }

        public PeriodFilter(PeriodFilter filter) {
            super(filter);
        }

        @Override
        public PeriodFilter copy() {
            return new PeriodFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private HabitTypeFilter type;

    private PeriodFilter goalPeriod;

    private DoubleFilter completionGoal;

    private BooleanFilter isGroupTracking;

    private StringFilter noteText;

    private StringFilter motivateText;

    private BooleanFilter isReminder;

    private LongFilter reminderId;

    private LongFilter eventTimesId;

    public HabitCriteria() {
    }

    public HabitCriteria(HabitCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.goalPeriod = other.goalPeriod == null ? null : other.goalPeriod.copy();
        this.completionGoal = other.completionGoal == null ? null : other.completionGoal.copy();
        this.isGroupTracking = other.isGroupTracking == null ? null : other.isGroupTracking.copy();
        this.noteText = other.noteText == null ? null : other.noteText.copy();
        this.motivateText = other.motivateText == null ? null : other.motivateText.copy();
        this.isReminder = other.isReminder == null ? null : other.isReminder.copy();
        this.reminderId = other.reminderId == null ? null : other.reminderId.copy();
        this.eventTimesId = other.eventTimesId == null ? null : other.eventTimesId.copy();
    }

    @Override
    public HabitCriteria copy() {
        return new HabitCriteria(this);
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

    public HabitTypeFilter getType() {
        return type;
    }

    public void setType(HabitTypeFilter type) {
        this.type = type;
    }

    public PeriodFilter getGoalPeriod() {
        return goalPeriod;
    }

    public void setGoalPeriod(PeriodFilter goalPeriod) {
        this.goalPeriod = goalPeriod;
    }

    public DoubleFilter getCompletionGoal() {
        return completionGoal;
    }

    public void setCompletionGoal(DoubleFilter completionGoal) {
        this.completionGoal = completionGoal;
    }

    public BooleanFilter getIsGroupTracking() {
        return isGroupTracking;
    }

    public void setIsGroupTracking(BooleanFilter isGroupTracking) {
        this.isGroupTracking = isGroupTracking;
    }

    public StringFilter getNoteText() {
        return noteText;
    }

    public void setNoteText(StringFilter noteText) {
        this.noteText = noteText;
    }

    public StringFilter getMotivateText() {
        return motivateText;
    }

    public void setMotivateText(StringFilter motivateText) {
        this.motivateText = motivateText;
    }

    public BooleanFilter getIsReminder() {
        return isReminder;
    }

    public void setIsReminder(BooleanFilter isReminder) {
        this.isReminder = isReminder;
    }

    public LongFilter getReminderId() {
        return reminderId;
    }

    public void setReminderId(LongFilter reminderId) {
        this.reminderId = reminderId;
    }

    public LongFilter getEventTimesId() {
        return eventTimesId;
    }

    public void setEventTimesId(LongFilter eventTimesId) {
        this.eventTimesId = eventTimesId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final HabitCriteria that = (HabitCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(type, that.type) &&
            Objects.equals(goalPeriod, that.goalPeriod) &&
            Objects.equals(completionGoal, that.completionGoal) &&
            Objects.equals(isGroupTracking, that.isGroupTracking) &&
            Objects.equals(noteText, that.noteText) &&
            Objects.equals(motivateText, that.motivateText) &&
            Objects.equals(isReminder, that.isReminder) &&
            Objects.equals(reminderId, that.reminderId) &&
            Objects.equals(eventTimesId, that.eventTimesId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        type,
        goalPeriod,
        completionGoal,
        isGroupTracking,
        noteText,
        motivateText,
        isReminder,
        reminderId,
        eventTimesId
        );
    }

    @Override
    public String toString() {
        return "HabitCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (goalPeriod != null ? "goalPeriod=" + goalPeriod + ", " : "") +
                (completionGoal != null ? "completionGoal=" + completionGoal + ", " : "") +
                (isGroupTracking != null ? "isGroupTracking=" + isGroupTracking + ", " : "") +
                (noteText != null ? "noteText=" + noteText + ", " : "") +
                (motivateText != null ? "motivateText=" + motivateText + ", " : "") +
                (isReminder != null ? "isReminder=" + isReminder + ", " : "") +
                (reminderId != null ? "reminderId=" + reminderId + ", " : "") +
                (eventTimesId != null ? "eventTimesId=" + eventTimesId + ", " : "") +
            "}";
    }

}
