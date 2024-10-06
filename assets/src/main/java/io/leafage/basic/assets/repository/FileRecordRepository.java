package io.leafage.basic.assets.repository;

import io.leafage.basic.assets.domain.FileRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * file repository.
 *
 * @author wq li
 */
@Repository
public interface FileRecordRepository extends JpaRepository<FileRecord, Long>, JpaSpecificationExecutor<FileRecord> {
}
