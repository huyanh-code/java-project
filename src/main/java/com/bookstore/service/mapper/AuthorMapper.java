package com.bookstore.service.mapper;

import com.bookstore.domain.Author;
import com.bookstore.service.dto.AuthorDTO;
import java.util.List;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Author} and its DTO {@link AuthorDTO}.
 */
@Mapper(componentModel = "spring")
public interface AuthorMapper extends EntityMapper<AuthorDTO, Author> {
    @Named("myCustomizeToDTO")
    default AuthorDTO toAuthorDTO(Author author) {
        if (author == null) return null;

        var authorDTO = toDto(author);

        if (!author.getImages().isEmpty()) {
            var firstImage = author.getImages().iterator().next();
            authorDTO.setImageUrl(firstImage.getImage_url());
        }

        return authorDTO;
    }

    @Override
    @IterableMapping(qualifiedByName = "myCustomizeToDTO")
    List<AuthorDTO> toDto(List<Author> entityList);
}
