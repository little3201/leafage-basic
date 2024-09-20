package io.leafage.basic.generator.repository;

import io.leafage.basic.generator.domain.ColumnConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * column config repository.
 *
 * @author wq li
 **/
@Repository
public interface ColumnConfigRepository extends JpaRepository<ColumnConfig, Long> {
}
