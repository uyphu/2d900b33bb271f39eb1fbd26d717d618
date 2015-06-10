package com.proconco.report.repository;

import com.proconco.report.domain.Groupp;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Groupp entity.
 */
public interface GrouppRepository extends JpaRepository<Groupp,Long> {

}
