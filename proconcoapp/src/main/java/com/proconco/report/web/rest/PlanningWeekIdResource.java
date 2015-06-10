package com.proconco.report.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.proconco.report.domain.PlanningWeekId;
import com.proconco.report.repository.PlanningWeekIdRepository;
import com.proconco.report.repository.search.PlanningWeekIdSearchRepository;
import com.proconco.report.web.rest.util.PaginationUtil;
import com.proconco.report.web.rest.dto.PlanningWeekIdDTO;
import com.proconco.report.web.rest.mapper.PlanningWeekIdMapper;
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
 * REST controller for managing PlanningWeekId.
 */
@RestController
@RequestMapping("/api")
public class PlanningWeekIdResource {

    private final Logger log = LoggerFactory.getLogger(PlanningWeekIdResource.class);

    @Inject
    private PlanningWeekIdRepository planningWeekIdRepository;

    @Inject
    private PlanningWeekIdMapper planningWeekIdMapper;

    @Inject
    private PlanningWeekIdSearchRepository planningWeekIdSearchRepository;

    /**
     * POST  /planningWeekIds -> Create a new planningWeekId.
     */
    @RequestMapping(value = "/planningWeekIds",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody PlanningWeekIdDTO planningWeekIdDTO) throws URISyntaxException {
        log.debug("REST request to save PlanningWeekId : {}", planningWeekIdDTO);
        if (planningWeekIdDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new planningWeekId cannot already have an ID").build();
        }
        PlanningWeekId planningWeekId = planningWeekIdMapper.planningWeekIdDTOToPlanningWeekId(planningWeekIdDTO);
        planningWeekIdRepository.save(planningWeekId);
        planningWeekIdSearchRepository.save(planningWeekId);
        return ResponseEntity.created(new URI("/api/planningWeekIds/" + planningWeekIdDTO.getId())).build();
    }

    /**
     * PUT  /planningWeekIds -> Updates an existing planningWeekId.
     */
    @RequestMapping(value = "/planningWeekIds",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody PlanningWeekIdDTO planningWeekIdDTO) throws URISyntaxException {
        log.debug("REST request to update PlanningWeekId : {}", planningWeekIdDTO);
        if (planningWeekIdDTO.getId() == null) {
            return create(planningWeekIdDTO);
        }
        PlanningWeekId planningWeekId = planningWeekIdMapper.planningWeekIdDTOToPlanningWeekId(planningWeekIdDTO);
        planningWeekIdRepository.save(planningWeekId);
        planningWeekIdSearchRepository.save(planningWeekId);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /planningWeekIds -> get all the planningWeekIds.
     */
    @RequestMapping(value = "/planningWeekIds",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<PlanningWeekIdDTO>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<PlanningWeekId> page = planningWeekIdRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/planningWeekIds", offset, limit);
        return new ResponseEntity<>(page.getContent().stream()
            .map(planningWeekId -> planningWeekIdMapper.planningWeekIdToPlanningWeekIdDTO(planningWeekId))
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /planningWeekIds/:id -> get the "id" planningWeekId.
     */
    @RequestMapping(value = "/planningWeekIds/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PlanningWeekIdDTO> get(@PathVariable Long id) {
        log.debug("REST request to get PlanningWeekId : {}", id);
        return Optional.ofNullable(planningWeekIdRepository.findOne(id))
            .map(planningWeekId -> planningWeekIdMapper.planningWeekIdToPlanningWeekIdDTO(planningWeekId))
            .map(planningWeekIdDTO -> new ResponseEntity<>(
                planningWeekIdDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /planningWeekIds/:id -> delete the "id" planningWeekId.
     */
    @RequestMapping(value = "/planningWeekIds/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete PlanningWeekId : {}", id);
        planningWeekIdRepository.delete(id);
        planningWeekIdSearchRepository.delete(id);
    }

    /**
     * SEARCH  /_search/planningWeekIds/:query -> search for the planningWeekId corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/planningWeekIds/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<PlanningWeekId> search(@PathVariable String query) {
        return StreamSupport
            .stream(planningWeekIdSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
