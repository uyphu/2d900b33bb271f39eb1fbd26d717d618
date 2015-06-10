package com.proconco.report.repository.search;

import com.proconco.report.domain.PlanningWeek;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the PlanningWeek entity.
 */
public interface PlanningWeekSearchRepository extends ElasticsearchRepository<PlanningWeek, Long> {
}
