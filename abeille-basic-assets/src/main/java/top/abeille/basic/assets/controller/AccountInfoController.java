/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import top.abeille.basic.assets.entity.AccountInfo;
import top.abeille.basic.assets.service.AccountInfoService;
import top.abeille.common.basic.AbstractController;

import javax.validation.Valid;
import java.util.Objects;

/**
 * 账户信息Controller
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
     * 查询账号信息——根据ID
     *
     * @param accountId 账户ID
     * @return Mono<AccountInfo>
     */
    @GetMapping("/{accountId}")
    public ResponseEntity getAccount(@PathVariable String accountId) {
        Mono<AccountInfo> infoMono = accountInfoService.getByAccountId(accountId);
        if (Objects.isNull(infoMono)) {
            log.info("Not found with accountId: {}.", accountId);
            return ResponseEntity.ok(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(infoMono);
    }

    /**
     * 保存账号信息
     *
     * @param account 账户信息
     * @return Mono<AccountInfo>
     */
    @PostMapping
    public ResponseEntity saveAccount(@RequestBody @Valid AccountInfo account) {
        Mono<AccountInfo> infoMono = accountInfoService.save(account);
        if (Objects.isNull(infoMono)) {
            log.error("Save user occurred error.");
            return ResponseEntity.ok(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    /**
     * 修改账号信息
     *
     * @param account 账户信息
     * @return Mono<AccountInfo>
     */
    @PutMapping
    public ResponseEntity modifyAccount(@RequestBody @Valid AccountInfo account) {
        if (Objects.isNull(account.getId())) {
            return ResponseEntity.ok(HttpStatus.NOT_ACCEPTABLE);
        }
        Mono<AccountInfo> infoMono = accountInfoService.save(account);
        if (Objects.isNull(infoMono)) {
            log.error("Modify user occurred error.");
            return ResponseEntity.ok(HttpStatus.NOT_MODIFIED);
        }
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    /**
     * 删除账号信息
     *
     * @param accountId 主键
     * @return Mono<Void>
     */
    @DeleteMapping("/{accountId}")
    public ResponseEntity removeAccount(@PathVariable String accountId) {
        return ResponseEntity.ok(accountInfoService.removeByAccountId(accountId));
    }
}
