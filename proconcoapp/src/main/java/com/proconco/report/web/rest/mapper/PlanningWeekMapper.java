package com.proconco.report.web.rest.mapper;

import com.proconco.report.domain.*;
import com.proconco.report.web.rest.dto.PlanningWeekDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity PlanningWeek and its DTO PlanningWeekDTO.
 */
@Mapper(uses = {})
public interface PlanningWeekMapper {

    PlanningWeekDTO planningWeekToPlanningWeekDTO(PlanningWeek planningWeek);

    PlanningWeek planningWeekDTOToPlanningWeek(PlanningWeekDTO planningWeekDTO);
}
