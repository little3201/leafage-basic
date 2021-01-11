/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.abeille.basic.assets.dto.CategoryDTO;
import top.abeille.basic.assets.service.CategoryService;
import top.abeille.basic.assets.vo.AccountVO;

/**
 * 账户信息接口
 *
 * @author liwenqiang 2018/12/20 9:54
 **/
@RestController
@RequestMapping("/account")
public class CategoryController {

    private final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * 查询账号信息
     *
     * @param code 代码
     * @return ResponseEntity
     */
    @GetMapping("/{businessId}")
    public ResponseEntity<Object> fetch(@PathVariable String code) {
        AccountVO account = categoryService.fetch(code);
        if (account == null) {
            logger.info("Not found anything about account with code {}.", code);
            return ResponseEntity.ok(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(account);
    }

    /**
     * 保存账号信息
     *
     * @param account 账户信息
     * @return ResponseEntity
     */
    @PostMapping
    public ResponseEntity<Object> save(@RequestBody CategoryDTO account) {
        AccountVO accountVO;
        try {
            accountVO = categoryService.create(account);
        } catch (Exception e) {
            logger.error("Save account occurred an error: ", e);
            return ResponseEntity.ok(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.ok(accountVO);
    }

    /**
     * 修改账号信息
     *
     * @param code        代码
     * @param categoryDTO 账户信息
     * @return ResponseEntity
     */
    @PutMapping("/{code}")
    public ResponseEntity<Object> modify(@PathVariable String code, @RequestBody CategoryDTO categoryDTO) {
        try {
            categoryService.modify(code, categoryDTO);
        } catch (Exception e) {
            logger.error("Modify account occurred an error: ", e);
            return ResponseEntity.ok(HttpStatus.NOT_MODIFIED);
        }
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    /**
     * 删除账号信息
     *
     * @param code 代码
     * @return ResponseEntity
     */
    @DeleteMapping("/{code}")
    public ResponseEntity<Object> remove(@PathVariable String code) {
        try {
            categoryService.remove(code);
        } catch (Exception e) {
            logger.error("Remove account occurred an error: ", e);
            return ResponseEntity.ok(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
