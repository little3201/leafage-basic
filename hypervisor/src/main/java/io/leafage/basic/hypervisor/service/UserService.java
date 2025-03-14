/*
 * Copyright (c) 2024-2025.  little3201.
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
package io.leafage.basic.hypervisor.service;

import io.leafage.basic.hypervisor.dto.UserDTO;
import io.leafage.basic.hypervisor.vo.UserVO;
import org.springframework.data.domain.Page;
import top.leafage.common.servlet.ServletBasicService;

/**
 * user service.
 *
 * @author wq li
 */
public interface UserService extends ServletBasicService<UserDTO, UserVO> {

    /**
     * Retrieves a paginated list of records.
     *
     * @param page       The page number (zero-based).
     * @param size       The number of records per page.
     * @param sortBy     The field to sort by. If null, records are unsorted.
     * @param descending Whether sorting should be in descending order.
     * @return A paginated list of records.
     * @since 0.3.0
     */
    Page<UserVO> retrieve(int page, int size, String sortBy, boolean descending, String username);

    /**
     * Fetch user
     *
     * @param username username
     * @return Record
     */
    UserVO findByUsername(String username);
}
