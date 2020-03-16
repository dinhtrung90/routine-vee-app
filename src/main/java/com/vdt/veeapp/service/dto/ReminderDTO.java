package com.vdt.veeapp.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.vdt.veeapp.domain.Reminder} entity.
 */
public class ReminderDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String reminderText;

    @NotNull
    private Instant date;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReminderText() {
        return reminderText;
    }

    public void setReminderText(String reminderText) {
        this.reminderText = reminderText;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
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

        ReminderDTO reminderDTO = (ReminderDTO) o;
        if (reminderDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), reminderDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ReminderDTO{" +
            "id=" + getId() +
            ", reminderText='" + getReminderText() + "'" +
            ", date='" + getDate() + "'" +
            "}";
    }
}
