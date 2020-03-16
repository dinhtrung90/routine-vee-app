package com.vdt.veeapp.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import com.vdt.veeapp.domain.enumeration.HabitType;
import com.vdt.veeapp.domain.enumeration.Period;

/**
 * A DTO for the {@link com.vdt.veeapp.domain.Habit} entity.
 */
public class HabitDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private HabitType type;

    @NotNull
    private Period goalPeriod;

    @NotNull
    private Double completionGoal;

    @NotNull
    private Boolean isGroupTracking;

    private String noteText;

    private String motivateText;

    @NotNull
    private Boolean isReminder;


    private Long reminderId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HabitType getType() {
        return type;
    }

    public void setType(HabitType type) {
        this.type = type;
    }

    public Period getGoalPeriod() {
        return goalPeriod;
    }

    public void setGoalPeriod(Period goalPeriod) {
        this.goalPeriod = goalPeriod;
    }

    public Double getCompletionGoal() {
        return completionGoal;
    }

    public void setCompletionGoal(Double completionGoal) {
        this.completionGoal = completionGoal;
    }

    public Boolean isIsGroupTracking() {
        return isGroupTracking;
    }

    public void setIsGroupTracking(Boolean isGroupTracking) {
        this.isGroupTracking = isGroupTracking;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public String getMotivateText() {
        return motivateText;
    }

    public void setMotivateText(String motivateText) {
        this.motivateText = motivateText;
    }

    public Boolean isIsReminder() {
        return isReminder;
    }

    public void setIsReminder(Boolean isReminder) {
        this.isReminder = isReminder;
    }

    public Long getReminderId() {
        return reminderId;
    }

    public void setReminderId(Long reminderId) {
        this.reminderId = reminderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        HabitDTO habitDTO = (HabitDTO) o;
        if (habitDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), habitDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "HabitDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", goalPeriod='" + getGoalPeriod() + "'" +
            ", completionGoal=" + getCompletionGoal() +
            ", isGroupTracking='" + isIsGroupTracking() + "'" +
            ", noteText='" + getNoteText() + "'" +
            ", motivateText='" + getMotivateText() + "'" +
            ", isReminder='" + isIsReminder() + "'" +
            ", reminderId=" + getReminderId() +
            "}";
    }
}
