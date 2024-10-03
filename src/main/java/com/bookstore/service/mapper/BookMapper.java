package com.bookstore.service.mapper;

import com.bookstore.domain.Author;
import com.bookstore.domain.Book;
import com.bookstore.service.dto.AuthorDTO;
import com.bookstore.service.dto.BookDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Book} and its DTO {@link BookDTO}.
 */
@Mapper(componentModel = "spring")
public interface BookMapper extends EntityMapper<BookDTO, Book> {
    @Mapping(target = "author", source = "author", qualifiedByName = "authorId")
    BookDTO toDto(Book s);

    @Named("authorId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    AuthorDTO toDtoAuthorId(Author author);
}
