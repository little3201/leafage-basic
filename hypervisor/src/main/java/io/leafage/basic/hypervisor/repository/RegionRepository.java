package io.leafage.basic.hypervisor.repository;

import io.leafage.basic.hypervisor.domain.Region;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * region repository.
 *
 * @author liwenqiang 2021/11/27 14:18
 **/
@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {

    /**
     * 分页查询
     *
     * @param pageable 分页参数
     * @return 有效数据集
     */
    Page<Region> findByEnabledTrue(Pageable pageable);

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
