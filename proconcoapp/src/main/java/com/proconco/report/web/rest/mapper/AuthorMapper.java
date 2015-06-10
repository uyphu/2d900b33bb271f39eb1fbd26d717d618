package com.proconco.report.web.rest.mapper;

import com.proconco.report.domain.*;
import com.proconco.report.web.rest.dto.AuthorDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Author and its DTO AuthorDTO.
 */
@Mapper(uses = {})
public interface AuthorMapper {

    AuthorDTO authorToAuthorDTO(Author author);

    @Mapping(target = "books", ignore = true)
    Author authorDTOToAuthor(AuthorDTO authorDTO);
}
