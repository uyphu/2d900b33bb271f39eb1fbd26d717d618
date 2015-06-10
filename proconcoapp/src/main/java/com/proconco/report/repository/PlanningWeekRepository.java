package com.proconco.report.repository;

import com.proconco.report.domain.PlanningWeek;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the PlanningWeek entity.
 */
public interface PlanningWeekRepository extends JpaRepository<PlanningWeek,Long> {

}
