package com.proconco.report.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.proconco.report.domain.Position;
import com.proconco.report.repository.PositionRepository;
import com.proconco.report.repository.search.PositionSearchRepository;
import com.proconco.report.web.rest.util.PaginationUtil;
import com.proconco.report.web.rest.dto.PositionDTO;
import com.proconco.report.web.rest.mapper.PositionMapper;
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
 * REST controller for managing Position.
 */
@RestController
@RequestMapping("/api")
public class PositionResource {

    private final Logger log = LoggerFactory.getLogger(PositionResource.class);

    @Inject
    private PositionRepository positionRepository;

    @Inject
    private PositionMapper positionMapper;

    @Inject
    private PositionSearchRepository positionSearchRepository;

    /**
     * POST  /positions -> Create a new position.
     */
    @RequestMapping(value = "/positions",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody PositionDTO positionDTO) throws URISyntaxException {
        log.debug("REST request to save Position : {}", positionDTO);
        if (positionDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new position cannot already have an ID").build();
        }
        Position position = positionMapper.positionDTOToPosition(positionDTO);
        positionRepository.save(position);
        positionSearchRepository.save(position);
        return ResponseEntity.created(new URI("/api/positions/" + positionDTO.getId())).build();
    }

    /**
     * PUT  /positions -> Updates an existing position.
     */
    @RequestMapping(value = "/positions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody PositionDTO positionDTO) throws URISyntaxException {
        log.debug("REST request to update Position : {}", positionDTO);
        if (positionDTO.getId() == null) {
            return create(positionDTO);
        }
        Position position = positionMapper.positionDTOToPosition(positionDTO);
        positionRepository.save(position);
        positionSearchRepository.save(position);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /positions -> get all the positions.
     */
    @RequestMapping(value = "/positions",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<PositionDTO>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Position> page = positionRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/positions", offset, limit);
        return new ResponseEntity<>(page.getContent().stream()
            .map(position -> positionMapper.positionToPositionDTO(position))
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /positions/:id -> get the "id" position.
     */
    @RequestMapping(value = "/positions/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PositionDTO> get(@PathVariable Long id) {
        log.debug("REST request to get Position : {}", id);
        return Optional.ofNullable(positionRepository.findOne(id))
            .map(position -> positionMapper.positionToPositionDTO(position))
            .map(positionDTO -> new ResponseEntity<>(
                positionDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /positions/:id -> delete the "id" position.
     */
    @RequestMapping(value = "/positions/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Position : {}", id);
        positionRepository.delete(id);
        positionSearchRepository.delete(id);
    }

    /**
     * SEARCH  /_search/positions/:query -> search for the position corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/positions/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Position> search(@PathVariable String query) {
        return StreamSupport
            .stream(positionSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
