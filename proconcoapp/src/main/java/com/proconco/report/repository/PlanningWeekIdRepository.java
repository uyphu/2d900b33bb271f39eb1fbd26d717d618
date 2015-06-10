package com.proconco.report.repository;

import com.proconco.report.domain.PlanningWeekId;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the PlanningWeekId entity.
 */
public interface PlanningWeekIdRepository extends JpaRepository<PlanningWeekId,Long> {

    @Query("select planningWeekId from PlanningWeekId planningWeekId where planningWeekId.user.login = ?#{principal.username}")
    List<PlanningWeekId> findAllForCurrentUser();

}
