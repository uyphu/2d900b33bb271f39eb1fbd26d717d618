package com.proconco.report.web.rest;

import com.proconco.report.Application;
import com.proconco.report.domain.Groupp;
import com.proconco.report.repository.GrouppRepository;
import com.proconco.report.repository.search.GrouppSearchRepository;
import com.proconco.report.web.rest.mapper.GrouppMapper;

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
 * Test class for the GrouppResource REST controller.
 *
 * @see GrouppResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class GrouppResourceTest {

    private static final String DEFAULT_GRP_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_GRP_NAME = "UPDATED_TEXT";
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
    private GrouppRepository grouppRepository;

    @Inject
    private GrouppMapper grouppMapper;

    @Inject
    private GrouppSearchRepository grouppSearchRepository;

    private MockMvc restGrouppMockMvc;

    private Groupp groupp;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        GrouppResource grouppResource = new GrouppResource();
        ReflectionTestUtils.setField(grouppResource, "grouppRepository", grouppRepository);
        ReflectionTestUtils.setField(grouppResource, "grouppMapper", grouppMapper);
        ReflectionTestUtils.setField(grouppResource, "grouppSearchRepository", grouppSearchRepository);
        this.restGrouppMockMvc = MockMvcBuilders.standaloneSetup(grouppResource).build();
    }

    @Before
    public void initTest() {
        groupp = new Groupp();
        groupp.setGrpName(DEFAULT_GRP_NAME);
        groupp.setDelFlag(DEFAULT_DEL_FLAG);
        groupp.setCrtUid(DEFAULT_CRT_UID);
        groupp.setCrtTms(DEFAULT_CRT_TMS);
        groupp.setUpdUid(DEFAULT_UPD_UID);
        groupp.setUpdTms(DEFAULT_UPD_TMS);
    }

    @Test
    @Transactional
    public void createGroupp() throws Exception {
        int databaseSizeBeforeCreate = grouppRepository.findAll().size();

        // Create the Groupp
        restGrouppMockMvc.perform(post("/api/groupps")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(groupp)))
                .andExpect(status().isCreated());

        // Validate the Groupp in the database
        List<Groupp> groupps = grouppRepository.findAll();
        assertThat(groupps).hasSize(databaseSizeBeforeCreate + 1);
        Groupp testGroupp = groupps.get(groupps.size() - 1);
        assertThat(testGroupp.getGrpName()).isEqualTo(DEFAULT_GRP_NAME);
        assertThat(testGroupp.getDelFlag()).isEqualTo(DEFAULT_DEL_FLAG);
        assertThat(testGroupp.getCrtUid()).isEqualTo(DEFAULT_CRT_UID);
        assertThat(testGroupp.getCrtTms()).isEqualTo(DEFAULT_CRT_TMS);
        assertThat(testGroupp.getUpdUid()).isEqualTo(DEFAULT_UPD_UID);
        assertThat(testGroupp.getUpdTms()).isEqualTo(DEFAULT_UPD_TMS);
    }

    @Test
    @Transactional
    public void checkGrpNameIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(grouppRepository.findAll()).hasSize(0);
        // set the field null
        groupp.setGrpName(null);

        // Create the Groupp, which fails.
        restGrouppMockMvc.perform(post("/api/groupps")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(groupp)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Groupp> groupps = grouppRepository.findAll();
        assertThat(groupps).hasSize(0);
    }

    @Test
    @Transactional
    public void checkDelFlagIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(grouppRepository.findAll()).hasSize(0);
        // set the field null
        groupp.setDelFlag(null);

        // Create the Groupp, which fails.
        restGrouppMockMvc.perform(post("/api/groupps")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(groupp)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Groupp> groupps = grouppRepository.findAll();
        assertThat(groupps).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllGroupps() throws Exception {
        // Initialize the database
        grouppRepository.saveAndFlush(groupp);

        // Get all the groupps
        restGrouppMockMvc.perform(get("/api/groupps"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(groupp.getId().intValue())))
                .andExpect(jsonPath("$.[*].grpName").value(hasItem(DEFAULT_GRP_NAME.toString())))
                .andExpect(jsonPath("$.[*].delFlag").value(hasItem(DEFAULT_DEL_FLAG.toString())))
                .andExpect(jsonPath("$.[*].crtUid").value(hasItem(DEFAULT_CRT_UID.toString())))
                .andExpect(jsonPath("$.[*].crtTms").value(hasItem(DEFAULT_CRT_TMS.toString())))
                .andExpect(jsonPath("$.[*].updUid").value(hasItem(DEFAULT_UPD_UID.toString())))
                .andExpect(jsonPath("$.[*].updTms").value(hasItem(DEFAULT_UPD_TMS.toString())));
    }

    @Test
    @Transactional
    public void getGroupp() throws Exception {
        // Initialize the database
        grouppRepository.saveAndFlush(groupp);

        // Get the groupp
        restGrouppMockMvc.perform(get("/api/groupps/{id}", groupp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(groupp.getId().intValue()))
            .andExpect(jsonPath("$.grpName").value(DEFAULT_GRP_NAME.toString()))
            .andExpect(jsonPath("$.delFlag").value(DEFAULT_DEL_FLAG.toString()))
            .andExpect(jsonPath("$.crtUid").value(DEFAULT_CRT_UID.toString()))
            .andExpect(jsonPath("$.crtTms").value(DEFAULT_CRT_TMS.toString()))
            .andExpect(jsonPath("$.updUid").value(DEFAULT_UPD_UID.toString()))
            .andExpect(jsonPath("$.updTms").value(DEFAULT_UPD_TMS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingGroupp() throws Exception {
        // Get the groupp
        restGrouppMockMvc.perform(get("/api/groupps/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGroupp() throws Exception {
        // Initialize the database
        grouppRepository.saveAndFlush(groupp);

		int databaseSizeBeforeUpdate = grouppRepository.findAll().size();

        // Update the groupp
        groupp.setGrpName(UPDATED_GRP_NAME);
        groupp.setDelFlag(UPDATED_DEL_FLAG);
        groupp.setCrtUid(UPDATED_CRT_UID);
        groupp.setCrtTms(UPDATED_CRT_TMS);
        groupp.setUpdUid(UPDATED_UPD_UID);
        groupp.setUpdTms(UPDATED_UPD_TMS);
        restGrouppMockMvc.perform(put("/api/groupps")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(groupp)))
                .andExpect(status().isOk());

        // Validate the Groupp in the database
        List<Groupp> groupps = grouppRepository.findAll();
        assertThat(groupps).hasSize(databaseSizeBeforeUpdate);
        Groupp testGroupp = groupps.get(groupps.size() - 1);
        assertThat(testGroupp.getGrpName()).isEqualTo(UPDATED_GRP_NAME);
        assertThat(testGroupp.getDelFlag()).isEqualTo(UPDATED_DEL_FLAG);
        assertThat(testGroupp.getCrtUid()).isEqualTo(UPDATED_CRT_UID);
        assertThat(testGroupp.getCrtTms()).isEqualTo(UPDATED_CRT_TMS);
        assertThat(testGroupp.getUpdUid()).isEqualTo(UPDATED_UPD_UID);
        assertThat(testGroupp.getUpdTms()).isEqualTo(UPDATED_UPD_TMS);
    }

    @Test
    @Transactional
    public void deleteGroupp() throws Exception {
        // Initialize the database
        grouppRepository.saveAndFlush(groupp);

		int databaseSizeBeforeDelete = grouppRepository.findAll().size();

        // Get the groupp
        restGrouppMockMvc.perform(delete("/api/groupps/{id}", groupp.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Groupp> groupps = grouppRepository.findAll();
        assertThat(groupps).hasSize(databaseSizeBeforeDelete - 1);
    }
}
