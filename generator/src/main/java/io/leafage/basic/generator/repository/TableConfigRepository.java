package io.leafage.basic.generator.repository;

import io.leafage.basic.generator.domain.TableConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * table config repository.
 *
 * @author wq li
 **/
@Repository
public interface TableConfigRepository extends JpaRepository<TableConfig, Long> {
}
