package com.bookstore.repository;

import com.bookstore.domain.OtherEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OtherEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OtherEntityRepository extends JpaRepository<OtherEntity, Long>, JpaSpecificationExecutor<OtherEntity> {}
