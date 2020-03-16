package com.vdt.veeapp.service;

import com.vdt.veeapp.service.dto.SubCategoryDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.vdt.veeapp.domain.SubCategory}.
 */
public interface SubCategoryService {

    /**
     * Save a subCategory.
     *
     * @param subCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    SubCategoryDTO save(SubCategoryDTO subCategoryDTO);

    /**
     * Get all the subCategories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SubCategoryDTO> findAll(Pageable pageable);

    /**
     * Get the "id" subCategory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SubCategoryDTO> findOne(Long id);

    /**
     * Delete the "id" subCategory.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
