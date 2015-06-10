package com.proconco.report.repository.search;

import com.proconco.report.domain.PlanningWeekId;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the PlanningWeekId entity.
 */
public interface PlanningWeekIdSearchRepository extends ElasticsearchRepository<PlanningWeekId, Long> {
}
