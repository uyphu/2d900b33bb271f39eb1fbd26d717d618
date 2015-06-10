package com.proconco.report.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.proconco.report.domain.Report;
import com.proconco.report.repository.ReportRepository;
import com.proconco.report.repository.search.ReportSearchRepository;
import com.proconco.report.web.rest.util.PaginationUtil;
import com.proconco.report.web.rest.dto.ReportDTO;
import com.proconco.report.web.rest.mapper.ReportMapper;
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
 * REST controller for managing Report.
 */
@RestController
@RequestMapping("/api")
public class ReportResource {

    private final Logger log = LoggerFactory.getLogger(ReportResource.class);

    @Inject
    private ReportRepository reportRepository;

    @Inject
    private ReportMapper reportMapper;

    @Inject
    private ReportSearchRepository reportSearchRepository;

    /**
     * POST  /reports -> Create a new report.
     */
    @RequestMapping(value = "/reports",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody ReportDTO reportDTO) throws URISyntaxException {
        log.debug("REST request to save Report : {}", reportDTO);
        if (reportDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new report cannot already have an ID").build();
        }
        Report report = reportMapper.reportDTOToReport(reportDTO);
        reportRepository.save(report);
        reportSearchRepository.save(report);
        return ResponseEntity.created(new URI("/api/reports/" + reportDTO.getId())).build();
    }

    /**
     * PUT  /reports -> Updates an existing report.
     */
    @RequestMapping(value = "/reports",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody ReportDTO reportDTO) throws URISyntaxException {
        log.debug("REST request to update Report : {}", reportDTO);
        if (reportDTO.getId() == null) {
            return create(reportDTO);
        }
        Report report = reportMapper.reportDTOToReport(reportDTO);
        reportRepository.save(report);
        reportSearchRepository.save(report);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /reports -> get all the reports.
     */
    @RequestMapping(value = "/reports",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<ReportDTO>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Report> page = reportRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/reports", offset, limit);
        return new ResponseEntity<>(page.getContent().stream()
            .map(report -> reportMapper.reportToReportDTO(report))
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /reports/:id -> get the "id" report.
     */
    @RequestMapping(value = "/reports/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ReportDTO> get(@PathVariable Long id) {
        log.debug("REST request to get Report : {}", id);
        return Optional.ofNullable(reportRepository.findOne(id))
            .map(report -> reportMapper.reportToReportDTO(report))
            .map(reportDTO -> new ResponseEntity<>(
                reportDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /reports/:id -> delete the "id" report.
     */
    @RequestMapping(value = "/reports/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Report : {}", id);
        reportRepository.delete(id);
        reportSearchRepository.delete(id);
    }

    /**
     * SEARCH  /_search/reports/:query -> search for the report corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/reports/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Report> search(@PathVariable String query) {
        return StreamSupport
            .stream(reportSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
