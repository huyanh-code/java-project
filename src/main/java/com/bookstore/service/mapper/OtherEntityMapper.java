package com.bookstore.service.mapper;

import com.bookstore.domain.OtherEntity;
import com.bookstore.service.dto.OtherEntityDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OtherEntity} and its DTO {@link OtherEntityDTO}.
 */
@Mapper(componentModel = "spring")
public interface OtherEntityMapper extends EntityMapper<OtherEntityDTO, OtherEntity> {}
