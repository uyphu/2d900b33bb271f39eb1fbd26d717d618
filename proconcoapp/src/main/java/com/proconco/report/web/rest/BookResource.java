package com.proconco.report.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.proconco.report.domain.Book;
import com.proconco.report.repository.BookRepository;
import com.proconco.report.repository.search.BookSearchRepository;
import com.proconco.report.web.rest.util.PaginationUtil;
import com.proconco.report.web.rest.dto.BookDTO;
import com.proconco.report.web.rest.mapper.BookMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Book.
 */
@RestController
@RequestMapping("/api")
public class BookResource {

    private final Logger log = LoggerFactory.getLogger(BookResource.class);

    @Inject
    private BookRepository bookRepository;

    @Inject
    private BookMapper bookMapper;

    @Inject
    private BookSearchRepository bookSearchRepository;

    /**
     * POST  /books -> Create a new book.
     */
    @RequestMapping(value = "/books",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody BookDTO bookDTO) throws URISyntaxException {
        log.debug("REST request to save Book : {}", bookDTO);
        if (bookDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new book cannot already have an ID").build();
        }
        Book book = bookMapper.bookDTOToBook(bookDTO);
        bookRepository.save(book);
        bookSearchRepository.save(book);
        return ResponseEntity.created(new URI("/api/books/" + bookDTO.getId())).build();
    }

    /**
     * PUT  /books -> Updates an existing book.
     */
    @RequestMapping(value = "/books",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody BookDTO bookDTO) throws URISyntaxException {
        log.debug("REST request to update Book : {}", bookDTO);
        if (bookDTO.getId() == null) {
            return create(bookDTO);
        }
        Book book = bookMapper.bookDTOToBook(bookDTO);
        bookRepository.save(book);
        bookSearchRepository.save(book);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /books -> get all the books.
     */
    @RequestMapping(value = "/books",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<BookDTO>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Book> page = bookRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/books", offset, limit);
        return new ResponseEntity<>(page.getContent().stream()
            .map(book -> bookMapper.bookToBookDTO(book))
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /books/:id -> get the "id" book.
     */
    @RequestMapping(value = "/books/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BookDTO> get(@PathVariable Long id) {
        log.debug("REST request to get Book : {}", id);
        return Optional.ofNullable(bookRepository.findOne(id))
            .map(book -> bookMapper.bookToBookDTO(book))
            .map(bookDTO -> new ResponseEntity<>(
                bookDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /books/:id -> delete the "id" book.
     */
    @RequestMapping(value = "/books/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Book : {}", id);
        bookRepository.delete(id);
        bookSearchRepository.delete(id);
    }

    /**
     * SEARCH  /_search/books/:query -> search for the book corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/books/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Book> search(@PathVariable String query) {
        return StreamSupport
            .stream(bookSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
