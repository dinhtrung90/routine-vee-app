package com.vdt.veeapp.web.rest;

import com.vdt.veeapp.RoutineveeApp;
import com.vdt.veeapp.domain.Habit;
import com.vdt.veeapp.domain.Reminder;
import com.vdt.veeapp.domain.EventTimes;
import com.vdt.veeapp.repository.HabitRepository;
import com.vdt.veeapp.service.HabitService;
import com.vdt.veeapp.service.dto.HabitDTO;
import com.vdt.veeapp.service.mapper.HabitMapper;
import com.vdt.veeapp.service.dto.HabitCriteria;
import com.vdt.veeapp.service.HabitQueryService;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.vdt.veeapp.domain.enumeration.HabitType;
import com.vdt.veeapp.domain.enumeration.Period;
/**
 * Integration tests for the {@link HabitResource} REST controller.
 */
@SpringBootTest(classes = RoutineveeApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class HabitResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final HabitType DEFAULT_TYPE = HabitType.BUILD;
    private static final HabitType UPDATED_TYPE = HabitType.QUIT;

    private static final Period DEFAULT_GOAL_PERIOD = Period.DAILY;
    private static final Period UPDATED_GOAL_PERIOD = Period.WEEKLY;

    private static final Double DEFAULT_COMPLETION_GOAL = 1D;
    private static final Double UPDATED_COMPLETION_GOAL = 2D;
    private static final Double SMALLER_COMPLETION_GOAL = 1D - 1D;

    private static final Boolean DEFAULT_IS_GROUP_TRACKING = false;
    private static final Boolean UPDATED_IS_GROUP_TRACKING = true;

    private static final String DEFAULT_NOTE_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_NOTE_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_MOTIVATE_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_MOTIVATE_TEXT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_REMINDER = false;
    private static final Boolean UPDATED_IS_REMINDER = true;

    @Autowired
    private HabitRepository habitRepository;

    @Autowired
    private HabitMapper habitMapper;

    @Autowired
    private HabitService habitService;

    @Autowired
    private HabitQueryService habitQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHabitMockMvc;

    private Habit habit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Habit createEntity(EntityManager em) {
        Habit habit = new Habit()
            .name(DEFAULT_NAME)
            .type(DEFAULT_TYPE)
            .goalPeriod(DEFAULT_GOAL_PERIOD)
            .completionGoal(DEFAULT_COMPLETION_GOAL)
            .isGroupTracking(DEFAULT_IS_GROUP_TRACKING)
            .noteText(DEFAULT_NOTE_TEXT)
            .motivateText(DEFAULT_MOTIVATE_TEXT)
            .isReminder(DEFAULT_IS_REMINDER);
        // Add required entity
        Reminder reminder;
        if (TestUtil.findAll(em, Reminder.class).isEmpty()) {
            reminder = ReminderResourceIT.createEntity(em);
            em.persist(reminder);
            em.flush();
        } else {
            reminder = TestUtil.findAll(em, Reminder.class).get(0);
        }
        habit.setReminder(reminder);
        return habit;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Habit createUpdatedEntity(EntityManager em) {
        Habit habit = new Habit()
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .goalPeriod(UPDATED_GOAL_PERIOD)
            .completionGoal(UPDATED_COMPLETION_GOAL)
            .isGroupTracking(UPDATED_IS_GROUP_TRACKING)
            .noteText(UPDATED_NOTE_TEXT)
            .motivateText(UPDATED_MOTIVATE_TEXT)
            .isReminder(UPDATED_IS_REMINDER);
        // Add required entity
        Reminder reminder;
        if (TestUtil.findAll(em, Reminder.class).isEmpty()) {
            reminder = ReminderResourceIT.createUpdatedEntity(em);
            em.persist(reminder);
            em.flush();
        } else {
            reminder = TestUtil.findAll(em, Reminder.class).get(0);
        }
        habit.setReminder(reminder);
        return habit;
    }

    @BeforeEach
    public void initTest() {
        habit = createEntity(em);
    }

    @Test
    @Transactional
    public void createHabit() throws Exception {
        int databaseSizeBeforeCreate = habitRepository.findAll().size();

        // Create the Habit
        HabitDTO habitDTO = habitMapper.toDto(habit);
        restHabitMockMvc.perform(post("/api/habits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(habitDTO)))
            .andExpect(status().isCreated());

        // Validate the Habit in the database
        List<Habit> habitList = habitRepository.findAll();
        assertThat(habitList).hasSize(databaseSizeBeforeCreate + 1);
        Habit testHabit = habitList.get(habitList.size() - 1);
        assertThat(testHabit.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testHabit.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testHabit.getGoalPeriod()).isEqualTo(DEFAULT_GOAL_PERIOD);
        assertThat(testHabit.getCompletionGoal()).isEqualTo(DEFAULT_COMPLETION_GOAL);
        assertThat(testHabit.isIsGroupTracking()).isEqualTo(DEFAULT_IS_GROUP_TRACKING);
        assertThat(testHabit.getNoteText()).isEqualTo(DEFAULT_NOTE_TEXT);
        assertThat(testHabit.getMotivateText()).isEqualTo(DEFAULT_MOTIVATE_TEXT);
        assertThat(testHabit.isIsReminder()).isEqualTo(DEFAULT_IS_REMINDER);

        // Validate the id for MapsId, the ids must be same
        assertThat(testHabit.getId()).isEqualTo(testHabit.getReminder().getId());
    }

    @Test
    @Transactional
    public void createHabitWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = habitRepository.findAll().size();

        // Create the Habit with an existing ID
        habit.setId(1L);
        HabitDTO habitDTO = habitMapper.toDto(habit);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHabitMockMvc.perform(post("/api/habits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(habitDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Habit in the database
        List<Habit> habitList = habitRepository.findAll();
        assertThat(habitList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void updateHabitMapsIdAssociationWithNewId() throws Exception {
        // Initialize the database
        habitRepository.saveAndFlush(habit);
        int databaseSizeBeforeCreate = habitRepository.findAll().size();

        // Add a new parent entity
        Reminder reminder = ReminderResourceIT.createUpdatedEntity(em);
        em.persist(reminder);
        em.flush();

        // Load the habit
        Habit updatedHabit = habitRepository.findById(habit.getId()).get();
        // Disconnect from session so that the updates on updatedHabit are not directly saved in db
        em.detach(updatedHabit);

        // Update the Reminder with new association value
        updatedHabit.setReminder(reminder);
        HabitDTO updatedHabitDTO = habitMapper.toDto(updatedHabit);

        // Update the entity
        restHabitMockMvc.perform(put("/api/habits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedHabitDTO)))
            .andExpect(status().isOk());

        // Validate the Habit in the database
        List<Habit> habitList = habitRepository.findAll();
        assertThat(habitList).hasSize(databaseSizeBeforeCreate);
        Habit testHabit = habitList.get(habitList.size() - 1);

        // Validate the id for MapsId, the ids must be same
        // Uncomment the following line for assertion. However, please note that there is a known issue and uncommenting will fail the test.
        // Please look at https://github.com/jhipster/generator-jhipster/issues/9100. You can modify this test as necessary.
        // assertThat(testHabit.getId()).isEqualTo(testHabit.getReminder().getId());
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = habitRepository.findAll().size();
        // set the field null
        habit.setName(null);

        // Create the Habit, which fails.
        HabitDTO habitDTO = habitMapper.toDto(habit);

        restHabitMockMvc.perform(post("/api/habits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(habitDTO)))
            .andExpect(status().isBadRequest());

        List<Habit> habitList = habitRepository.findAll();
        assertThat(habitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = habitRepository.findAll().size();
        // set the field null
        habit.setType(null);

        // Create the Habit, which fails.
        HabitDTO habitDTO = habitMapper.toDto(habit);

        restHabitMockMvc.perform(post("/api/habits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(habitDTO)))
            .andExpect(status().isBadRequest());

        List<Habit> habitList = habitRepository.findAll();
        assertThat(habitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkGoalPeriodIsRequired() throws Exception {
        int databaseSizeBeforeTest = habitRepository.findAll().size();
        // set the field null
        habit.setGoalPeriod(null);

        // Create the Habit, which fails.
        HabitDTO habitDTO = habitMapper.toDto(habit);

        restHabitMockMvc.perform(post("/api/habits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(habitDTO)))
            .andExpect(status().isBadRequest());

        List<Habit> habitList = habitRepository.findAll();
        assertThat(habitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCompletionGoalIsRequired() throws Exception {
        int databaseSizeBeforeTest = habitRepository.findAll().size();
        // set the field null
        habit.setCompletionGoal(null);

        // Create the Habit, which fails.
        HabitDTO habitDTO = habitMapper.toDto(habit);

        restHabitMockMvc.perform(post("/api/habits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(habitDTO)))
            .andExpect(status().isBadRequest());

        List<Habit> habitList = habitRepository.findAll();
        assertThat(habitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsGroupTrackingIsRequired() throws Exception {
        int databaseSizeBeforeTest = habitRepository.findAll().size();
        // set the field null
        habit.setIsGroupTracking(null);

        // Create the Habit, which fails.
        HabitDTO habitDTO = habitMapper.toDto(habit);

        restHabitMockMvc.perform(post("/api/habits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(habitDTO)))
            .andExpect(status().isBadRequest());

        List<Habit> habitList = habitRepository.findAll();
        assertThat(habitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsReminderIsRequired() throws Exception {
        int databaseSizeBeforeTest = habitRepository.findAll().size();
        // set the field null
        habit.setIsReminder(null);

        // Create the Habit, which fails.
        HabitDTO habitDTO = habitMapper.toDto(habit);

        restHabitMockMvc.perform(post("/api/habits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(habitDTO)))
            .andExpect(status().isBadRequest());

        List<Habit> habitList = habitRepository.findAll();
        assertThat(habitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHabits() throws Exception {
        // Initialize the database
        habitRepository.saveAndFlush(habit);

        // Get all the habitList
        restHabitMockMvc.perform(get("/api/habits?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(habit.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].goalPeriod").value(hasItem(DEFAULT_GOAL_PERIOD.toString())))
            .andExpect(jsonPath("$.[*].completionGoal").value(hasItem(DEFAULT_COMPLETION_GOAL.doubleValue())))
            .andExpect(jsonPath("$.[*].isGroupTracking").value(hasItem(DEFAULT_IS_GROUP_TRACKING.booleanValue())))
            .andExpect(jsonPath("$.[*].noteText").value(hasItem(DEFAULT_NOTE_TEXT)))
            .andExpect(jsonPath("$.[*].motivateText").value(hasItem(DEFAULT_MOTIVATE_TEXT)))
            .andExpect(jsonPath("$.[*].isReminder").value(hasItem(DEFAULT_IS_REMINDER.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getHabit() throws Exception {
        // Initialize the database
        habitRepository.saveAndFlush(habit);

        // Get the habit
        restHabitMockMvc.perform(get("/api/habits/{id}", habit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(habit.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.goalPeriod").value(DEFAULT_GOAL_PERIOD.toString()))
            .andExpect(jsonPath("$.completionGoal").value(DEFAULT_COMPLETION_GOAL.doubleValue()))
            .andExpect(jsonPath("$.isGroupTracking").value(DEFAULT_IS_GROUP_TRACKING.booleanValue()))
            .andExpect(jsonPath("$.noteText").value(DEFAULT_NOTE_TEXT))
            .andExpect(jsonPath("$.motivateText").value(DEFAULT_MOTIVATE_TEXT))
            .andExpect(jsonPath("$.isReminder").value(DEFAULT_IS_REMINDER.booleanValue()));
    }


    @Test
    @Transactional
    public void getHabitsByIdFiltering() throws Exception {
        // Initialize the database
        habitRepository.saveAndFlush(habit);

        Long id = habit.getId();

        defaultHabitShouldBeFound("id.equals=" + id);
        defaultHabitShouldNotBeFound("id.notEquals=" + id);

        defaultHabitShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultHabitShouldNotBeFound("id.greaterThan=" + id);

        defaultHabitShouldBeFound("id.lessThanOrEqual=" + id);
        defaultHabitShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllHabitsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        habitRepository.saveAndFlush(habit);

        // Get all the habitList where name equals to DEFAULT_NAME
        defaultHabitShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the habitList where name equals to UPDATED_NAME
        defaultHabitShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllHabitsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        habitRepository.saveAndFlush(habit);

        // Get all the habitList where name not equals to DEFAULT_NAME
        defaultHabitShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the habitList where name not equals to UPDATED_NAME
        defaultHabitShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllHabitsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        habitRepository.saveAndFlush(habit);

        // Get all the habitList where name in DEFAULT_NAME or UPDATED_NAME
        defaultHabitShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the habitList where name equals to UPDATED_NAME
        defaultHabitShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllHabitsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        habitRepository.saveAndFlush(habit);

        // Get all the habitList where name is not null
        defaultHabitShouldBeFound("name.specified=true");

        // Get all the habitList where name is null
        defaultHabitShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllHabitsByNameContainsSomething() throws Exception {
        // Initialize the database
        habitRepository.saveAndFlush(habit);

        // Get all the habitList where name contains DEFAULT_NAME
        defaultHabitShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the habitList where name contains UPDATED_NAME
        defaultHabitShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllHabitsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        habitRepository.saveAndFlush(habit);

        // Get all the habitList where name does not contain DEFAULT_NAME
        defaultHabitShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the habitList where name does not contain UPDATED_NAME
        defaultHabitShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllHabitsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        habitRepository.saveAndFlush(habit);

        // Get all the habitList where type equals to DEFAULT_TYPE
        defaultHabitShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the habitList where type equals to UPDATED_TYPE
        defaultHabitShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllHabitsByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        habitRepository.saveAndFlush(habit);

        // Get all the habitList where type not equals to DEFAULT_TYPE
        defaultHabitShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the habitList where type not equals to UPDATED_TYPE
        defaultHabitShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllHabitsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        habitRepository.saveAndFlush(habit);

        // Get all the habitList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultHabitShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the habitList where type equals to UPDATED_TYPE
        defaultHabitShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllHabitsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        habitRepository.saveAndFlush(habit);

        // Get all the habitList where type is not null
        defaultHabitShouldBeFound("type.specified=true");

        // Get all the habitList where type is null
        defaultHabitShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllHabitsByGoalPeriodIsEqualToSomething() throws Exception {
        // Initialize the database
        habitRepository.saveAndFlush(habit);

        // Get all the habitList where goalPeriod equals to DEFAULT_GOAL_PERIOD
        defaultHabitShouldBeFound("goalPeriod.equals=" + DEFAULT_GOAL_PERIOD);

        // Get all the habitList where goalPeriod equals to UPDATED_GOAL_PERIOD
        defaultHabitShouldNotBeFound("goalPeriod.equals=" + UPDATED_GOAL_PERIOD);
    }

    @Test
    @Transactional
    public void getAllHabitsByGoalPeriodIsNotEqualToSomething() throws Exception {
        // Initialize the database
        habitRepository.saveAndFlush(habit);

        // Get all the habitList where goalPeriod not equals to DEFAULT_GOAL_PERIOD
        defaultHabitShouldNotBeFound("goalPeriod.notEquals=" + DEFAULT_GOAL_PERIOD);

        // Get all the habitList where goalPeriod not equals to UPDATED_GOAL_PERIOD
        defaultHabitShouldBeFound("goalPeriod.notEquals=" + UPDATED_GOAL_PERIOD);
    }

    @Test
    @Transactional
    public void getAllHabitsByGoalPeriodIsInShouldWork() throws Exception {
        // Initialize the database
        habitRepository.saveAndFlush(habit);

        // Get all the habitList where goalPeriod in DEFAULT_GOAL_PERIOD or UPDATED_GOAL_PERIOD
        defaultHabitShouldBeFound("goalPeriod.in=" + DEFAULT_GOAL_PERIOD + "," + UPDATED_GOAL_PERIOD);

        // Get all the habitList where goalPeriod equals to UPDATED_GOAL_PERIOD
        defaultHabitShouldNotBeFound("goalPeriod.in=" + UPDATED_GOAL_PERIOD);
    }

    @Test
    @Transactional
    public void getAllHabitsByGoalPeriodIsNullOrNotNull() throws Exception {
        // Initialize the database
        habitRepository.saveAndFlush(habit);

        // Get all the habitList where goalPeriod is not null
        defaultHabitShouldBeFound("goalPeriod.specified=true");

        // Get all the habitList where goalPeriod is null
        defaultHabitShouldNotBeFound("goalPeriod.specified=false");
    }

    @Test
    @Transactional
    public void getAllHabitsByCompletionGoalIsEqualToSomething() throws Exception {
        // Initialize the database
        habitRepository.saveAndFlush(habit);

        // Get all the habitList where completionGoal equals to DEFAULT_COMPLETION_GOAL
        defaultHabitShouldBeFound("completionGoal.equals=" + DEFAULT_COMPLETION_GOAL);

        // Get all the habitList where completionGoal equals to UPDATED_COMPLETION_GOAL
        defaultHabitShouldNotBeFound("completionGoal.equals=" + UPDATED_COMPLETION_GOAL);
    }

    @Test
    @Transactional
    public void getAllHabitsByCompletionGoalIsNotEqualToSomething() throws Exception {
        // Initialize the database
        habitRepository.saveAndFlush(habit);

        // Get all the habitList where completionGoal not equals to DEFAULT_COMPLETION_GOAL
        defaultHabitShouldNotBeFound("completionGoal.notEquals=" + DEFAULT_COMPLETION_GOAL);

        // Get all the habitList where completionGoal not equals to UPDATED_COMPLETION_GOAL
        defaultHabitShouldBeFound("completionGoal.notEquals=" + UPDATED_COMPLETION_GOAL);
    }

    @Test
    @Transactional
    public void getAllHabitsByCompletionGoalIsInShouldWork() throws Exception {
        // Initialize the database
        habitRepository.saveAndFlush(habit);

        // Get all the habitList where completionGoal in DEFAULT_COMPLETION_GOAL or UPDATED_COMPLETION_GOAL
        defaultHabitShouldBeFound("completionGoal.in=" + DEFAULT_COMPLETION_GOAL + "," + UPDATED_COMPLETION_GOAL);

        // Get all the habitList where completionGoal equals to UPDATED_COMPLETION_GOAL
        defaultHabitShouldNotBeFound("completionGoal.in=" + UPDATED_COMPLETION_GOAL);
    }

    @Test
    @Transactional
    public void getAllHabitsByCompletionGoalIsNullOrNotNull() throws Exception {
        // Initialize the database
        habitRepository.saveAndFlush(habit);

        // Get all the habitList where completionGoal is not null
        defaultHabitShouldBeFound("completionGoal.specified=true");

        // Get all the habitList where completionGoal is null
        defaultHabitShouldNotBeFound("completionGoal.specified=false");
    }

    @Test
    @Transactional
    public void getAllHabitsByCompletionGoalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        habitRepository.saveAndFlush(habit);

        // Get all the habitList where completionGoal is greater than or equal to DEFAULT_COMPLETION_GOAL
        defaultHabitShouldBeFound("completionGoal.greaterThanOrEqual=" + DEFAULT_COMPLETION_GOAL);

        // Get all the habitList where completionGoal is greater than or equal to UPDATED_COMPLETION_GOAL
        defaultHabitShouldNotBeFound("completionGoal.greaterThanOrEqual=" + UPDATED_COMPLETION_GOAL);
    }

    @Test
    @Transactional
    public void getAllHabitsByCompletionGoalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        habitRepository.saveAndFlush(habit);

        // Get all the habitList where completionGoal is less than or equal to DEFAULT_COMPLETION_GOAL
        defaultHabitShouldBeFound("completionGoal.lessThanOrEqual=" + DEFAULT_COMPLETION_GOAL);

        // Get all the habitList where completionGoal is less than or equal to SMALLER_COMPLETION_GOAL
        defaultHabitShouldNotBeFound("completionGoal.lessThanOrEqual=" + SMALLER_COMPLETION_GOAL);
    }

    @Test
    @Transactional
    public void getAllHabitsByCompletionGoalIsLessThanSomething() throws Exception {
        // Initialize the database
        habitRepository.saveAndFlush(habit);

        // Get all the habitList where completionGoal is less than DEFAULT_COMPLETION_GOAL
        defaultHabitShouldNotBeFound("completionGoal.lessThan=" + DEFAULT_COMPLETION_GOAL);

        // Get all the habitList where completionGoal is less than UPDATED_COMPLETION_GOAL
        defaultHabitShouldBeFound("completionGoal.lessThan=" + UPDATED_COMPLETION_GOAL);
    }

    @Test
    @Transactional
    public void getAllHabitsByCompletionGoalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        habitRepository.saveAndFlush(habit);

        // Get all the habitList where completionGoal is greater than DEFAULT_COMPLETION_GOAL
        defaultHabitShouldNotBeFound("completionGoal.greaterThan=" + DEFAULT_COMPLETION_GOAL);

        // Get all the habitList where completionGoal is greater than SMALLER_COMPLETION_GOAL
        defaultHabitShouldBeFound("completionGoal.greaterThan=" + SMALLER_COMPLETION_GOAL);
    }


    @Test
    @Transactional
    public void getAllHabitsByIsGroupTrackingIsEqualToSomething() throws Exception {
        // Initialize the database
        habitRepository.saveAndFlush(habit);

        // Get all the habitList where isGroupTracking equals to DEFAULT_IS_GROUP_TRACKING
        defaultHabitShouldBeFound("isGroupTracking.equals=" + DEFAULT_IS_GROUP_TRACKING);

        // Get all the habitList where isGroupTracking equals to UPDATED_IS_GROUP_TRACKING
        defaultHabitShouldNotBeFound("isGroupTracking.equals=" + UPDATED_IS_GROUP_TRACKING);
    }

    @Test
    @Transactional
    public void getAllHabitsByIsGroupTrackingIsNotEqualToSomething() throws Exception {
        // Initialize the database
        habitRepository.saveAndFlush(habit);

        // Get all the habitList where isGroupTracking not equals to DEFAULT_IS_GROUP_TRACKING
        defaultHabitShouldNotBeFound("isGroupTracking.notEquals=" + DEFAULT_IS_GROUP_TRACKING);

        // Get all the habitList where isGroupTracking not equals to UPDATED_IS_GROUP_TRACKING
        defaultHabitShouldBeFound("isGroupTracking.notEquals=" + UPDATED_IS_GROUP_TRACKING);
    }

    @Test
    @Transactional
    public void getAllHabitsByIsGroupTrackingIsInShouldWork() throws Exception {
        // Initialize the database
        habitRepository.saveAndFlush(habit);

        // Get all the habitList where isGroupTracking in DEFAULT_IS_GROUP_TRACKING or UPDATED_IS_GROUP_TRACKING
        defaultHabitShouldBeFound("isGroupTracking.in=" + DEFAULT_IS_GROUP_TRACKING + "," + UPDATED_IS_GROUP_TRACKING);

        // Get all the habitList where isGroupTracking equals to UPDATED_IS_GROUP_TRACKING
        defaultHabitShouldNotBeFound("isGroupTracking.in=" + UPDATED_IS_GROUP_TRACKING);
    }

    @Test
    @Transactional
    public void getAllHabitsByIsGroupTrackingIsNullOrNotNull() throws Exception {
        // Initialize the database
        habitRepository.saveAndFlush(habit);

        // Get all the habitList where isGroupTracking is not null
        defaultHabitShouldBeFound("isGroupTracking.specified=true");

        // Get all the habitList where isGroupTracking is null
        defaultHabitShouldNotBeFound("isGroupTracking.specified=false");
    }

    @Test
    @Transactional
    public void getAllHabitsByNoteTextIsEqualToSomething() throws Exception {
        // Initialize the database
        habitRepository.saveAndFlush(habit);

        // Get all the habitList where noteText equals to DEFAULT_NOTE_TEXT
        defaultHabitShouldBeFound("noteText.equals=" + DEFAULT_NOTE_TEXT);

        // Get all the habitList where noteText equals to UPDATED_NOTE_TEXT
        defaultHabitShouldNotBeFound("noteText.equals=" + UPDATED_NOTE_TEXT);
    }

    @Test
    @Transactional
    public void getAllHabitsByNoteTextIsNotEqualToSomething() throws Exception {
        // Initialize the database
        habitRepository.saveAndFlush(habit);

        // Get all the habitList where noteText not equals to DEFAULT_NOTE_TEXT
        defaultHabitShouldNotBeFound("noteText.notEquals=" + DEFAULT_NOTE_TEXT);

        // Get all the habitList where noteText not equals to UPDATED_NOTE_TEXT
        defaultHabitShouldBeFound("noteText.notEquals=" + UPDATED_NOTE_TEXT);
    }

    @Test
    @Transactional
    public void getAllHabitsByNoteTextIsInShouldWork() throws Exception {
        // Initialize the database
        habitRepository.saveAndFlush(habit);

        // Get all the habitList where noteText in DEFAULT_NOTE_TEXT or UPDATED_NOTE_TEXT
        defaultHabitShouldBeFound("noteText.in=" + DEFAULT_NOTE_TEXT + "," + UPDATED_NOTE_TEXT);

        // Get all the habitList where noteText equals to UPDATED_NOTE_TEXT
        defaultHabitShouldNotBeFound("noteText.in=" + UPDATED_NOTE_TEXT);
    }

    @Test
    @Transactional
    public void getAllHabitsByNoteTextIsNullOrNotNull() throws Exception {
        // Initialize the database
        habitRepository.saveAndFlush(habit);

        // Get all the habitList where noteText is not null
        defaultHabitShouldBeFound("noteText.specified=true");

        // Get all the habitList where noteText is null
        defaultHabitShouldNotBeFound("noteText.specified=false");
    }
                @Test
    @Transactional
    public void getAllHabitsByNoteTextContainsSomething() throws Exception {
        // Initialize the database
        habitRepository.saveAndFlush(habit);

        // Get all the habitList where noteText contains DEFAULT_NOTE_TEXT
        defaultHabitShouldBeFound("noteText.contains=" + DEFAULT_NOTE_TEXT);

        // Get all the habitList where noteText contains UPDATED_NOTE_TEXT
        defaultHabitShouldNotBeFound("noteText.contains=" + UPDATED_NOTE_TEXT);
    }

    @Test
    @Transactional
    public void getAllHabitsByNoteTextNotContainsSomething() throws Exception {
        // Initialize the database
        habitRepository.saveAndFlush(habit);

        // Get all the habitList where noteText does not contain DEFAULT_NOTE_TEXT
        defaultHabitShouldNotBeFound("noteText.doesNotContain=" + DEFAULT_NOTE_TEXT);

        // Get all the habitList where noteText does not contain UPDATED_NOTE_TEXT
        defaultHabitShouldBeFound("noteText.doesNotContain=" + UPDATED_NOTE_TEXT);
    }


    @Test
    @Transactional
    public void getAllHabitsByMotivateTextIsEqualToSomething() throws Exception {
        // Initialize the database
        habitRepository.saveAndFlush(habit);

        // Get all the habitList where motivateText equals to DEFAULT_MOTIVATE_TEXT
        defaultHabitShouldBeFound("motivateText.equals=" + DEFAULT_MOTIVATE_TEXT);

        // Get all the habitList where motivateText equals to UPDATED_MOTIVATE_TEXT
        defaultHabitShouldNotBeFound("motivateText.equals=" + UPDATED_MOTIVATE_TEXT);
    }

    @Test
    @Transactional
    public void getAllHabitsByMotivateTextIsNotEqualToSomething() throws Exception {
        // Initialize the database
        habitRepository.saveAndFlush(habit);

        // Get all the habitList where motivateText not equals to DEFAULT_MOTIVATE_TEXT
        defaultHabitShouldNotBeFound("motivateText.notEquals=" + DEFAULT_MOTIVATE_TEXT);

        // Get all the habitList where motivateText not equals to UPDATED_MOTIVATE_TEXT
        defaultHabitShouldBeFound("motivateText.notEquals=" + UPDATED_MOTIVATE_TEXT);
    }

    @Test
    @Transactional
    public void getAllHabitsByMotivateTextIsInShouldWork() throws Exception {
        // Initialize the database
        habitRepository.saveAndFlush(habit);

        // Get all the habitList where motivateText in DEFAULT_MOTIVATE_TEXT or UPDATED_MOTIVATE_TEXT
        defaultHabitShouldBeFound("motivateText.in=" + DEFAULT_MOTIVATE_TEXT + "," + UPDATED_MOTIVATE_TEXT);

        // Get all the habitList where motivateText equals to UPDATED_MOTIVATE_TEXT
        defaultHabitShouldNotBeFound("motivateText.in=" + UPDATED_MOTIVATE_TEXT);
    }

    @Test
    @Transactional
    public void getAllHabitsByMotivateTextIsNullOrNotNull() throws Exception {
        // Initialize the database
        habitRepository.saveAndFlush(habit);

        // Get all the habitList where motivateText is not null
        defaultHabitShouldBeFound("motivateText.specified=true");

        // Get all the habitList where motivateText is null
        defaultHabitShouldNotBeFound("motivateText.specified=false");
    }
                @Test
    @Transactional
    public void getAllHabitsByMotivateTextContainsSomething() throws Exception {
        // Initialize the database
        habitRepository.saveAndFlush(habit);

        // Get all the habitList where motivateText contains DEFAULT_MOTIVATE_TEXT
        defaultHabitShouldBeFound("motivateText.contains=" + DEFAULT_MOTIVATE_TEXT);

        // Get all the habitList where motivateText contains UPDATED_MOTIVATE_TEXT
        defaultHabitShouldNotBeFound("motivateText.contains=" + UPDATED_MOTIVATE_TEXT);
    }

    @Test
    @Transactional
    public void getAllHabitsByMotivateTextNotContainsSomething() throws Exception {
        // Initialize the database
        habitRepository.saveAndFlush(habit);

        // Get all the habitList where motivateText does not contain DEFAULT_MOTIVATE_TEXT
        defaultHabitShouldNotBeFound("motivateText.doesNotContain=" + DEFAULT_MOTIVATE_TEXT);

        // Get all the habitList where motivateText does not contain UPDATED_MOTIVATE_TEXT
        defaultHabitShouldBeFound("motivateText.doesNotContain=" + UPDATED_MOTIVATE_TEXT);
    }


    @Test
    @Transactional
    public void getAllHabitsByIsReminderIsEqualToSomething() throws Exception {
        // Initialize the database
        habitRepository.saveAndFlush(habit);

        // Get all the habitList where isReminder equals to DEFAULT_IS_REMINDER
        defaultHabitShouldBeFound("isReminder.equals=" + DEFAULT_IS_REMINDER);

        // Get all the habitList where isReminder equals to UPDATED_IS_REMINDER
        defaultHabitShouldNotBeFound("isReminder.equals=" + UPDATED_IS_REMINDER);
    }

    @Test
    @Transactional
    public void getAllHabitsByIsReminderIsNotEqualToSomething() throws Exception {
        // Initialize the database
        habitRepository.saveAndFlush(habit);

        // Get all the habitList where isReminder not equals to DEFAULT_IS_REMINDER
        defaultHabitShouldNotBeFound("isReminder.notEquals=" + DEFAULT_IS_REMINDER);

        // Get all the habitList where isReminder not equals to UPDATED_IS_REMINDER
        defaultHabitShouldBeFound("isReminder.notEquals=" + UPDATED_IS_REMINDER);
    }

    @Test
    @Transactional
    public void getAllHabitsByIsReminderIsInShouldWork() throws Exception {
        // Initialize the database
        habitRepository.saveAndFlush(habit);

        // Get all the habitList where isReminder in DEFAULT_IS_REMINDER or UPDATED_IS_REMINDER
        defaultHabitShouldBeFound("isReminder.in=" + DEFAULT_IS_REMINDER + "," + UPDATED_IS_REMINDER);

        // Get all the habitList where isReminder equals to UPDATED_IS_REMINDER
        defaultHabitShouldNotBeFound("isReminder.in=" + UPDATED_IS_REMINDER);
    }

    @Test
    @Transactional
    public void getAllHabitsByIsReminderIsNullOrNotNull() throws Exception {
        // Initialize the database
        habitRepository.saveAndFlush(habit);

        // Get all the habitList where isReminder is not null
        defaultHabitShouldBeFound("isReminder.specified=true");

        // Get all the habitList where isReminder is null
        defaultHabitShouldNotBeFound("isReminder.specified=false");
    }

    @Test
    @Transactional
    public void getAllHabitsByReminderIsEqualToSomething() throws Exception {
        // Get already existing entity
        Reminder reminder = habit.getReminder();
        habitRepository.saveAndFlush(habit);
        Long reminderId = reminder.getId();

        // Get all the habitList where reminder equals to reminderId
        defaultHabitShouldBeFound("reminderId.equals=" + reminderId);

        // Get all the habitList where reminder equals to reminderId + 1
        defaultHabitShouldNotBeFound("reminderId.equals=" + (reminderId + 1));
    }


    @Test
    @Transactional
    public void getAllHabitsByEventTimesIsEqualToSomething() throws Exception {
        // Initialize the database
        habitRepository.saveAndFlush(habit);
        EventTimes eventTimes = EventTimesResourceIT.createEntity(em);
        em.persist(eventTimes);
        em.flush();
        habit.addEventTimes(eventTimes);
        habitRepository.saveAndFlush(habit);
        Long eventTimesId = eventTimes.getId();

        // Get all the habitList where eventTimes equals to eventTimesId
        defaultHabitShouldBeFound("eventTimesId.equals=" + eventTimesId);

        // Get all the habitList where eventTimes equals to eventTimesId + 1
        defaultHabitShouldNotBeFound("eventTimesId.equals=" + (eventTimesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHabitShouldBeFound(String filter) throws Exception {
        restHabitMockMvc.perform(get("/api/habits?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(habit.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].goalPeriod").value(hasItem(DEFAULT_GOAL_PERIOD.toString())))
            .andExpect(jsonPath("$.[*].completionGoal").value(hasItem(DEFAULT_COMPLETION_GOAL.doubleValue())))
            .andExpect(jsonPath("$.[*].isGroupTracking").value(hasItem(DEFAULT_IS_GROUP_TRACKING.booleanValue())))
            .andExpect(jsonPath("$.[*].noteText").value(hasItem(DEFAULT_NOTE_TEXT)))
            .andExpect(jsonPath("$.[*].motivateText").value(hasItem(DEFAULT_MOTIVATE_TEXT)))
            .andExpect(jsonPath("$.[*].isReminder").value(hasItem(DEFAULT_IS_REMINDER.booleanValue())));

        // Check, that the count call also returns 1
        restHabitMockMvc.perform(get("/api/habits/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHabitShouldNotBeFound(String filter) throws Exception {
        restHabitMockMvc.perform(get("/api/habits?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHabitMockMvc.perform(get("/api/habits/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingHabit() throws Exception {
        // Get the habit
        restHabitMockMvc.perform(get("/api/habits/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHabit() throws Exception {
        // Initialize the database
        habitRepository.saveAndFlush(habit);

        int databaseSizeBeforeUpdate = habitRepository.findAll().size();

        // Update the habit
        Habit updatedHabit = habitRepository.findById(habit.getId()).get();
        // Disconnect from session so that the updates on updatedHabit are not directly saved in db
        em.detach(updatedHabit);
        updatedHabit
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .goalPeriod(UPDATED_GOAL_PERIOD)
            .completionGoal(UPDATED_COMPLETION_GOAL)
            .isGroupTracking(UPDATED_IS_GROUP_TRACKING)
            .noteText(UPDATED_NOTE_TEXT)
            .motivateText(UPDATED_MOTIVATE_TEXT)
            .isReminder(UPDATED_IS_REMINDER);
        HabitDTO habitDTO = habitMapper.toDto(updatedHabit);

        restHabitMockMvc.perform(put("/api/habits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(habitDTO)))
            .andExpect(status().isOk());

        // Validate the Habit in the database
        List<Habit> habitList = habitRepository.findAll();
        assertThat(habitList).hasSize(databaseSizeBeforeUpdate);
        Habit testHabit = habitList.get(habitList.size() - 1);
        assertThat(testHabit.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHabit.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testHabit.getGoalPeriod()).isEqualTo(UPDATED_GOAL_PERIOD);
        assertThat(testHabit.getCompletionGoal()).isEqualTo(UPDATED_COMPLETION_GOAL);
        assertThat(testHabit.isIsGroupTracking()).isEqualTo(UPDATED_IS_GROUP_TRACKING);
        assertThat(testHabit.getNoteText()).isEqualTo(UPDATED_NOTE_TEXT);
        assertThat(testHabit.getMotivateText()).isEqualTo(UPDATED_MOTIVATE_TEXT);
        assertThat(testHabit.isIsReminder()).isEqualTo(UPDATED_IS_REMINDER);
    }

    @Test
    @Transactional
    public void updateNonExistingHabit() throws Exception {
        int databaseSizeBeforeUpdate = habitRepository.findAll().size();

        // Create the Habit
        HabitDTO habitDTO = habitMapper.toDto(habit);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHabitMockMvc.perform(put("/api/habits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(habitDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Habit in the database
        List<Habit> habitList = habitRepository.findAll();
        assertThat(habitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteHabit() throws Exception {
        // Initialize the database
        habitRepository.saveAndFlush(habit);

        int databaseSizeBeforeDelete = habitRepository.findAll().size();

        // Delete the habit
        restHabitMockMvc.perform(delete("/api/habits/{id}", habit.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Habit> habitList = habitRepository.findAll();
        assertThat(habitList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
