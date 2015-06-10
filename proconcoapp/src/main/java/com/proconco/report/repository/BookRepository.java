package com.proconco.report.repository;

import com.proconco.report.domain.Book;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Book entity.
 */
public interface BookRepository extends JpaRepository<Book,Long> {

}
