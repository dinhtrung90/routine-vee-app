package com.vdt.veeapp.web.rest;

import com.vdt.veeapp.RoutineveeApp;
import com.vdt.veeapp.domain.SubCategory;
import com.vdt.veeapp.domain.Category;
import com.vdt.veeapp.repository.SubCategoryRepository;
import com.vdt.veeapp.service.SubCategoryService;
import com.vdt.veeapp.service.dto.SubCategoryDTO;
import com.vdt.veeapp.service.mapper.SubCategoryMapper;
import com.vdt.veeapp.service.dto.SubCategoryCriteria;
import com.vdt.veeapp.service.SubCategoryQueryService;

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
 * Integration tests for the {@link SubCategoryResource} REST controller.
 */
@SpringBootTest(classes = RoutineveeApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class SubCategoryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private SubCategoryMapper subCategoryMapper;

    @Autowired
    private SubCategoryService subCategoryService;

    @Autowired
    private SubCategoryQueryService subCategoryQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubCategoryMockMvc;

    private SubCategory subCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubCategory createEntity(EntityManager em) {
        SubCategory subCategory = new SubCategory()
            .name(DEFAULT_NAME)
            .code(DEFAULT_CODE)
            .description(DEFAULT_DESCRIPTION);
        return subCategory;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubCategory createUpdatedEntity(EntityManager em) {
        SubCategory subCategory = new SubCategory()
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION);
        return subCategory;
    }

    @BeforeEach
    public void initTest() {
        subCategory = createEntity(em);
    }

    @Test
    @Transactional
    public void createSubCategory() throws Exception {
        int databaseSizeBeforeCreate = subCategoryRepository.findAll().size();

        // Create the SubCategory
        SubCategoryDTO subCategoryDTO = subCategoryMapper.toDto(subCategory);
        restSubCategoryMockMvc.perform(post("/api/sub-categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(subCategoryDTO)))
            .andExpect(status().isCreated());

        // Validate the SubCategory in the database
        List<SubCategory> subCategoryList = subCategoryRepository.findAll();
        assertThat(subCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        SubCategory testSubCategory = subCategoryList.get(subCategoryList.size() - 1);
        assertThat(testSubCategory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSubCategory.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testSubCategory.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createSubCategoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = subCategoryRepository.findAll().size();

        // Create the SubCategory with an existing ID
        subCategory.setId(1L);
        SubCategoryDTO subCategoryDTO = subCategoryMapper.toDto(subCategory);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubCategoryMockMvc.perform(post("/api/sub-categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(subCategoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SubCategory in the database
        List<SubCategory> subCategoryList = subCategoryRepository.findAll();
        assertThat(subCategoryList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = subCategoryRepository.findAll().size();
        // set the field null
        subCategory.setName(null);

        // Create the SubCategory, which fails.
        SubCategoryDTO subCategoryDTO = subCategoryMapper.toDto(subCategory);

        restSubCategoryMockMvc.perform(post("/api/sub-categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(subCategoryDTO)))
            .andExpect(status().isBadRequest());

        List<SubCategory> subCategoryList = subCategoryRepository.findAll();
        assertThat(subCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = subCategoryRepository.findAll().size();
        // set the field null
        subCategory.setCode(null);

        // Create the SubCategory, which fails.
        SubCategoryDTO subCategoryDTO = subCategoryMapper.toDto(subCategory);

        restSubCategoryMockMvc.perform(post("/api/sub-categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(subCategoryDTO)))
            .andExpect(status().isBadRequest());

        List<SubCategory> subCategoryList = subCategoryRepository.findAll();
        assertThat(subCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSubCategories() throws Exception {
        // Initialize the database
        subCategoryRepository.saveAndFlush(subCategory);

        // Get all the subCategoryList
        restSubCategoryMockMvc.perform(get("/api/sub-categories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
    
    @Test
    @Transactional
    public void getSubCategory() throws Exception {
        // Initialize the database
        subCategoryRepository.saveAndFlush(subCategory);

        // Get the subCategory
        restSubCategoryMockMvc.perform(get("/api/sub-categories/{id}", subCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(subCategory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }


    @Test
    @Transactional
    public void getSubCategoriesByIdFiltering() throws Exception {
        // Initialize the database
        subCategoryRepository.saveAndFlush(subCategory);

        Long id = subCategory.getId();

        defaultSubCategoryShouldBeFound("id.equals=" + id);
        defaultSubCategoryShouldNotBeFound("id.notEquals=" + id);

        defaultSubCategoryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSubCategoryShouldNotBeFound("id.greaterThan=" + id);

        defaultSubCategoryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSubCategoryShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllSubCategoriesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        subCategoryRepository.saveAndFlush(subCategory);

        // Get all the subCategoryList where name equals to DEFAULT_NAME
        defaultSubCategoryShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the subCategoryList where name equals to UPDATED_NAME
        defaultSubCategoryShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllSubCategoriesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        subCategoryRepository.saveAndFlush(subCategory);

        // Get all the subCategoryList where name not equals to DEFAULT_NAME
        defaultSubCategoryShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the subCategoryList where name not equals to UPDATED_NAME
        defaultSubCategoryShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllSubCategoriesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        subCategoryRepository.saveAndFlush(subCategory);

        // Get all the subCategoryList where name in DEFAULT_NAME or UPDATED_NAME
        defaultSubCategoryShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the subCategoryList where name equals to UPDATED_NAME
        defaultSubCategoryShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllSubCategoriesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        subCategoryRepository.saveAndFlush(subCategory);

        // Get all the subCategoryList where name is not null
        defaultSubCategoryShouldBeFound("name.specified=true");

        // Get all the subCategoryList where name is null
        defaultSubCategoryShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllSubCategoriesByNameContainsSomething() throws Exception {
        // Initialize the database
        subCategoryRepository.saveAndFlush(subCategory);

        // Get all the subCategoryList where name contains DEFAULT_NAME
        defaultSubCategoryShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the subCategoryList where name contains UPDATED_NAME
        defaultSubCategoryShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllSubCategoriesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        subCategoryRepository.saveAndFlush(subCategory);

        // Get all the subCategoryList where name does not contain DEFAULT_NAME
        defaultSubCategoryShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the subCategoryList where name does not contain UPDATED_NAME
        defaultSubCategoryShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllSubCategoriesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        subCategoryRepository.saveAndFlush(subCategory);

        // Get all the subCategoryList where code equals to DEFAULT_CODE
        defaultSubCategoryShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the subCategoryList where code equals to UPDATED_CODE
        defaultSubCategoryShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllSubCategoriesByCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        subCategoryRepository.saveAndFlush(subCategory);

        // Get all the subCategoryList where code not equals to DEFAULT_CODE
        defaultSubCategoryShouldNotBeFound("code.notEquals=" + DEFAULT_CODE);

        // Get all the subCategoryList where code not equals to UPDATED_CODE
        defaultSubCategoryShouldBeFound("code.notEquals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllSubCategoriesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        subCategoryRepository.saveAndFlush(subCategory);

        // Get all the subCategoryList where code in DEFAULT_CODE or UPDATED_CODE
        defaultSubCategoryShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the subCategoryList where code equals to UPDATED_CODE
        defaultSubCategoryShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllSubCategoriesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        subCategoryRepository.saveAndFlush(subCategory);

        // Get all the subCategoryList where code is not null
        defaultSubCategoryShouldBeFound("code.specified=true");

        // Get all the subCategoryList where code is null
        defaultSubCategoryShouldNotBeFound("code.specified=false");
    }
                @Test
    @Transactional
    public void getAllSubCategoriesByCodeContainsSomething() throws Exception {
        // Initialize the database
        subCategoryRepository.saveAndFlush(subCategory);

        // Get all the subCategoryList where code contains DEFAULT_CODE
        defaultSubCategoryShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the subCategoryList where code contains UPDATED_CODE
        defaultSubCategoryShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllSubCategoriesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        subCategoryRepository.saveAndFlush(subCategory);

        // Get all the subCategoryList where code does not contain DEFAULT_CODE
        defaultSubCategoryShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the subCategoryList where code does not contain UPDATED_CODE
        defaultSubCategoryShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }


    @Test
    @Transactional
    public void getAllSubCategoriesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        subCategoryRepository.saveAndFlush(subCategory);

        // Get all the subCategoryList where description equals to DEFAULT_DESCRIPTION
        defaultSubCategoryShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the subCategoryList where description equals to UPDATED_DESCRIPTION
        defaultSubCategoryShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllSubCategoriesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        subCategoryRepository.saveAndFlush(subCategory);

        // Get all the subCategoryList where description not equals to DEFAULT_DESCRIPTION
        defaultSubCategoryShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the subCategoryList where description not equals to UPDATED_DESCRIPTION
        defaultSubCategoryShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllSubCategoriesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        subCategoryRepository.saveAndFlush(subCategory);

        // Get all the subCategoryList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultSubCategoryShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the subCategoryList where description equals to UPDATED_DESCRIPTION
        defaultSubCategoryShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllSubCategoriesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        subCategoryRepository.saveAndFlush(subCategory);

        // Get all the subCategoryList where description is not null
        defaultSubCategoryShouldBeFound("description.specified=true");

        // Get all the subCategoryList where description is null
        defaultSubCategoryShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllSubCategoriesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        subCategoryRepository.saveAndFlush(subCategory);

        // Get all the subCategoryList where description contains DEFAULT_DESCRIPTION
        defaultSubCategoryShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the subCategoryList where description contains UPDATED_DESCRIPTION
        defaultSubCategoryShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllSubCategoriesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        subCategoryRepository.saveAndFlush(subCategory);

        // Get all the subCategoryList where description does not contain DEFAULT_DESCRIPTION
        defaultSubCategoryShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the subCategoryList where description does not contain UPDATED_DESCRIPTION
        defaultSubCategoryShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllSubCategoriesByCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        subCategoryRepository.saveAndFlush(subCategory);
        Category category = CategoryResourceIT.createEntity(em);
        em.persist(category);
        em.flush();
        subCategory.setCategory(category);
        subCategoryRepository.saveAndFlush(subCategory);
        Long categoryId = category.getId();

        // Get all the subCategoryList where category equals to categoryId
        defaultSubCategoryShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the subCategoryList where category equals to categoryId + 1
        defaultSubCategoryShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSubCategoryShouldBeFound(String filter) throws Exception {
        restSubCategoryMockMvc.perform(get("/api/sub-categories?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restSubCategoryMockMvc.perform(get("/api/sub-categories/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSubCategoryShouldNotBeFound(String filter) throws Exception {
        restSubCategoryMockMvc.perform(get("/api/sub-categories?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSubCategoryMockMvc.perform(get("/api/sub-categories/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingSubCategory() throws Exception {
        // Get the subCategory
        restSubCategoryMockMvc.perform(get("/api/sub-categories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSubCategory() throws Exception {
        // Initialize the database
        subCategoryRepository.saveAndFlush(subCategory);

        int databaseSizeBeforeUpdate = subCategoryRepository.findAll().size();

        // Update the subCategory
        SubCategory updatedSubCategory = subCategoryRepository.findById(subCategory.getId()).get();
        // Disconnect from session so that the updates on updatedSubCategory are not directly saved in db
        em.detach(updatedSubCategory);
        updatedSubCategory
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION);
        SubCategoryDTO subCategoryDTO = subCategoryMapper.toDto(updatedSubCategory);

        restSubCategoryMockMvc.perform(put("/api/sub-categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(subCategoryDTO)))
            .andExpect(status().isOk());

        // Validate the SubCategory in the database
        List<SubCategory> subCategoryList = subCategoryRepository.findAll();
        assertThat(subCategoryList).hasSize(databaseSizeBeforeUpdate);
        SubCategory testSubCategory = subCategoryList.get(subCategoryList.size() - 1);
        assertThat(testSubCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSubCategory.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testSubCategory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingSubCategory() throws Exception {
        int databaseSizeBeforeUpdate = subCategoryRepository.findAll().size();

        // Create the SubCategory
        SubCategoryDTO subCategoryDTO = subCategoryMapper.toDto(subCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubCategoryMockMvc.perform(put("/api/sub-categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(subCategoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SubCategory in the database
        List<SubCategory> subCategoryList = subCategoryRepository.findAll();
        assertThat(subCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSubCategory() throws Exception {
        // Initialize the database
        subCategoryRepository.saveAndFlush(subCategory);

        int databaseSizeBeforeDelete = subCategoryRepository.findAll().size();

        // Delete the subCategory
        restSubCategoryMockMvc.perform(delete("/api/sub-categories/{id}", subCategory.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SubCategory> subCategoryList = subCategoryRepository.findAll();
        assertThat(subCategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
