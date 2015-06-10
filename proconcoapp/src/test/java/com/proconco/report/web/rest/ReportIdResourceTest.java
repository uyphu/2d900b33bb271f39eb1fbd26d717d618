package com.proconco.report.web.rest;

import com.proconco.report.Application;
import com.proconco.report.domain.ReportId;
import com.proconco.report.repository.ReportIdRepository;
import com.proconco.report.repository.search.ReportIdSearchRepository;
import com.proconco.report.web.rest.mapper.ReportIdMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ReportIdResource REST controller.
 *
 * @see ReportIdResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ReportIdResourceTest {


    private static final Integer DEFAULT_WEEK = 1;
    private static final Integer UPDATED_WEEK = 2;

    private static final Integer DEFAULT_YEAR = 1970;
    private static final Integer UPDATED_YEAR = 1971;

    @Inject
    private ReportIdRepository reportIdRepository;

    @Inject
    private ReportIdMapper reportIdMapper;

    @Inject
    private ReportIdSearchRepository reportIdSearchRepository;

    private MockMvc restReportIdMockMvc;

    private ReportId reportId;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReportIdResource reportIdResource = new ReportIdResource();
        ReflectionTestUtils.setField(reportIdResource, "reportIdRepository", reportIdRepository);
        ReflectionTestUtils.setField(reportIdResource, "reportIdMapper", reportIdMapper);
        ReflectionTestUtils.setField(reportIdResource, "reportIdSearchRepository", reportIdSearchRepository);
        this.restReportIdMockMvc = MockMvcBuilders.standaloneSetup(reportIdResource).build();
    }

    @Before
    public void initTest() {
        reportId = new ReportId();
        reportId.setWeek(DEFAULT_WEEK);
        reportId.setYear(DEFAULT_YEAR);
    }

    @Test
    @Transactional
    public void createReportId() throws Exception {
        int databaseSizeBeforeCreate = reportIdRepository.findAll().size();

        // Create the ReportId
        restReportIdMockMvc.perform(post("/api/reportIds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(reportId)))
                .andExpect(status().isCreated());

        // Validate the ReportId in the database
        List<ReportId> reportIds = reportIdRepository.findAll();
        assertThat(reportIds).hasSize(databaseSizeBeforeCreate + 1);
        ReportId testReportId = reportIds.get(reportIds.size() - 1);
        assertThat(testReportId.getWeek()).isEqualTo(DEFAULT_WEEK);
        assertThat(testReportId.getYear()).isEqualTo(DEFAULT_YEAR);
    }

    @Test
    @Transactional
    public void checkWeekIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(reportIdRepository.findAll()).hasSize(0);
        // set the field null
        reportId.setWeek(null);

        // Create the ReportId, which fails.
        restReportIdMockMvc.perform(post("/api/reportIds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(reportId)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<ReportId> reportIds = reportIdRepository.findAll();
        assertThat(reportIds).hasSize(0);
    }

    @Test
    @Transactional
    public void checkYearIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(reportIdRepository.findAll()).hasSize(0);
        // set the field null
        reportId.setYear(null);

        // Create the ReportId, which fails.
        restReportIdMockMvc.perform(post("/api/reportIds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(reportId)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<ReportId> reportIds = reportIdRepository.findAll();
        assertThat(reportIds).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllReportIds() throws Exception {
        // Initialize the database
        reportIdRepository.saveAndFlush(reportId);

        // Get all the reportIds
        restReportIdMockMvc.perform(get("/api/reportIds"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(reportId.getId().intValue())))
                .andExpect(jsonPath("$.[*].week").value(hasItem(DEFAULT_WEEK)))
                .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)));
    }

    @Test
    @Transactional
    public void getReportId() throws Exception {
        // Initialize the database
        reportIdRepository.saveAndFlush(reportId);

        // Get the reportId
        restReportIdMockMvc.perform(get("/api/reportIds/{id}", reportId.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(reportId.getId().intValue()))
            .andExpect(jsonPath("$.week").value(DEFAULT_WEEK))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR));
    }

    @Test
    @Transactional
    public void getNonExistingReportId() throws Exception {
        // Get the reportId
        restReportIdMockMvc.perform(get("/api/reportIds/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReportId() throws Exception {
        // Initialize the database
        reportIdRepository.saveAndFlush(reportId);

		int databaseSizeBeforeUpdate = reportIdRepository.findAll().size();

        // Update the reportId
        reportId.setWeek(UPDATED_WEEK);
        reportId.setYear(UPDATED_YEAR);
        restReportIdMockMvc.perform(put("/api/reportIds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(reportId)))
                .andExpect(status().isOk());

        // Validate the ReportId in the database
        List<ReportId> reportIds = reportIdRepository.findAll();
        assertThat(reportIds).hasSize(databaseSizeBeforeUpdate);
        ReportId testReportId = reportIds.get(reportIds.size() - 1);
        assertThat(testReportId.getWeek()).isEqualTo(UPDATED_WEEK);
        assertThat(testReportId.getYear()).isEqualTo(UPDATED_YEAR);
    }

    @Test
    @Transactional
    public void deleteReportId() throws Exception {
        // Initialize the database
        reportIdRepository.saveAndFlush(reportId);

		int databaseSizeBeforeDelete = reportIdRepository.findAll().size();

        // Get the reportId
        restReportIdMockMvc.perform(delete("/api/reportIds/{id}", reportId.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ReportId> reportIds = reportIdRepository.findAll();
        assertThat(reportIds).hasSize(databaseSizeBeforeDelete - 1);
    }
}
