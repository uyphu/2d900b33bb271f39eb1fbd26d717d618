package com.proconco.report.repository.search;

import com.proconco.report.domain.ReportId;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ReportId entity.
 */
public interface ReportIdSearchRepository extends ElasticsearchRepository<ReportId, Long> {
}
