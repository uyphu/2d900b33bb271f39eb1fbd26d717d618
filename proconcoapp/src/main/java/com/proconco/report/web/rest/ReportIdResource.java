package com.proconco.report.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.proconco.report.domain.ReportId;
import com.proconco.report.repository.ReportIdRepository;
import com.proconco.report.repository.search.ReportIdSearchRepository;
import com.proconco.report.web.rest.util.PaginationUtil;
import com.proconco.report.web.rest.dto.ReportIdDTO;
import com.proconco.report.web.rest.mapper.ReportIdMapper;
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
 * REST controller for managing ReportId.
 */
@RestController
@RequestMapping("/api")
public class ReportIdResource {

    private final Logger log = LoggerFactory.getLogger(ReportIdResource.class);

    @Inject
    private ReportIdRepository reportIdRepository;

    @Inject
    private ReportIdMapper reportIdMapper;

    @Inject
    private ReportIdSearchRepository reportIdSearchRepository;

    /**
     * POST  /reportIds -> Create a new reportId.
     */
    @RequestMapping(value = "/reportIds",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody ReportIdDTO reportIdDTO) throws URISyntaxException {
        log.debug("REST request to save ReportId : {}", reportIdDTO);
        if (reportIdDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new reportId cannot already have an ID").build();
        }
        ReportId reportId = reportIdMapper.reportIdDTOToReportId(reportIdDTO);
        reportIdRepository.save(reportId);
        reportIdSearchRepository.save(reportId);
        return ResponseEntity.created(new URI("/api/reportIds/" + reportIdDTO.getId())).build();
    }

    /**
     * PUT  /reportIds -> Updates an existing reportId.
     */
    @RequestMapping(value = "/reportIds",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody ReportIdDTO reportIdDTO) throws URISyntaxException {
        log.debug("REST request to update ReportId : {}", reportIdDTO);
        if (reportIdDTO.getId() == null) {
            return create(reportIdDTO);
        }
        ReportId reportId = reportIdMapper.reportIdDTOToReportId(reportIdDTO);
        reportIdRepository.save(reportId);
        reportIdSearchRepository.save(reportId);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /reportIds -> get all the reportIds.
     */
    @RequestMapping(value = "/reportIds",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<ReportIdDTO>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<ReportId> page = reportIdRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/reportIds", offset, limit);
        return new ResponseEntity<>(page.getContent().stream()
            .map(reportId -> reportIdMapper.reportIdToReportIdDTO(reportId))
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /reportIds/:id -> get the "id" reportId.
     */
    @RequestMapping(value = "/reportIds/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ReportIdDTO> get(@PathVariable Long id) {
        log.debug("REST request to get ReportId : {}", id);
        return Optional.ofNullable(reportIdRepository.findOne(id))
            .map(reportId -> reportIdMapper.reportIdToReportIdDTO(reportId))
            .map(reportIdDTO -> new ResponseEntity<>(
                reportIdDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /reportIds/:id -> delete the "id" reportId.
     */
    @RequestMapping(value = "/reportIds/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete ReportId : {}", id);
        reportIdRepository.delete(id);
        reportIdSearchRepository.delete(id);
    }

    /**
     * SEARCH  /_search/reportIds/:query -> search for the reportId corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/reportIds/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ReportId> search(@PathVariable String query) {
        return StreamSupport
            .stream(reportIdSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
