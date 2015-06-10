package com.proconco.report.web.rest;

import com.proconco.report.Application;
import com.proconco.report.domain.PlanningWeek;
import com.proconco.report.repository.PlanningWeekRepository;
import com.proconco.report.repository.search.PlanningWeekSearchRepository;
import com.proconco.report.web.rest.mapper.PlanningWeekMapper;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PlanningWeekResource REST controller.
 *
 * @see PlanningWeekResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PlanningWeekResourceTest {

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
    private static final String DEFAULT_UPD_TMS = "SAMPLE_TEXT";
    private static final String UPDATED_UPD_TMS = "UPDATED_TEXT";

    @Inject
    private PlanningWeekRepository planningWeekRepository;

    @Inject
    private PlanningWeekMapper planningWeekMapper;

    @Inject
    private PlanningWeekSearchRepository planningWeekSearchRepository;

    private MockMvc restPlanningWeekMockMvc;

    private PlanningWeek planningWeek;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PlanningWeekResource planningWeekResource = new PlanningWeekResource();
        ReflectionTestUtils.setField(planningWeekResource, "planningWeekRepository", planningWeekRepository);
        ReflectionTestUtils.setField(planningWeekResource, "planningWeekMapper", planningWeekMapper);
        ReflectionTestUtils.setField(planningWeekResource, "planningWeekSearchRepository", planningWeekSearchRepository);
        this.restPlanningWeekMockMvc = MockMvcBuilders.standaloneSetup(planningWeekResource).build();
    }

    @Before
    public void initTest() {
        planningWeek = new PlanningWeek();
        planningWeek.setMonday(DEFAULT_MONDAY);
        planningWeek.setTuesday(DEFAULT_TUESDAY);
        planningWeek.setWednesday(DEFAULT_WEDNESDAY);
        planningWeek.setThursday(DEFAULT_THURSDAY);
        planningWeek.setFriday(DEFAULT_FRIDAY);
        planningWeek.setSaturday(DEFAULT_SATURDAY);
        planningWeek.setSunday(DEFAULT_SUNDAY);
        planningWeek.setDelFlag(DEFAULT_DEL_FLAG);
        planningWeek.setCrtUid(DEFAULT_CRT_UID);
        planningWeek.setCrtTms(DEFAULT_CRT_TMS);
        planningWeek.setUpdUid(DEFAULT_UPD_UID);
        planningWeek.setUpdTms(DEFAULT_UPD_TMS);
    }

    @Test
    @Transactional
    public void createPlanningWeek() throws Exception {
        int databaseSizeBeforeCreate = planningWeekRepository.findAll().size();

        // Create the PlanningWeek
        restPlanningWeekMockMvc.perform(post("/api/planningWeeks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(planningWeek)))
                .andExpect(status().isCreated());

        // Validate the PlanningWeek in the database
        List<PlanningWeek> planningWeeks = planningWeekRepository.findAll();
        assertThat(planningWeeks).hasSize(databaseSizeBeforeCreate + 1);
        PlanningWeek testPlanningWeek = planningWeeks.get(planningWeeks.size() - 1);
        assertThat(testPlanningWeek.getMonday()).isEqualTo(DEFAULT_MONDAY);
        assertThat(testPlanningWeek.getTuesday()).isEqualTo(DEFAULT_TUESDAY);
        assertThat(testPlanningWeek.getWednesday()).isEqualTo(DEFAULT_WEDNESDAY);
        assertThat(testPlanningWeek.getThursday()).isEqualTo(DEFAULT_THURSDAY);
        assertThat(testPlanningWeek.getFriday()).isEqualTo(DEFAULT_FRIDAY);
        assertThat(testPlanningWeek.getSaturday()).isEqualTo(DEFAULT_SATURDAY);
        assertThat(testPlanningWeek.getSunday()).isEqualTo(DEFAULT_SUNDAY);
        assertThat(testPlanningWeek.getDelFlag()).isEqualTo(DEFAULT_DEL_FLAG);
        assertThat(testPlanningWeek.getCrtUid()).isEqualTo(DEFAULT_CRT_UID);
        assertThat(testPlanningWeek.getCrtTms()).isEqualTo(DEFAULT_CRT_TMS);
        assertThat(testPlanningWeek.getUpdUid()).isEqualTo(DEFAULT_UPD_UID);
        assertThat(testPlanningWeek.getUpdTms()).isEqualTo(DEFAULT_UPD_TMS);
    }

    @Test
    @Transactional
    public void checkDelFlagIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(planningWeekRepository.findAll()).hasSize(0);
        // set the field null
        planningWeek.setDelFlag(null);

        // Create the PlanningWeek, which fails.
        restPlanningWeekMockMvc.perform(post("/api/planningWeeks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(planningWeek)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<PlanningWeek> planningWeeks = planningWeekRepository.findAll();
        assertThat(planningWeeks).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllPlanningWeeks() throws Exception {
        // Initialize the database
        planningWeekRepository.saveAndFlush(planningWeek);

        // Get all the planningWeeks
        restPlanningWeekMockMvc.perform(get("/api/planningWeeks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(planningWeek.getId().intValue())))
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
    public void getPlanningWeek() throws Exception {
        // Initialize the database
        planningWeekRepository.saveAndFlush(planningWeek);

        // Get the planningWeek
        restPlanningWeekMockMvc.perform(get("/api/planningWeeks/{id}", planningWeek.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(planningWeek.getId().intValue()))
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
    public void getNonExistingPlanningWeek() throws Exception {
        // Get the planningWeek
        restPlanningWeekMockMvc.perform(get("/api/planningWeeks/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlanningWeek() throws Exception {
        // Initialize the database
        planningWeekRepository.saveAndFlush(planningWeek);

		int databaseSizeBeforeUpdate = planningWeekRepository.findAll().size();

        // Update the planningWeek
        planningWeek.setMonday(UPDATED_MONDAY);
        planningWeek.setTuesday(UPDATED_TUESDAY);
        planningWeek.setWednesday(UPDATED_WEDNESDAY);
        planningWeek.setThursday(UPDATED_THURSDAY);
        planningWeek.setFriday(UPDATED_FRIDAY);
        planningWeek.setSaturday(UPDATED_SATURDAY);
        planningWeek.setSunday(UPDATED_SUNDAY);
        planningWeek.setDelFlag(UPDATED_DEL_FLAG);
        planningWeek.setCrtUid(UPDATED_CRT_UID);
        planningWeek.setCrtTms(UPDATED_CRT_TMS);
        planningWeek.setUpdUid(UPDATED_UPD_UID);
        planningWeek.setUpdTms(UPDATED_UPD_TMS);
        restPlanningWeekMockMvc.perform(put("/api/planningWeeks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(planningWeek)))
                .andExpect(status().isOk());

        // Validate the PlanningWeek in the database
        List<PlanningWeek> planningWeeks = planningWeekRepository.findAll();
        assertThat(planningWeeks).hasSize(databaseSizeBeforeUpdate);
        PlanningWeek testPlanningWeek = planningWeeks.get(planningWeeks.size() - 1);
        assertThat(testPlanningWeek.getMonday()).isEqualTo(UPDATED_MONDAY);
        assertThat(testPlanningWeek.getTuesday()).isEqualTo(UPDATED_TUESDAY);
        assertThat(testPlanningWeek.getWednesday()).isEqualTo(UPDATED_WEDNESDAY);
        assertThat(testPlanningWeek.getThursday()).isEqualTo(UPDATED_THURSDAY);
        assertThat(testPlanningWeek.getFriday()).isEqualTo(UPDATED_FRIDAY);
        assertThat(testPlanningWeek.getSaturday()).isEqualTo(UPDATED_SATURDAY);
        assertThat(testPlanningWeek.getSunday()).isEqualTo(UPDATED_SUNDAY);
        assertThat(testPlanningWeek.getDelFlag()).isEqualTo(UPDATED_DEL_FLAG);
        assertThat(testPlanningWeek.getCrtUid()).isEqualTo(UPDATED_CRT_UID);
        assertThat(testPlanningWeek.getCrtTms()).isEqualTo(UPDATED_CRT_TMS);
        assertThat(testPlanningWeek.getUpdUid()).isEqualTo(UPDATED_UPD_UID);
        assertThat(testPlanningWeek.getUpdTms()).isEqualTo(UPDATED_UPD_TMS);
    }

    @Test
    @Transactional
    public void deletePlanningWeek() throws Exception {
        // Initialize the database
        planningWeekRepository.saveAndFlush(planningWeek);

		int databaseSizeBeforeDelete = planningWeekRepository.findAll().size();

        // Get the planningWeek
        restPlanningWeekMockMvc.perform(delete("/api/planningWeeks/{id}", planningWeek.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<PlanningWeek> planningWeeks = planningWeekRepository.findAll();
        assertThat(planningWeeks).hasSize(databaseSizeBeforeDelete - 1);
    }
}
