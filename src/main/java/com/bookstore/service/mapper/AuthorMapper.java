package com.bookstore.service.mapper;

import com.bookstore.domain.Author;
import com.bookstore.service.dto.AuthorDTO;
import java.util.List;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link Author} and its DTO {@link AuthorDTO}.
 */
@Mapper(componentModel = "spring")
public interface AuthorMapper extends EntityMapper<AuthorDTO, Author> {
    @Override
    AuthorDTO toDto(Author entity);

    @Named(value = "useMe")
    default AuthorDTO authorToAuthorDTO(Author entity) {
        if (entity == null) return null;

        AuthorDTO authorDTO = new AuthorDTO();

        authorDTO.setId(entity.getId());
        authorDTO.setName(entity.getName());
        authorDTO.setBirthDate(entity.getBirthDate());

        if (entity.getAuthors() != null && !entity.getAuthors().isEmpty()) {
            var firstImageAuthor = entity.getAuthors().iterator().next();
            authorDTO.setImageAuthor(firstImageAuthor.getImageAuthor());
        }

        return authorDTO;
    }

    @Override
    @IterableMapping(qualifiedByName = "useMe")
    List<AuthorDTO> toDto(List<Author> entityList);
}
