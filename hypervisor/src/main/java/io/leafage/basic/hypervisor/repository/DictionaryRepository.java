package io.leafage.basic.hypervisor.repository;

import io.leafage.basic.hypervisor.domain.Dictionary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * dictionary repository.
 *
 * @author wq li 2022-04-06 17:38
 **/
@Repository
public interface DictionaryRepository extends JpaRepository<Dictionary, Long> {

    /**
     * 是否存在
     *
     * @param name 名称
     * @return true-存在，false-否
     */
    boolean existsByName(String name);

    /**
     * 查询下级信息
     *
     * @param superiorId 上级主键
     * @return 结果信息
     */
    List<Dictionary> findBySuperiorIdAndEnabledTrue(Long superiorId);

}
