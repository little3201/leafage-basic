/*
 * Copyright(c) 2019-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package top.leafage.hypervisor.messages.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import top.leafage.hypervisor.messages.domain.Message;

/**
 * message repository.
 *
 * @author wq li
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Long>, JpaSpecificationExecutor<Message> {

    /**
     * is exists.
     *
     * @param title the title.
     * @return if exists return true or false.
     */
    boolean existsByTitle(String title);

    /**
     * Publish.
     *
     * @param id     The pk.
     * @param status the status.
     * @return result.
     */
    @Modifying
    @Query("UPDATE Message t SET t.status = :status, t.publishedAt = CURRENT_TIMESTAMP WHERE t.id = :id")
    int updateStatusAndPublishedAtById(Long id, Message.Status status);

    /**
     * Revoke.
     *
     * @param id     The pk.
     * @param status the status.
     * @return result.
     */
    @Modifying
    @Query("UPDATE Message t SET t.status = :status, t.publishedAt = NULL WHERE t.id = :id")
    int updateStatusAndPublishedAtNullById(Long id, Message.Status status);
}
