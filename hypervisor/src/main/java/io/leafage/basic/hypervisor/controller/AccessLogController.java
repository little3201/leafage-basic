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

package io.leafage.basic.hypervisor.controller;

import io.leafage.basic.hypervisor.dto.AccessLogDTO;
import io.leafage.basic.hypervisor.service.AccessLogService;
import io.leafage.basic.hypervisor.vo.AccessLogVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * access log controller.
 *
 * @author wq li
 */
@RestController
@RequestMapping("/access-logs")
public class AccessLogController {

    private final Logger logger = LoggerFactory.getLogger(AccessLogController.class);

    private final AccessLogService accessLogService;

    /**
     * <p>Constructor for AccessLogController.</p>
     *
     * @param accessLogService a {@link io.leafage.basic.hypervisor.service.AccessLogService} object
     */
    public AccessLogController(AccessLogService accessLogService) {
        this.accessLogService = accessLogService;
    }

    /**
     * 查询
     *
     * @param page       页码
     * @param size       大小
     * @param sortBy     排序字段
     * @param descending 排序方向
     * @return 查询到数据集，异常时返回204
     */
    @GetMapping
    public ResponseEntity<Page<AccessLogVO>> retrieve(@RequestParam int page, @RequestParam int size,
                                                      String sortBy, boolean descending) {
        Page<AccessLogVO> voPage;
        try {
            voPage = accessLogService.retrieve(page, size, sortBy, descending);
        } catch (Exception e) {
            logger.error("Retrieve record occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(voPage);
    }

    /**
     * 新增信息
     *
     * @param accessLogDTO 帖子代码
     * @return 添加后的信息，异常时返回417状态码
     */
    @PostMapping
    public ResponseEntity<AccessLogVO> create(@RequestBody AccessLogDTO accessLogDTO) {
        AccessLogVO vo;
        try {
            vo = accessLogService.create(accessLogDTO);
        } catch (Exception e) {
            logger.error("Create record occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(vo);
    }
}
