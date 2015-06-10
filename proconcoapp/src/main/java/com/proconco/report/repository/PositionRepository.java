package com.proconco.report.repository;

import com.proconco.report.domain.Position;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Position entity.
 */
public interface PositionRepository extends JpaRepository<Position,Long> {

}
