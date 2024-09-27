package com.bookstore.service.impl;

import com.bookstore.domain.ImageAuthor;
import com.bookstore.repository.ImageAuthorRepository;
import com.bookstore.service.ImageAuthorService;
import com.bookstore.service.dto.ImageAuthorDTO;
import com.bookstore.service.mapper.ImageAuthorMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bookstore.domain.ImageAuthor}.
 */
@Service
@Transactional
public class ImageAuthorServiceImpl implements ImageAuthorService {

    private static final Logger LOG = LoggerFactory.getLogger(ImageAuthorServiceImpl.class);

    private final ImageAuthorRepository imageAuthorRepository;

    private final ImageAuthorMapper imageAuthorMapper;

    public ImageAuthorServiceImpl(ImageAuthorRepository imageAuthorRepository, ImageAuthorMapper imageAuthorMapper) {
        this.imageAuthorRepository = imageAuthorRepository;
        this.imageAuthorMapper = imageAuthorMapper;
    }

    @Override
    public ImageAuthorDTO save(ImageAuthorDTO imageAuthorDTO) {
        LOG.debug("Request to save ImageAuthor : {}", imageAuthorDTO);
        ImageAuthor imageAuthor = imageAuthorMapper.toEntity(imageAuthorDTO);
        imageAuthor = imageAuthorRepository.save(imageAuthor);
        return imageAuthorMapper.toDto(imageAuthor);
    }

    @Override
    public ImageAuthorDTO update(ImageAuthorDTO imageAuthorDTO) {
        LOG.debug("Request to update ImageAuthor : {}", imageAuthorDTO);
        ImageAuthor imageAuthor = imageAuthorMapper.toEntity(imageAuthorDTO);
        imageAuthor = imageAuthorRepository.save(imageAuthor);
        return imageAuthorMapper.toDto(imageAuthor);
    }

    @Override
    public Optional<ImageAuthorDTO> partialUpdate(ImageAuthorDTO imageAuthorDTO) {
        LOG.debug("Request to partially update ImageAuthor : {}", imageAuthorDTO);

        return imageAuthorRepository
            .findById(imageAuthorDTO.getId())
            .map(existingImageAuthor -> {
                imageAuthorMapper.partialUpdate(existingImageAuthor, imageAuthorDTO);

                return existingImageAuthor;
            })
            .map(imageAuthorRepository::save)
            .map(imageAuthorMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ImageAuthorDTO> findOne(Long id) {
        LOG.debug("Request to get ImageAuthor : {}", id);
        return imageAuthorRepository.findById(id).map(imageAuthorMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ImageAuthor : {}", id);
        imageAuthorRepository.deleteById(id);
    }
}
