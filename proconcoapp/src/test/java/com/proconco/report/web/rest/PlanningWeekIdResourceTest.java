package com.proconco.report.web.rest;

import com.proconco.report.Application;
import com.proconco.report.domain.PlanningWeekId;
import com.proconco.report.repository.PlanningWeekIdRepository;
import com.proconco.report.repository.search.PlanningWeekIdSearchRepository;
import com.proconco.report.web.rest.mapper.PlanningWeekIdMapper;

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
 * Test class for the PlanningWeekIdResource REST controller.
 *
 * @see PlanningWeekIdResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PlanningWeekIdResourceTest {


    private static final Integer DEFAULT_WEEK = 1;
    private static final Integer UPDATED_WEEK = 2;

    private static final Integer DEFAULT_YEAR = 1970;
    private static final Integer UPDATED_YEAR = 1971;

    @Inject
    private PlanningWeekIdRepository planningWeekIdRepository;

    @Inject
    private PlanningWeekIdMapper planningWeekIdMapper;

    @Inject
    private PlanningWeekIdSearchRepository planningWeekIdSearchRepository;

    private MockMvc restPlanningWeekIdMockMvc;

    private PlanningWeekId planningWeekId;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PlanningWeekIdResource planningWeekIdResource = new PlanningWeekIdResource();
        ReflectionTestUtils.setField(planningWeekIdResource, "planningWeekIdRepository", planningWeekIdRepository);
        ReflectionTestUtils.setField(planningWeekIdResource, "planningWeekIdMapper", planningWeekIdMapper);
        ReflectionTestUtils.setField(planningWeekIdResource, "planningWeekIdSearchRepository", planningWeekIdSearchRepository);
        this.restPlanningWeekIdMockMvc = MockMvcBuilders.standaloneSetup(planningWeekIdResource).build();
    }

    @Before
    public void initTest() {
        planningWeekId = new PlanningWeekId();
        planningWeekId.setWeek(DEFAULT_WEEK);
        planningWeekId.setYear(DEFAULT_YEAR);
    }

    @Test
    @Transactional
    public void createPlanningWeekId() throws Exception {
        int databaseSizeBeforeCreate = planningWeekIdRepository.findAll().size();

        // Create the PlanningWeekId
        restPlanningWeekIdMockMvc.perform(post("/api/planningWeekIds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(planningWeekId)))
                .andExpect(status().isCreated());

        // Validate the PlanningWeekId in the database
        List<PlanningWeekId> planningWeekIds = planningWeekIdRepository.findAll();
        assertThat(planningWeekIds).hasSize(databaseSizeBeforeCreate + 1);
        PlanningWeekId testPlanningWeekId = planningWeekIds.get(planningWeekIds.size() - 1);
        assertThat(testPlanningWeekId.getWeek()).isEqualTo(DEFAULT_WEEK);
        assertThat(testPlanningWeekId.getYear()).isEqualTo(DEFAULT_YEAR);
    }

    @Test
    @Transactional
    public void checkWeekIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(planningWeekIdRepository.findAll()).hasSize(0);
        // set the field null
        planningWeekId.setWeek(null);

        // Create the PlanningWeekId, which fails.
        restPlanningWeekIdMockMvc.perform(post("/api/planningWeekIds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(planningWeekId)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<PlanningWeekId> planningWeekIds = planningWeekIdRepository.findAll();
        assertThat(planningWeekIds).hasSize(0);
    }

    @Test
    @Transactional
    public void checkYearIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(planningWeekIdRepository.findAll()).hasSize(0);
        // set the field null
        planningWeekId.setYear(null);

        // Create the PlanningWeekId, which fails.
        restPlanningWeekIdMockMvc.perform(post("/api/planningWeekIds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(planningWeekId)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<PlanningWeekId> planningWeekIds = planningWeekIdRepository.findAll();
        assertThat(planningWeekIds).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllPlanningWeekIds() throws Exception {
        // Initialize the database
        planningWeekIdRepository.saveAndFlush(planningWeekId);

        // Get all the planningWeekIds
        restPlanningWeekIdMockMvc.perform(get("/api/planningWeekIds"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(planningWeekId.getId().intValue())))
                .andExpect(jsonPath("$.[*].week").value(hasItem(DEFAULT_WEEK)))
                .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)));
    }

    @Test
    @Transactional
    public void getPlanningWeekId() throws Exception {
        // Initialize the database
        planningWeekIdRepository.saveAndFlush(planningWeekId);

        // Get the planningWeekId
        restPlanningWeekIdMockMvc.perform(get("/api/planningWeekIds/{id}", planningWeekId.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(planningWeekId.getId().intValue()))
            .andExpect(jsonPath("$.week").value(DEFAULT_WEEK))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR));
    }

    @Test
    @Transactional
    public void getNonExistingPlanningWeekId() throws Exception {
        // Get the planningWeekId
        restPlanningWeekIdMockMvc.perform(get("/api/planningWeekIds/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlanningWeekId() throws Exception {
        // Initialize the database
        planningWeekIdRepository.saveAndFlush(planningWeekId);

		int databaseSizeBeforeUpdate = planningWeekIdRepository.findAll().size();

        // Update the planningWeekId
        planningWeekId.setWeek(UPDATED_WEEK);
        planningWeekId.setYear(UPDATED_YEAR);
        restPlanningWeekIdMockMvc.perform(put("/api/planningWeekIds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(planningWeekId)))
                .andExpect(status().isOk());

        // Validate the PlanningWeekId in the database
        List<PlanningWeekId> planningWeekIds = planningWeekIdRepository.findAll();
        assertThat(planningWeekIds).hasSize(databaseSizeBeforeUpdate);
        PlanningWeekId testPlanningWeekId = planningWeekIds.get(planningWeekIds.size() - 1);
        assertThat(testPlanningWeekId.getWeek()).isEqualTo(UPDATED_WEEK);
        assertThat(testPlanningWeekId.getYear()).isEqualTo(UPDATED_YEAR);
    }

    @Test
    @Transactional
    public void deletePlanningWeekId() throws Exception {
        // Initialize the database
        planningWeekIdRepository.saveAndFlush(planningWeekId);

		int databaseSizeBeforeDelete = planningWeekIdRepository.findAll().size();

        // Get the planningWeekId
        restPlanningWeekIdMockMvc.perform(delete("/api/planningWeekIds/{id}", planningWeekId.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<PlanningWeekId> planningWeekIds = planningWeekIdRepository.findAll();
        assertThat(planningWeekIds).hasSize(databaseSizeBeforeDelete - 1);
    }
}
