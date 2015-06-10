package com.proconco.report.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.proconco.report.domain.Author;
import com.proconco.report.repository.AuthorRepository;
import com.proconco.report.repository.search.AuthorSearchRepository;
import com.proconco.report.web.rest.util.PaginationUtil;
import com.proconco.report.web.rest.dto.AuthorDTO;
import com.proconco.report.web.rest.mapper.AuthorMapper;
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
 * REST controller for managing Author.
 */
@RestController
@RequestMapping("/api")
public class AuthorResource {

    private final Logger log = LoggerFactory.getLogger(AuthorResource.class);

    @Inject
    private AuthorRepository authorRepository;

    @Inject
    private AuthorMapper authorMapper;

    @Inject
    private AuthorSearchRepository authorSearchRepository;

    /**
     * POST  /authors -> Create a new author.
     */
    @RequestMapping(value = "/authors",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody AuthorDTO authorDTO) throws URISyntaxException {
        log.debug("REST request to save Author : {}", authorDTO);
        if (authorDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new author cannot already have an ID").build();
        }
        Author author = authorMapper.authorDTOToAuthor(authorDTO);
        authorRepository.save(author);
        authorSearchRepository.save(author);
        return ResponseEntity.created(new URI("/api/authors/" + authorDTO.getId())).build();
    }

    /**
     * PUT  /authors -> Updates an existing author.
     */
    @RequestMapping(value = "/authors",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody AuthorDTO authorDTO) throws URISyntaxException {
        log.debug("REST request to update Author : {}", authorDTO);
        if (authorDTO.getId() == null) {
            return create(authorDTO);
        }
        Author author = authorMapper.authorDTOToAuthor(authorDTO);
        authorRepository.save(author);
        authorSearchRepository.save(author);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /authors -> get all the authors.
     */
    @RequestMapping(value = "/authors",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<AuthorDTO>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Author> page = authorRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/authors", offset, limit);
        return new ResponseEntity<>(page.getContent().stream()
            .map(author -> authorMapper.authorToAuthorDTO(author))
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /authors/:id -> get the "id" author.
     */
    @RequestMapping(value = "/authors/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AuthorDTO> get(@PathVariable Long id) {
        log.debug("REST request to get Author : {}", id);
        return Optional.ofNullable(authorRepository.findOne(id))
            .map(author -> authorMapper.authorToAuthorDTO(author))
            .map(authorDTO -> new ResponseEntity<>(
                authorDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /authors/:id -> delete the "id" author.
     */
    @RequestMapping(value = "/authors/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Author : {}", id);
        authorRepository.delete(id);
        authorSearchRepository.delete(id);
    }

    /**
     * SEARCH  /_search/authors/:query -> search for the author corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/authors/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Author> search(@PathVariable String query) {
        return StreamSupport
            .stream(authorSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
