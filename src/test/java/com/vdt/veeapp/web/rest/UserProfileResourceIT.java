package com.vdt.veeapp.web.rest;

import com.vdt.veeapp.RoutineveeApp;
import com.vdt.veeapp.domain.UserProfile;
import com.vdt.veeapp.domain.User;
import com.vdt.veeapp.domain.UserGroups;
import com.vdt.veeapp.repository.UserProfileRepository;
import com.vdt.veeapp.service.UserProfileService;
import com.vdt.veeapp.service.dto.UserProfileDTO;
import com.vdt.veeapp.service.mapper.UserProfileMapper;
import com.vdt.veeapp.service.dto.UserProfileCriteria;
import com.vdt.veeapp.service.UserProfileQueryService;

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

/**
 * Integration tests for the {@link UserProfileResource} REST controller.
 */
@SpringBootTest(classes = RoutineveeApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class UserProfileResourceIT {

    private static final String DEFAULT_USER_KEY = "AAAAAAAAAA";
    private static final String UPDATED_USER_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_AVARTAR_URL = "AAAAAAAAAA";
    private static final String UPDATED_AVARTAR_URL = "BBBBBBBBBB";

    private static final String DEFAULT_COVER_U_RL = "AAAAAAAAAA";
    private static final String UPDATED_COVER_U_RL = "BBBBBBBBBB";

    private static final Double DEFAULT_LONGITUDE = 1D;
    private static final Double UPDATED_LONGITUDE = 2D;
    private static final Double SMALLER_LONGITUDE = 1D - 1D;

    private static final Double DEFAULT_LATITUDE = 1D;
    private static final Double UPDATED_LATITUDE = 2D;
    private static final Double SMALLER_LATITUDE = 1D - 1D;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UserProfileMapper userProfileMapper;

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private UserProfileQueryService userProfileQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserProfileMockMvc;

    private UserProfile userProfile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserProfile createEntity(EntityManager em) {
        UserProfile userProfile = new UserProfile()
            .userKey(DEFAULT_USER_KEY)
            .fullName(DEFAULT_FULL_NAME)
            .avartarUrl(DEFAULT_AVARTAR_URL)
            .coverURl(DEFAULT_COVER_U_RL)
            .longitude(DEFAULT_LONGITUDE)
            .latitude(DEFAULT_LATITUDE);
        return userProfile;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserProfile createUpdatedEntity(EntityManager em) {
        UserProfile userProfile = new UserProfile()
            .userKey(UPDATED_USER_KEY)
            .fullName(UPDATED_FULL_NAME)
            .avartarUrl(UPDATED_AVARTAR_URL)
            .coverURl(UPDATED_COVER_U_RL)
            .longitude(UPDATED_LONGITUDE)
            .latitude(UPDATED_LATITUDE);
        return userProfile;
    }

    @BeforeEach
    public void initTest() {
        userProfile = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserProfile() throws Exception {
        int databaseSizeBeforeCreate = userProfileRepository.findAll().size();

        // Create the UserProfile
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);
        restUserProfileMockMvc.perform(post("/api/user-profiles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(userProfileDTO)))
            .andExpect(status().isCreated());

        // Validate the UserProfile in the database
        List<UserProfile> userProfileList = userProfileRepository.findAll();
        assertThat(userProfileList).hasSize(databaseSizeBeforeCreate + 1);
        UserProfile testUserProfile = userProfileList.get(userProfileList.size() - 1);
        assertThat(testUserProfile.getUserKey()).isEqualTo(DEFAULT_USER_KEY);
        assertThat(testUserProfile.getFullName()).isEqualTo(DEFAULT_FULL_NAME);
        assertThat(testUserProfile.getAvartarUrl()).isEqualTo(DEFAULT_AVARTAR_URL);
        assertThat(testUserProfile.getCoverURl()).isEqualTo(DEFAULT_COVER_U_RL);
        assertThat(testUserProfile.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testUserProfile.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
    }

    @Test
    @Transactional
    public void createUserProfileWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userProfileRepository.findAll().size();

        // Create the UserProfile with an existing ID
        userProfile.setId(1L);
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserProfileMockMvc.perform(post("/api/user-profiles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(userProfileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserProfile in the database
        List<UserProfile> userProfileList = userProfileRepository.findAll();
        assertThat(userProfileList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkUserKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = userProfileRepository.findAll().size();
        // set the field null
        userProfile.setUserKey(null);

        // Create the UserProfile, which fails.
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        restUserProfileMockMvc.perform(post("/api/user-profiles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(userProfileDTO)))
            .andExpect(status().isBadRequest());

        List<UserProfile> userProfileList = userProfileRepository.findAll();
        assertThat(userProfileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUserProfiles() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList
        restUserProfileMockMvc.perform(get("/api/user-profiles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].userKey").value(hasItem(DEFAULT_USER_KEY)))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].avartarUrl").value(hasItem(DEFAULT_AVARTAR_URL)))
            .andExpect(jsonPath("$.[*].coverURl").value(hasItem(DEFAULT_COVER_U_RL)))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getUserProfile() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get the userProfile
        restUserProfileMockMvc.perform(get("/api/user-profiles/{id}", userProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userProfile.getId().intValue()))
            .andExpect(jsonPath("$.userKey").value(DEFAULT_USER_KEY))
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME))
            .andExpect(jsonPath("$.avartarUrl").value(DEFAULT_AVARTAR_URL))
            .andExpect(jsonPath("$.coverURl").value(DEFAULT_COVER_U_RL))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.doubleValue()))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.doubleValue()));
    }


    @Test
    @Transactional
    public void getUserProfilesByIdFiltering() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        Long id = userProfile.getId();

        defaultUserProfileShouldBeFound("id.equals=" + id);
        defaultUserProfileShouldNotBeFound("id.notEquals=" + id);

        defaultUserProfileShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUserProfileShouldNotBeFound("id.greaterThan=" + id);

        defaultUserProfileShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUserProfileShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllUserProfilesByUserKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where userKey equals to DEFAULT_USER_KEY
        defaultUserProfileShouldBeFound("userKey.equals=" + DEFAULT_USER_KEY);

        // Get all the userProfileList where userKey equals to UPDATED_USER_KEY
        defaultUserProfileShouldNotBeFound("userKey.equals=" + UPDATED_USER_KEY);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByUserKeyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where userKey not equals to DEFAULT_USER_KEY
        defaultUserProfileShouldNotBeFound("userKey.notEquals=" + DEFAULT_USER_KEY);

        // Get all the userProfileList where userKey not equals to UPDATED_USER_KEY
        defaultUserProfileShouldBeFound("userKey.notEquals=" + UPDATED_USER_KEY);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByUserKeyIsInShouldWork() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where userKey in DEFAULT_USER_KEY or UPDATED_USER_KEY
        defaultUserProfileShouldBeFound("userKey.in=" + DEFAULT_USER_KEY + "," + UPDATED_USER_KEY);

        // Get all the userProfileList where userKey equals to UPDATED_USER_KEY
        defaultUserProfileShouldNotBeFound("userKey.in=" + UPDATED_USER_KEY);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByUserKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where userKey is not null
        defaultUserProfileShouldBeFound("userKey.specified=true");

        // Get all the userProfileList where userKey is null
        defaultUserProfileShouldNotBeFound("userKey.specified=false");
    }
                @Test
    @Transactional
    public void getAllUserProfilesByUserKeyContainsSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where userKey contains DEFAULT_USER_KEY
        defaultUserProfileShouldBeFound("userKey.contains=" + DEFAULT_USER_KEY);

        // Get all the userProfileList where userKey contains UPDATED_USER_KEY
        defaultUserProfileShouldNotBeFound("userKey.contains=" + UPDATED_USER_KEY);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByUserKeyNotContainsSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where userKey does not contain DEFAULT_USER_KEY
        defaultUserProfileShouldNotBeFound("userKey.doesNotContain=" + DEFAULT_USER_KEY);

        // Get all the userProfileList where userKey does not contain UPDATED_USER_KEY
        defaultUserProfileShouldBeFound("userKey.doesNotContain=" + UPDATED_USER_KEY);
    }


    @Test
    @Transactional
    public void getAllUserProfilesByFullNameIsEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where fullName equals to DEFAULT_FULL_NAME
        defaultUserProfileShouldBeFound("fullName.equals=" + DEFAULT_FULL_NAME);

        // Get all the userProfileList where fullName equals to UPDATED_FULL_NAME
        defaultUserProfileShouldNotBeFound("fullName.equals=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByFullNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where fullName not equals to DEFAULT_FULL_NAME
        defaultUserProfileShouldNotBeFound("fullName.notEquals=" + DEFAULT_FULL_NAME);

        // Get all the userProfileList where fullName not equals to UPDATED_FULL_NAME
        defaultUserProfileShouldBeFound("fullName.notEquals=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByFullNameIsInShouldWork() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where fullName in DEFAULT_FULL_NAME or UPDATED_FULL_NAME
        defaultUserProfileShouldBeFound("fullName.in=" + DEFAULT_FULL_NAME + "," + UPDATED_FULL_NAME);

        // Get all the userProfileList where fullName equals to UPDATED_FULL_NAME
        defaultUserProfileShouldNotBeFound("fullName.in=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByFullNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where fullName is not null
        defaultUserProfileShouldBeFound("fullName.specified=true");

        // Get all the userProfileList where fullName is null
        defaultUserProfileShouldNotBeFound("fullName.specified=false");
    }
                @Test
    @Transactional
    public void getAllUserProfilesByFullNameContainsSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where fullName contains DEFAULT_FULL_NAME
        defaultUserProfileShouldBeFound("fullName.contains=" + DEFAULT_FULL_NAME);

        // Get all the userProfileList where fullName contains UPDATED_FULL_NAME
        defaultUserProfileShouldNotBeFound("fullName.contains=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByFullNameNotContainsSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where fullName does not contain DEFAULT_FULL_NAME
        defaultUserProfileShouldNotBeFound("fullName.doesNotContain=" + DEFAULT_FULL_NAME);

        // Get all the userProfileList where fullName does not contain UPDATED_FULL_NAME
        defaultUserProfileShouldBeFound("fullName.doesNotContain=" + UPDATED_FULL_NAME);
    }


    @Test
    @Transactional
    public void getAllUserProfilesByAvartarUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where avartarUrl equals to DEFAULT_AVARTAR_URL
        defaultUserProfileShouldBeFound("avartarUrl.equals=" + DEFAULT_AVARTAR_URL);

        // Get all the userProfileList where avartarUrl equals to UPDATED_AVARTAR_URL
        defaultUserProfileShouldNotBeFound("avartarUrl.equals=" + UPDATED_AVARTAR_URL);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByAvartarUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where avartarUrl not equals to DEFAULT_AVARTAR_URL
        defaultUserProfileShouldNotBeFound("avartarUrl.notEquals=" + DEFAULT_AVARTAR_URL);

        // Get all the userProfileList where avartarUrl not equals to UPDATED_AVARTAR_URL
        defaultUserProfileShouldBeFound("avartarUrl.notEquals=" + UPDATED_AVARTAR_URL);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByAvartarUrlIsInShouldWork() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where avartarUrl in DEFAULT_AVARTAR_URL or UPDATED_AVARTAR_URL
        defaultUserProfileShouldBeFound("avartarUrl.in=" + DEFAULT_AVARTAR_URL + "," + UPDATED_AVARTAR_URL);

        // Get all the userProfileList where avartarUrl equals to UPDATED_AVARTAR_URL
        defaultUserProfileShouldNotBeFound("avartarUrl.in=" + UPDATED_AVARTAR_URL);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByAvartarUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where avartarUrl is not null
        defaultUserProfileShouldBeFound("avartarUrl.specified=true");

        // Get all the userProfileList where avartarUrl is null
        defaultUserProfileShouldNotBeFound("avartarUrl.specified=false");
    }
                @Test
    @Transactional
    public void getAllUserProfilesByAvartarUrlContainsSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where avartarUrl contains DEFAULT_AVARTAR_URL
        defaultUserProfileShouldBeFound("avartarUrl.contains=" + DEFAULT_AVARTAR_URL);

        // Get all the userProfileList where avartarUrl contains UPDATED_AVARTAR_URL
        defaultUserProfileShouldNotBeFound("avartarUrl.contains=" + UPDATED_AVARTAR_URL);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByAvartarUrlNotContainsSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where avartarUrl does not contain DEFAULT_AVARTAR_URL
        defaultUserProfileShouldNotBeFound("avartarUrl.doesNotContain=" + DEFAULT_AVARTAR_URL);

        // Get all the userProfileList where avartarUrl does not contain UPDATED_AVARTAR_URL
        defaultUserProfileShouldBeFound("avartarUrl.doesNotContain=" + UPDATED_AVARTAR_URL);
    }


    @Test
    @Transactional
    public void getAllUserProfilesByCoverURlIsEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where coverURl equals to DEFAULT_COVER_U_RL
        defaultUserProfileShouldBeFound("coverURl.equals=" + DEFAULT_COVER_U_RL);

        // Get all the userProfileList where coverURl equals to UPDATED_COVER_U_RL
        defaultUserProfileShouldNotBeFound("coverURl.equals=" + UPDATED_COVER_U_RL);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByCoverURlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where coverURl not equals to DEFAULT_COVER_U_RL
        defaultUserProfileShouldNotBeFound("coverURl.notEquals=" + DEFAULT_COVER_U_RL);

        // Get all the userProfileList where coverURl not equals to UPDATED_COVER_U_RL
        defaultUserProfileShouldBeFound("coverURl.notEquals=" + UPDATED_COVER_U_RL);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByCoverURlIsInShouldWork() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where coverURl in DEFAULT_COVER_U_RL or UPDATED_COVER_U_RL
        defaultUserProfileShouldBeFound("coverURl.in=" + DEFAULT_COVER_U_RL + "," + UPDATED_COVER_U_RL);

        // Get all the userProfileList where coverURl equals to UPDATED_COVER_U_RL
        defaultUserProfileShouldNotBeFound("coverURl.in=" + UPDATED_COVER_U_RL);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByCoverURlIsNullOrNotNull() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where coverURl is not null
        defaultUserProfileShouldBeFound("coverURl.specified=true");

        // Get all the userProfileList where coverURl is null
        defaultUserProfileShouldNotBeFound("coverURl.specified=false");
    }
                @Test
    @Transactional
    public void getAllUserProfilesByCoverURlContainsSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where coverURl contains DEFAULT_COVER_U_RL
        defaultUserProfileShouldBeFound("coverURl.contains=" + DEFAULT_COVER_U_RL);

        // Get all the userProfileList where coverURl contains UPDATED_COVER_U_RL
        defaultUserProfileShouldNotBeFound("coverURl.contains=" + UPDATED_COVER_U_RL);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByCoverURlNotContainsSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where coverURl does not contain DEFAULT_COVER_U_RL
        defaultUserProfileShouldNotBeFound("coverURl.doesNotContain=" + DEFAULT_COVER_U_RL);

        // Get all the userProfileList where coverURl does not contain UPDATED_COVER_U_RL
        defaultUserProfileShouldBeFound("coverURl.doesNotContain=" + UPDATED_COVER_U_RL);
    }


    @Test
    @Transactional
    public void getAllUserProfilesByLongitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where longitude equals to DEFAULT_LONGITUDE
        defaultUserProfileShouldBeFound("longitude.equals=" + DEFAULT_LONGITUDE);

        // Get all the userProfileList where longitude equals to UPDATED_LONGITUDE
        defaultUserProfileShouldNotBeFound("longitude.equals=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByLongitudeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where longitude not equals to DEFAULT_LONGITUDE
        defaultUserProfileShouldNotBeFound("longitude.notEquals=" + DEFAULT_LONGITUDE);

        // Get all the userProfileList where longitude not equals to UPDATED_LONGITUDE
        defaultUserProfileShouldBeFound("longitude.notEquals=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByLongitudeIsInShouldWork() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where longitude in DEFAULT_LONGITUDE or UPDATED_LONGITUDE
        defaultUserProfileShouldBeFound("longitude.in=" + DEFAULT_LONGITUDE + "," + UPDATED_LONGITUDE);

        // Get all the userProfileList where longitude equals to UPDATED_LONGITUDE
        defaultUserProfileShouldNotBeFound("longitude.in=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByLongitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where longitude is not null
        defaultUserProfileShouldBeFound("longitude.specified=true");

        // Get all the userProfileList where longitude is null
        defaultUserProfileShouldNotBeFound("longitude.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserProfilesByLongitudeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where longitude is greater than or equal to DEFAULT_LONGITUDE
        defaultUserProfileShouldBeFound("longitude.greaterThanOrEqual=" + DEFAULT_LONGITUDE);

        // Get all the userProfileList where longitude is greater than or equal to UPDATED_LONGITUDE
        defaultUserProfileShouldNotBeFound("longitude.greaterThanOrEqual=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByLongitudeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where longitude is less than or equal to DEFAULT_LONGITUDE
        defaultUserProfileShouldBeFound("longitude.lessThanOrEqual=" + DEFAULT_LONGITUDE);

        // Get all the userProfileList where longitude is less than or equal to SMALLER_LONGITUDE
        defaultUserProfileShouldNotBeFound("longitude.lessThanOrEqual=" + SMALLER_LONGITUDE);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByLongitudeIsLessThanSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where longitude is less than DEFAULT_LONGITUDE
        defaultUserProfileShouldNotBeFound("longitude.lessThan=" + DEFAULT_LONGITUDE);

        // Get all the userProfileList where longitude is less than UPDATED_LONGITUDE
        defaultUserProfileShouldBeFound("longitude.lessThan=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByLongitudeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where longitude is greater than DEFAULT_LONGITUDE
        defaultUserProfileShouldNotBeFound("longitude.greaterThan=" + DEFAULT_LONGITUDE);

        // Get all the userProfileList where longitude is greater than SMALLER_LONGITUDE
        defaultUserProfileShouldBeFound("longitude.greaterThan=" + SMALLER_LONGITUDE);
    }


    @Test
    @Transactional
    public void getAllUserProfilesByLatitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where latitude equals to DEFAULT_LATITUDE
        defaultUserProfileShouldBeFound("latitude.equals=" + DEFAULT_LATITUDE);

        // Get all the userProfileList where latitude equals to UPDATED_LATITUDE
        defaultUserProfileShouldNotBeFound("latitude.equals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByLatitudeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where latitude not equals to DEFAULT_LATITUDE
        defaultUserProfileShouldNotBeFound("latitude.notEquals=" + DEFAULT_LATITUDE);

        // Get all the userProfileList where latitude not equals to UPDATED_LATITUDE
        defaultUserProfileShouldBeFound("latitude.notEquals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByLatitudeIsInShouldWork() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where latitude in DEFAULT_LATITUDE or UPDATED_LATITUDE
        defaultUserProfileShouldBeFound("latitude.in=" + DEFAULT_LATITUDE + "," + UPDATED_LATITUDE);

        // Get all the userProfileList where latitude equals to UPDATED_LATITUDE
        defaultUserProfileShouldNotBeFound("latitude.in=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByLatitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where latitude is not null
        defaultUserProfileShouldBeFound("latitude.specified=true");

        // Get all the userProfileList where latitude is null
        defaultUserProfileShouldNotBeFound("latitude.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserProfilesByLatitudeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where latitude is greater than or equal to DEFAULT_LATITUDE
        defaultUserProfileShouldBeFound("latitude.greaterThanOrEqual=" + DEFAULT_LATITUDE);

        // Get all the userProfileList where latitude is greater than or equal to UPDATED_LATITUDE
        defaultUserProfileShouldNotBeFound("latitude.greaterThanOrEqual=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByLatitudeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where latitude is less than or equal to DEFAULT_LATITUDE
        defaultUserProfileShouldBeFound("latitude.lessThanOrEqual=" + DEFAULT_LATITUDE);

        // Get all the userProfileList where latitude is less than or equal to SMALLER_LATITUDE
        defaultUserProfileShouldNotBeFound("latitude.lessThanOrEqual=" + SMALLER_LATITUDE);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByLatitudeIsLessThanSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where latitude is less than DEFAULT_LATITUDE
        defaultUserProfileShouldNotBeFound("latitude.lessThan=" + DEFAULT_LATITUDE);

        // Get all the userProfileList where latitude is less than UPDATED_LATITUDE
        defaultUserProfileShouldBeFound("latitude.lessThan=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByLatitudeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where latitude is greater than DEFAULT_LATITUDE
        defaultUserProfileShouldNotBeFound("latitude.greaterThan=" + DEFAULT_LATITUDE);

        // Get all the userProfileList where latitude is greater than SMALLER_LATITUDE
        defaultUserProfileShouldBeFound("latitude.greaterThan=" + SMALLER_LATITUDE);
    }


    @Test
    @Transactional
    public void getAllUserProfilesByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        userProfile.setUser(user);
        userProfileRepository.saveAndFlush(userProfile);
        Long userId = user.getId();

        // Get all the userProfileList where user equals to userId
        defaultUserProfileShouldBeFound("userId.equals=" + userId);

        // Get all the userProfileList where user equals to userId + 1
        defaultUserProfileShouldNotBeFound("userId.equals=" + (userId + 1));
    }


    @Test
    @Transactional
    public void getAllUserProfilesByUserGroupsIsEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);
        UserGroups userGroups = UserGroupsResourceIT.createEntity(em);
        em.persist(userGroups);
        em.flush();
        userProfile.addUserGroups(userGroups);
        userProfileRepository.saveAndFlush(userProfile);
        Long userGroupsId = userGroups.getId();

        // Get all the userProfileList where userGroups equals to userGroupsId
        defaultUserProfileShouldBeFound("userGroupsId.equals=" + userGroupsId);

        // Get all the userProfileList where userGroups equals to userGroupsId + 1
        defaultUserProfileShouldNotBeFound("userGroupsId.equals=" + (userGroupsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUserProfileShouldBeFound(String filter) throws Exception {
        restUserProfileMockMvc.perform(get("/api/user-profiles?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].userKey").value(hasItem(DEFAULT_USER_KEY)))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].avartarUrl").value(hasItem(DEFAULT_AVARTAR_URL)))
            .andExpect(jsonPath("$.[*].coverURl").value(hasItem(DEFAULT_COVER_U_RL)))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())));

        // Check, that the count call also returns 1
        restUserProfileMockMvc.perform(get("/api/user-profiles/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUserProfileShouldNotBeFound(String filter) throws Exception {
        restUserProfileMockMvc.perform(get("/api/user-profiles?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUserProfileMockMvc.perform(get("/api/user-profiles/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingUserProfile() throws Exception {
        // Get the userProfile
        restUserProfileMockMvc.perform(get("/api/user-profiles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserProfile() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        int databaseSizeBeforeUpdate = userProfileRepository.findAll().size();

        // Update the userProfile
        UserProfile updatedUserProfile = userProfileRepository.findById(userProfile.getId()).get();
        // Disconnect from session so that the updates on updatedUserProfile are not directly saved in db
        em.detach(updatedUserProfile);
        updatedUserProfile
            .userKey(UPDATED_USER_KEY)
            .fullName(UPDATED_FULL_NAME)
            .avartarUrl(UPDATED_AVARTAR_URL)
            .coverURl(UPDATED_COVER_U_RL)
            .longitude(UPDATED_LONGITUDE)
            .latitude(UPDATED_LATITUDE);
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(updatedUserProfile);

        restUserProfileMockMvc.perform(put("/api/user-profiles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(userProfileDTO)))
            .andExpect(status().isOk());

        // Validate the UserProfile in the database
        List<UserProfile> userProfileList = userProfileRepository.findAll();
        assertThat(userProfileList).hasSize(databaseSizeBeforeUpdate);
        UserProfile testUserProfile = userProfileList.get(userProfileList.size() - 1);
        assertThat(testUserProfile.getUserKey()).isEqualTo(UPDATED_USER_KEY);
        assertThat(testUserProfile.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testUserProfile.getAvartarUrl()).isEqualTo(UPDATED_AVARTAR_URL);
        assertThat(testUserProfile.getCoverURl()).isEqualTo(UPDATED_COVER_U_RL);
        assertThat(testUserProfile.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testUserProfile.getLatitude()).isEqualTo(UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    public void updateNonExistingUserProfile() throws Exception {
        int databaseSizeBeforeUpdate = userProfileRepository.findAll().size();

        // Create the UserProfile
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserProfileMockMvc.perform(put("/api/user-profiles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(userProfileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserProfile in the database
        List<UserProfile> userProfileList = userProfileRepository.findAll();
        assertThat(userProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteUserProfile() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        int databaseSizeBeforeDelete = userProfileRepository.findAll().size();

        // Delete the userProfile
        restUserProfileMockMvc.perform(delete("/api/user-profiles/{id}", userProfile.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserProfile> userProfileList = userProfileRepository.findAll();
        assertThat(userProfileList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
