package io.leafage.basic.generator.repository;

import io.leafage.basic.generator.domain.TableConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * table config repository.
 *
 * @author wq li 2024-09-20 11:10
 **/
@Repository
public interface TableConfigRepository extends JpaRepository<TableConfig, Long> {
}
