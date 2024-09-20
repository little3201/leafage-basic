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

import io.leafage.basic.assets.dto.TagDTO;
import io.leafage.basic.assets.service.TagService;
import io.leafage.basic.assets.vo.TagVO;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * tag controller.
 *
 * @author wq li
 */
@RestController
@RequestMapping("/tags")
public class TagController {

    private final Logger logger = LoggerFactory.getLogger(TagController.class);

    private final TagService tagService;

    /**
     * <p>Constructor for TagController.</p>
     *
     * @param tagService a {@link io.leafage.basic.assets.service.TagService} object
     */
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    /**
     * 分页查询类目
     *
     * @param page   页码
     * @param size   大小
     * @param sortBy 排序字段
     * @return 分页结果集
     * @param descending a boolean
     */
    @GetMapping
    public ResponseEntity<Page<TagVO>> retrieve(@RequestParam int page, @RequestParam int size,
                                                String sortBy, boolean descending) {
        Page<TagVO> voPage;
        try {
            voPage = tagService.retrieve(page, size, sortBy, descending);
        } catch (Exception e) {
            logger.error("Retrieve posts occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(voPage);
    }

    /**
     * 查询类目信息
     *
     * @param id 主键
     * @return 匹配到的类目信息
     */
    @GetMapping("/{id}")
    public ResponseEntity<TagVO> fetch(@PathVariable Long id) {
        TagVO categoryVO;
        try {
            categoryVO = tagService.fetch(id);
        } catch (Exception e) {
            logger.error("Fetch posts occurred an error: ", e);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(categoryVO);
    }

    /**
     * 保存类目信息
     *
     * @param categoryDTO 类目信息
     * @return 类目信息
     */
    @PostMapping
    public ResponseEntity<TagVO> create(@RequestBody @Valid TagDTO categoryDTO) {
        TagVO categoryVO;
        try {
            categoryVO = tagService.create(categoryDTO);
        } catch (Exception e) {
            logger.error("Save tag occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryVO);
    }

    /**
     * 修改类目信息
     *
     * @param id          主键
     * @param categoryDTO 类目信息
     * @return 修改后的类目信息
     */
    @PutMapping("/{id}")
    public ResponseEntity<TagVO> modify(@PathVariable Long id, @RequestBody @Valid TagDTO categoryDTO) {
        TagVO categoryVO;
        try {
            categoryVO = tagService.modify(id, categoryDTO);
        } catch (Exception e) {
            logger.error("Modify tag occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
        return ResponseEntity.accepted().body(categoryVO);
    }

    /**
     * 删除类目信息
     *
     * @param id 主键
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        try {
            tagService.remove(id);
        } catch (Exception e) {
            logger.error("Remove tag occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
        return ResponseEntity.ok().build();
    }
}
