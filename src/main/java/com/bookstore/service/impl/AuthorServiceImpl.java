package com.bookstore.service.impl;

import com.bookstore.domain.Author;
import com.bookstore.repository.AuthorRepository;
import com.bookstore.repository.ImageAuthorRepository;
import com.bookstore.service.AuthorService;
import com.bookstore.service.dto.AuthorDTO;
import com.bookstore.service.mapper.AuthorMapper;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bookstore.domain.Author}.
 */
@Service
@Transactional
public class AuthorServiceImpl implements AuthorService {

    private static final Logger LOG = LoggerFactory.getLogger(AuthorServiceImpl.class);

    private final AuthorRepository authorRepository;
    private final ImageAuthorRepository imageAuthorRepository;

    private final AuthorMapper authorMapper;

    public AuthorServiceImpl(AuthorRepository authorRepository, ImageAuthorRepository imageAuthorRepository, AuthorMapper authorMapper) {
        this.authorRepository = authorRepository;
        this.imageAuthorRepository = imageAuthorRepository;
        this.authorMapper = authorMapper;
    }

    @Override
    public AuthorDTO save(AuthorDTO authorDTO) {
        LOG.debug("Request to save Author : {}", authorDTO);
        Author author = authorMapper.toEntity(authorDTO);
        author = authorRepository.save(author);
        return authorMapper.toDto(author);
    }

    @Override
    public AuthorDTO update(AuthorDTO authorDTO, String contextPath) {
        LOG.debug("Request to update Author : {}", authorDTO);
        Author author = authorMapper.toEntity(authorDTO);
        author = authorRepository.save(author);

        var imageAuthorList = imageAuthorRepository.findByAuthorId(author.getId());

        if (imageAuthorList != null && imageAuthorList.size() > 0) {
            // update anh dau tien
            var firstImage = imageAuthorList.get(0);

            // xoa file cu
            var oldImageFile = firstImage.getImage_url();
            var oldImageFilePath = contextPath + "/content/img/authors/" + oldImageFile;
            LOG.info("Prepare to delete old image {}", oldImageFilePath);

            new File(oldImageFilePath).delete();
            LOG.info("Deleted old image {}", oldImageFilePath);

            // luu lai file anh moi
            String filePath = contextPath + "/content/img/authors/" + authorDTO.getImage().getOriginalFilename();
            try {
                //                authorDTO.getImage().transferTo(new File(filePath));

                Files.write(Path.of(filePath), authorDTO.getImage().getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            firstImage.setImage_url(authorDTO.getImage().getOriginalFilename());
            imageAuthorRepository.save(firstImage);
        } else {
            // them moi anh
        }

        return authorMapper.toDto(author);
    }

    @Override
    public Optional<AuthorDTO> partialUpdate(AuthorDTO authorDTO) {
        LOG.debug("Request to partially update Author : {}", authorDTO);

        return authorRepository
            .findById(authorDTO.getId())
            .map(existingAuthor -> {
                authorMapper.partialUpdate(existingAuthor, authorDTO);

                return existingAuthor;
            })
            .map(authorRepository::save)
            .map(authorMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AuthorDTO> findOne(Long id) {
        LOG.debug("Request to get Author : {}", id);
        return authorRepository.findById(id).map(authorMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Author : {}", id);
        authorRepository.deleteById(id);
    }
}
