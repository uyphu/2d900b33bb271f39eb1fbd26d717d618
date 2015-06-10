package com.proconco.report.repository.search;

import com.proconco.report.domain.Groupp;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Groupp entity.
 */
public interface GrouppSearchRepository extends ElasticsearchRepository<Groupp, Long> {
}
