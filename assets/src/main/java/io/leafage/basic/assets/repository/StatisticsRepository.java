package io.leafage.basic.assets.repository;

import io.leafage.basic.assets.entity.Statistics;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;

/**
 * statistics repository.
 *
 * @author liwenqiang  2021-09-29 14:19
 **/
@Repository
public interface StatisticsRepository extends JpaRepository<Statistics, Long> {

    /**
     * 根据data查询当日数据
     *
     * @param date 日期
     * @return 统计数据
     */
    Statistics getByDate(LocalDate date);

    /**
     * 分页查询
     *
     * @param pageable 分页参数
     * @return 有效帖子
     */
    Page<Statistics> findByEnabledTrue(Pageable pageable);
}
