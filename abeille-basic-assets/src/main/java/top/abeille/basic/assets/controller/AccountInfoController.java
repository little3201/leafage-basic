/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import top.abeille.basic.assets.service.AccountInfoService;
import top.abeille.basic.assets.vo.enter.AccountEnter;
import top.abeille.basic.assets.vo.outer.AccountOuter;
import top.abeille.common.basic.AbstractController;

import javax.validation.Valid;

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
    public Mono<ResponseEntity<AccountOuter>> getAccount(@PathVariable Long accountId) {
        return accountInfoService.getByAccountId(accountId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * 保存账号信息
     *
     * @param account 账户信息
     * @return Mono<AccountInfo>
     */
    @PostMapping
    public Mono<ResponseEntity<AccountOuter>> saveAccount(@RequestBody @Valid AccountEnter account) {
        return accountInfoService.save(account)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED));
    }

    /**
     * 修改账号信息
     *
     * @param account 账户信息
     * @return Mono<AccountInfo>
     */
    @PutMapping("/{accountId}")
    public Mono<ResponseEntity<AccountOuter>> modifyAccount(@PathVariable String accountId, @RequestBody @Valid AccountEnter account) {
        return accountInfoService.save(account)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_MODIFIED));
    }

    /**
     * 删除账号信息
     *
     * @param accountId 主键
     * @return Mono<Void>
     */
    @DeleteMapping("/{accountId}")
    public Mono<ResponseEntity<Void>> removeAccount(@PathVariable Long accountId) {
        return accountInfoService.removeByAccountId(accountId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED));
    }
}
