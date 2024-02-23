/*
 *  Copyright 2018-2024 little3201.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

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
