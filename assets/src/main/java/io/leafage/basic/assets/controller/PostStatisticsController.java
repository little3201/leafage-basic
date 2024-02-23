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

package io.leafage.basic.assets.controller;

import io.leafage.basic.assets.service.PostStatisticsService;
import io.leafage.basic.assets.vo.PostStatisticsVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * resource controller.
 *
 * @author wq li 2018/12/20 9:54
 **/
@RestController
@RequestMapping("/statistics")
public class PostStatisticsController {

    private final Logger logger = LoggerFactory.getLogger(PostStatisticsController.class);

    private final PostStatisticsService postStatisticsService;

    public PostStatisticsController(PostStatisticsService postStatisticsService) {
        this.postStatisticsService = postStatisticsService;
    }

    /**
     * 分页查询浏览量统计
     *
     * @param page 页码
     * @param size 大小
     * @return 查询到数据，异常时返回204
     */
    @GetMapping
    public ResponseEntity<Page<PostStatisticsVO>> retrieve(@RequestParam int page, @RequestParam int size) {
        Page<PostStatisticsVO> voFlux;
        try {
            voFlux = postStatisticsService.retrieve(page, size);
        } catch (Exception e) {
            logger.error("Statistics viewed occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(voFlux);
    }

}
