package com.bookstore.service.impl;

import com.bookstore.domain.OtherEntity;
import com.bookstore.repository.OtherEntityRepository;
import com.bookstore.service.OtherEntityService;
import com.bookstore.service.dto.OtherEntityDTO;
import com.bookstore.service.mapper.OtherEntityMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bookstore.domain.OtherEntity}.
 */
@Service
@Transactional
public class OtherEntityServiceImpl implements OtherEntityService {

    private static final Logger LOG = LoggerFactory.getLogger(OtherEntityServiceImpl.class);

    private final OtherEntityRepository otherEntityRepository;

    private final OtherEntityMapper otherEntityMapper;

    public OtherEntityServiceImpl(OtherEntityRepository otherEntityRepository, OtherEntityMapper otherEntityMapper) {
        this.otherEntityRepository = otherEntityRepository;
        this.otherEntityMapper = otherEntityMapper;
    }

    @Override
    public OtherEntityDTO save(OtherEntityDTO otherEntityDTO) {
        LOG.debug("Request to save OtherEntity : {}", otherEntityDTO);
        OtherEntity otherEntity = otherEntityMapper.toEntity(otherEntityDTO);
        otherEntity = otherEntityRepository.save(otherEntity);
        return otherEntityMapper.toDto(otherEntity);
    }

    @Override
    public OtherEntityDTO update(OtherEntityDTO otherEntityDTO) {
        LOG.debug("Request to update OtherEntity : {}", otherEntityDTO);
        OtherEntity otherEntity = otherEntityMapper.toEntity(otherEntityDTO);
        otherEntity = otherEntityRepository.save(otherEntity);
        return otherEntityMapper.toDto(otherEntity);
    }

    @Override
    public Optional<OtherEntityDTO> partialUpdate(OtherEntityDTO otherEntityDTO) {
        LOG.debug("Request to partially update OtherEntity : {}", otherEntityDTO);

        return otherEntityRepository
            .findById(otherEntityDTO.getId())
            .map(existingOtherEntity -> {
                otherEntityMapper.partialUpdate(existingOtherEntity, otherEntityDTO);

                return existingOtherEntity;
            })
            .map(otherEntityRepository::save)
            .map(otherEntityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OtherEntityDTO> findOne(Long id) {
        LOG.debug("Request to get OtherEntity : {}", id);
        return otherEntityRepository.findById(id).map(otherEntityMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete OtherEntity : {}", id);
        otherEntityRepository.deleteById(id);
    }
}
