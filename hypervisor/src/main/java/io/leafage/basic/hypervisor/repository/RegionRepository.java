package io.leafage.basic.hypervisor.repository;

import io.leafage.basic.hypervisor.entity.Region;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {

    /**
     * 分页查询region
     *
     * @param pageable 分页参数
     * @return 有效数据集
     */
    Page<Region> findByEnabledTrue(Pageable pageable);

    /**
     * 根据code查询enabled信息
     *
     * @param code 代码
     * @return 查询结果信息
     */
    Region getByCodeAndEnabledTrue(Long code);
}
