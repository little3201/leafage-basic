/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.controller;

import io.leafage.basic.assets.dto.CategoryDTO;
import io.leafage.basic.assets.service.CategoryService;
import io.leafage.basic.assets.vo.CategoryVO;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * category controller.
 *
 * @author liwenqiang 2018/12/20 9:54
 **/
@RestController
@RequestMapping("/category")
public class CategoryController {

    private final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * 分页查询类目
     *
     * @param page 页码
     * @param size 大小
     * @param sort 排序字段
     * @return 分页结果集
     */
    @GetMapping
    public ResponseEntity<Page<CategoryVO>> retrieve(@RequestParam int page, @RequestParam int size, String sort) {
        Page<CategoryVO> voPage;
        try {
            voPage = categoryService.retrieve(page, size, sort);
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
    public ResponseEntity<CategoryVO> fetch(@PathVariable Long id) {
        CategoryVO categoryVO;
        try {
            categoryVO = categoryService.fetch(id);
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
    public ResponseEntity<CategoryVO> create(@RequestBody @Valid CategoryDTO categoryDTO) {
        CategoryVO categoryVO;
        try {
            categoryVO = categoryService.create(categoryDTO);
        } catch (Exception e) {
            logger.error("Save category occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryVO);
    }

    /**
     * 修改类目信息
     *
     * @param id        主键
     * @param categoryDTO 类目信息
     * @return 修改后的类目信息
     */
    @PutMapping("/{id}")
    public ResponseEntity<CategoryVO> modify(@PathVariable Long id, @RequestBody @Valid CategoryDTO categoryDTO) {
        CategoryVO categoryVO;
        try {
            categoryVO = categoryService.modify(id, categoryDTO);
        } catch (Exception e) {
            logger.error("Modify category occurred an error: ", e);
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
            categoryService.remove(id);
        } catch (Exception e) {
            logger.error("Remove category occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
        return ResponseEntity.ok().build();
    }
}
