package com.vdt.veeapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.Instant;

/**
 * A Reminder.
 */
@Entity
@Table(name = "reminder")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Reminder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "reminder_text", nullable = false)
    private String reminderText;

    @NotNull
    @Column(name = "date", nullable = false)
    private Instant date;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReminderText() {
        return reminderText;
    }

    public Reminder reminderText(String reminderText) {
        this.reminderText = reminderText;
        return this;
    }

    public void setReminderText(String reminderText) {
        this.reminderText = reminderText;
    }

    public Instant getDate() {
        return date;
    }

    public Reminder date(Instant date) {
        this.date = date;
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reminder)) {
            return false;
        }
        return id != null && id.equals(((Reminder) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Reminder{" +
            "id=" + getId() +
            ", reminderText='" + getReminderText() + "'" +
            ", date='" + getDate() + "'" +
            "}";
    }
}
