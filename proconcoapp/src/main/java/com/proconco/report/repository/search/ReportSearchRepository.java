package com.proconco.report.repository.search;

import com.proconco.report.domain.Report;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Report entity.
 */
public interface ReportSearchRepository extends ElasticsearchRepository<Report, Long> {
}
