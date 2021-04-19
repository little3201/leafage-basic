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
 * 分类接口
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
     * 查询类目
     *
     * @param page 页码
     * @param size 大小
     * @return ResponseEntity
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
     * @return ResponseEntity
     */
    @GetMapping("/{code}")
    public ResponseEntity<Object> fetch(@PathVariable String code) {
        CategoryVO account = categoryService.fetch(code);
        if (account == null) {
            logger.info("Not found anything about category with code {}.", code);
            return ResponseEntity.ok(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(account);
    }

    /**
     * 保存类目信息
     *
     * @param categoryDTO 类目信息
     * @return ResponseEntity
     */
    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid CategoryDTO categoryDTO) {
        CategoryVO categoryVO;
        try {
            categoryVO = categoryService.create(categoryDTO);
        } catch (Exception e) {
            logger.error("Save category occurred an error: ", e);
            return ResponseEntity.ok(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.ok(categoryVO);
    }

    /**
     * 修改类目信息
     *
     * @param code        代码
     * @param categoryDTO 类目信息
     * @return ResponseEntity
     */
    @PutMapping("/{code}")
    public ResponseEntity<Object> modify(@PathVariable String code, @RequestBody @Valid CategoryDTO categoryDTO) {
        try {
            categoryService.modify(code, categoryDTO);
        } catch (Exception e) {
            logger.error("Modify category occurred an error: ", e);
            return ResponseEntity.ok(HttpStatus.NOT_MODIFIED);
        }
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    /**
     * 删除类目信息
     *
     * @param code 代码
     * @return ResponseEntity
     */
    @DeleteMapping("/{code}")
    public ResponseEntity<Object> remove(@PathVariable String code) {
        try {
            categoryService.remove(code);
        } catch (Exception e) {
            logger.error("Remove category occurred an error: ", e);
            return ResponseEntity.ok(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
