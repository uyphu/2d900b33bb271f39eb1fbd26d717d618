package com.proconco.report.repository;

import com.proconco.report.domain.ReportId;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ReportId entity.
 */
public interface ReportIdRepository extends JpaRepository<ReportId,Long> {

    @Query("select reportId from ReportId reportId where reportId.user.login = ?#{principal.username}")
    List<ReportId> findAllForCurrentUser();

}
