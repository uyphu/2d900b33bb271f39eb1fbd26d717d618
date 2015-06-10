package com.proconco.report.web.rest.mapper;

import com.proconco.report.domain.*;
import com.proconco.report.web.rest.dto.BookDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Book and its DTO BookDTO.
 */
@Mapper(uses = {})
public interface BookMapper {

    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "author.id", target = "authorId")
    BookDTO bookToBookDTO(Book book);

    @Mapping(source = "authorId", target = "author")
    Book bookDTOToBook(BookDTO bookDTO);

    default Author authorFromId(Long id) {
        if (id == null) {
            return null;
        }
        Author author = new Author();
        author.setId(id);
        return author;
    }
}
