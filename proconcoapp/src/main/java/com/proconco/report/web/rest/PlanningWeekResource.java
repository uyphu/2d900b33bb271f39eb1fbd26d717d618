package com.proconco.report.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.proconco.report.domain.PlanningWeek;
import com.proconco.report.repository.PlanningWeekRepository;
import com.proconco.report.repository.search.PlanningWeekSearchRepository;
import com.proconco.report.web.rest.util.PaginationUtil;
import com.proconco.report.web.rest.dto.PlanningWeekDTO;
import com.proconco.report.web.rest.mapper.PlanningWeekMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing PlanningWeek.
 */
@RestController
@RequestMapping("/api")
public class PlanningWeekResource {

    private final Logger log = LoggerFactory.getLogger(PlanningWeekResource.class);

    @Inject
    private PlanningWeekRepository planningWeekRepository;

    @Inject
    private PlanningWeekMapper planningWeekMapper;

    @Inject
    private PlanningWeekSearchRepository planningWeekSearchRepository;

    /**
     * POST  /planningWeeks -> Create a new planningWeek.
     */
    @RequestMapping(value = "/planningWeeks",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody PlanningWeekDTO planningWeekDTO) throws URISyntaxException {
        log.debug("REST request to save PlanningWeek : {}", planningWeekDTO);
        if (planningWeekDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new planningWeek cannot already have an ID").build();
        }
        PlanningWeek planningWeek = planningWeekMapper.planningWeekDTOToPlanningWeek(planningWeekDTO);
        planningWeekRepository.save(planningWeek);
        planningWeekSearchRepository.save(planningWeek);
        return ResponseEntity.created(new URI("/api/planningWeeks/" + planningWeekDTO.getId())).build();
    }

    /**
     * PUT  /planningWeeks -> Updates an existing planningWeek.
     */
    @RequestMapping(value = "/planningWeeks",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody PlanningWeekDTO planningWeekDTO) throws URISyntaxException {
        log.debug("REST request to update PlanningWeek : {}", planningWeekDTO);
        if (planningWeekDTO.getId() == null) {
            return create(planningWeekDTO);
        }
        PlanningWeek planningWeek = planningWeekMapper.planningWeekDTOToPlanningWeek(planningWeekDTO);
        planningWeekRepository.save(planningWeek);
        planningWeekSearchRepository.save(planningWeek);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /planningWeeks -> get all the planningWeeks.
     */
    @RequestMapping(value = "/planningWeeks",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<PlanningWeekDTO>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<PlanningWeek> page = planningWeekRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/planningWeeks", offset, limit);
        return new ResponseEntity<>(page.getContent().stream()
            .map(planningWeek -> planningWeekMapper.planningWeekToPlanningWeekDTO(planningWeek))
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /planningWeeks/:id -> get the "id" planningWeek.
     */
    @RequestMapping(value = "/planningWeeks/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PlanningWeekDTO> get(@PathVariable Long id) {
        log.debug("REST request to get PlanningWeek : {}", id);
        return Optional.ofNullable(planningWeekRepository.findOne(id))
            .map(planningWeek -> planningWeekMapper.planningWeekToPlanningWeekDTO(planningWeek))
            .map(planningWeekDTO -> new ResponseEntity<>(
                planningWeekDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /planningWeeks/:id -> delete the "id" planningWeek.
     */
    @RequestMapping(value = "/planningWeeks/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete PlanningWeek : {}", id);
        planningWeekRepository.delete(id);
        planningWeekSearchRepository.delete(id);
    }

    /**
     * SEARCH  /_search/planningWeeks/:query -> search for the planningWeek corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/planningWeeks/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<PlanningWeek> search(@PathVariable String query) {
        return StreamSupport
            .stream(planningWeekSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
