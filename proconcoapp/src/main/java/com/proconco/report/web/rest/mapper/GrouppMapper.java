package com.proconco.report.web.rest.mapper;

import com.proconco.report.domain.*;
import com.proconco.report.web.rest.dto.GrouppDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Groupp and its DTO GrouppDTO.
 */
@Mapper(uses = {})
public interface GrouppMapper {

    GrouppDTO grouppToGrouppDTO(Groupp groupp);

    Groupp grouppDTOToGroupp(GrouppDTO grouppDTO);
}
