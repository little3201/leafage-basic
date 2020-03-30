/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.abeille.basic.assets.dto.AccountDTO;
import top.abeille.basic.assets.service.AccountInfoService;
import top.abeille.basic.assets.vo.AccountVO;
import top.abeille.common.basic.AbstractController;

/**
 * 账户信息接口
 *
 * @author liwenqiang 2018/12/20 9:54
 **/
@RestController
@RequestMapping("/account")
public class AccountInfoController extends AbstractController {

    private final AccountInfoService accountInfoService;

    public AccountInfoController(AccountInfoService accountInfoService) {
        this.accountInfoService = accountInfoService;
    }

    /**
     * 根据业务ID查询账号信息
     *
     * @param businessId 业务ID
     * @return ResponseEntity
     */
    @GetMapping("/{businessId}")
    public ResponseEntity<Object> fetchAccount(@PathVariable String businessId) {
        AccountVO account = accountInfoService.fetchByBusinessId(businessId);
        if (account == null) {
            logger.info("Not found anything about account with businessId {}.", businessId);
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
    public ResponseEntity<Object> saveAccount(@RequestBody AccountDTO account) {
        AccountVO accountVO;
        try {
            accountVO = accountInfoService.create(account);
        } catch (Exception e) {
            logger.error("Save account occurred an error: ", e);
            return ResponseEntity.ok(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.ok(accountVO);
    }

    /**
     * 修改账号信息
     *
     * @param businessId 业务ID
     * @param accountDTO 账户信息
     * @return ResponseEntity
     */
    @PutMapping("/{businessId}")
    public ResponseEntity<Object> modifyAccount(@PathVariable String businessId, @RequestBody AccountDTO accountDTO) {
        try {
            accountInfoService.modify(businessId, accountDTO);
        } catch (Exception e) {
            logger.error("Modify account occurred an error: ", e);
            return ResponseEntity.ok(HttpStatus.NOT_MODIFIED);
        }
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    /**
     * 删除账号信息
     *
     * @param businessId 业务ID
     * @return ResponseEntity
     */
    @DeleteMapping("/{businessId}")
    public ResponseEntity<Object> removeAccount(@PathVariable String businessId) {
        try {
            accountInfoService.removeById(businessId);
        } catch (Exception e) {
            logger.error("Remove account occurred an error: ", e);
            return ResponseEntity.ok(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
