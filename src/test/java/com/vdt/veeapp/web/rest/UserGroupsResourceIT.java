package com.vdt.veeapp.web.rest;

import com.vdt.veeapp.RoutineveeApp;
import com.vdt.veeapp.domain.UserGroups;
import com.vdt.veeapp.domain.UserProfile;
import com.vdt.veeapp.repository.UserGroupsRepository;
import com.vdt.veeapp.service.UserGroupsService;
import com.vdt.veeapp.service.dto.UserGroupsDTO;
import com.vdt.veeapp.service.mapper.UserGroupsMapper;
import com.vdt.veeapp.service.dto.UserGroupsCriteria;
import com.vdt.veeapp.service.UserGroupsQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link UserGroupsResource} REST controller.
 */
@SpringBootTest(classes = RoutineveeApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class UserGroupsResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_AVATA_GROUP_URL = "AAAAAAAAAA";
    private static final String UPDATED_AVATA_GROUP_URL = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATE_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private UserGroupsRepository userGroupsRepository;

    @Mock
    private UserGroupsRepository userGroupsRepositoryMock;

    @Autowired
    private UserGroupsMapper userGroupsMapper;

    @Mock
    private UserGroupsService userGroupsServiceMock;

    @Autowired
    private UserGroupsService userGroupsService;

    @Autowired
    private UserGroupsQueryService userGroupsQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserGroupsMockMvc;

    private UserGroups userGroups;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserGroups createEntity(EntityManager em) {
        UserGroups userGroups = new UserGroups()
            .name(DEFAULT_NAME)
            .avataGroupUrl(DEFAULT_AVATA_GROUP_URL)
            .createAt(DEFAULT_CREATE_AT);
        return userGroups;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserGroups createUpdatedEntity(EntityManager em) {
        UserGroups userGroups = new UserGroups()
            .name(UPDATED_NAME)
            .avataGroupUrl(UPDATED_AVATA_GROUP_URL)
            .createAt(UPDATED_CREATE_AT);
        return userGroups;
    }

    @BeforeEach
    public void initTest() {
        userGroups = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserGroups() throws Exception {
        int databaseSizeBeforeCreate = userGroupsRepository.findAll().size();

        // Create the UserGroups
        UserGroupsDTO userGroupsDTO = userGroupsMapper.toDto(userGroups);
        restUserGroupsMockMvc.perform(post("/api/user-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(userGroupsDTO)))
            .andExpect(status().isCreated());

        // Validate the UserGroups in the database
        List<UserGroups> userGroupsList = userGroupsRepository.findAll();
        assertThat(userGroupsList).hasSize(databaseSizeBeforeCreate + 1);
        UserGroups testUserGroups = userGroupsList.get(userGroupsList.size() - 1);
        assertThat(testUserGroups.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUserGroups.getAvataGroupUrl()).isEqualTo(DEFAULT_AVATA_GROUP_URL);
        assertThat(testUserGroups.getCreateAt()).isEqualTo(DEFAULT_CREATE_AT);
    }

    @Test
    @Transactional
    public void createUserGroupsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userGroupsRepository.findAll().size();

        // Create the UserGroups with an existing ID
        userGroups.setId(1L);
        UserGroupsDTO userGroupsDTO = userGroupsMapper.toDto(userGroups);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserGroupsMockMvc.perform(post("/api/user-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(userGroupsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserGroups in the database
        List<UserGroups> userGroupsList = userGroupsRepository.findAll();
        assertThat(userGroupsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllUserGroups() throws Exception {
        // Initialize the database
        userGroupsRepository.saveAndFlush(userGroups);

        // Get all the userGroupsList
        restUserGroupsMockMvc.perform(get("/api/user-groups?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userGroups.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].avataGroupUrl").value(hasItem(DEFAULT_AVATA_GROUP_URL)))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT.toString())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllUserGroupsWithEagerRelationshipsIsEnabled() throws Exception {
        when(userGroupsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUserGroupsMockMvc.perform(get("/api/user-groups?eagerload=true"))
            .andExpect(status().isOk());

        verify(userGroupsServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllUserGroupsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(userGroupsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUserGroupsMockMvc.perform(get("/api/user-groups?eagerload=true"))
            .andExpect(status().isOk());

        verify(userGroupsServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getUserGroups() throws Exception {
        // Initialize the database
        userGroupsRepository.saveAndFlush(userGroups);

        // Get the userGroups
        restUserGroupsMockMvc.perform(get("/api/user-groups/{id}", userGroups.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userGroups.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.avataGroupUrl").value(DEFAULT_AVATA_GROUP_URL))
            .andExpect(jsonPath("$.createAt").value(DEFAULT_CREATE_AT.toString()));
    }


    @Test
    @Transactional
    public void getUserGroupsByIdFiltering() throws Exception {
        // Initialize the database
        userGroupsRepository.saveAndFlush(userGroups);

        Long id = userGroups.getId();

        defaultUserGroupsShouldBeFound("id.equals=" + id);
        defaultUserGroupsShouldNotBeFound("id.notEquals=" + id);

        defaultUserGroupsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUserGroupsShouldNotBeFound("id.greaterThan=" + id);

        defaultUserGroupsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUserGroupsShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllUserGroupsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        userGroupsRepository.saveAndFlush(userGroups);

        // Get all the userGroupsList where name equals to DEFAULT_NAME
        defaultUserGroupsShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the userGroupsList where name equals to UPDATED_NAME
        defaultUserGroupsShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllUserGroupsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userGroupsRepository.saveAndFlush(userGroups);

        // Get all the userGroupsList where name not equals to DEFAULT_NAME
        defaultUserGroupsShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the userGroupsList where name not equals to UPDATED_NAME
        defaultUserGroupsShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllUserGroupsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        userGroupsRepository.saveAndFlush(userGroups);

        // Get all the userGroupsList where name in DEFAULT_NAME or UPDATED_NAME
        defaultUserGroupsShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the userGroupsList where name equals to UPDATED_NAME
        defaultUserGroupsShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllUserGroupsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        userGroupsRepository.saveAndFlush(userGroups);

        // Get all the userGroupsList where name is not null
        defaultUserGroupsShouldBeFound("name.specified=true");

        // Get all the userGroupsList where name is null
        defaultUserGroupsShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllUserGroupsByNameContainsSomething() throws Exception {
        // Initialize the database
        userGroupsRepository.saveAndFlush(userGroups);

        // Get all the userGroupsList where name contains DEFAULT_NAME
        defaultUserGroupsShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the userGroupsList where name contains UPDATED_NAME
        defaultUserGroupsShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllUserGroupsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        userGroupsRepository.saveAndFlush(userGroups);

        // Get all the userGroupsList where name does not contain DEFAULT_NAME
        defaultUserGroupsShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the userGroupsList where name does not contain UPDATED_NAME
        defaultUserGroupsShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllUserGroupsByAvataGroupUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        userGroupsRepository.saveAndFlush(userGroups);

        // Get all the userGroupsList where avataGroupUrl equals to DEFAULT_AVATA_GROUP_URL
        defaultUserGroupsShouldBeFound("avataGroupUrl.equals=" + DEFAULT_AVATA_GROUP_URL);

        // Get all the userGroupsList where avataGroupUrl equals to UPDATED_AVATA_GROUP_URL
        defaultUserGroupsShouldNotBeFound("avataGroupUrl.equals=" + UPDATED_AVATA_GROUP_URL);
    }

    @Test
    @Transactional
    public void getAllUserGroupsByAvataGroupUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userGroupsRepository.saveAndFlush(userGroups);

        // Get all the userGroupsList where avataGroupUrl not equals to DEFAULT_AVATA_GROUP_URL
        defaultUserGroupsShouldNotBeFound("avataGroupUrl.notEquals=" + DEFAULT_AVATA_GROUP_URL);

        // Get all the userGroupsList where avataGroupUrl not equals to UPDATED_AVATA_GROUP_URL
        defaultUserGroupsShouldBeFound("avataGroupUrl.notEquals=" + UPDATED_AVATA_GROUP_URL);
    }

    @Test
    @Transactional
    public void getAllUserGroupsByAvataGroupUrlIsInShouldWork() throws Exception {
        // Initialize the database
        userGroupsRepository.saveAndFlush(userGroups);

        // Get all the userGroupsList where avataGroupUrl in DEFAULT_AVATA_GROUP_URL or UPDATED_AVATA_GROUP_URL
        defaultUserGroupsShouldBeFound("avataGroupUrl.in=" + DEFAULT_AVATA_GROUP_URL + "," + UPDATED_AVATA_GROUP_URL);

        // Get all the userGroupsList where avataGroupUrl equals to UPDATED_AVATA_GROUP_URL
        defaultUserGroupsShouldNotBeFound("avataGroupUrl.in=" + UPDATED_AVATA_GROUP_URL);
    }

    @Test
    @Transactional
    public void getAllUserGroupsByAvataGroupUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        userGroupsRepository.saveAndFlush(userGroups);

        // Get all the userGroupsList where avataGroupUrl is not null
        defaultUserGroupsShouldBeFound("avataGroupUrl.specified=true");

        // Get all the userGroupsList where avataGroupUrl is null
        defaultUserGroupsShouldNotBeFound("avataGroupUrl.specified=false");
    }
                @Test
    @Transactional
    public void getAllUserGroupsByAvataGroupUrlContainsSomething() throws Exception {
        // Initialize the database
        userGroupsRepository.saveAndFlush(userGroups);

        // Get all the userGroupsList where avataGroupUrl contains DEFAULT_AVATA_GROUP_URL
        defaultUserGroupsShouldBeFound("avataGroupUrl.contains=" + DEFAULT_AVATA_GROUP_URL);

        // Get all the userGroupsList where avataGroupUrl contains UPDATED_AVATA_GROUP_URL
        defaultUserGroupsShouldNotBeFound("avataGroupUrl.contains=" + UPDATED_AVATA_GROUP_URL);
    }

    @Test
    @Transactional
    public void getAllUserGroupsByAvataGroupUrlNotContainsSomething() throws Exception {
        // Initialize the database
        userGroupsRepository.saveAndFlush(userGroups);

        // Get all the userGroupsList where avataGroupUrl does not contain DEFAULT_AVATA_GROUP_URL
        defaultUserGroupsShouldNotBeFound("avataGroupUrl.doesNotContain=" + DEFAULT_AVATA_GROUP_URL);

        // Get all the userGroupsList where avataGroupUrl does not contain UPDATED_AVATA_GROUP_URL
        defaultUserGroupsShouldBeFound("avataGroupUrl.doesNotContain=" + UPDATED_AVATA_GROUP_URL);
    }


    @Test
    @Transactional
    public void getAllUserGroupsByCreateAtIsEqualToSomething() throws Exception {
        // Initialize the database
        userGroupsRepository.saveAndFlush(userGroups);

        // Get all the userGroupsList where createAt equals to DEFAULT_CREATE_AT
        defaultUserGroupsShouldBeFound("createAt.equals=" + DEFAULT_CREATE_AT);

        // Get all the userGroupsList where createAt equals to UPDATED_CREATE_AT
        defaultUserGroupsShouldNotBeFound("createAt.equals=" + UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    public void getAllUserGroupsByCreateAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userGroupsRepository.saveAndFlush(userGroups);

        // Get all the userGroupsList where createAt not equals to DEFAULT_CREATE_AT
        defaultUserGroupsShouldNotBeFound("createAt.notEquals=" + DEFAULT_CREATE_AT);

        // Get all the userGroupsList where createAt not equals to UPDATED_CREATE_AT
        defaultUserGroupsShouldBeFound("createAt.notEquals=" + UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    public void getAllUserGroupsByCreateAtIsInShouldWork() throws Exception {
        // Initialize the database
        userGroupsRepository.saveAndFlush(userGroups);

        // Get all the userGroupsList where createAt in DEFAULT_CREATE_AT or UPDATED_CREATE_AT
        defaultUserGroupsShouldBeFound("createAt.in=" + DEFAULT_CREATE_AT + "," + UPDATED_CREATE_AT);

        // Get all the userGroupsList where createAt equals to UPDATED_CREATE_AT
        defaultUserGroupsShouldNotBeFound("createAt.in=" + UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    public void getAllUserGroupsByCreateAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        userGroupsRepository.saveAndFlush(userGroups);

        // Get all the userGroupsList where createAt is not null
        defaultUserGroupsShouldBeFound("createAt.specified=true");

        // Get all the userGroupsList where createAt is null
        defaultUserGroupsShouldNotBeFound("createAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserGroupsByUserProfileIsEqualToSomething() throws Exception {
        // Initialize the database
        userGroupsRepository.saveAndFlush(userGroups);
        UserProfile userProfile = UserProfileResourceIT.createEntity(em);
        em.persist(userProfile);
        em.flush();
        userGroups.addUserProfile(userProfile);
        userGroupsRepository.saveAndFlush(userGroups);
        Long userProfileId = userProfile.getId();

        // Get all the userGroupsList where userProfile equals to userProfileId
        defaultUserGroupsShouldBeFound("userProfileId.equals=" + userProfileId);

        // Get all the userGroupsList where userProfile equals to userProfileId + 1
        defaultUserGroupsShouldNotBeFound("userProfileId.equals=" + (userProfileId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUserGroupsShouldBeFound(String filter) throws Exception {
        restUserGroupsMockMvc.perform(get("/api/user-groups?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userGroups.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].avataGroupUrl").value(hasItem(DEFAULT_AVATA_GROUP_URL)))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT.toString())));

        // Check, that the count call also returns 1
        restUserGroupsMockMvc.perform(get("/api/user-groups/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUserGroupsShouldNotBeFound(String filter) throws Exception {
        restUserGroupsMockMvc.perform(get("/api/user-groups?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUserGroupsMockMvc.perform(get("/api/user-groups/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingUserGroups() throws Exception {
        // Get the userGroups
        restUserGroupsMockMvc.perform(get("/api/user-groups/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserGroups() throws Exception {
        // Initialize the database
        userGroupsRepository.saveAndFlush(userGroups);

        int databaseSizeBeforeUpdate = userGroupsRepository.findAll().size();

        // Update the userGroups
        UserGroups updatedUserGroups = userGroupsRepository.findById(userGroups.getId()).get();
        // Disconnect from session so that the updates on updatedUserGroups are not directly saved in db
        em.detach(updatedUserGroups);
        updatedUserGroups
            .name(UPDATED_NAME)
            .avataGroupUrl(UPDATED_AVATA_GROUP_URL)
            .createAt(UPDATED_CREATE_AT);
        UserGroupsDTO userGroupsDTO = userGroupsMapper.toDto(updatedUserGroups);

        restUserGroupsMockMvc.perform(put("/api/user-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(userGroupsDTO)))
            .andExpect(status().isOk());

        // Validate the UserGroups in the database
        List<UserGroups> userGroupsList = userGroupsRepository.findAll();
        assertThat(userGroupsList).hasSize(databaseSizeBeforeUpdate);
        UserGroups testUserGroups = userGroupsList.get(userGroupsList.size() - 1);
        assertThat(testUserGroups.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUserGroups.getAvataGroupUrl()).isEqualTo(UPDATED_AVATA_GROUP_URL);
        assertThat(testUserGroups.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    public void updateNonExistingUserGroups() throws Exception {
        int databaseSizeBeforeUpdate = userGroupsRepository.findAll().size();

        // Create the UserGroups
        UserGroupsDTO userGroupsDTO = userGroupsMapper.toDto(userGroups);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserGroupsMockMvc.perform(put("/api/user-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(userGroupsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserGroups in the database
        List<UserGroups> userGroupsList = userGroupsRepository.findAll();
        assertThat(userGroupsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteUserGroups() throws Exception {
        // Initialize the database
        userGroupsRepository.saveAndFlush(userGroups);

        int databaseSizeBeforeDelete = userGroupsRepository.findAll().size();

        // Delete the userGroups
        restUserGroupsMockMvc.perform(delete("/api/user-groups/{id}", userGroups.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserGroups> userGroupsList = userGroupsRepository.findAll();
        assertThat(userGroupsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
