package io.leafage.basic.hypervisor.repository;

import io.leafage.basic.hypervisor.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * message repository.
 *
 * @author wq li 2022/1/29 17:34
 **/
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
}
