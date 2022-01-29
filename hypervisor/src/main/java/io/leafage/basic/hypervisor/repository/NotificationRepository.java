package io.leafage.basic.hypervisor.repository;

import io.leafage.basic.hypervisor.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * notification repository.
 *
 * @author liwenqiang 2022/1/29 17:34
 **/
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
