package io.leafage.basic.hypervisor.repository;

import io.leafage.basic.hypervisor.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * notification repository.
 *
 * @author liwenqiang 2022/1/29 17:34
 **/
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    /**
     * 分页查询
     *
     * @param pageable 分页参数
     * @return 信息
     */
    Page<Notification> findByEnabledTrue(Pageable pageable);

    /**
     * 根据code查询信息
     *
     * @param code 代码
     * @return 结果信息
     */
    Notification getByCodeAndEnabledTrue(String code);
}
