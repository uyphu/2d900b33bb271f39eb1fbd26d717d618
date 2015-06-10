package com.proconco.report.web.rest.mapper;

import com.proconco.report.domain.*;
import com.proconco.report.web.rest.dto.ReportIdDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ReportId and its DTO ReportIdDTO.
 */
@Mapper(uses = {})
public interface ReportIdMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.id", target = "userId")
    ReportIdDTO reportIdToReportIdDTO(ReportId reportId);

    @Mapping(source = "userId", target = "user")
    ReportId reportIdDTOToReportId(ReportIdDTO reportIdDTO);

    default User userFromId(Long id) {
        if (id == null) {
            return null;
        }
        User user = new User();
        user.setId(id);
        return user;
    }
}
