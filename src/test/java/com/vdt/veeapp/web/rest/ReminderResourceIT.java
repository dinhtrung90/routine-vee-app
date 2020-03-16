package com.vdt.veeapp.web.rest;

import com.vdt.veeapp.RoutineveeApp;
import com.vdt.veeapp.domain.Reminder;
import com.vdt.veeapp.repository.ReminderRepository;
import com.vdt.veeapp.service.ReminderService;
import com.vdt.veeapp.service.dto.ReminderDTO;
import com.vdt.veeapp.service.mapper.ReminderMapper;
import com.vdt.veeapp.service.dto.ReminderCriteria;
import com.vdt.veeapp.service.ReminderQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ReminderResource} REST controller.
 */
@SpringBootTest(classes = RoutineveeApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class ReminderResourceIT {

    private static final String DEFAULT_REMINDER_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_REMINDER_TEXT = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private ReminderRepository reminderRepository;

    @Autowired
    private ReminderMapper reminderMapper;

    @Autowired
    private ReminderService reminderService;

    @Autowired
    private ReminderQueryService reminderQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReminderMockMvc;

    private Reminder reminder;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reminder createEntity(EntityManager em) {
        Reminder reminder = new Reminder()
            .reminderText(DEFAULT_REMINDER_TEXT)
            .date(DEFAULT_DATE);
        return reminder;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reminder createUpdatedEntity(EntityManager em) {
        Reminder reminder = new Reminder()
            .reminderText(UPDATED_REMINDER_TEXT)
            .date(UPDATED_DATE);
        return reminder;
    }

    @BeforeEach
    public void initTest() {
        reminder = createEntity(em);
    }

    @Test
    @Transactional
    public void createReminder() throws Exception {
        int databaseSizeBeforeCreate = reminderRepository.findAll().size();

        // Create the Reminder
        ReminderDTO reminderDTO = reminderMapper.toDto(reminder);
        restReminderMockMvc.perform(post("/api/reminders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(reminderDTO)))
            .andExpect(status().isCreated());

        // Validate the Reminder in the database
        List<Reminder> reminderList = reminderRepository.findAll();
        assertThat(reminderList).hasSize(databaseSizeBeforeCreate + 1);
        Reminder testReminder = reminderList.get(reminderList.size() - 1);
        assertThat(testReminder.getReminderText()).isEqualTo(DEFAULT_REMINDER_TEXT);
        assertThat(testReminder.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void createReminderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = reminderRepository.findAll().size();

        // Create the Reminder with an existing ID
        reminder.setId(1L);
        ReminderDTO reminderDTO = reminderMapper.toDto(reminder);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReminderMockMvc.perform(post("/api/reminders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(reminderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Reminder in the database
        List<Reminder> reminderList = reminderRepository.findAll();
        assertThat(reminderList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkReminderTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = reminderRepository.findAll().size();
        // set the field null
        reminder.setReminderText(null);

        // Create the Reminder, which fails.
        ReminderDTO reminderDTO = reminderMapper.toDto(reminder);

        restReminderMockMvc.perform(post("/api/reminders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(reminderDTO)))
            .andExpect(status().isBadRequest());

        List<Reminder> reminderList = reminderRepository.findAll();
        assertThat(reminderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = reminderRepository.findAll().size();
        // set the field null
        reminder.setDate(null);

        // Create the Reminder, which fails.
        ReminderDTO reminderDTO = reminderMapper.toDto(reminder);

        restReminderMockMvc.perform(post("/api/reminders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(reminderDTO)))
            .andExpect(status().isBadRequest());

        List<Reminder> reminderList = reminderRepository.findAll();
        assertThat(reminderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllReminders() throws Exception {
        // Initialize the database
        reminderRepository.saveAndFlush(reminder);

        // Get all the reminderList
        restReminderMockMvc.perform(get("/api/reminders?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reminder.getId().intValue())))
            .andExpect(jsonPath("$.[*].reminderText").value(hasItem(DEFAULT_REMINDER_TEXT)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getReminder() throws Exception {
        // Initialize the database
        reminderRepository.saveAndFlush(reminder);

        // Get the reminder
        restReminderMockMvc.perform(get("/api/reminders/{id}", reminder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reminder.getId().intValue()))
            .andExpect(jsonPath("$.reminderText").value(DEFAULT_REMINDER_TEXT))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }


    @Test
    @Transactional
    public void getRemindersByIdFiltering() throws Exception {
        // Initialize the database
        reminderRepository.saveAndFlush(reminder);

        Long id = reminder.getId();

        defaultReminderShouldBeFound("id.equals=" + id);
        defaultReminderShouldNotBeFound("id.notEquals=" + id);

        defaultReminderShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultReminderShouldNotBeFound("id.greaterThan=" + id);

        defaultReminderShouldBeFound("id.lessThanOrEqual=" + id);
        defaultReminderShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllRemindersByReminderTextIsEqualToSomething() throws Exception {
        // Initialize the database
        reminderRepository.saveAndFlush(reminder);

        // Get all the reminderList where reminderText equals to DEFAULT_REMINDER_TEXT
        defaultReminderShouldBeFound("reminderText.equals=" + DEFAULT_REMINDER_TEXT);

        // Get all the reminderList where reminderText equals to UPDATED_REMINDER_TEXT
        defaultReminderShouldNotBeFound("reminderText.equals=" + UPDATED_REMINDER_TEXT);
    }

    @Test
    @Transactional
    public void getAllRemindersByReminderTextIsNotEqualToSomething() throws Exception {
        // Initialize the database
        reminderRepository.saveAndFlush(reminder);

        // Get all the reminderList where reminderText not equals to DEFAULT_REMINDER_TEXT
        defaultReminderShouldNotBeFound("reminderText.notEquals=" + DEFAULT_REMINDER_TEXT);

        // Get all the reminderList where reminderText not equals to UPDATED_REMINDER_TEXT
        defaultReminderShouldBeFound("reminderText.notEquals=" + UPDATED_REMINDER_TEXT);
    }

    @Test
    @Transactional
    public void getAllRemindersByReminderTextIsInShouldWork() throws Exception {
        // Initialize the database
        reminderRepository.saveAndFlush(reminder);

        // Get all the reminderList where reminderText in DEFAULT_REMINDER_TEXT or UPDATED_REMINDER_TEXT
        defaultReminderShouldBeFound("reminderText.in=" + DEFAULT_REMINDER_TEXT + "," + UPDATED_REMINDER_TEXT);

        // Get all the reminderList where reminderText equals to UPDATED_REMINDER_TEXT
        defaultReminderShouldNotBeFound("reminderText.in=" + UPDATED_REMINDER_TEXT);
    }

    @Test
    @Transactional
    public void getAllRemindersByReminderTextIsNullOrNotNull() throws Exception {
        // Initialize the database
        reminderRepository.saveAndFlush(reminder);

        // Get all the reminderList where reminderText is not null
        defaultReminderShouldBeFound("reminderText.specified=true");

        // Get all the reminderList where reminderText is null
        defaultReminderShouldNotBeFound("reminderText.specified=false");
    }
                @Test
    @Transactional
    public void getAllRemindersByReminderTextContainsSomething() throws Exception {
        // Initialize the database
        reminderRepository.saveAndFlush(reminder);

        // Get all the reminderList where reminderText contains DEFAULT_REMINDER_TEXT
        defaultReminderShouldBeFound("reminderText.contains=" + DEFAULT_REMINDER_TEXT);

        // Get all the reminderList where reminderText contains UPDATED_REMINDER_TEXT
        defaultReminderShouldNotBeFound("reminderText.contains=" + UPDATED_REMINDER_TEXT);
    }

    @Test
    @Transactional
    public void getAllRemindersByReminderTextNotContainsSomething() throws Exception {
        // Initialize the database
        reminderRepository.saveAndFlush(reminder);

        // Get all the reminderList where reminderText does not contain DEFAULT_REMINDER_TEXT
        defaultReminderShouldNotBeFound("reminderText.doesNotContain=" + DEFAULT_REMINDER_TEXT);

        // Get all the reminderList where reminderText does not contain UPDATED_REMINDER_TEXT
        defaultReminderShouldBeFound("reminderText.doesNotContain=" + UPDATED_REMINDER_TEXT);
    }


    @Test
    @Transactional
    public void getAllRemindersByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        reminderRepository.saveAndFlush(reminder);

        // Get all the reminderList where date equals to DEFAULT_DATE
        defaultReminderShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the reminderList where date equals to UPDATED_DATE
        defaultReminderShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllRemindersByDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        reminderRepository.saveAndFlush(reminder);

        // Get all the reminderList where date not equals to DEFAULT_DATE
        defaultReminderShouldNotBeFound("date.notEquals=" + DEFAULT_DATE);

        // Get all the reminderList where date not equals to UPDATED_DATE
        defaultReminderShouldBeFound("date.notEquals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllRemindersByDateIsInShouldWork() throws Exception {
        // Initialize the database
        reminderRepository.saveAndFlush(reminder);

        // Get all the reminderList where date in DEFAULT_DATE or UPDATED_DATE
        defaultReminderShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the reminderList where date equals to UPDATED_DATE
        defaultReminderShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllRemindersByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        reminderRepository.saveAndFlush(reminder);

        // Get all the reminderList where date is not null
        defaultReminderShouldBeFound("date.specified=true");

        // Get all the reminderList where date is null
        defaultReminderShouldNotBeFound("date.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultReminderShouldBeFound(String filter) throws Exception {
        restReminderMockMvc.perform(get("/api/reminders?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reminder.getId().intValue())))
            .andExpect(jsonPath("$.[*].reminderText").value(hasItem(DEFAULT_REMINDER_TEXT)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));

        // Check, that the count call also returns 1
        restReminderMockMvc.perform(get("/api/reminders/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultReminderShouldNotBeFound(String filter) throws Exception {
        restReminderMockMvc.perform(get("/api/reminders?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restReminderMockMvc.perform(get("/api/reminders/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingReminder() throws Exception {
        // Get the reminder
        restReminderMockMvc.perform(get("/api/reminders/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReminder() throws Exception {
        // Initialize the database
        reminderRepository.saveAndFlush(reminder);

        int databaseSizeBeforeUpdate = reminderRepository.findAll().size();

        // Update the reminder
        Reminder updatedReminder = reminderRepository.findById(reminder.getId()).get();
        // Disconnect from session so that the updates on updatedReminder are not directly saved in db
        em.detach(updatedReminder);
        updatedReminder
            .reminderText(UPDATED_REMINDER_TEXT)
            .date(UPDATED_DATE);
        ReminderDTO reminderDTO = reminderMapper.toDto(updatedReminder);

        restReminderMockMvc.perform(put("/api/reminders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(reminderDTO)))
            .andExpect(status().isOk());

        // Validate the Reminder in the database
        List<Reminder> reminderList = reminderRepository.findAll();
        assertThat(reminderList).hasSize(databaseSizeBeforeUpdate);
        Reminder testReminder = reminderList.get(reminderList.size() - 1);
        assertThat(testReminder.getReminderText()).isEqualTo(UPDATED_REMINDER_TEXT);
        assertThat(testReminder.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingReminder() throws Exception {
        int databaseSizeBeforeUpdate = reminderRepository.findAll().size();

        // Create the Reminder
        ReminderDTO reminderDTO = reminderMapper.toDto(reminder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReminderMockMvc.perform(put("/api/reminders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(reminderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Reminder in the database
        List<Reminder> reminderList = reminderRepository.findAll();
        assertThat(reminderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteReminder() throws Exception {
        // Initialize the database
        reminderRepository.saveAndFlush(reminder);

        int databaseSizeBeforeDelete = reminderRepository.findAll().size();

        // Delete the reminder
        restReminderMockMvc.perform(delete("/api/reminders/{id}", reminder.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Reminder> reminderList = reminderRepository.findAll();
        assertThat(reminderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
