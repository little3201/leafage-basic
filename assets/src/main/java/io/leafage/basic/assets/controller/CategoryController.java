/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.controller;

import io.leafage.basic.assets.dto.CategoryDTO;
import io.leafage.basic.assets.service.CategoryService;
import io.leafage.basic.assets.vo.CategoryVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 分类接口.
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
     * @return 分页结果集
     */
    @GetMapping
    public ResponseEntity<Object> retrieve(@RequestParam int page, @RequestParam int size, String order) {
        Page<CategoryVO> voPage = categoryService.retrieve(page, size, order);
        if (voPage.hasContent()) {
            return ResponseEntity.ok(voPage);
        }
        logger.info("Not found anything about category with pageable.");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 查询类目信息
     *
     * @param code 代码
     * @return 匹配到的类目信息
     */
    @GetMapping("/{code}")
    public ResponseEntity<Object> fetch(@PathVariable String code) {
        CategoryVO categoryVO = categoryService.fetch(code);
        if (categoryVO == null) {
            logger.info("Not found anything about category with code {}.", code);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
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
    public ResponseEntity<Object> create(@RequestBody @Valid CategoryDTO categoryDTO) {
        CategoryVO categoryVO;
        try {
            categoryVO = categoryService.create(categoryDTO);
        } catch (Exception e) {
            logger.error("Save category occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
        return ResponseEntity.ok(categoryVO);
    }

    /**
     * 修改类目信息
     *
     * @param code        代码
     * @param categoryDTO 类目信息
     * @return 修改后的类目信息
     */
    @PutMapping("/{code}")
    public ResponseEntity<Object> modify(@PathVariable String code, @RequestBody @Valid CategoryDTO categoryDTO) {
        try {
            categoryService.modify(code, categoryDTO);
        } catch (Exception e) {
            logger.error("Modify category occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    /**
     * 删除类目信息
     *
     * @param code 代码
     * @return 删除结果
     */
    @DeleteMapping("/{code}")
    public ResponseEntity<Object> remove(@PathVariable String code) {
        try {
            categoryService.remove(code);
        } catch (Exception e) {
            logger.error("Remove category occurred an error: ", e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
        return ResponseEntity.ok().build();
    }
}
