package com.vdt.veeapp.web.rest;

import com.vdt.veeapp.service.EventTimesService;
import com.vdt.veeapp.web.rest.errors.BadRequestAlertException;
import com.vdt.veeapp.service.dto.EventTimesDTO;
import com.vdt.veeapp.service.dto.EventTimesCriteria;
import com.vdt.veeapp.service.EventTimesQueryService;

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
 * REST controller for managing {@link com.vdt.veeapp.domain.EventTimes}.
 */
@RestController
@RequestMapping("/api")
public class EventTimesResource {

    private final Logger log = LoggerFactory.getLogger(EventTimesResource.class);

    private static final String ENTITY_NAME = "eventTimes";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EventTimesService eventTimesService;

    private final EventTimesQueryService eventTimesQueryService;

    public EventTimesResource(EventTimesService eventTimesService, EventTimesQueryService eventTimesQueryService) {
        this.eventTimesService = eventTimesService;
        this.eventTimesQueryService = eventTimesQueryService;
    }

    /**
     * {@code POST  /event-times} : Create a new eventTimes.
     *
     * @param eventTimesDTO the eventTimesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new eventTimesDTO, or with status {@code 400 (Bad Request)} if the eventTimes has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/event-times")
    public ResponseEntity<EventTimesDTO> createEventTimes(@Valid @RequestBody EventTimesDTO eventTimesDTO) throws URISyntaxException {
        log.debug("REST request to save EventTimes : {}", eventTimesDTO);
        if (eventTimesDTO.getId() != null) {
            throw new BadRequestAlertException("A new eventTimes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EventTimesDTO result = eventTimesService.save(eventTimesDTO);
        return ResponseEntity.created(new URI("/api/event-times/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /event-times} : Updates an existing eventTimes.
     *
     * @param eventTimesDTO the eventTimesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventTimesDTO,
     * or with status {@code 400 (Bad Request)} if the eventTimesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the eventTimesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/event-times")
    public ResponseEntity<EventTimesDTO> updateEventTimes(@Valid @RequestBody EventTimesDTO eventTimesDTO) throws URISyntaxException {
        log.debug("REST request to update EventTimes : {}", eventTimesDTO);
        if (eventTimesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EventTimesDTO result = eventTimesService.save(eventTimesDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventTimesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /event-times} : get all the eventTimes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eventTimes in body.
     */
    @GetMapping("/event-times")
    public ResponseEntity<List<EventTimesDTO>> getAllEventTimes(EventTimesCriteria criteria, Pageable pageable) {
        log.debug("REST request to get EventTimes by criteria: {}", criteria);
        Page<EventTimesDTO> page = eventTimesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /event-times/count} : count all the eventTimes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/event-times/count")
    public ResponseEntity<Long> countEventTimes(EventTimesCriteria criteria) {
        log.debug("REST request to count EventTimes by criteria: {}", criteria);
        return ResponseEntity.ok().body(eventTimesQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /event-times/:id} : get the "id" eventTimes.
     *
     * @param id the id of the eventTimesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eventTimesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/event-times/{id}")
    public ResponseEntity<EventTimesDTO> getEventTimes(@PathVariable Long id) {
        log.debug("REST request to get EventTimes : {}", id);
        Optional<EventTimesDTO> eventTimesDTO = eventTimesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eventTimesDTO);
    }

    /**
     * {@code DELETE  /event-times/:id} : delete the "id" eventTimes.
     *
     * @param id the id of the eventTimesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/event-times/{id}")
    public ResponseEntity<Void> deleteEventTimes(@PathVariable Long id) {
        log.debug("REST request to delete EventTimes : {}", id);
        eventTimesService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
