package com.bookstore.service.mapper;

import com.bookstore.domain.Author;
import com.bookstore.domain.ImageAuthor;
import com.bookstore.service.dto.AuthorDTO;
import com.bookstore.service.dto.ImageAuthorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ImageAuthor} and its DTO {@link ImageAuthorDTO}.
 */
@Mapper(componentModel = "spring")
public interface ImageAuthorMapper extends EntityMapper<ImageAuthorDTO, ImageAuthor> {
    @Mapping(target = "author", source = "author", qualifiedByName = "authorId")
    ImageAuthorDTO toDto(ImageAuthor s);

    @Named("authorId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AuthorDTO toDtoAuthorId(Author author);
}
