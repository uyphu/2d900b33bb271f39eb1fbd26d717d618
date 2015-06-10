package com.proconco.report.web.rest.mapper;

import com.proconco.report.domain.*;
import com.proconco.report.web.rest.dto.PlanningWeekIdDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity PlanningWeekId and its DTO PlanningWeekIdDTO.
 */
@Mapper(uses = {})
public interface PlanningWeekIdMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.id", target = "userId")
    PlanningWeekIdDTO planningWeekIdToPlanningWeekIdDTO(PlanningWeekId planningWeekId);

    @Mapping(source = "userId", target = "user")
    PlanningWeekId planningWeekIdDTOToPlanningWeekId(PlanningWeekIdDTO planningWeekIdDTO);

    default User userFromId(Long id) {
        if (id == null) {
            return null;
        }
        User user = new User();
        user.setId(id);
        return user;
    }
}
