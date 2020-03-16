package com.vdt.veeapp.web.rest;

import com.vdt.veeapp.service.SubCategoryService;
import com.vdt.veeapp.web.rest.errors.BadRequestAlertException;
import com.vdt.veeapp.service.dto.SubCategoryDTO;
import com.vdt.veeapp.service.dto.SubCategoryCriteria;
import com.vdt.veeapp.service.SubCategoryQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.vdt.veeapp.domain.SubCategory}.
 */
@RestController
@RequestMapping("/api")
public class SubCategoryResource {

    private final Logger log = LoggerFactory.getLogger(SubCategoryResource.class);

    private static final String ENTITY_NAME = "subCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubCategoryService subCategoryService;

    private final SubCategoryQueryService subCategoryQueryService;

    public SubCategoryResource(SubCategoryService subCategoryService, SubCategoryQueryService subCategoryQueryService) {
        this.subCategoryService = subCategoryService;
        this.subCategoryQueryService = subCategoryQueryService;
    }

    /**
     * {@code POST  /sub-categories} : Create a new subCategory.
     *
     * @param subCategoryDTO the subCategoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subCategoryDTO, or with status {@code 400 (Bad Request)} if the subCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sub-categories")
    public ResponseEntity<SubCategoryDTO> createSubCategory(@Valid @RequestBody SubCategoryDTO subCategoryDTO) throws URISyntaxException {
        log.debug("REST request to save SubCategory : {}", subCategoryDTO);
        if (subCategoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new subCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SubCategoryDTO result = subCategoryService.save(subCategoryDTO);
        return ResponseEntity.created(new URI("/api/sub-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sub-categories} : Updates an existing subCategory.
     *
     * @param subCategoryDTO the subCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the subCategoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sub-categories")
    public ResponseEntity<SubCategoryDTO> updateSubCategory(@Valid @RequestBody SubCategoryDTO subCategoryDTO) throws URISyntaxException {
        log.debug("REST request to update SubCategory : {}", subCategoryDTO);
        if (subCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SubCategoryDTO result = subCategoryService.save(subCategoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subCategoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /sub-categories} : get all the subCategories.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subCategories in body.
     */
    @GetMapping("/sub-categories")
    public ResponseEntity<List<SubCategoryDTO>> getAllSubCategories(SubCategoryCriteria criteria, Pageable pageable) {
        log.debug("REST request to get SubCategories by criteria: {}", criteria);
        Page<SubCategoryDTO> page = subCategoryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sub-categories/count} : count all the subCategories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/sub-categories/count")
    public ResponseEntity<Long> countSubCategories(SubCategoryCriteria criteria) {
        log.debug("REST request to count SubCategories by criteria: {}", criteria);
        return ResponseEntity.ok().body(subCategoryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sub-categories/:id} : get the "id" subCategory.
     *
     * @param id the id of the subCategoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subCategoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sub-categories/{id}")
    public ResponseEntity<SubCategoryDTO> getSubCategory(@PathVariable Long id) {
        log.debug("REST request to get SubCategory : {}", id);
        Optional<SubCategoryDTO> subCategoryDTO = subCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(subCategoryDTO);
    }

    /**
     * {@code DELETE  /sub-categories/:id} : delete the "id" subCategory.
     *
     * @param id the id of the subCategoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sub-categories/{id}")
    public ResponseEntity<Void> deleteSubCategory(@PathVariable Long id) {
        log.debug("REST request to delete SubCategory : {}", id);
        subCategoryService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
