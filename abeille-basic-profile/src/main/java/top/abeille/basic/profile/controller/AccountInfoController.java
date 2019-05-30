/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.profile.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.abeille.basic.profile.entity.AccountInfo;
import top.abeille.basic.profile.service.AccountInfoService;
import top.abeille.common.basic.AbstractController;

/**
 * 账户信息Controller
 *
 * @author liwenqiang 2018/12/20 9:54
 **/
@RestController
public class AccountInfoController extends AbstractController {

    private final AccountInfoService accountInfoService;

    public AccountInfoController(AccountInfoService accountInfoService) {
        this.accountInfoService = accountInfoService;
    }

    /**
     * 查询账号信息——根据ID
     *
     * @param id 主键
     * @return ResponseEntity
     */
    @GetMapping("/account/{id}")
    public ResponseEntity getAccount(@PathVariable Long id) {
        if (id == null) {
            return ResponseEntity.ok(HttpStatus.NOT_ACCEPTABLE);
        }
        AccountInfo account = accountInfoService.getById(id);
        if (account == null) {
            log.info("Not found anything about account with id {}.", id);
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
    @PostMapping("/account")
    public ResponseEntity saveAccount(@RequestBody AccountInfo account) {
        try {
            accountInfoService.save(account);
        } catch (Exception e) {
            log.error("Save account occurred an error: ", e);
            return ResponseEntity.ok("error");
        }
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    /**
     * 修改账号信息
     *
     * @param account 账户信息
     * @return ResponseEntity
     */
    @PutMapping("/account")
    public ResponseEntity modifyAccount(@RequestBody AccountInfo account) {
        if (account.getId() == null) {
            return ResponseEntity.ok(HttpStatus.NOT_ACCEPTABLE);
        }
        try {
            accountInfoService.save(account);
        } catch (Exception e) {
            log.error("Modify account occurred an error: ", e);
            return ResponseEntity.ok(HttpStatus.NOT_MODIFIED);
        }
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    /**
     * 删除账号信息
     *
     * @param id 主键
     * @return ResponseEntity
     */
    @DeleteMapping("/account/{id}")
    public ResponseEntity removeAccount(@PathVariable Long id) {
        if (id == null) {
            return ResponseEntity.ok(HttpStatus.NOT_ACCEPTABLE);
        }
        try {
            accountInfoService.removeById(id);
        } catch (Exception e) {
            log.error("Remove account occurred an error: ", e);
            return ResponseEntity.ok(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
