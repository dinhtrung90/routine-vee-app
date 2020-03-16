package com.vdt.veeapp.web.rest;

import com.vdt.veeapp.service.HabitService;
import com.vdt.veeapp.web.rest.errors.BadRequestAlertException;
import com.vdt.veeapp.service.dto.HabitDTO;
import com.vdt.veeapp.service.dto.HabitCriteria;
import com.vdt.veeapp.service.HabitQueryService;

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
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link com.vdt.veeapp.domain.Habit}.
 */
@RestController
@RequestMapping("/api")
public class HabitResource {

    private final Logger log = LoggerFactory.getLogger(HabitResource.class);

    private static final String ENTITY_NAME = "habit";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HabitService habitService;

    private final HabitQueryService habitQueryService;

    public HabitResource(HabitService habitService, HabitQueryService habitQueryService) {
        this.habitService = habitService;
        this.habitQueryService = habitQueryService;
    }

    /**
     * {@code POST  /habits} : Create a new habit.
     *
     * @param habitDTO the habitDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new habitDTO, or with status {@code 400 (Bad Request)} if the habit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/habits")
    public ResponseEntity<HabitDTO> createHabit(@Valid @RequestBody HabitDTO habitDTO) throws URISyntaxException {
        log.debug("REST request to save Habit : {}", habitDTO);
        if (habitDTO.getId() != null) {
            throw new BadRequestAlertException("A new habit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (Objects.isNull(habitDTO.getReminderId())) {
            throw new BadRequestAlertException("Invalid association value provided", ENTITY_NAME, "null");
        }
        HabitDTO result = habitService.save(habitDTO);
        return ResponseEntity.created(new URI("/api/habits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /habits} : Updates an existing habit.
     *
     * @param habitDTO the habitDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated habitDTO,
     * or with status {@code 400 (Bad Request)} if the habitDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the habitDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/habits")
    public ResponseEntity<HabitDTO> updateHabit(@Valid @RequestBody HabitDTO habitDTO) throws URISyntaxException {
        log.debug("REST request to update Habit : {}", habitDTO);
        if (habitDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        HabitDTO result = habitService.save(habitDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, habitDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /habits} : get all the habits.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of habits in body.
     */
    @GetMapping("/habits")
    public ResponseEntity<List<HabitDTO>> getAllHabits(HabitCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Habits by criteria: {}", criteria);
        Page<HabitDTO> page = habitQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /habits/count} : count all the habits.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/habits/count")
    public ResponseEntity<Long> countHabits(HabitCriteria criteria) {
        log.debug("REST request to count Habits by criteria: {}", criteria);
        return ResponseEntity.ok().body(habitQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /habits/:id} : get the "id" habit.
     *
     * @param id the id of the habitDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the habitDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/habits/{id}")
    public ResponseEntity<HabitDTO> getHabit(@PathVariable Long id) {
        log.debug("REST request to get Habit : {}", id);
        Optional<HabitDTO> habitDTO = habitService.findOne(id);
        return ResponseUtil.wrapOrNotFound(habitDTO);
    }

    /**
     * {@code DELETE  /habits/:id} : delete the "id" habit.
     *
     * @param id the id of the habitDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/habits/{id}")
    public ResponseEntity<Void> deleteHabit(@PathVariable Long id) {
        log.debug("REST request to delete Habit : {}", id);
        habitService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
