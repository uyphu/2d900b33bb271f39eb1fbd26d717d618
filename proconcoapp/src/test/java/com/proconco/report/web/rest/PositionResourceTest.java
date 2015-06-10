package com.proconco.report.web.rest;

import com.proconco.report.Application;
import com.proconco.report.domain.Position;
import com.proconco.report.repository.PositionRepository;
import com.proconco.report.repository.search.PositionSearchRepository;
import com.proconco.report.web.rest.mapper.PositionMapper;

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
 * Test class for the PositionResource REST controller.
 *
 * @see PositionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PositionResourceTest {

    private static final String DEFAULT_POST_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_POST_NAME = "UPDATED_TEXT";
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
    private PositionRepository positionRepository;

    @Inject
    private PositionMapper positionMapper;

    @Inject
    private PositionSearchRepository positionSearchRepository;

    private MockMvc restPositionMockMvc;

    private Position position;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PositionResource positionResource = new PositionResource();
        ReflectionTestUtils.setField(positionResource, "positionRepository", positionRepository);
        ReflectionTestUtils.setField(positionResource, "positionMapper", positionMapper);
        ReflectionTestUtils.setField(positionResource, "positionSearchRepository", positionSearchRepository);
        this.restPositionMockMvc = MockMvcBuilders.standaloneSetup(positionResource).build();
    }

    @Before
    public void initTest() {
        position = new Position();
        position.setPostName(DEFAULT_POST_NAME);
        position.setDelFlag(DEFAULT_DEL_FLAG);
        position.setCrtUid(DEFAULT_CRT_UID);
        position.setCrtTms(DEFAULT_CRT_TMS);
        position.setUpdUid(DEFAULT_UPD_UID);
        position.setUpdTms(DEFAULT_UPD_TMS);
    }

    @Test
    @Transactional
    public void createPosition() throws Exception {
        int databaseSizeBeforeCreate = positionRepository.findAll().size();

        // Create the Position
        restPositionMockMvc.perform(post("/api/positions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(position)))
                .andExpect(status().isCreated());

        // Validate the Position in the database
        List<Position> positions = positionRepository.findAll();
        assertThat(positions).hasSize(databaseSizeBeforeCreate + 1);
        Position testPosition = positions.get(positions.size() - 1);
        assertThat(testPosition.getPostName()).isEqualTo(DEFAULT_POST_NAME);
        assertThat(testPosition.getDelFlag()).isEqualTo(DEFAULT_DEL_FLAG);
        assertThat(testPosition.getCrtUid()).isEqualTo(DEFAULT_CRT_UID);
        assertThat(testPosition.getCrtTms()).isEqualTo(DEFAULT_CRT_TMS);
        assertThat(testPosition.getUpdUid()).isEqualTo(DEFAULT_UPD_UID);
        assertThat(testPosition.getUpdTms()).isEqualTo(DEFAULT_UPD_TMS);
    }

    @Test
    @Transactional
    public void checkPostNameIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(positionRepository.findAll()).hasSize(0);
        // set the field null
        position.setPostName(null);

        // Create the Position, which fails.
        restPositionMockMvc.perform(post("/api/positions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(position)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Position> positions = positionRepository.findAll();
        assertThat(positions).hasSize(0);
    }

    @Test
    @Transactional
    public void checkDelFlagIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(positionRepository.findAll()).hasSize(0);
        // set the field null
        position.setDelFlag(null);

        // Create the Position, which fails.
        restPositionMockMvc.perform(post("/api/positions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(position)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Position> positions = positionRepository.findAll();
        assertThat(positions).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllPositions() throws Exception {
        // Initialize the database
        positionRepository.saveAndFlush(position);

        // Get all the positions
        restPositionMockMvc.perform(get("/api/positions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(position.getId().intValue())))
                .andExpect(jsonPath("$.[*].postName").value(hasItem(DEFAULT_POST_NAME.toString())))
                .andExpect(jsonPath("$.[*].delFlag").value(hasItem(DEFAULT_DEL_FLAG.toString())))
                .andExpect(jsonPath("$.[*].crtUid").value(hasItem(DEFAULT_CRT_UID.toString())))
                .andExpect(jsonPath("$.[*].crtTms").value(hasItem(DEFAULT_CRT_TMS.toString())))
                .andExpect(jsonPath("$.[*].updUid").value(hasItem(DEFAULT_UPD_UID.toString())))
                .andExpect(jsonPath("$.[*].updTms").value(hasItem(DEFAULT_UPD_TMS.toString())));
    }

    @Test
    @Transactional
    public void getPosition() throws Exception {
        // Initialize the database
        positionRepository.saveAndFlush(position);

        // Get the position
        restPositionMockMvc.perform(get("/api/positions/{id}", position.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(position.getId().intValue()))
            .andExpect(jsonPath("$.postName").value(DEFAULT_POST_NAME.toString()))
            .andExpect(jsonPath("$.delFlag").value(DEFAULT_DEL_FLAG.toString()))
            .andExpect(jsonPath("$.crtUid").value(DEFAULT_CRT_UID.toString()))
            .andExpect(jsonPath("$.crtTms").value(DEFAULT_CRT_TMS.toString()))
            .andExpect(jsonPath("$.updUid").value(DEFAULT_UPD_UID.toString()))
            .andExpect(jsonPath("$.updTms").value(DEFAULT_UPD_TMS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPosition() throws Exception {
        // Get the position
        restPositionMockMvc.perform(get("/api/positions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePosition() throws Exception {
        // Initialize the database
        positionRepository.saveAndFlush(position);

		int databaseSizeBeforeUpdate = positionRepository.findAll().size();

        // Update the position
        position.setPostName(UPDATED_POST_NAME);
        position.setDelFlag(UPDATED_DEL_FLAG);
        position.setCrtUid(UPDATED_CRT_UID);
        position.setCrtTms(UPDATED_CRT_TMS);
        position.setUpdUid(UPDATED_UPD_UID);
        position.setUpdTms(UPDATED_UPD_TMS);
        restPositionMockMvc.perform(put("/api/positions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(position)))
                .andExpect(status().isOk());

        // Validate the Position in the database
        List<Position> positions = positionRepository.findAll();
        assertThat(positions).hasSize(databaseSizeBeforeUpdate);
        Position testPosition = positions.get(positions.size() - 1);
        assertThat(testPosition.getPostName()).isEqualTo(UPDATED_POST_NAME);
        assertThat(testPosition.getDelFlag()).isEqualTo(UPDATED_DEL_FLAG);
        assertThat(testPosition.getCrtUid()).isEqualTo(UPDATED_CRT_UID);
        assertThat(testPosition.getCrtTms()).isEqualTo(UPDATED_CRT_TMS);
        assertThat(testPosition.getUpdUid()).isEqualTo(UPDATED_UPD_UID);
        assertThat(testPosition.getUpdTms()).isEqualTo(UPDATED_UPD_TMS);
    }

    @Test
    @Transactional
    public void deletePosition() throws Exception {
        // Initialize the database
        positionRepository.saveAndFlush(position);

		int databaseSizeBeforeDelete = positionRepository.findAll().size();

        // Get the position
        restPositionMockMvc.perform(delete("/api/positions/{id}", position.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Position> positions = positionRepository.findAll();
        assertThat(positions).hasSize(databaseSizeBeforeDelete - 1);
    }
}
