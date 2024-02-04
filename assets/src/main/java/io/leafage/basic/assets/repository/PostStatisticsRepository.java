package io.leafage.basic.assets.repository;

import io.leafage.basic.assets.domain.PostStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * post statistics repository.
 *
 * @author wq li  2021-09-29 14:19
 **/
@Repository
public interface PostStatisticsRepository extends JpaRepository<PostStatistics, Long> {

    /**
     * 增加viewed
     *
     * @param id 主键
     */
    @Modifying
    @Query("update #{#entityName} set viewed = viewed + 1 where id = ?1")
    void increaseViewed(Long id);

    /**
     * 增加comment
     *
     * @param id 主键
     */
    @Modifying
    @Query("update #{#entityName} set comments = comments + 1 where id = ?1")
    void increaseComment(Long id);
}
