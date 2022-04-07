package io.leafage.basic.hypervisor.repository;

import io.leafage.basic.hypervisor.entity.Dictionary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * dictionary repository.
 *
 * @author liwenqiang 2022-04-06 17:38
 **/
@Repository
public interface DictionaryRepository extends JpaRepository<Dictionary, Long> {

    /**
     * 分页查询
     *
     * @param pageable 分页参数
     * @return 有效数据集
     */
    Page<Dictionary> findByEnabledTrue(Pageable pageable);

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
    List<Dictionary> findBySuperiorAndEnabledTrue(String code);

    /**
     * 根据code查询
     *
     * @param code 代码
     * @return 数据信息
     */
    Dictionary getByCodeAndEnabledTrue(String code);
}
