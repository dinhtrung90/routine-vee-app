package com.vdt.veeapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

import com.vdt.veeapp.domain.enumeration.HabitType;

import com.vdt.veeapp.domain.enumeration.Period;
import org.hibernate.annotations.Fetch;

/**
 * A Habit.
 */
@Entity
@Table(name = "habit")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Habit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private HabitType type;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "goal_period", nullable = false)
    private Period goalPeriod;

    @NotNull
    @Column(name = "completion_goal", nullable = false)
    private Double completionGoal;

    @NotNull
    @Column(name = "is_group_tracking", nullable = false)
    private Boolean isGroupTracking;

    @Column(name = "note_text")
    private String noteText;

    @Column(name = "motivate_text")
    private String motivateText;

    @NotNull
    @Column(name = "is_reminder", nullable = false)
    private Boolean isReminder;

    @OneToOne

    @MapsId
    @JoinColumn(name = "id")
    private Reminder reminder;

    @OneToMany(mappedBy = "habit", fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<EventTimes> eventTimes = new HashSet<>();

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

    public Habit name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HabitType getType() {
        return type;
    }

    public Habit type(HabitType type) {
        this.type = type;
        return this;
    }

    public void setType(HabitType type) {
        this.type = type;
    }

    public Period getGoalPeriod() {
        return goalPeriod;
    }

    public Habit goalPeriod(Period goalPeriod) {
        this.goalPeriod = goalPeriod;
        return this;
    }

    public void setGoalPeriod(Period goalPeriod) {
        this.goalPeriod = goalPeriod;
    }

    public Double getCompletionGoal() {
        return completionGoal;
    }

    public Habit completionGoal(Double completionGoal) {
        this.completionGoal = completionGoal;
        return this;
    }

    public void setCompletionGoal(Double completionGoal) {
        this.completionGoal = completionGoal;
    }

    public Boolean isIsGroupTracking() {
        return isGroupTracking;
    }

    public Habit isGroupTracking(Boolean isGroupTracking) {
        this.isGroupTracking = isGroupTracking;
        return this;
    }

    public void setIsGroupTracking(Boolean isGroupTracking) {
        this.isGroupTracking = isGroupTracking;
    }

    public String getNoteText() {
        return noteText;
    }

    public Habit noteText(String noteText) {
        this.noteText = noteText;
        return this;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public String getMotivateText() {
        return motivateText;
    }

    public Habit motivateText(String motivateText) {
        this.motivateText = motivateText;
        return this;
    }

    public void setMotivateText(String motivateText) {
        this.motivateText = motivateText;
    }

    public Boolean isIsReminder() {
        return isReminder;
    }

    public Habit isReminder(Boolean isReminder) {
        this.isReminder = isReminder;
        return this;
    }

    public void setIsReminder(Boolean isReminder) {
        this.isReminder = isReminder;
    }

    public Reminder getReminder() {
        return reminder;
    }

    public Habit reminder(Reminder reminder) {
        this.reminder = reminder;
        return this;
    }

    public void setReminder(Reminder reminder) {
        this.reminder = reminder;
    }

    public Set<EventTimes> getEventTimes() {
        return eventTimes;
    }

    public Habit eventTimes(Set<EventTimes> eventTimes) {
        this.eventTimes = eventTimes;
        return this;
    }

    public Habit addEventTimes(EventTimes eventTimes) {
        this.eventTimes.add(eventTimes);
        eventTimes.setHabit(this);
        return this;
    }

    public Habit removeEventTimes(EventTimes eventTimes) {
        this.eventTimes.remove(eventTimes);
        eventTimes.setHabit(null);
        return this;
    }

    public void setEventTimes(Set<EventTimes> eventTimes) {
        this.eventTimes = eventTimes;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Habit)) {
            return false;
        }
        return id != null && id.equals(((Habit) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Habit{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", goalPeriod='" + getGoalPeriod() + "'" +
            ", completionGoal=" + getCompletionGoal() +
            ", isGroupTracking='" + isIsGroupTracking() + "'" +
            ", noteText='" + getNoteText() + "'" +
            ", motivateText='" + getMotivateText() + "'" +
            ", isReminder='" + isIsReminder() + "'" +
            "}";
    }
}
