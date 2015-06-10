package com.proconco.report.web.rest;

import com.proconco.report.Application;
import com.proconco.report.domain.Report;
import com.proconco.report.repository.ReportRepository;
import com.proconco.report.repository.search.ReportSearchRepository;
import com.proconco.report.web.rest.mapper.ReportMapper;

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
import org.joda.time.LocalDate;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ReportResource REST controller.
 *
 * @see ReportResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ReportResourceTest {


    private static final BigDecimal DEFAULT_AMT = new BigDecimal(0);
    private static final BigDecimal UPDATED_AMT = new BigDecimal(1);
    private static final String DEFAULT_MARKET_RPT = "SAMPLE_TEXT";
    private static final String UPDATED_MARKET_RPT = "UPDATED_TEXT";
    private static final String DEFAULT_MONDAY = "SAMPLE_TEXT";
    private static final String UPDATED_MONDAY = "UPDATED_TEXT";
    private static final String DEFAULT_TUESDAY = "SAMPLE_TEXT";
    private static final String UPDATED_TUESDAY = "UPDATED_TEXT";
    private static final String DEFAULT_WEDNESDAY = "SAMPLE_TEXT";
    private static final String UPDATED_WEDNESDAY = "UPDATED_TEXT";
    private static final String DEFAULT_THURSDAY = "SAMPLE_TEXT";
    private static final String UPDATED_THURSDAY = "UPDATED_TEXT";
    private static final String DEFAULT_FRIDAY = "SAMPLE_TEXT";
    private static final String UPDATED_FRIDAY = "UPDATED_TEXT";
    private static final String DEFAULT_SATURDAY = "SAMPLE_TEXT";
    private static final String UPDATED_SATURDAY = "UPDATED_TEXT";
    private static final String DEFAULT_SUNDAY = "SAMPLE_TEXT";
    private static final String UPDATED_SUNDAY = "UPDATED_TEXT";
    private static final String DEFAULT_DEL_FLAG = "SAMPLE_TEXT";
    private static final String UPDATED_DEL_FLAG = "UPDATED_TEXT";
    private static final String DEFAULT_CRT_UID = "SAMPLE_TEXT";
    private static final String UPDATED_CRT_UID = "UPDATED_TEXT";

    private static final LocalDate DEFAULT_CRT_TMS = new LocalDate(0L);
    private static final LocalDate UPDATED_CRT_TMS = new LocalDate();
    private static final String DEFAULT_UPD_UID = "SAMPLE_TEXT";
    private static final String UPDATED_UPD_UID = "UPDATED_TEXT";

    private static final LocalDate DEFAULT_UPD_TMS = new LocalDate(0L);
    private static final LocalDate UPDATED_UPD_TMS = new LocalDate();

    @Inject
    private ReportRepository reportRepository;

    @Inject
    private ReportMapper reportMapper;

    @Inject
    private ReportSearchRepository reportSearchRepository;

    private MockMvc restReportMockMvc;

    private Report report;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReportResource reportResource = new ReportResource();
        ReflectionTestUtils.setField(reportResource, "reportRepository", reportRepository);
        ReflectionTestUtils.setField(reportResource, "reportMapper", reportMapper);
        ReflectionTestUtils.setField(reportResource, "reportSearchRepository", reportSearchRepository);
        this.restReportMockMvc = MockMvcBuilders.standaloneSetup(reportResource).build();
    }

    @Before
    public void initTest() {
        report = new Report();
        report.setAmt(DEFAULT_AMT);
        report.setMarketRpt(DEFAULT_MARKET_RPT);
        report.setMonday(DEFAULT_MONDAY);
        report.setTuesday(DEFAULT_TUESDAY);
        report.setWednesday(DEFAULT_WEDNESDAY);
        report.setThursday(DEFAULT_THURSDAY);
        report.setFriday(DEFAULT_FRIDAY);
        report.setSaturday(DEFAULT_SATURDAY);
        report.setSunday(DEFAULT_SUNDAY);
        report.setDelFlag(DEFAULT_DEL_FLAG);
        report.setCrtUid(DEFAULT_CRT_UID);
        report.setCrtTms(DEFAULT_CRT_TMS);
        report.setUpdUid(DEFAULT_UPD_UID);
        report.setUpdTms(DEFAULT_UPD_TMS);
    }

    @Test
    @Transactional
    public void createReport() throws Exception {
        int databaseSizeBeforeCreate = reportRepository.findAll().size();

        // Create the Report
        restReportMockMvc.perform(post("/api/reports")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(report)))
                .andExpect(status().isCreated());

        // Validate the Report in the database
        List<Report> reports = reportRepository.findAll();
        assertThat(reports).hasSize(databaseSizeBeforeCreate + 1);
        Report testReport = reports.get(reports.size() - 1);
        assertThat(testReport.getAmt()).isEqualTo(DEFAULT_AMT);
        assertThat(testReport.getMarketRpt()).isEqualTo(DEFAULT_MARKET_RPT);
        assertThat(testReport.getMonday()).isEqualTo(DEFAULT_MONDAY);
        assertThat(testReport.getTuesday()).isEqualTo(DEFAULT_TUESDAY);
        assertThat(testReport.getWednesday()).isEqualTo(DEFAULT_WEDNESDAY);
        assertThat(testReport.getThursday()).isEqualTo(DEFAULT_THURSDAY);
        assertThat(testReport.getFriday()).isEqualTo(DEFAULT_FRIDAY);
        assertThat(testReport.getSaturday()).isEqualTo(DEFAULT_SATURDAY);
        assertThat(testReport.getSunday()).isEqualTo(DEFAULT_SUNDAY);
        assertThat(testReport.getDelFlag()).isEqualTo(DEFAULT_DEL_FLAG);
        assertThat(testReport.getCrtUid()).isEqualTo(DEFAULT_CRT_UID);
        assertThat(testReport.getCrtTms()).isEqualTo(DEFAULT_CRT_TMS);
        assertThat(testReport.getUpdUid()).isEqualTo(DEFAULT_UPD_UID);
        assertThat(testReport.getUpdTms()).isEqualTo(DEFAULT_UPD_TMS);
    }

    @Test
    @Transactional
    public void checkAmtIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(reportRepository.findAll()).hasSize(0);
        // set the field null
        report.setAmt(null);

        // Create the Report, which fails.
        restReportMockMvc.perform(post("/api/reports")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(report)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Report> reports = reportRepository.findAll();
        assertThat(reports).hasSize(0);
    }

    @Test
    @Transactional
    public void checkMarketRptIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(reportRepository.findAll()).hasSize(0);
        // set the field null
        report.setMarketRpt(null);

        // Create the Report, which fails.
        restReportMockMvc.perform(post("/api/reports")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(report)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Report> reports = reportRepository.findAll();
        assertThat(reports).hasSize(0);
    }

    @Test
    @Transactional
    public void checkDelFlagIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(reportRepository.findAll()).hasSize(0);
        // set the field null
        report.setDelFlag(null);

        // Create the Report, which fails.
        restReportMockMvc.perform(post("/api/reports")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(report)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Report> reports = reportRepository.findAll();
        assertThat(reports).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllReports() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reports
        restReportMockMvc.perform(get("/api/reports"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(report.getId().intValue())))
                .andExpect(jsonPath("$.[*].amt").value(hasItem(DEFAULT_AMT.intValue())))
                .andExpect(jsonPath("$.[*].marketRpt").value(hasItem(DEFAULT_MARKET_RPT.toString())))
                .andExpect(jsonPath("$.[*].monday").value(hasItem(DEFAULT_MONDAY.toString())))
                .andExpect(jsonPath("$.[*].tuesday").value(hasItem(DEFAULT_TUESDAY.toString())))
                .andExpect(jsonPath("$.[*].wednesday").value(hasItem(DEFAULT_WEDNESDAY.toString())))
                .andExpect(jsonPath("$.[*].thursday").value(hasItem(DEFAULT_THURSDAY.toString())))
                .andExpect(jsonPath("$.[*].friday").value(hasItem(DEFAULT_FRIDAY.toString())))
                .andExpect(jsonPath("$.[*].saturday").value(hasItem(DEFAULT_SATURDAY.toString())))
                .andExpect(jsonPath("$.[*].sunday").value(hasItem(DEFAULT_SUNDAY.toString())))
                .andExpect(jsonPath("$.[*].delFlag").value(hasItem(DEFAULT_DEL_FLAG.toString())))
                .andExpect(jsonPath("$.[*].crtUid").value(hasItem(DEFAULT_CRT_UID.toString())))
                .andExpect(jsonPath("$.[*].crtTms").value(hasItem(DEFAULT_CRT_TMS.toString())))
                .andExpect(jsonPath("$.[*].updUid").value(hasItem(DEFAULT_UPD_UID.toString())))
                .andExpect(jsonPath("$.[*].updTms").value(hasItem(DEFAULT_UPD_TMS.toString())));
    }

    @Test
    @Transactional
    public void getReport() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get the report
        restReportMockMvc.perform(get("/api/reports/{id}", report.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(report.getId().intValue()))
            .andExpect(jsonPath("$.amt").value(DEFAULT_AMT.intValue()))
            .andExpect(jsonPath("$.marketRpt").value(DEFAULT_MARKET_RPT.toString()))
            .andExpect(jsonPath("$.monday").value(DEFAULT_MONDAY.toString()))
            .andExpect(jsonPath("$.tuesday").value(DEFAULT_TUESDAY.toString()))
            .andExpect(jsonPath("$.wednesday").value(DEFAULT_WEDNESDAY.toString()))
            .andExpect(jsonPath("$.thursday").value(DEFAULT_THURSDAY.toString()))
            .andExpect(jsonPath("$.friday").value(DEFAULT_FRIDAY.toString()))
            .andExpect(jsonPath("$.saturday").value(DEFAULT_SATURDAY.toString()))
            .andExpect(jsonPath("$.sunday").value(DEFAULT_SUNDAY.toString()))
            .andExpect(jsonPath("$.delFlag").value(DEFAULT_DEL_FLAG.toString()))
            .andExpect(jsonPath("$.crtUid").value(DEFAULT_CRT_UID.toString()))
            .andExpect(jsonPath("$.crtTms").value(DEFAULT_CRT_TMS.toString()))
            .andExpect(jsonPath("$.updUid").value(DEFAULT_UPD_UID.toString()))
            .andExpect(jsonPath("$.updTms").value(DEFAULT_UPD_TMS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingReport() throws Exception {
        // Get the report
        restReportMockMvc.perform(get("/api/reports/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReport() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

		int databaseSizeBeforeUpdate = reportRepository.findAll().size();

        // Update the report
        report.setAmt(UPDATED_AMT);
        report.setMarketRpt(UPDATED_MARKET_RPT);
        report.setMonday(UPDATED_MONDAY);
        report.setTuesday(UPDATED_TUESDAY);
        report.setWednesday(UPDATED_WEDNESDAY);
        report.setThursday(UPDATED_THURSDAY);
        report.setFriday(UPDATED_FRIDAY);
        report.setSaturday(UPDATED_SATURDAY);
        report.setSunday(UPDATED_SUNDAY);
        report.setDelFlag(UPDATED_DEL_FLAG);
        report.setCrtUid(UPDATED_CRT_UID);
        report.setCrtTms(UPDATED_CRT_TMS);
        report.setUpdUid(UPDATED_UPD_UID);
        report.setUpdTms(UPDATED_UPD_TMS);
        restReportMockMvc.perform(put("/api/reports")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(report)))
                .andExpect(status().isOk());

        // Validate the Report in the database
        List<Report> reports = reportRepository.findAll();
        assertThat(reports).hasSize(databaseSizeBeforeUpdate);
        Report testReport = reports.get(reports.size() - 1);
        assertThat(testReport.getAmt()).isEqualTo(UPDATED_AMT);
        assertThat(testReport.getMarketRpt()).isEqualTo(UPDATED_MARKET_RPT);
        assertThat(testReport.getMonday()).isEqualTo(UPDATED_MONDAY);
        assertThat(testReport.getTuesday()).isEqualTo(UPDATED_TUESDAY);
        assertThat(testReport.getWednesday()).isEqualTo(UPDATED_WEDNESDAY);
        assertThat(testReport.getThursday()).isEqualTo(UPDATED_THURSDAY);
        assertThat(testReport.getFriday()).isEqualTo(UPDATED_FRIDAY);
        assertThat(testReport.getSaturday()).isEqualTo(UPDATED_SATURDAY);
        assertThat(testReport.getSunday()).isEqualTo(UPDATED_SUNDAY);
        assertThat(testReport.getDelFlag()).isEqualTo(UPDATED_DEL_FLAG);
        assertThat(testReport.getCrtUid()).isEqualTo(UPDATED_CRT_UID);
        assertThat(testReport.getCrtTms()).isEqualTo(UPDATED_CRT_TMS);
        assertThat(testReport.getUpdUid()).isEqualTo(UPDATED_UPD_UID);
        assertThat(testReport.getUpdTms()).isEqualTo(UPDATED_UPD_TMS);
    }

    @Test
    @Transactional
    public void deleteReport() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

		int databaseSizeBeforeDelete = reportRepository.findAll().size();

        // Get the report
        restReportMockMvc.perform(delete("/api/reports/{id}", report.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Report> reports = reportRepository.findAll();
        assertThat(reports).hasSize(databaseSizeBeforeDelete - 1);
    }
}
