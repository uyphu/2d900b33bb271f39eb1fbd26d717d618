package com.proconco.report.web.rest.mapper;

import com.proconco.report.domain.*;
import com.proconco.report.web.rest.dto.ReportDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Report and its DTO ReportDTO.
 */
@Mapper(uses = {})
public interface ReportMapper {

    ReportDTO reportToReportDTO(Report report);

    Report reportDTOToReport(ReportDTO reportDTO);
}
