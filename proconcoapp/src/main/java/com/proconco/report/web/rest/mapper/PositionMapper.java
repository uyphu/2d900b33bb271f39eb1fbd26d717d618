package com.proconco.report.web.rest.mapper;

import com.proconco.report.domain.*;
import com.proconco.report.web.rest.dto.PositionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Position and its DTO PositionDTO.
 */
@Mapper(uses = {})
public interface PositionMapper {

    PositionDTO positionToPositionDTO(Position position);

    Position positionDTOToPosition(PositionDTO positionDTO);
}
