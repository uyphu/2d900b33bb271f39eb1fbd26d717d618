package com.proconco.report.repository.search;

import com.proconco.report.domain.Position;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Position entity.
 */
public interface PositionSearchRepository extends ElasticsearchRepository<Position, Long> {
}
