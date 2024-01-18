package io.leafage.basic.hypervisor.repository;

import io.leafage.basic.hypervisor.domain.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * message repository.
 *
 * @author liwenqiang 2022/1/29 17:34
 **/
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    /**
     * 分页查询
     *
     * @param pageable 分页参数
     * @return 信息
     */
    Page<Message> findByEnabledTrue(Pageable pageable);

}
