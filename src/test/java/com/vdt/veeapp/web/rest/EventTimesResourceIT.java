package com.vdt.veeapp.web.rest;

import com.vdt.veeapp.RoutineveeApp;
import com.vdt.veeapp.domain.EventTimes;
import com.vdt.veeapp.domain.Habit;
import com.vdt.veeapp.repository.EventTimesRepository;
import com.vdt.veeapp.service.EventTimesService;
import com.vdt.veeapp.service.dto.EventTimesDTO;
import com.vdt.veeapp.service.mapper.EventTimesMapper;
import com.vdt.veeapp.service.dto.EventTimesCriteria;
import com.vdt.veeapp.service.EventTimesQueryService;

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
 * Integration tests for the {@link EventTimesResource} REST controller.
 */
@SpringBootTest(classes = RoutineveeApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class EventTimesResourceIT {

    private static final Integer DEFAULT_DAY_OF_WEEK = 1;
    private static final Integer UPDATED_DAY_OF_WEEK = 2;
    private static final Integer SMALLER_DAY_OF_WEEK = 1 - 1;

    private static final Instant DEFAULT_START_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private EventTimesRepository eventTimesRepository;

    @Autowired
    private EventTimesMapper eventTimesMapper;

    @Autowired
    private EventTimesService eventTimesService;

    @Autowired
    private EventTimesQueryService eventTimesQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEventTimesMockMvc;

    private EventTimes eventTimes;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventTimes createEntity(EntityManager em) {
        EventTimes eventTimes = new EventTimes()
            .dayOfWeek(DEFAULT_DAY_OF_WEEK)
            .startTime(DEFAULT_START_TIME)
            .endTime(DEFAULT_END_TIME);
        return eventTimes;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventTimes createUpdatedEntity(EntityManager em) {
        EventTimes eventTimes = new EventTimes()
            .dayOfWeek(UPDATED_DAY_OF_WEEK)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME);
        return eventTimes;
    }

    @BeforeEach
    public void initTest() {
        eventTimes = createEntity(em);
    }

    @Test
    @Transactional
    public void createEventTimes() throws Exception {
        int databaseSizeBeforeCreate = eventTimesRepository.findAll().size();

        // Create the EventTimes
        EventTimesDTO eventTimesDTO = eventTimesMapper.toDto(eventTimes);
        restEventTimesMockMvc.perform(post("/api/event-times")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(eventTimesDTO)))
            .andExpect(status().isCreated());

        // Validate the EventTimes in the database
        List<EventTimes> eventTimesList = eventTimesRepository.findAll();
        assertThat(eventTimesList).hasSize(databaseSizeBeforeCreate + 1);
        EventTimes testEventTimes = eventTimesList.get(eventTimesList.size() - 1);
        assertThat(testEventTimes.getDayOfWeek()).isEqualTo(DEFAULT_DAY_OF_WEEK);
        assertThat(testEventTimes.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testEventTimes.getEndTime()).isEqualTo(DEFAULT_END_TIME);
    }

    @Test
    @Transactional
    public void createEventTimesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = eventTimesRepository.findAll().size();

        // Create the EventTimes with an existing ID
        eventTimes.setId(1L);
        EventTimesDTO eventTimesDTO = eventTimesMapper.toDto(eventTimes);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventTimesMockMvc.perform(post("/api/event-times")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(eventTimesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EventTimes in the database
        List<EventTimes> eventTimesList = eventTimesRepository.findAll();
        assertThat(eventTimesList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkDayOfWeekIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventTimesRepository.findAll().size();
        // set the field null
        eventTimes.setDayOfWeek(null);

        // Create the EventTimes, which fails.
        EventTimesDTO eventTimesDTO = eventTimesMapper.toDto(eventTimes);

        restEventTimesMockMvc.perform(post("/api/event-times")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(eventTimesDTO)))
            .andExpect(status().isBadRequest());

        List<EventTimes> eventTimesList = eventTimesRepository.findAll();
        assertThat(eventTimesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStartTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventTimesRepository.findAll().size();
        // set the field null
        eventTimes.setStartTime(null);

        // Create the EventTimes, which fails.
        EventTimesDTO eventTimesDTO = eventTimesMapper.toDto(eventTimes);

        restEventTimesMockMvc.perform(post("/api/event-times")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(eventTimesDTO)))
            .andExpect(status().isBadRequest());

        List<EventTimes> eventTimesList = eventTimesRepository.findAll();
        assertThat(eventTimesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventTimesRepository.findAll().size();
        // set the field null
        eventTimes.setEndTime(null);

        // Create the EventTimes, which fails.
        EventTimesDTO eventTimesDTO = eventTimesMapper.toDto(eventTimes);

        restEventTimesMockMvc.perform(post("/api/event-times")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(eventTimesDTO)))
            .andExpect(status().isBadRequest());

        List<EventTimes> eventTimesList = eventTimesRepository.findAll();
        assertThat(eventTimesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEventTimes() throws Exception {
        // Initialize the database
        eventTimesRepository.saveAndFlush(eventTimes);

        // Get all the eventTimesList
        restEventTimesMockMvc.perform(get("/api/event-times?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventTimes.getId().intValue())))
            .andExpect(jsonPath("$.[*].dayOfWeek").value(hasItem(DEFAULT_DAY_OF_WEEK)))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())));
    }
    
    @Test
    @Transactional
    public void getEventTimes() throws Exception {
        // Initialize the database
        eventTimesRepository.saveAndFlush(eventTimes);

        // Get the eventTimes
        restEventTimesMockMvc.perform(get("/api/event-times/{id}", eventTimes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(eventTimes.getId().intValue()))
            .andExpect(jsonPath("$.dayOfWeek").value(DEFAULT_DAY_OF_WEEK))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME.toString()))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME.toString()));
    }


    @Test
    @Transactional
    public void getEventTimesByIdFiltering() throws Exception {
        // Initialize the database
        eventTimesRepository.saveAndFlush(eventTimes);

        Long id = eventTimes.getId();

        defaultEventTimesShouldBeFound("id.equals=" + id);
        defaultEventTimesShouldNotBeFound("id.notEquals=" + id);

        defaultEventTimesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEventTimesShouldNotBeFound("id.greaterThan=" + id);

        defaultEventTimesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEventTimesShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllEventTimesByDayOfWeekIsEqualToSomething() throws Exception {
        // Initialize the database
        eventTimesRepository.saveAndFlush(eventTimes);

        // Get all the eventTimesList where dayOfWeek equals to DEFAULT_DAY_OF_WEEK
        defaultEventTimesShouldBeFound("dayOfWeek.equals=" + DEFAULT_DAY_OF_WEEK);

        // Get all the eventTimesList where dayOfWeek equals to UPDATED_DAY_OF_WEEK
        defaultEventTimesShouldNotBeFound("dayOfWeek.equals=" + UPDATED_DAY_OF_WEEK);
    }

    @Test
    @Transactional
    public void getAllEventTimesByDayOfWeekIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventTimesRepository.saveAndFlush(eventTimes);

        // Get all the eventTimesList where dayOfWeek not equals to DEFAULT_DAY_OF_WEEK
        defaultEventTimesShouldNotBeFound("dayOfWeek.notEquals=" + DEFAULT_DAY_OF_WEEK);

        // Get all the eventTimesList where dayOfWeek not equals to UPDATED_DAY_OF_WEEK
        defaultEventTimesShouldBeFound("dayOfWeek.notEquals=" + UPDATED_DAY_OF_WEEK);
    }

    @Test
    @Transactional
    public void getAllEventTimesByDayOfWeekIsInShouldWork() throws Exception {
        // Initialize the database
        eventTimesRepository.saveAndFlush(eventTimes);

        // Get all the eventTimesList where dayOfWeek in DEFAULT_DAY_OF_WEEK or UPDATED_DAY_OF_WEEK
        defaultEventTimesShouldBeFound("dayOfWeek.in=" + DEFAULT_DAY_OF_WEEK + "," + UPDATED_DAY_OF_WEEK);

        // Get all the eventTimesList where dayOfWeek equals to UPDATED_DAY_OF_WEEK
        defaultEventTimesShouldNotBeFound("dayOfWeek.in=" + UPDATED_DAY_OF_WEEK);
    }

    @Test
    @Transactional
    public void getAllEventTimesByDayOfWeekIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventTimesRepository.saveAndFlush(eventTimes);

        // Get all the eventTimesList where dayOfWeek is not null
        defaultEventTimesShouldBeFound("dayOfWeek.specified=true");

        // Get all the eventTimesList where dayOfWeek is null
        defaultEventTimesShouldNotBeFound("dayOfWeek.specified=false");
    }

    @Test
    @Transactional
    public void getAllEventTimesByDayOfWeekIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventTimesRepository.saveAndFlush(eventTimes);

        // Get all the eventTimesList where dayOfWeek is greater than or equal to DEFAULT_DAY_OF_WEEK
        defaultEventTimesShouldBeFound("dayOfWeek.greaterThanOrEqual=" + DEFAULT_DAY_OF_WEEK);

        // Get all the eventTimesList where dayOfWeek is greater than or equal to UPDATED_DAY_OF_WEEK
        defaultEventTimesShouldNotBeFound("dayOfWeek.greaterThanOrEqual=" + UPDATED_DAY_OF_WEEK);
    }

    @Test
    @Transactional
    public void getAllEventTimesByDayOfWeekIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventTimesRepository.saveAndFlush(eventTimes);

        // Get all the eventTimesList where dayOfWeek is less than or equal to DEFAULT_DAY_OF_WEEK
        defaultEventTimesShouldBeFound("dayOfWeek.lessThanOrEqual=" + DEFAULT_DAY_OF_WEEK);

        // Get all the eventTimesList where dayOfWeek is less than or equal to SMALLER_DAY_OF_WEEK
        defaultEventTimesShouldNotBeFound("dayOfWeek.lessThanOrEqual=" + SMALLER_DAY_OF_WEEK);
    }

    @Test
    @Transactional
    public void getAllEventTimesByDayOfWeekIsLessThanSomething() throws Exception {
        // Initialize the database
        eventTimesRepository.saveAndFlush(eventTimes);

        // Get all the eventTimesList where dayOfWeek is less than DEFAULT_DAY_OF_WEEK
        defaultEventTimesShouldNotBeFound("dayOfWeek.lessThan=" + DEFAULT_DAY_OF_WEEK);

        // Get all the eventTimesList where dayOfWeek is less than UPDATED_DAY_OF_WEEK
        defaultEventTimesShouldBeFound("dayOfWeek.lessThan=" + UPDATED_DAY_OF_WEEK);
    }

    @Test
    @Transactional
    public void getAllEventTimesByDayOfWeekIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventTimesRepository.saveAndFlush(eventTimes);

        // Get all the eventTimesList where dayOfWeek is greater than DEFAULT_DAY_OF_WEEK
        defaultEventTimesShouldNotBeFound("dayOfWeek.greaterThan=" + DEFAULT_DAY_OF_WEEK);

        // Get all the eventTimesList where dayOfWeek is greater than SMALLER_DAY_OF_WEEK
        defaultEventTimesShouldBeFound("dayOfWeek.greaterThan=" + SMALLER_DAY_OF_WEEK);
    }


    @Test
    @Transactional
    public void getAllEventTimesByStartTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        eventTimesRepository.saveAndFlush(eventTimes);

        // Get all the eventTimesList where startTime equals to DEFAULT_START_TIME
        defaultEventTimesShouldBeFound("startTime.equals=" + DEFAULT_START_TIME);

        // Get all the eventTimesList where startTime equals to UPDATED_START_TIME
        defaultEventTimesShouldNotBeFound("startTime.equals=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    public void getAllEventTimesByStartTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventTimesRepository.saveAndFlush(eventTimes);

        // Get all the eventTimesList where startTime not equals to DEFAULT_START_TIME
        defaultEventTimesShouldNotBeFound("startTime.notEquals=" + DEFAULT_START_TIME);

        // Get all the eventTimesList where startTime not equals to UPDATED_START_TIME
        defaultEventTimesShouldBeFound("startTime.notEquals=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    public void getAllEventTimesByStartTimeIsInShouldWork() throws Exception {
        // Initialize the database
        eventTimesRepository.saveAndFlush(eventTimes);

        // Get all the eventTimesList where startTime in DEFAULT_START_TIME or UPDATED_START_TIME
        defaultEventTimesShouldBeFound("startTime.in=" + DEFAULT_START_TIME + "," + UPDATED_START_TIME);

        // Get all the eventTimesList where startTime equals to UPDATED_START_TIME
        defaultEventTimesShouldNotBeFound("startTime.in=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    public void getAllEventTimesByStartTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventTimesRepository.saveAndFlush(eventTimes);

        // Get all the eventTimesList where startTime is not null
        defaultEventTimesShouldBeFound("startTime.specified=true");

        // Get all the eventTimesList where startTime is null
        defaultEventTimesShouldNotBeFound("startTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllEventTimesByEndTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        eventTimesRepository.saveAndFlush(eventTimes);

        // Get all the eventTimesList where endTime equals to DEFAULT_END_TIME
        defaultEventTimesShouldBeFound("endTime.equals=" + DEFAULT_END_TIME);

        // Get all the eventTimesList where endTime equals to UPDATED_END_TIME
        defaultEventTimesShouldNotBeFound("endTime.equals=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    public void getAllEventTimesByEndTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventTimesRepository.saveAndFlush(eventTimes);

        // Get all the eventTimesList where endTime not equals to DEFAULT_END_TIME
        defaultEventTimesShouldNotBeFound("endTime.notEquals=" + DEFAULT_END_TIME);

        // Get all the eventTimesList where endTime not equals to UPDATED_END_TIME
        defaultEventTimesShouldBeFound("endTime.notEquals=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    public void getAllEventTimesByEndTimeIsInShouldWork() throws Exception {
        // Initialize the database
        eventTimesRepository.saveAndFlush(eventTimes);

        // Get all the eventTimesList where endTime in DEFAULT_END_TIME or UPDATED_END_TIME
        defaultEventTimesShouldBeFound("endTime.in=" + DEFAULT_END_TIME + "," + UPDATED_END_TIME);

        // Get all the eventTimesList where endTime equals to UPDATED_END_TIME
        defaultEventTimesShouldNotBeFound("endTime.in=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    public void getAllEventTimesByEndTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventTimesRepository.saveAndFlush(eventTimes);

        // Get all the eventTimesList where endTime is not null
        defaultEventTimesShouldBeFound("endTime.specified=true");

        // Get all the eventTimesList where endTime is null
        defaultEventTimesShouldNotBeFound("endTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllEventTimesByHabitIsEqualToSomething() throws Exception {
        // Initialize the database
        eventTimesRepository.saveAndFlush(eventTimes);
        Habit habit = HabitResourceIT.createEntity(em);
        em.persist(habit);
        em.flush();
        eventTimes.setHabit(habit);
        eventTimesRepository.saveAndFlush(eventTimes);
        Long habitId = habit.getId();

        // Get all the eventTimesList where habit equals to habitId
        defaultEventTimesShouldBeFound("habitId.equals=" + habitId);

        // Get all the eventTimesList where habit equals to habitId + 1
        defaultEventTimesShouldNotBeFound("habitId.equals=" + (habitId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEventTimesShouldBeFound(String filter) throws Exception {
        restEventTimesMockMvc.perform(get("/api/event-times?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventTimes.getId().intValue())))
            .andExpect(jsonPath("$.[*].dayOfWeek").value(hasItem(DEFAULT_DAY_OF_WEEK)))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())));

        // Check, that the count call also returns 1
        restEventTimesMockMvc.perform(get("/api/event-times/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEventTimesShouldNotBeFound(String filter) throws Exception {
        restEventTimesMockMvc.perform(get("/api/event-times?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEventTimesMockMvc.perform(get("/api/event-times/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingEventTimes() throws Exception {
        // Get the eventTimes
        restEventTimesMockMvc.perform(get("/api/event-times/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEventTimes() throws Exception {
        // Initialize the database
        eventTimesRepository.saveAndFlush(eventTimes);

        int databaseSizeBeforeUpdate = eventTimesRepository.findAll().size();

        // Update the eventTimes
        EventTimes updatedEventTimes = eventTimesRepository.findById(eventTimes.getId()).get();
        // Disconnect from session so that the updates on updatedEventTimes are not directly saved in db
        em.detach(updatedEventTimes);
        updatedEventTimes
            .dayOfWeek(UPDATED_DAY_OF_WEEK)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME);
        EventTimesDTO eventTimesDTO = eventTimesMapper.toDto(updatedEventTimes);

        restEventTimesMockMvc.perform(put("/api/event-times")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(eventTimesDTO)))
            .andExpect(status().isOk());

        // Validate the EventTimes in the database
        List<EventTimes> eventTimesList = eventTimesRepository.findAll();
        assertThat(eventTimesList).hasSize(databaseSizeBeforeUpdate);
        EventTimes testEventTimes = eventTimesList.get(eventTimesList.size() - 1);
        assertThat(testEventTimes.getDayOfWeek()).isEqualTo(UPDATED_DAY_OF_WEEK);
        assertThat(testEventTimes.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testEventTimes.getEndTime()).isEqualTo(UPDATED_END_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingEventTimes() throws Exception {
        int databaseSizeBeforeUpdate = eventTimesRepository.findAll().size();

        // Create the EventTimes
        EventTimesDTO eventTimesDTO = eventTimesMapper.toDto(eventTimes);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventTimesMockMvc.perform(put("/api/event-times")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(eventTimesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EventTimes in the database
        List<EventTimes> eventTimesList = eventTimesRepository.findAll();
        assertThat(eventTimesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEventTimes() throws Exception {
        // Initialize the database
        eventTimesRepository.saveAndFlush(eventTimes);

        int databaseSizeBeforeDelete = eventTimesRepository.findAll().size();

        // Delete the eventTimes
        restEventTimesMockMvc.perform(delete("/api/event-times/{id}", eventTimes.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EventTimes> eventTimesList = eventTimesRepository.findAll();
        assertThat(eventTimesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
