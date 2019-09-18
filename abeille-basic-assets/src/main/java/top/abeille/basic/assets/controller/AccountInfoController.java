/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.assets.controller;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import top.abeille.basic.assets.entity.AccountInfo;
import top.abeille.basic.assets.service.AccountInfoService;
import top.abeille.common.basic.AbstractController;

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
    public Mono<AccountInfo> getAccount(@PathVariable String accountId) {
        return accountInfoService.getByAccountId(accountId);
    }

    /**
     * 保存账号信息
     *
     * @param account 账户信息
     * @return Mono<AccountInfo>
     */
    @PostMapping
    public Mono<AccountInfo> saveAccount(@RequestBody AccountInfo account) {
        return accountInfoService.save(account);
    }

    /**
     * 修改账号信息
     *
     * @param account 账户信息
     * @return Mono<AccountInfo>
     */
    @PutMapping
    public Mono<AccountInfo> modifyAccount(@RequestBody AccountInfo account) {
        return accountInfoService.save(account);
    }

    /**
     * 删除账号信息
     *
     * @param accountId 主键
     * @return Mono<Void>
     */
    @DeleteMapping("/{accountId}")
    public Mono<Void> removeAccount(@PathVariable String accountId) {
        return accountInfoService.removeByAccountId(accountId);
    }
}
