package io.leafage.basic.hypervisor.repository;

import io.leafage.basic.hypervisor.domain.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * region repository.
 *
 * @author wq li 2021/11/27 14:18
 **/
@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {

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
     * @return 结果信息
     */
    List<Region> findBySuperiorIdAndEnabledTrue(Long id);
}
