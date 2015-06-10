package com.proconco.report.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.proconco.report.domain.Groupp;
import com.proconco.report.repository.GrouppRepository;
import com.proconco.report.repository.search.GrouppSearchRepository;
import com.proconco.report.web.rest.util.PaginationUtil;
import com.proconco.report.web.rest.dto.GrouppDTO;
import com.proconco.report.web.rest.mapper.GrouppMapper;
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
 * REST controller for managing Groupp.
 */
@RestController
@RequestMapping("/api")
public class GrouppResource {

    private final Logger log = LoggerFactory.getLogger(GrouppResource.class);

    @Inject
    private GrouppRepository grouppRepository;

    @Inject
    private GrouppMapper grouppMapper;

    @Inject
    private GrouppSearchRepository grouppSearchRepository;

    /**
     * POST  /groupps -> Create a new groupp.
     */
    @RequestMapping(value = "/groupps",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody GrouppDTO grouppDTO) throws URISyntaxException {
        log.debug("REST request to save Groupp : {}", grouppDTO);
        if (grouppDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new groupp cannot already have an ID").build();
        }
        Groupp groupp = grouppMapper.grouppDTOToGroupp(grouppDTO);
        grouppRepository.save(groupp);
        grouppSearchRepository.save(groupp);
        return ResponseEntity.created(new URI("/api/groupps/" + grouppDTO.getId())).build();
    }

    /**
     * PUT  /groupps -> Updates an existing groupp.
     */
    @RequestMapping(value = "/groupps",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody GrouppDTO grouppDTO) throws URISyntaxException {
        log.debug("REST request to update Groupp : {}", grouppDTO);
        if (grouppDTO.getId() == null) {
            return create(grouppDTO);
        }
        Groupp groupp = grouppMapper.grouppDTOToGroupp(grouppDTO);
        grouppRepository.save(groupp);
        grouppSearchRepository.save(groupp);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /groupps -> get all the groupps.
     */
    @RequestMapping(value = "/groupps",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<GrouppDTO>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Groupp> page = grouppRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/groupps", offset, limit);
        return new ResponseEntity<>(page.getContent().stream()
            .map(groupp -> grouppMapper.grouppToGrouppDTO(groupp))
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /groupps/:id -> get the "id" groupp.
     */
    @RequestMapping(value = "/groupps/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<GrouppDTO> get(@PathVariable Long id) {
        log.debug("REST request to get Groupp : {}", id);
        return Optional.ofNullable(grouppRepository.findOne(id))
            .map(groupp -> grouppMapper.grouppToGrouppDTO(groupp))
            .map(grouppDTO -> new ResponseEntity<>(
                grouppDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /groupps/:id -> delete the "id" groupp.
     */
    @RequestMapping(value = "/groupps/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Groupp : {}", id);
        grouppRepository.delete(id);
        grouppSearchRepository.delete(id);
    }

    /**
     * SEARCH  /_search/groupps/:query -> search for the groupp corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/groupps/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Groupp> search(@PathVariable String query) {
        return StreamSupport
            .stream(grouppSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
