package com.vdt.veeapp.web.rest;

import com.vdt.veeapp.RoutineveeApp;
import com.vdt.veeapp.domain.FollowingRelationships;
import com.vdt.veeapp.domain.User;
import com.vdt.veeapp.repository.FollowingRelationshipsRepository;
import com.vdt.veeapp.service.FollowingRelationshipsService;
import com.vdt.veeapp.service.dto.FollowingRelationshipsDTO;
import com.vdt.veeapp.service.mapper.FollowingRelationshipsMapper;
import com.vdt.veeapp.service.dto.FollowingRelationshipsCriteria;
import com.vdt.veeapp.service.FollowingRelationshipsQueryService;

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
 * Integration tests for the {@link FollowingRelationshipsResource} REST controller.
 */
@SpringBootTest(classes = RoutineveeApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class FollowingRelationshipsResourceIT {

    private static final Instant DEFAULT_DATE_FOLLOWED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_FOLLOWED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_ACTION_USER_ID = 1L;
    private static final Long UPDATED_ACTION_USER_ID = 2L;
    private static final Long SMALLER_ACTION_USER_ID = 1L - 1L;

    @Autowired
    private FollowingRelationshipsRepository followingRelationshipsRepository;

    @Autowired
    private FollowingRelationshipsMapper followingRelationshipsMapper;

    @Autowired
    private FollowingRelationshipsService followingRelationshipsService;

    @Autowired
    private FollowingRelationshipsQueryService followingRelationshipsQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFollowingRelationshipsMockMvc;

    private FollowingRelationships followingRelationships;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FollowingRelationships createEntity(EntityManager em) {
        FollowingRelationships followingRelationships = new FollowingRelationships()
            .dateFollowed(DEFAULT_DATE_FOLLOWED)
            .actionUserId(DEFAULT_ACTION_USER_ID);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        followingRelationships.setUser(user);
        // Add required entity
        followingRelationships.setUserFollowing(user);
        return followingRelationships;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FollowingRelationships createUpdatedEntity(EntityManager em) {
        FollowingRelationships followingRelationships = new FollowingRelationships()
            .dateFollowed(UPDATED_DATE_FOLLOWED)
            .actionUserId(UPDATED_ACTION_USER_ID);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        followingRelationships.setUser(user);
        // Add required entity
        followingRelationships.setUserFollowing(user);
        return followingRelationships;
    }

    @BeforeEach
    public void initTest() {
        followingRelationships = createEntity(em);
    }

    @Test
    @Transactional
    public void createFollowingRelationships() throws Exception {
        int databaseSizeBeforeCreate = followingRelationshipsRepository.findAll().size();

        // Create the FollowingRelationships
        FollowingRelationshipsDTO followingRelationshipsDTO = followingRelationshipsMapper.toDto(followingRelationships);
        restFollowingRelationshipsMockMvc.perform(post("/api/following-relationships")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(followingRelationshipsDTO)))
            .andExpect(status().isCreated());

        // Validate the FollowingRelationships in the database
        List<FollowingRelationships> followingRelationshipsList = followingRelationshipsRepository.findAll();
        assertThat(followingRelationshipsList).hasSize(databaseSizeBeforeCreate + 1);
        FollowingRelationships testFollowingRelationships = followingRelationshipsList.get(followingRelationshipsList.size() - 1);
        assertThat(testFollowingRelationships.getDateFollowed()).isEqualTo(DEFAULT_DATE_FOLLOWED);
        assertThat(testFollowingRelationships.getActionUserId()).isEqualTo(DEFAULT_ACTION_USER_ID);
    }

    @Test
    @Transactional
    public void createFollowingRelationshipsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = followingRelationshipsRepository.findAll().size();

        // Create the FollowingRelationships with an existing ID
        followingRelationships.setId(1L);
        FollowingRelationshipsDTO followingRelationshipsDTO = followingRelationshipsMapper.toDto(followingRelationships);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFollowingRelationshipsMockMvc.perform(post("/api/following-relationships")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(followingRelationshipsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FollowingRelationships in the database
        List<FollowingRelationships> followingRelationshipsList = followingRelationshipsRepository.findAll();
        assertThat(followingRelationshipsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkDateFollowedIsRequired() throws Exception {
        int databaseSizeBeforeTest = followingRelationshipsRepository.findAll().size();
        // set the field null
        followingRelationships.setDateFollowed(null);

        // Create the FollowingRelationships, which fails.
        FollowingRelationshipsDTO followingRelationshipsDTO = followingRelationshipsMapper.toDto(followingRelationships);

        restFollowingRelationshipsMockMvc.perform(post("/api/following-relationships")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(followingRelationshipsDTO)))
            .andExpect(status().isBadRequest());

        List<FollowingRelationships> followingRelationshipsList = followingRelationshipsRepository.findAll();
        assertThat(followingRelationshipsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActionUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = followingRelationshipsRepository.findAll().size();
        // set the field null
        followingRelationships.setActionUserId(null);

        // Create the FollowingRelationships, which fails.
        FollowingRelationshipsDTO followingRelationshipsDTO = followingRelationshipsMapper.toDto(followingRelationships);

        restFollowingRelationshipsMockMvc.perform(post("/api/following-relationships")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(followingRelationshipsDTO)))
            .andExpect(status().isBadRequest());

        List<FollowingRelationships> followingRelationshipsList = followingRelationshipsRepository.findAll();
        assertThat(followingRelationshipsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFollowingRelationships() throws Exception {
        // Initialize the database
        followingRelationshipsRepository.saveAndFlush(followingRelationships);

        // Get all the followingRelationshipsList
        restFollowingRelationshipsMockMvc.perform(get("/api/following-relationships?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(followingRelationships.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateFollowed").value(hasItem(DEFAULT_DATE_FOLLOWED.toString())))
            .andExpect(jsonPath("$.[*].actionUserId").value(hasItem(DEFAULT_ACTION_USER_ID.intValue())));
    }
    
    @Test
    @Transactional
    public void getFollowingRelationships() throws Exception {
        // Initialize the database
        followingRelationshipsRepository.saveAndFlush(followingRelationships);

        // Get the followingRelationships
        restFollowingRelationshipsMockMvc.perform(get("/api/following-relationships/{id}", followingRelationships.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(followingRelationships.getId().intValue()))
            .andExpect(jsonPath("$.dateFollowed").value(DEFAULT_DATE_FOLLOWED.toString()))
            .andExpect(jsonPath("$.actionUserId").value(DEFAULT_ACTION_USER_ID.intValue()));
    }


    @Test
    @Transactional
    public void getFollowingRelationshipsByIdFiltering() throws Exception {
        // Initialize the database
        followingRelationshipsRepository.saveAndFlush(followingRelationships);

        Long id = followingRelationships.getId();

        defaultFollowingRelationshipsShouldBeFound("id.equals=" + id);
        defaultFollowingRelationshipsShouldNotBeFound("id.notEquals=" + id);

        defaultFollowingRelationshipsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFollowingRelationshipsShouldNotBeFound("id.greaterThan=" + id);

        defaultFollowingRelationshipsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFollowingRelationshipsShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllFollowingRelationshipsByDateFollowedIsEqualToSomething() throws Exception {
        // Initialize the database
        followingRelationshipsRepository.saveAndFlush(followingRelationships);

        // Get all the followingRelationshipsList where dateFollowed equals to DEFAULT_DATE_FOLLOWED
        defaultFollowingRelationshipsShouldBeFound("dateFollowed.equals=" + DEFAULT_DATE_FOLLOWED);

        // Get all the followingRelationshipsList where dateFollowed equals to UPDATED_DATE_FOLLOWED
        defaultFollowingRelationshipsShouldNotBeFound("dateFollowed.equals=" + UPDATED_DATE_FOLLOWED);
    }

    @Test
    @Transactional
    public void getAllFollowingRelationshipsByDateFollowedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        followingRelationshipsRepository.saveAndFlush(followingRelationships);

        // Get all the followingRelationshipsList where dateFollowed not equals to DEFAULT_DATE_FOLLOWED
        defaultFollowingRelationshipsShouldNotBeFound("dateFollowed.notEquals=" + DEFAULT_DATE_FOLLOWED);

        // Get all the followingRelationshipsList where dateFollowed not equals to UPDATED_DATE_FOLLOWED
        defaultFollowingRelationshipsShouldBeFound("dateFollowed.notEquals=" + UPDATED_DATE_FOLLOWED);
    }

    @Test
    @Transactional
    public void getAllFollowingRelationshipsByDateFollowedIsInShouldWork() throws Exception {
        // Initialize the database
        followingRelationshipsRepository.saveAndFlush(followingRelationships);

        // Get all the followingRelationshipsList where dateFollowed in DEFAULT_DATE_FOLLOWED or UPDATED_DATE_FOLLOWED
        defaultFollowingRelationshipsShouldBeFound("dateFollowed.in=" + DEFAULT_DATE_FOLLOWED + "," + UPDATED_DATE_FOLLOWED);

        // Get all the followingRelationshipsList where dateFollowed equals to UPDATED_DATE_FOLLOWED
        defaultFollowingRelationshipsShouldNotBeFound("dateFollowed.in=" + UPDATED_DATE_FOLLOWED);
    }

    @Test
    @Transactional
    public void getAllFollowingRelationshipsByDateFollowedIsNullOrNotNull() throws Exception {
        // Initialize the database
        followingRelationshipsRepository.saveAndFlush(followingRelationships);

        // Get all the followingRelationshipsList where dateFollowed is not null
        defaultFollowingRelationshipsShouldBeFound("dateFollowed.specified=true");

        // Get all the followingRelationshipsList where dateFollowed is null
        defaultFollowingRelationshipsShouldNotBeFound("dateFollowed.specified=false");
    }

    @Test
    @Transactional
    public void getAllFollowingRelationshipsByActionUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        followingRelationshipsRepository.saveAndFlush(followingRelationships);

        // Get all the followingRelationshipsList where actionUserId equals to DEFAULT_ACTION_USER_ID
        defaultFollowingRelationshipsShouldBeFound("actionUserId.equals=" + DEFAULT_ACTION_USER_ID);

        // Get all the followingRelationshipsList where actionUserId equals to UPDATED_ACTION_USER_ID
        defaultFollowingRelationshipsShouldNotBeFound("actionUserId.equals=" + UPDATED_ACTION_USER_ID);
    }

    @Test
    @Transactional
    public void getAllFollowingRelationshipsByActionUserIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        followingRelationshipsRepository.saveAndFlush(followingRelationships);

        // Get all the followingRelationshipsList where actionUserId not equals to DEFAULT_ACTION_USER_ID
        defaultFollowingRelationshipsShouldNotBeFound("actionUserId.notEquals=" + DEFAULT_ACTION_USER_ID);

        // Get all the followingRelationshipsList where actionUserId not equals to UPDATED_ACTION_USER_ID
        defaultFollowingRelationshipsShouldBeFound("actionUserId.notEquals=" + UPDATED_ACTION_USER_ID);
    }

    @Test
    @Transactional
    public void getAllFollowingRelationshipsByActionUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        followingRelationshipsRepository.saveAndFlush(followingRelationships);

        // Get all the followingRelationshipsList where actionUserId in DEFAULT_ACTION_USER_ID or UPDATED_ACTION_USER_ID
        defaultFollowingRelationshipsShouldBeFound("actionUserId.in=" + DEFAULT_ACTION_USER_ID + "," + UPDATED_ACTION_USER_ID);

        // Get all the followingRelationshipsList where actionUserId equals to UPDATED_ACTION_USER_ID
        defaultFollowingRelationshipsShouldNotBeFound("actionUserId.in=" + UPDATED_ACTION_USER_ID);
    }

    @Test
    @Transactional
    public void getAllFollowingRelationshipsByActionUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        followingRelationshipsRepository.saveAndFlush(followingRelationships);

        // Get all the followingRelationshipsList where actionUserId is not null
        defaultFollowingRelationshipsShouldBeFound("actionUserId.specified=true");

        // Get all the followingRelationshipsList where actionUserId is null
        defaultFollowingRelationshipsShouldNotBeFound("actionUserId.specified=false");
    }

    @Test
    @Transactional
    public void getAllFollowingRelationshipsByActionUserIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        followingRelationshipsRepository.saveAndFlush(followingRelationships);

        // Get all the followingRelationshipsList where actionUserId is greater than or equal to DEFAULT_ACTION_USER_ID
        defaultFollowingRelationshipsShouldBeFound("actionUserId.greaterThanOrEqual=" + DEFAULT_ACTION_USER_ID);

        // Get all the followingRelationshipsList where actionUserId is greater than or equal to UPDATED_ACTION_USER_ID
        defaultFollowingRelationshipsShouldNotBeFound("actionUserId.greaterThanOrEqual=" + UPDATED_ACTION_USER_ID);
    }

    @Test
    @Transactional
    public void getAllFollowingRelationshipsByActionUserIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        followingRelationshipsRepository.saveAndFlush(followingRelationships);

        // Get all the followingRelationshipsList where actionUserId is less than or equal to DEFAULT_ACTION_USER_ID
        defaultFollowingRelationshipsShouldBeFound("actionUserId.lessThanOrEqual=" + DEFAULT_ACTION_USER_ID);

        // Get all the followingRelationshipsList where actionUserId is less than or equal to SMALLER_ACTION_USER_ID
        defaultFollowingRelationshipsShouldNotBeFound("actionUserId.lessThanOrEqual=" + SMALLER_ACTION_USER_ID);
    }

    @Test
    @Transactional
    public void getAllFollowingRelationshipsByActionUserIdIsLessThanSomething() throws Exception {
        // Initialize the database
        followingRelationshipsRepository.saveAndFlush(followingRelationships);

        // Get all the followingRelationshipsList where actionUserId is less than DEFAULT_ACTION_USER_ID
        defaultFollowingRelationshipsShouldNotBeFound("actionUserId.lessThan=" + DEFAULT_ACTION_USER_ID);

        // Get all the followingRelationshipsList where actionUserId is less than UPDATED_ACTION_USER_ID
        defaultFollowingRelationshipsShouldBeFound("actionUserId.lessThan=" + UPDATED_ACTION_USER_ID);
    }

    @Test
    @Transactional
    public void getAllFollowingRelationshipsByActionUserIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        followingRelationshipsRepository.saveAndFlush(followingRelationships);

        // Get all the followingRelationshipsList where actionUserId is greater than DEFAULT_ACTION_USER_ID
        defaultFollowingRelationshipsShouldNotBeFound("actionUserId.greaterThan=" + DEFAULT_ACTION_USER_ID);

        // Get all the followingRelationshipsList where actionUserId is greater than SMALLER_ACTION_USER_ID
        defaultFollowingRelationshipsShouldBeFound("actionUserId.greaterThan=" + SMALLER_ACTION_USER_ID);
    }


    @Test
    @Transactional
    public void getAllFollowingRelationshipsByUserIsEqualToSomething() throws Exception {
        // Get already existing entity
        User user = followingRelationships.getUser();
        followingRelationshipsRepository.saveAndFlush(followingRelationships);
        Long userId = user.getId();

        // Get all the followingRelationshipsList where user equals to userId
        defaultFollowingRelationshipsShouldBeFound("userId.equals=" + userId);

        // Get all the followingRelationshipsList where user equals to userId + 1
        defaultFollowingRelationshipsShouldNotBeFound("userId.equals=" + (userId + 1));
    }


    @Test
    @Transactional
    public void getAllFollowingRelationshipsByUserFollowingIsEqualToSomething() throws Exception {
        // Get already existing entity
        User userFollowing = followingRelationships.getUserFollowing();
        followingRelationshipsRepository.saveAndFlush(followingRelationships);
        Long userFollowingId = userFollowing.getId();

        // Get all the followingRelationshipsList where userFollowing equals to userFollowingId
        defaultFollowingRelationshipsShouldBeFound("userFollowingId.equals=" + userFollowingId);

        // Get all the followingRelationshipsList where userFollowing equals to userFollowingId + 1
        defaultFollowingRelationshipsShouldNotBeFound("userFollowingId.equals=" + (userFollowingId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFollowingRelationshipsShouldBeFound(String filter) throws Exception {
        restFollowingRelationshipsMockMvc.perform(get("/api/following-relationships?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(followingRelationships.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateFollowed").value(hasItem(DEFAULT_DATE_FOLLOWED.toString())))
            .andExpect(jsonPath("$.[*].actionUserId").value(hasItem(DEFAULT_ACTION_USER_ID.intValue())));

        // Check, that the count call also returns 1
        restFollowingRelationshipsMockMvc.perform(get("/api/following-relationships/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFollowingRelationshipsShouldNotBeFound(String filter) throws Exception {
        restFollowingRelationshipsMockMvc.perform(get("/api/following-relationships?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFollowingRelationshipsMockMvc.perform(get("/api/following-relationships/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingFollowingRelationships() throws Exception {
        // Get the followingRelationships
        restFollowingRelationshipsMockMvc.perform(get("/api/following-relationships/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFollowingRelationships() throws Exception {
        // Initialize the database
        followingRelationshipsRepository.saveAndFlush(followingRelationships);

        int databaseSizeBeforeUpdate = followingRelationshipsRepository.findAll().size();

        // Update the followingRelationships
        FollowingRelationships updatedFollowingRelationships = followingRelationshipsRepository.findById(followingRelationships.getId()).get();
        // Disconnect from session so that the updates on updatedFollowingRelationships are not directly saved in db
        em.detach(updatedFollowingRelationships);
        updatedFollowingRelationships
            .dateFollowed(UPDATED_DATE_FOLLOWED)
            .actionUserId(UPDATED_ACTION_USER_ID);
        FollowingRelationshipsDTO followingRelationshipsDTO = followingRelationshipsMapper.toDto(updatedFollowingRelationships);

        restFollowingRelationshipsMockMvc.perform(put("/api/following-relationships")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(followingRelationshipsDTO)))
            .andExpect(status().isOk());

        // Validate the FollowingRelationships in the database
        List<FollowingRelationships> followingRelationshipsList = followingRelationshipsRepository.findAll();
        assertThat(followingRelationshipsList).hasSize(databaseSizeBeforeUpdate);
        FollowingRelationships testFollowingRelationships = followingRelationshipsList.get(followingRelationshipsList.size() - 1);
        assertThat(testFollowingRelationships.getDateFollowed()).isEqualTo(UPDATED_DATE_FOLLOWED);
        assertThat(testFollowingRelationships.getActionUserId()).isEqualTo(UPDATED_ACTION_USER_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingFollowingRelationships() throws Exception {
        int databaseSizeBeforeUpdate = followingRelationshipsRepository.findAll().size();

        // Create the FollowingRelationships
        FollowingRelationshipsDTO followingRelationshipsDTO = followingRelationshipsMapper.toDto(followingRelationships);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFollowingRelationshipsMockMvc.perform(put("/api/following-relationships")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(followingRelationshipsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FollowingRelationships in the database
        List<FollowingRelationships> followingRelationshipsList = followingRelationshipsRepository.findAll();
        assertThat(followingRelationshipsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFollowingRelationships() throws Exception {
        // Initialize the database
        followingRelationshipsRepository.saveAndFlush(followingRelationships);

        int databaseSizeBeforeDelete = followingRelationshipsRepository.findAll().size();

        // Delete the followingRelationships
        restFollowingRelationshipsMockMvc.perform(delete("/api/following-relationships/{id}", followingRelationships.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FollowingRelationships> followingRelationshipsList = followingRelationshipsRepository.findAll();
        assertThat(followingRelationshipsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
