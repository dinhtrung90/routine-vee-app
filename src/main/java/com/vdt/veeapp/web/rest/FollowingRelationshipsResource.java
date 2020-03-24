package com.vdt.veeapp.web.rest;

import com.vdt.veeapp.service.FollowingRelationshipsService;
import com.vdt.veeapp.web.rest.errors.BadRequestAlertException;
import com.vdt.veeapp.service.dto.FollowingRelationshipsDTO;
import com.vdt.veeapp.service.dto.FollowingRelationshipsCriteria;
import com.vdt.veeapp.service.FollowingRelationshipsQueryService;

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
 * REST controller for managing {@link com.vdt.veeapp.domain.FollowingRelationships}.
 */
@RestController
@RequestMapping("/api")
public class FollowingRelationshipsResource {

    private final Logger log = LoggerFactory.getLogger(FollowingRelationshipsResource.class);

    private static final String ENTITY_NAME = "followingRelationships";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FollowingRelationshipsService followingRelationshipsService;

    private final FollowingRelationshipsQueryService followingRelationshipsQueryService;

    public FollowingRelationshipsResource(FollowingRelationshipsService followingRelationshipsService, FollowingRelationshipsQueryService followingRelationshipsQueryService) {
        this.followingRelationshipsService = followingRelationshipsService;
        this.followingRelationshipsQueryService = followingRelationshipsQueryService;
    }

    /**
     * {@code POST  /following-relationships} : Create a new followingRelationships.
     *
     * @param followingRelationshipsDTO the followingRelationshipsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new followingRelationshipsDTO, or with status {@code 400 (Bad Request)} if the followingRelationships has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/following-relationships")
    public ResponseEntity<FollowingRelationshipsDTO> createFollowingRelationships(@Valid @RequestBody FollowingRelationshipsDTO followingRelationshipsDTO) throws URISyntaxException {
        log.debug("REST request to save FollowingRelationships : {}", followingRelationshipsDTO);
        if (followingRelationshipsDTO.getId() != null) {
            throw new BadRequestAlertException("A new followingRelationships cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FollowingRelationshipsDTO result = followingRelationshipsService.save(followingRelationshipsDTO);
        return ResponseEntity.created(new URI("/api/following-relationships/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /following-relationships} : Updates an existing followingRelationships.
     *
     * @param followingRelationshipsDTO the followingRelationshipsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated followingRelationshipsDTO,
     * or with status {@code 400 (Bad Request)} if the followingRelationshipsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the followingRelationshipsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/following-relationships")
    public ResponseEntity<FollowingRelationshipsDTO> updateFollowingRelationships(@Valid @RequestBody FollowingRelationshipsDTO followingRelationshipsDTO) throws URISyntaxException {
        log.debug("REST request to update FollowingRelationships : {}", followingRelationshipsDTO);
        if (followingRelationshipsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FollowingRelationshipsDTO result = followingRelationshipsService.save(followingRelationshipsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, followingRelationshipsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /following-relationships} : get all the followingRelationships.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of followingRelationships in body.
     */
    @GetMapping("/following-relationships")
    public ResponseEntity<List<FollowingRelationshipsDTO>> getAllFollowingRelationships(FollowingRelationshipsCriteria criteria, Pageable pageable) {
        log.debug("REST request to get FollowingRelationships by criteria: {}", criteria);
        Page<FollowingRelationshipsDTO> page = followingRelationshipsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /following-relationships/count} : count all the followingRelationships.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/following-relationships/count")
    public ResponseEntity<Long> countFollowingRelationships(FollowingRelationshipsCriteria criteria) {
        log.debug("REST request to count FollowingRelationships by criteria: {}", criteria);
        return ResponseEntity.ok().body(followingRelationshipsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /following-relationships/:id} : get the "id" followingRelationships.
     *
     * @param id the id of the followingRelationshipsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the followingRelationshipsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/following-relationships/{id}")
    public ResponseEntity<FollowingRelationshipsDTO> getFollowingRelationships(@PathVariable Long id) {
        log.debug("REST request to get FollowingRelationships : {}", id);
        Optional<FollowingRelationshipsDTO> followingRelationshipsDTO = followingRelationshipsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(followingRelationshipsDTO);
    }

    /**
     * {@code DELETE  /following-relationships/:id} : delete the "id" followingRelationships.
     *
     * @param id the id of the followingRelationshipsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/following-relationships/{id}")
    public ResponseEntity<Void> deleteFollowingRelationships(@PathVariable Long id) {
        log.debug("REST request to delete FollowingRelationships : {}", id);
        followingRelationshipsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
